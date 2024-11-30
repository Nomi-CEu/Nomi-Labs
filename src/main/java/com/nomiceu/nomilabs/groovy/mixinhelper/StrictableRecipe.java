package com.nomiceu.nomilabs.groovy.mixinhelper;

/**
 * A recipe that can return the matching stacks exactly for JEI,
 * ignoring duplicates according to JEI and wildcard itemstacks.
 */
public interface StrictableRecipe {

    void labs$setStrict();

    boolean labs$getIsStrict();
}
