package com.nomiceu.nomilabs.groovy;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.nomiceu.nomilabs.tooltip.LabsTooltipHelper;
import com.nomiceu.nomilabs.tooltip.TooltipChanger;
import com.nomiceu.nomilabs.util.ItemMeta;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@GroovyBlacklist
public class GroovyTooltipChanger {

    private static final Map<ItemMeta, TooltipOperation> OPERATIONS = new Object2ObjectOpenHashMap<>();

    /**
     * For use in GroovyScript ONLY.
     * <p>
     * If you want to add a tooltip in Labs, add a function to {@link TooltipChanger}.
     */
    public static void addOperation(ItemMeta itemMeta, Consumer<List<String>> op) {
        var tooltipOp = new TooltipOperation(op);

        if (OPERATIONS.containsKey(itemMeta)) OPERATIONS.get(itemMeta).addNext(tooltipOp);
        else OPERATIONS.put(itemMeta, tooltipOp);
    }

    @SideOnly(Side.CLIENT)
    public static void clear() {
        OPERATIONS.clear();
    }

    @SideOnly(Side.CLIENT)
    public static void modifyTooltip(List<String> tooltip, ItemStack stack) {
        if (stack.isEmpty()) return;
        ItemMeta itemMeta = new ItemMeta(stack);
        TooltipOperation op = OPERATIONS.get(itemMeta);

        if (op == null || tooltip.isEmpty()) return;

        // Modify Tooltip List for easy modification
        // Remove first item, which is the item name
        String itemName = tooltip.remove(0);

        // If advanced tooltips are enabled, remove last, because of resource location printing
        // No need to add it back, it gets added back regardless
        if (Minecraft.getMinecraft().gameSettings.advancedItemTooltips)
            LabsTooltipHelper.tryRemove(tooltip, tooltip.size() - 1, tooltip.size());

        op.apply(tooltip);
        tooltip.add(0, itemName);
    }

    public static class TooltipOperation {

        private TooltipOperation next;
        private final Consumer<List<String>> operation;

        public TooltipOperation(Consumer<List<String>> operation) {
            this.operation = operation;
        }

        public void addNext(TooltipOperation next) {
            if (this.next != null)
                this.next.addNext(next);
            else
                this.next = next;
        }

        public void apply(List<String> tooltip) {
            operation.accept(tooltip);
            if (next != null)
                next.apply(tooltip);
        }
    }
}
