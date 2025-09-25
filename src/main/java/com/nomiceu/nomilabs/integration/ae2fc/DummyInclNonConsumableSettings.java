package com.nomiceu.nomilabs.integration.ae2fc;

import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;

public class DummyInclNonConsumableSettings implements InclNonConsumeSettable {

    @Override
    public boolean labs$inclNonConsume() {
        return true;
    }

    @Override
    public void labs$setInclNonConsume(boolean inclNonConsume) {}
}
