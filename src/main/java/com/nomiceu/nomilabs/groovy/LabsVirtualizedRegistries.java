package com.nomiceu.nomilabs.groovy;

import static com.nomiceu.nomilabs.groovy.CompositionBuilder.CompositionSpecification;

import java.util.*;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.apache.commons.lang3.tuple.Pair;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.integration.jei.JEIPlugin;
import com.nomiceu.nomilabs.util.ItemMeta;
import com.nomiceu.nomilabs.util.ItemTagMeta;
import com.nomiceu.nomilabs.util.LabsSide;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.stack.ItemMaterialInfo;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@SuppressWarnings("unused")
@GroovyBlacklist
public class LabsVirtualizedRegistries {

    public static final ReplaceRecyclingManager REPLACE_RECYCLING_MANAGER = new ReplaceRecyclingManager();
    public static final ReplaceCompositionManager REPLACE_DECOMP_MANAGER = new ReplaceCompositionManager();
    public static final JEIManager JEI_MANAGER = new JEIManager();
    public static final DefaultKeybindOverrideManager DEFAULT_KEYBIND_OVERRIDE_MANAGER = new DefaultKeybindOverrideManager();

    public static class ReplaceCompositionManager extends VirtualizedRegistry<CompositionSpecification> {

        public final Deque<CompositionSpecification> needReloading = new ArrayDeque<>();

        @Override
        public void onReload() {
            restoreFromBackup().forEach(ChangeComposition::restore);
        }

        @Override
        public void afterScriptLoad() {
            ChangeComposition.reloadCompositionRecipes();
            needReloading.clear();
        }

        public void changeMaterialDecomp(CompositionSpecification spec) {
            addBackup(spec);
            // Add Items to the 'back' of the array deque, so that it's a LIFO structure
            needReloading.addFirst(spec);
        }
    }

    public static class ReplaceRecyclingManager extends VirtualizedRegistry<Pair<ItemMeta, ItemMaterialInfo>> {

        public final Map<ItemMeta, ItemMaterialInfo> needReloading = new Object2ObjectOpenHashMap<>();

        @Override
        public void onReload() {
            restoreFromBackup().forEach((pair) -> {
                OreDictUnifier.registerOre(pair.getLeft().toStack(), pair.getRight());
                needReloading.put(pair.getLeft(), pair.getRight());
            });
        }

        @Override
        public void afterScriptLoad() {
            ReplaceRecipe.reloadRecyclingRecipes();
            needReloading.clear();
        }

        @Override
        protected boolean compareRecipe(Pair<ItemMeta, ItemMaterialInfo> a, Pair<ItemMeta, ItemMaterialInfo> b) {
            return a.getKey().equals(b.getKey());
        }

        public void registerOre(ItemStack stack, ItemMaterialInfo info) {
            var in = new ItemTagMeta(stack);
            addBackup(Pair.of(in, OreDictUnifier.getMaterialInfo(stack)));
            needReloading.put(in, info);
            OreDictUnifier.registerOre(stack, info);
        }
    }

    public static class JEIManager extends VirtualizedRegistry<String> {

        @Override
        public void onReload() {
            JEIPlugin.onReload();
        }
    }

    public static class DefaultKeybindOverrideManager extends VirtualizedRegistry<String> {

        @Override
        public void onReload() {}

        // Re-read Options File to re-apply set Keybindings
        @Override
        public void afterScriptLoad() {
            if (LabsSide.isDedicatedServer()) return;
            NomiLabs.LOGGER.info("Reloading Options File.");
            FMLClientHandler.instance().getClient().gameSettings.loadOptions();
        }
    }
}
