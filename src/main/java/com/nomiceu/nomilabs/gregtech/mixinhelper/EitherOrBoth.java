package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;

@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class EitherOrBoth<L, R> {

    @Nullable
    private L valueL;

    @Nullable
    private R valueR;

    public Optional<L> getLeft() {
        return Optional.ofNullable(valueL);
    }

    public Optional<R> getRight() {
        return Optional.ofNullable(valueR);
    }

    public EitherOrBoth<L, R> setLeft(L value) {
        if (hasLeft())
            throw new IllegalArgumentException("Cannot set Either Or Both when value of that side already exists!");
        valueL = value;
        return this;
    }

    public EitherOrBoth<L, R> setRight(R value) {
        if (hasRight())
            throw new IllegalArgumentException("Cannot set Either Or Both when value of that side already exists!");
        valueR = value;
        return this;
    }

    public EitherOrBoth<L, R> setLeftIfNoValue(L value) {
        if (hasLeft()) return this;
        valueL = value;
        return this;
    }

    public EitherOrBoth<L, R> setRightIfNoValue(R value) {
        if (hasRight()) return this;
        valueR = value;
        return this;
    }

    public EitherOrBoth<L, R> removeLeft() {
        if (!hasLeft()) return this;
        valueL = null;
        return this;
    }

    public EitherOrBoth<L, R> removeRight() {
        if (!hasRight()) return this;
        valueR = null;
        return this;
    }

    public boolean hasLeft() {
        return valueL != null;
    }

    public boolean hasRight() {
        return valueR != null;
    }

    public static <L, R> EitherOrBoth<L, R> left(L value) {
        return new EitherOrBoth<L, R>().setLeft(value);
    }

    public static <L, R> EitherOrBoth<L, R> right(R value) {
        return new EitherOrBoth<L, R>().setRight(value);
    }

    public static <L, R> EitherOrBoth<L, R> both(L valueL, R valueR) {
        return new EitherOrBoth<L, R>().setLeft(valueL).setRight(valueR);
    }

    public <C, D> EitherOrBoth<C, D> map(Function<? super L, ? extends C> var1, Function<? super R, ? extends D> var2) {
        return both(hasLeft() ? var1.apply(valueL) : null, hasRight() ? var2.apply(valueR) : null);
    }

    public <T> EitherOrBoth<T, R> mapLeft(Function<? super L, ? extends T> l) {
        return map(l, (r) -> valueR);
    }

    public <T> EitherOrBoth<L, T> mapRight(Function<? super R, ? extends T> r) {
        return map((l) -> valueL, r);
    }
}
