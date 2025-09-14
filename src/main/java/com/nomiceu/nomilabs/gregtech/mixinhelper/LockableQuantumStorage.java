package com.nomiceu.nomilabs.gregtech.mixinhelper;

public interface LockableQuantumStorage<T> {

    boolean labs$isLocked();

    T labs$getLocked();
}
