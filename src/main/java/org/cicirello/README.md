# ρμ (Rho-Mu)
This module contains two files from the Java library [ρμ (Rho-Mu)](https://github.com/cicirello/rho-mu/).
In accordance with that project's license, all files in this directory are licensed under the GNU GPLv3.

## Files Used & Modifications
### Binomial
The file, `org/cicirello/math/rand/Binomial.java`, contains the main logic for generating random variates from binomial distributions.
This is used in Nomi Labs' changes to parallelized chanced outputs, in order to increase their performance.

Note that this logic is only used in the places where n >= 16, where n is the number of parallels.

The modifications performed to it are:

- Various visibility changes to functions, and removal of an unused function, to allow for more localized caching
- Removal of `RandomGenerator`, replacing it with `Random` instead, to make the code compatible with Java 8
- Replacing of a call to `Math.nextDouble` with a custom function; due to a lack of a `nextDouble` function with a bound in Java 8
- Storing a probability as an integer out of 10,000 for cache-matching purposes, to better align with its usage

### Math Functions
The file, `org/cicirello/math/MathFunctions.java`, has had all functions removed, except for the `pow` function.
This file is kept only for its purpose as a dependency for the main Binomial file.
