package com.nomiceu.nomilabs.remap.datafixer.fixes;
/*
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import com.nomiceu.nomilabs.remap.datafixer.storage.Representation;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixImpact;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import net.minecraft.util.datafix.IFixableData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class DataFixer implements IFixableData {
    protected final LabsFixTypes type;
    protected final int version;
    protected final Map<Function<? extends Representation, Boolean>, Consumer<? extends Representation>> fixes;

    public DataFixer(LabsFixTypes type) {
        this.type = type;
        this.version = DataFixerHandler.FIX_VERSION;
        switch (type) {
            case ITEM -> this.fixes = LabsFixes.itemFixes;
            case BLOCK -> this.fixes = LabsFixes.blockFixes;
        }
        this.fixes = new HashMap<>();
    }
}

 */
