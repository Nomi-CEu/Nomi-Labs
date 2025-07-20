
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
package org.cicirello.math;

/**
 * MathFunctions is a class of utility methods that implement various mathematical functions.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *         href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class MathFunctions {

    /**
     * Computes a<sup>b</sup>, where the exponent b is an integer. This is more efficient than using
     * {@link Math#pow} since it exploits the integer type of the exponent.
     *
     * @param a The base.
     * @param b The exponent.
     * @return a<sup>b</sup>
     */
    public static double pow(double a, int b) {
        if (b >= 0) {
            double c = 1;
            while (b > 0) {
                if ((b & 1) == 1) c *= a;
                b = b >> 1;
                a = a * a;
            }
            return c;
        } else {
            return 1.0 / pow(a, -b);
        }
    }
}
