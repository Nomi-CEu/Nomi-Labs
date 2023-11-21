package com.nomiceu.nomilabs.integration.draconicevolution;

import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorCore;
import com.nomiceu.nomilabs.util.ItemMeta;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ReactorLogic {
    private static final int BLOCK_MULTI = 1296;
    private static final int INGOT_MULTI = 144;
    private static final int NUGGET_MULTI = 16;
    public static int getFuelValue(ItemStack stack) {
        if (stack.isEmpty())
            return 0;

        var gtAwakened = DraconicHelpers.getGTAwakenedDraconium();
        var defaultAwakened = DraconicHelpers.getDefaultAwakened();
        int stackCount = stack.getCount();

        if (ItemMeta.compare(stack, gtAwakened.get(0)) || ItemMeta.compare(stack, defaultAwakened.get(0)))
            return stackCount * BLOCK_MULTI;
        if (ItemMeta.compare(stack, gtAwakened.get(1)) || ItemMeta.compare(stack, defaultAwakened.get(1)))
            return stackCount * INGOT_MULTI;
        else if (ItemMeta.compare(stack, gtAwakened.get(2)) || ItemMeta.compare(stack, defaultAwakened.get(2)))
            return stackCount * NUGGET_MULTI;

        return 0;
    }

    public static ItemStack getStack(int index, TileReactorCore tile) {
        var gtAwakened = DraconicHelpers.getGTAwakenedDraconium();
        int fuel = MathHelper.floor(tile.reactableFuel.value);

        // List of fuel values, with [0] = block, [1] = ingot, [2] = nugget
        int[] fuelAmount = new int[] {
                fuel / BLOCK_MULTI,
                (fuel % BLOCK_MULTI) / INGOT_MULTI,
                ((fuel % BLOCK_MULTI) % INGOT_MULTI) / NUGGET_MULTI
        };

        if (fuelAmount[index] == 0)
            return ItemStack.EMPTY;

        gtAwakened.get(index).setCount(fuelAmount[index]);
        return gtAwakened.get(index);
    }
}
