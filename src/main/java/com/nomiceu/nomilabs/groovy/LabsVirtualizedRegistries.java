package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.nomiceu.nomilabs.gregtech.AccessibleMaterial;
import com.nomiceu.nomilabs.integration.jei.JEIPlugin;
import com.nomiceu.nomilabs.util.ItemTagMeta;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.stack.ItemMaterialInfo;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@GroovyBlacklist
public class LabsVirtualizedRegistries {
    public static final ReplaceRecyclingManager REPLACE_RECYCLING_MANAGER = new ReplaceRecyclingManager();
    public static final ReplaceDecompositionManager REPLACE_DECOMP_MANAGER = new ReplaceDecompositionManager();
    public static final JEIManager JEI_MANAGER = new JEIManager();

    public static class ReplaceDecompositionManager extends VirtualizedRegistry<Pair<Material, List<MaterialStack>>> {
        public final Map<Material, List<MaterialStack>> needReloading = new HashMap<>();

        @Override
        public void onReload() {
            restoreFromBackup().forEach((pair) -> {
                needReloading.put(pair.getLeft(), pair.getRight());
                ((AccessibleMaterial) pair.getKey()).setComponents(pair.getRight());
            });
        }

        @Override
        public void afterScriptLoad() {
            ReplaceDecomposition.reloadDecompositionRecipes();
            needReloading.clear();
        }

        @Override
        protected boolean compareRecipe(Pair<Material, List<MaterialStack>> a, Pair<Material, List<MaterialStack>> b) {
            return a.getKey().getRegistryName().equals(b.getKey().getRegistryName());
        }

        public void changeMaterialDecomp(Material material, List<MaterialStack> components) {
            addBackup(Pair.of(material, material.getMaterialComponents()));
            needReloading.put(material, components);
            ((AccessibleMaterial) material).setComponents(components);
        }
    }

    public static class ReplaceRecyclingManager extends VirtualizedRegistry<Pair<ItemStack, ItemMaterialInfo>> {
        public final Map<ItemStack, ItemMaterialInfo> needReloading = new HashMap<>();

        @Override
        public void onReload() {
            restoreFromBackup().forEach((pair) -> {
                OreDictUnifier.registerOre(pair.getLeft(), pair.getRight());
                needReloading.put(pair.getLeft(), pair.getRight());
            });
        }

        @Override
        public void afterScriptLoad() {
            ReplaceRecipe.reloadRecyclingRecipes();
            needReloading.clear();
        }

        @Override
        protected boolean compareRecipe(Pair<ItemStack, ItemMaterialInfo> a, Pair<ItemStack, ItemMaterialInfo> b) {
            return ItemTagMeta.compare(a.getKey(), b.getKey());
        }

        public void registerOre(ItemStack stack, ItemMaterialInfo info) {
            addBackup(Pair.of(stack, OreDictUnifier.getMaterialInfo(stack)));
            needReloading.put(stack, info);
            OreDictUnifier.registerOre(stack, info);
        }
    }

    public static class JEIManager extends VirtualizedRegistry<String> {
        @Override
        public void onReload() {
            JEIPlugin.onReload();
        }
    }
}
