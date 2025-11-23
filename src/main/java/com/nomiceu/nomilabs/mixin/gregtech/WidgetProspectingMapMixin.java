package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.client.multiplayer.WorldClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.gregtech.mixinhelper.OreData;

import gregtech.api.unification.OreDictUnifier;
import gregtech.common.terminal.app.prospector.ProspectingTexture;
import gregtech.common.terminal.app.prospector.ProspectorMode;
import gregtech.common.terminal.app.prospector.widget.WidgetOreList;
import gregtech.common.terminal.app.prospector.widget.WidgetProspectingMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * Implements <a href="https://github.com/GregTechCEu/GregTech/pull/2726">GT #2726</a>.
 */
@Mixin(value = WidgetProspectingMap.class, remap = false)
public class WidgetProspectingMapMixin {

    @Shadow
    private ProspectingTexture texture;

    @Shadow
    private boolean darkMode;

    @Unique
    private OreData labs$hoveredOreData = null;

    @Unique
    private final Map<String, OreData> labs$nameToData = new Object2ObjectOpenHashMap<>();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void defaultDark(int xPosition, int yPosition, int chunkRadius, WidgetOreList widgetOreList,
                             ProspectorMode mode, int scanTick, CallbackInfo ci) {
        darkMode = LabsConfig.modIntegration.defaultDarkMode;
    }

    /* Calculation of Ore Height */

    @Inject(method = "drawInForeground", at = @At("HEAD"))
    private void resetLabsData(int mouseX, int mouseY, CallbackInfo ci) {
        labs$hoveredOreData = null;
        labs$nameToData.clear();
    }

    @Inject(method = "drawInForeground",
            at = @At(value = "INVOKE", target = "Ljava/util/HashMap;forEach(Ljava/util/function/BiConsumer;)V"),
            require = 1,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void initLabsData(int mouseX, int mouseY, CallbackInfo ci, @Local(ordinal = 2) int cX,
                              @Local(ordinal = 3) int cZ) { // Function Args count in this ordinal
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (this.texture.map[cX * 16 + i][cZ * 16 + j] == null) continue;

                texture.map[cX * 16 + i][cZ * 16 + j].forEach((key, value) -> {
                    String name = OreDictUnifier.get(value).getDisplayName();

                    if (!ProspectingTexture.SELECTED_ALL.equals(texture.getSelected()) &&
                            !texture.getSelected().equals(value)) {
                        return;
                    }

                    OreData nameData = labs$nameToData.get(name);
                    if (nameData == null)
                        labs$nameToData.put(name, new OreData(key));
                    else
                        nameData.update(key);

                    if (labs$hoveredOreData == null)
                        labs$hoveredOreData = new OreData(key);
                    else
                        labs$hoveredOreData.update(key);
                });
            }
        }
    }

    /* Handling in Tooltips */

    // Sorting Tooltips
    @Redirect(method = "drawInForeground",
              at = @At(value = "INVOKE", target = "Ljava/util/HashMap;forEach(Ljava/util/function/BiConsumer;)V"),
              require = 1)
    private void sortTooltipsByY(HashMap<String, Integer> instance, BiConsumer<String, Integer> biConsumer) {
        instance.entrySet().stream().sorted((a, b) -> {
            OreData dataA = labs$nameToData.get(a.getKey());
            OreData dataB = labs$nameToData.get(b.getKey());
            if (dataA == null || dataB == null) {
                return a.getKey().compareTo(b.getKey());
            }

            // Highest Y Values First
            return Integer.compare(dataB.minY(), dataA.minY());
        }).forEach((entry) -> biConsumer.accept(entry.getKey(), entry.getValue()));
    }

    // Adding to Tooltips
    @Redirect(method = "lambda$drawInForeground$2",
              at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0),
              require = 1)
    private <E> boolean addYDataToTooltips(List<E> instance, E e) {
        if (!(e instanceof String str)) {
            NomiLabs.LOGGER.error("[ProspectorMixin] Bad Injection!");
            return instance.add(e);
        }

        String[] splitStr = str.split(" --- ");
        OreData data = labs$nameToData.get(splitStr[0]);
        if (data == null) {
            NomiLabs.LOGGER.error("[ProspectorMixin] Could not find Y data for name {}!", splitStr[0]);
            return instance.add(e);
        }

        // noinspection unchecked
        return instance.add((E) (String.format("%s --- §e%s§r (y: §c%s - %s§r)", splitStr[0], splitStr[1], data.minY(),
                data.maxY())));
    }

    /* Waypoint Y Handling */
    @WrapOperation(method = "mouseClicked",
                   at = @At(value = "INVOKE",
                            target = "Lnet/minecraft/client/multiplayer/WorldClient;getHeight(II)I",
                            remap = true),
                   require = 1,
                   remap = false)
    private int useHoveredOreHeightMain(WorldClient instance, int x, int z, Operation<Integer> original) {
        if (labs$hoveredOreData == null) return original.call(instance, x, z);
        return labs$hoveredOreData.avgY();
    }

    @WrapOperation(method = "addVoxelMapWaypoint",
                   at = @At(value = "INVOKE",
                            target = "Lnet/minecraft/client/multiplayer/WorldClient;getHeight(II)I",
                            remap = true),
                   require = 1,
                   remap = false)
    private int useHoveredOreHeightVoxel(WorldClient instance, int x, int z, Operation<Integer> original) {
        if (labs$hoveredOreData == null) return original.call(instance, x, z);
        return labs$hoveredOreData.avgY();
    }

    @WrapOperation(method = "addXaeroMapWaypoint",
                   at = @At(value = "INVOKE",
                            target = "Lnet/minecraft/client/multiplayer/WorldClient;getHeight(II)I",
                            remap = true),
                   require = 1,
                   remap = false)
    private int useHoveredOreHeightXaero(WorldClient instance, int x, int z, Operation<Integer> original) {
        if (labs$hoveredOreData == null) return original.call(instance, x, z);
        return labs$hoveredOreData.avgY();
    }
}
