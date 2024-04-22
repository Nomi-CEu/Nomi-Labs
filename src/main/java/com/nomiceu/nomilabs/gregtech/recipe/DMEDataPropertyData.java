package com.nomiceu.nomilabs.gregtech.recipe;

import net.minecraft.item.Item;

public class DMEDataPropertyData {

    public final Item dataItem;

    public final int tier;

    private int addition;

    public DMEDataPropertyData(Item dataItem, int tier) {
        this.dataItem = dataItem;
        this.tier = tier;
        addition = 1;
    }

    public int getAddition() {
        return addition;
    }

    public void setAddition(int addition) {
        this.addition = addition;
    }
}
