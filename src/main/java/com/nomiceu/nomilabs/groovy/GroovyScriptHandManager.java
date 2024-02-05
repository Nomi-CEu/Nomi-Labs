package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.command.TextCopyable;
import com.cleanroommc.groovyscript.event.GsHandEvent;
import gregtech.api.GTValues;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.stream.Collectors;

public class GroovyScriptHandManager {
    public static void addToHand(GsHandEvent event) {
        var stack = event.stack;
        MaterialStack material = OreDictUnifier.getMaterial(stack);
        if (material == null) return;

        event.messages.add(new TextComponentString(""));
        addToMessage(event, material.copy(1), false);

        if (material.material.getMaterialComponents().isEmpty()) return;
        event.messages.add(new TextComponentTranslation("nomilabs.command.gs_hand.material_components"));
        for (var component : material.material.getMaterialComponents()) {
            addToMessage(event, component, true);
        }

        var allComponents = material.material.getMaterialComponents().stream()
                .map((comp) -> comp.amount > 1 ?
                        "materialstack('" + getName(comp) + "') * " + comp.amount :
                        "materialstack('" + getName(comp) + "')")
                .collect(Collectors.joining(", ", "[", "]"));

        event.messages.add(TextCopyable.translation(allComponents, "nomilabs.command.gs_hand.all_material_components").build());
    }

    private static void addToMessage(GsHandEvent event, MaterialStack material, boolean list) {
        String materialText = "material('" + getName(material) + "')";
        String stackText = "materialstack('" + getName(material) + "')";
        if (material.amount > 1) stackText = stackText + " * " + material.amount;

        var materialComp = TextCopyable.translation(materialText, "nomilabs.command.gs_hand.material").build()
                .appendSibling(new TextComponentString(" " + getName(material))
                        .setStyle(new Style().setColor(TextFormatting.GREEN)));

        var stackComp = TextCopyable.translation(stackText, "nomilabs.command.gs_hand.material_stack").build()
                .appendSibling(new TextComponentString(" " + getName(material) + " (" + material.amount + ")")
                        .setStyle(new Style().setColor(TextFormatting.GREEN)));

        if (!list) {
            event.messages.add(materialComp);
            event.messages.add(stackComp);
            return;
        }

        event.messages.add(new TextComponentString(" - ").appendSibling(new TextComponentString(material.material.getLocalizedName())
                .setStyle(new Style().setColor(TextFormatting.AQUA))));
        event.messages.add(new TextComponentString("   ").appendSibling(materialComp));
        event.messages.add(new TextComponentString("   ").appendSibling(stackComp));
    }

    private static String getName(MaterialStack material) {
        return material.material.getModid().equals(GTValues.MODID) ? material.material.toString() : material.material.getRegistryName();
    }
}
