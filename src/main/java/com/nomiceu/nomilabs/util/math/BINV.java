package com.nomiceu.nomilabs.util.math;

import gregtech.api.GTValues;

/**
 * See {@link BinomialGenerator}. BINV implementation.
 */
public class BINV implements BinomialMethod {

    // Initial Values
    private final int n;
    private final int pN;
    private final double p;

    // Calculated Values
    private final double s;
    private final double a;
    private final double qn;

    public BINV(int n, int pN, double p, double r) {
        this.n = n;
        this.pN = pN;
        this.p = p;

        double q = (1 - r);
        s = r / q;
        a = (n + 1) * s;
        qn = BinomialGenerator.pow(q, n);
    }

    @Override
    public boolean isSetupWith(int n, int pN) {
        return this.n == n && this.pN == pN;
    }

    @Override
    public int roll() {
        double u = GTValues.RNG.nextDouble();
        int x = 0;
        double f = qn;

        while (u > f) {
            u -= f;
            x++;
            f *= ((a / x) - s);
        }

        return p > 0.5 ? n - x : x;
    }
}
