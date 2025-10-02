package com.nomiceu.nomilabs.util.math;

import static com.nomiceu.nomilabs.util.math.BinomialGenerator.*;

import gregtech.api.GTValues;

/**
 * See {@link BinomialGenerator}. BTPE implementation.
 */
public class BTPE implements BinomialMethod {

    // Initial Values
    private final int n;
    private final int pN;
    private final double p;
    private final double r;

    // Calculated Values
    private final double q;
    private final double s;
    private final int m;
    private final double nrq;
    private final double nrqInv;
    private final double sn1;

    private final double p1;
    private final double p2;
    private final double p3;
    private final double p4;

    private final double xL;
    private final double xM;
    private final double xR;

    private final double c;

    private final double lL;
    private final double lR;

    private final int f1;
    private final int z1;

    private final double f2;
    private final double z2;

    private final double g;

    // Runtime Values
    private double u;
    private double v;
    private int k;
    private int y;

    public BTPE(int n, int pN, double p, double r) {
        this.n = n;
        this.pN = pN;
        this.p = p;
        this.r = r;

        // Step 0: Setup
        q = 1 - r;
        s = r / q;

        double nr = n * r;
        double fM = nr + r;
        m = (int) Math.floor(fM);
        nrq = nr * q;
        nrqInv = 1 / nrq;
        sn1 = s * (n + 1);

        p1 = Math.floor(2.195 * Math.sqrt(nrq) - 4.6 * q) + HALF;

        xL = m - p1;
        xM = m + HALF;
        xR = m + p1;

        c = 0.134 + 20.5 / (15.3 + m);

        double a = (fM - xL) / (fM - xL * r);
        lL = a * (1 + a * HALF);
        a = (xR - fM) / (xR * q);
        lR = a * (1 + a * HALF);

        p2 = p1 * (1 + 2 * c);
        p3 = p2 + c / lL;
        p4 = p3 + c / lR;

        f1 = (m + 1) * (m + 1);
        z1 = (n + 1 - m) * (n + 1 - m);

        f2 = stirlingApproximation(f1);
        z2 = stirlingApproximation(z1);

        g = n - m + HALF;
    }

    @Override
    public boolean isSetupWith(int n, int pN) {
        return this.n == n && this.pN == pN;
    }

    @Override
    public int roll() {
        return step1();
    }

    /* Random Generation & Triangular Distribution */
    private int step1() {
        // Generate random values
        u = nextBoundedDouble(p4);
        v = GTValues.RNG.nextDouble();

        if (u > p1) return step2();

        // Generate triangular distributed variate
        y = (int) Math.floor(xM - p1 * v + u);
        return step6();
    }

    /* Parallelogram Distribution */
    private int step2() {
        if (u > p2) return step3();

        // Generate parallelogram distributed variate
        double x = xL + (u - p1) / c;
        v = v * c + 1 - Math.abs(m - x + HALF) / p1;

        if (v > 1) return step1();

        y = (int) Math.floor(x);
        return step5_0();
    }

    /* Left Exponential Tail */
    private int step3() {
        if (u > p3) return step4();

        y = (int) Math.floor(xL + Math.log(v) / lL);
        if (y < 0) return step1();

        v = v * (u - p2) * lL;
        return step5_0();
    }

    /* Right Exponential Tail */
    private int step4() {
        y = (int) Math.floor(xR - Math.log(v) / lR);
        if (y > n) return step1();

        v = v * (u - p3) * lR;
        return step5_0();
    }

    /* Acceptance & Rejection (Sub-steps of step 5 */

    /* Method Determining (5_1 or 5_2) */
    private int step5_0() {
        k = Math.abs(y - m);

        if (k > 20 && k < nrq * HALF - 1)
            return step5_2();

        return step5_1();
    }

    /* Recursive Test */
    private int step5_1() {
        double f = 1.0;

        if (m < y) {
            for (int i = m + 1; i <= y; i++) {
                f = f * (sn1 / i - s);
            }
        } else if (m > y) {
            for (int i = y + 1; i <= m; i++) {
                f = f / (sn1 / i - s);
            }
        }

        if (v > f) return step1();
        return step6();
    }

    /* Squeezing Test */
    private int step5_2() {
        double b = (k * nrqInv) * ((k * (k * THIRD + 0.625) + SIXTH) * nrqInv + HALF);
        double t = -k * k * HALF * nrqInv;
        double a = Math.log(v);

        if (a < t - b) return step6();
        if (a > t + b) return step1();
        return step5_3(a);
    }

    /* Final Test */
    private int step5_3(double a) {
        int x1 = y + 1;
        int w1 = n - y + 1;

        double x2 = stirlingApproximation(x1);
        double w2 = stirlingApproximation(w1);

        double bound = xM * Math.log((double) f1 / x1) + g * Math.log((double) z1 / w1) +
                (y - m) * Math.log(w1 * r / x1 * q) + f2 + z2 + x2 + w2;

        if (a > bound) return step1();
        return step6();
    }

    /* Final Output */
    private int step6() {
        return p > HALF ? n - y : y;
    }
}
