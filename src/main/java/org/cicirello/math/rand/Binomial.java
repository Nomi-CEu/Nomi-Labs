/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright 2017-2023 Vincent A. Cicirello, <https://www.cicirello.org/>.
 *
 * This file is part of the rho mu library.
 *
 * The rho mu library is free software: you can
 * redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The rho mu library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the rho mu library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.cicirello.math.rand;

import static com.nomiceu.nomilabs.util.LabsConstants.MAX_CHANCE_D;

import java.util.Random;

import org.cicirello.math.MathFunctions;

/**
 * This class enables generating random variates from a binomial distribution. This is a mostly
 * direct implementation of the BTPE algorithm described in the article: Voratas Kachitvichyanukul
 * and Bruce W. Schmeiser. 1988. Binomial random variate generation. Commun. ACM 31, 2 (February
 * 1988), 216-222. DOI=http://dx.doi.org/10.1145/42372.42381
 *
 * <p>
 * There are a few, mostly minor, differences in my Java implementation of the algorithm
 * described in that paper. First, there are several minor optimizations, such as replacing division
 * by constants with multiplication of constants wherever practical. As mentioned in the above
 * article, BTPE is only valid when n*min(p,1-p) &ge; 10. The authors of that article include a
 * survey of various alternative approaches one might use when n*min(p,1-p) &lt; 10. We use a simple
 * approach based on the inverse transform in that case.
 *
 * <p>
 * We cache the various constants that depend only on n and p. We only recompute these if a
 * different n or p is given as a parameter. To enable a thread safe approach, we maintain one
 * constant cache for each thread using ThreadLocal. Specifically, we maintain one instance per
 * thread using ThreadLocal.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *         href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public abstract class Binomial {

    // BTPE is not valid if n*min(p,1-p) < 10
    private static final int BTPE_CUTOFF = 10;

    /**
     * Create an instance of either BTPE or InverseTransform, if required, depending upon n and p.
     *
     * @param n      the value of n
     * @param inputP the value of p, as an integer, such that p=10,000 = 100%
     * @return The thread-cached instance of Binomial if it fits;
     *         else a new instance of NTPE if n*min(p,1-p) is greater than BTPE_CUTOFF,
     *         otherwise a new instance of InverseTransform
     */
    /*
     * Private helper to handle getting the thread local instance,
     * initializing if necessary, and generating if it doesn't already exist.
     */
    public static Binomial getOrCreateInstance(int n, int inputP) {
        Binomial bin = tl.get();
        if (bin == null || !bin.consistentWith(n, inputP)) {
            bin = createInstance(n, inputP);
            tl.set(bin);
        }
        return bin;
    }

    /**
     * Generates the next random number from this Binomial.
     *
     * @param generator the source of randomness
     * @return the next random number from this Binomial
     */
    public abstract int next(Random generator);

    /**
     * Checks if this Binomial is configured for specific n and p.
     *
     * @param n      the value of n
     * @param inputP the value of p, as an integer, such that p=10,000 = 100%
     * @return true if and only if configured for n and p
     */
    public abstract boolean consistentWith(int n, int inputP);

    // We cache constants until n or p changes. We use ThreadLocal for
    // cache so that we are thread safe. Each thread has its own cache.
    private static final ThreadLocal<Binomial> tl = new ThreadLocal<>();

    /**
     * Private helper; ignores thread-cache.
     * Create an instance of either BTPE or InverseTransform depending upon n and p.
     *
     * @param n      the value of n
     * @param inputP the value of p, as an integer, such that p=10,000 = 100%
     * @return an instance of NTPE if n*min(p,1-p) is greater than BTPE_CUTOFF,
     *         otherwise an instance of InverseTransform
     */
    private static Binomial createInstance(int n, int inputP) {
        // The actual probability
        double p = inputP / MAX_CHANCE_D;
        return n * Math.min(p, 1 - p) < BTPE_CUTOFF ? new InverseTransform(n, p, inputP) : new BTPE(n, p, inputP);
    }

    private static class InverseTransform extends Binomial {

        private final int n;
        private final double p;
        private final double s;
        private final double a;
        private final double pow0;

        // Labs Added: A value of p such that p=10,000 = 100%
        private final int inputP;

        private InverseTransform(int n, double p, int inputP) {
            this.n = n;
            this.p = p;
            this.inputP = inputP;
            double r;
            double q;
            if (p <= 0.5) {
                r = p;
                q = 1 - p;
            } else {
                q = p;
                r = 1 - p;
            }
            s = r / q;
            a = (n + 1) * s;
            pow0 = MathFunctions.pow(q, n);
        }

        @Override
        public final boolean consistentWith(int n, int inputP) {
            return this.n == n && this.inputP == inputP;
        }

        @Override
        public final int next(Random generator) {
            double u = generator.nextDouble();
            int y = 0;
            double pow = pow0;
            while (u > pow) {
                u -= pow;
                y++;
                pow = pow * (a / y - s);
            }
            return p > 0.5 ? n - y : y;
        }
    }

    private static class BTPE extends Binomial {

        private final int n;
        private final double p;
        private final double r;
        private final double q;
        private final double s;
        private final int m;
        private final double nrq;
        private final double p1;
        private final double x_m;
        private final double x_l;
        private final double x_r;
        private final double c;
        private final double lambda_l;
        private final double lambda_r;
        private final double p2;
        private final double p3;
        private final double p4;
        private final double nrqInv;

        // Labs Added: A value of p such that p=10,000 = 100%
        private final int inputP;

        // Private constructor
        private BTPE(int n, double p, int inputP) {
            // Step 0: Set-up constants, etc.
            this.n = n;
            this.p = p;
            this.inputP = inputP;
            if (p <= 0.5) {
                r = p;
                q = 1 - p;
            } else {
                q = p;
                r = 1 - p;
            }
            final double nr = n * r;
            s = r / q;

            final double f_m = nr + r;
            m = (int) f_m;
            nrq = nr * q;
            p1 = Math.floor(2.195 * Math.sqrt(nrq) - 4.6 * q) + 0.5;
            x_m = m + 0.5;
            x_l = x_m - p1;
            x_r = x_m + p1;
            c = 0.134 + 20.5 / (15.3 + m);
            double a = (f_m - x_l) / (f_m - x_l * r);
            lambda_l = a * (1 + 0.5 * a);
            a = (x_r - f_m) / (x_r * q);
            lambda_r = a * (1 + 0.5 * a);
            p2 = p1 * (1 + c + c);
            p3 = p2 + c / lambda_l;
            p4 = p3 + c / lambda_r;
            nrqInv = 1.0 / nrq;
        }

        @Override
        public final boolean consistentWith(int n, int inputP) {
            return this.n == n && this.inputP == inputP;
        }

        @Override
        public final int next(Random generator) {
            int y;
            while (true) {

                // Step 1.
                double u = nextBoundedDouble(generator, p4);
                double v = generator.nextDouble();
                if (u <= p1) {
                    y = (int) (x_m - p1 * v + u);
                    break;
                }

                // Step 2: parallelograms
                if (u <= p2) {
                    double x = x_l + (u - p1) / c;
                    v = v * c + 1 - Math.abs(m - x + 0.5) / p1;
                    if (v > 1) continue;
                    y = (int) x;
                } else {
                    // Step 3: left exponential tail
                    if (u <= p3) {
                        y = (int) Math.floor(x_l + Math.log(v) / lambda_l);
                        if (y < 0) continue;
                        v = v * (u - p2) * lambda_l;
                    } else {
                        // Step 4: right exponential tail
                        y = (int) Math.floor(x_r - Math.log(v) / lambda_r);
                        if (y > n) continue;
                        v = v * (u - p3) * lambda_r;
                    }
                }

                // Step 5: Acceptance/rejection comparisons.

                // Step 5.0
                int k = m > y ? m - y : y - m;
                double a;
                if (k <= 20 || k >= nrq * 0.5 - 1) {
                    // Step 5.1
                    a = s * (n + 1);
                    double f = 1.0;
                    if (m < y) {
                        for (int i = m + 1; i <= y; i++) {
                            f = f * (a / i - s);
                        }
                    } else if (m > y) {
                        for (int i = y + 1; i <= m; i++) {
                            f = f / (a / i - s);
                        }
                    }
                    if (v > f) continue;
                    break;
                } else {
                    // Step 5.2: Squeezing
                    double rho = (k * nrqInv) *
                            ((k * (k * 0.3333333333333333 + 0.625) + 0.16666666666666666) * nrqInv + 0.5);
                    double t = -k * k * (0.5 * nrqInv);
                    a = Math.log(v);
                    if (a < t - rho) {
                        break;
                    }
                    if (a > t + rho) continue;
                }

                // Step 5.3: Final acceptance/rejection test
                int x1 = y + 1;
                int f1 = m + 1;
                int z = n + 1 - m;
                int w = n - y + 1;
                if (a > x_m * Math.log(((double) f1) / x1) + (n - m + 0.5) * Math.log(((double) z) / w) +
                        (y - m) * Math.log(w * r / (x1 * q)) + stirlingApproximation(f1) + stirlingApproximation(z) +
                        stirlingApproximation(x1) + stirlingApproximation(w))
                    continue;
                break;
            }

            return p > 0.5 ? n - y : y;
        }

        private double stirlingApproximation(int x) {
            int x2 = x * x;
            return (13860.0 - (462.0 - (132.0 - (99.0 - 140.0 / x2) / x2) / x2) / x2) / x / 166320.0;
        }

        /**
         * Custom implementation of a random bounded double.
         * This is akin to the modern java implementation of this in {@link Random}.
         */
        private double nextBoundedDouble(Random rng, double bound) {
            // Specialize boundedNextDouble for origin == 0, bound > 0
            double r = rng.nextDouble();
            r = r * bound; // next double is between 0 and 1; so to go to 'bound', we multiply by bound
            if (r >= bound)  // may need to correct a rounding problem, but this is unlikely
                r = Math.nextDown(bound);
            return r;
        }
    }
}
