package com.nomiceu.nomilabs.util.math;

import static gregtech.api.GTValues.RNG;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.config.LabsConfig;

/**
 * A fast random generator that uses different methods to simulate multiple independent rolls of a given chance.<br>
 * The approach uses algorithms specified in
 * <a href="https://dl.acm.org/doi/pdf/10.1145/42372.42381">Binomial Random Variate Generation (1988)</a>,
 * as well as being inspired by an implementation found in
 * <a href="https://github.com/cicirello/rho-mu/">rho-mu</a> in the general structure
 * (e.g. splitting methods into classes).
 * <hr>
 * <b>Definitions:</b>
 * <ul>
 * <li><code>n</code>: Number of Parallels/Rolls</li>
 * <li><code>p</code>: Probability (p)</li>
 * <li><code>pN</code>: Probability as an integer, where {@link BinomialGenerator#PN_MAX_VALUE} = 100%</li>
 * <li><code>r</code>: Minimized probability; the minimum value of p and 1-p</li>
 * <hr>
 * <b>Methods:</b>
 * <ul>
 * <li>When the number of rolls (n) is less than {@link LabsConfig.Advanced#binomialThreshold},
 * each roll is done separately. (XSTR method, named after the algorithm used to generate random values)</li>
 * <li>Else, if n * pM is less than 10, the algorithm BINV (Inverse Transform) is used.</li>
 * <li>Else, the algorithm BTPE is used, allowing close to constant-time processing for higher values of n.</li>
 * </ul>
 * <hr>
 * <b>Differences:</b>
 * <p>
 * Similar to the rho-mu implementation, this implementation differs from the specification in the paper by replacing
 * division operations by multiplication where feasible.
 * </p>
 * <p>
 * From the rho-mu implementation, the code is also more seperated (hopefully leading to higher code readability).<br>
 * Finally, our implementation is under the LGPLv3, whilst rho-mu's is under the GPLv3, which is also the main reason
 * that we have created our own implementation rather than using the library's code directly.
 * </p>
 */
public class BinomialGenerator {

    public static final double HALF = 0.5;
    public static final double THIRD = 0.33333333333333333;
    public static final double SIXTH = 0.16666666666666666;

    public static int PN_MAX_VALUE = 10_000;
    public static final int BTPE_CUTOFF = 10;

    /**
     * Generate the random integer based on the binomial distribution and provided variables.
     * 
     * @param n        The number of rolls / parallels
     * @param pN       The probability, as an integer, where pN = {@link BinomialGenerator#PN_MAX_VALUE} = 100%.
     * @param cache    The cached binomial method instance
     * @param setCache The method to call to update the binomial method cache
     */
    public static int generate(int n, int pN, @Nullable WeakReference<BinomialMethod> cache,
                               Consumer<BinomialMethod> setCache) {
        // Short-circuit values
        if (pN <= 0) return 0;
        if (pN >= PN_MAX_VALUE) return n;

        // XSTR rolling (no cache)
        if (n < LabsConfig.advanced.binomialThreshold)
            return rollXSTR(n, pN);

        // Attempt to use cached method
        if (cache != null) {
            BinomialMethod cached = cache.get();
            if (cached != null && cached.isSetupWith(n, pN))
                return cached.roll();
        }

        // Determine p & q values, and method to use
        double p = pN / (double) PN_MAX_VALUE;
        double r = Math.min(p, 1 - p);

        BinomialMethod method;
        if (n * r < BTPE_CUTOFF)
            method = new BINV(n, pN, p, r);
        else
            method = new BTPE(n, pN, p, r);

        // Set cache; roll
        setCache.accept(method);
        return method.roll();
    }

    private static int rollXSTR(int n, int pN) {
        int successes = 0;
        for (int i = 0; i < n; i++) {
            if (RNG.nextInt(PN_MAX_VALUE) < pN) {
                successes++;
            }
        }
        return successes;
    }

    /**
     * A fast exponent function, utilising the fact that the power is an integer.
     * From {@link com.google.common.math.IntMath#pow(int, int)}, but with checks removed and the type of the base
     * changed to double.
     * 
     * @param b The base of the exponent. Must not be zero.
     * @param k The power of the exponent. Must be positive.
     */
    public static double pow(double b, int k) {
        for (double accum = 1.0;; k >>= 1) {
            switch (k) {
                case 0:
                    return accum;
                case 1:
                    return b * accum;
                default:
                    accum *= ((k & 1) == 0) ? 1 : b;
                    b *= b;
            }
        }
    }

    /**
     * Util function used in BTPE setup and step 5.3; extracted to reduce code duplication.
     */
    public static double stirlingApproximation(int x) {
        int x2 = x * x;
        return (13860.0 - (462.0 - (132.0 - (99.0 - 140.0 / x2) / x2) / x2) / x2) / x / 166320.0;
    }

    /**
     * Custom implementation of a random bounded double.
     * This is akin to the modern java implementation of this in {@link java.util.Random}.
     */
    public static double nextBoundedDouble(double bound) {
        // Specialize boundedNextDouble for origin == 0, bound > 0
        double r = RNG.nextDouble();
        r = r * bound; // next double is between 0 and 1; so to go to 'bound', we multiply by bound
        if (r >= bound)  // may need to correct a rounding problem, but this is unlikely
            r = Math.nextDown(bound);
        return r;
    }
}
