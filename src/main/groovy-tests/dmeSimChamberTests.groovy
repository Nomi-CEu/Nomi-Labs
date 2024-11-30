//MODS_LOADED: deepmoblearning

import com.nomiceu.nomilabs.LabsValues
import mustapelto.deepmoblearning.common.metadata.MetadataDataModel
import mustapelto.deepmoblearning.common.metadata.MetadataManager
import net.minecraftforge.fml.common.Loader

// Demonstration of Dynamically Generated DME Sim Chamber Recipes. (Goes in Post Init)
// WILL NOT LOAD IF DME IS NOT INCLUDED!

def models = MetadataManager.dataModelMetadataList
for (var model : models) {
    if (!Loader.isModLoaded(model.modID)) continue

    int tier = MetadataManager.minDataModelTier
    while (!MetadataManager.isMaxDataModelTier(tier)){
        addDMERecipe(model, tier)
        tier = MetadataManager.getNextDataModelTier(tier)
    }
    // Since this does not include maximum tier...
    addDMERecipe(model, MetadataManager.getNextDataModelTier(tier))
}

void addDMERecipe(MetadataDataModel model, int tier) {
    def tierData = MetadataManager.getDataModelTierData(tier)
    if (!tierData.present || !tierData.get().canSimulate) return

    def modelPath = model.dataModelRegistryID
    def living = model.livingMatter
    def pristine = model.pristineMatter
    def eut = model.simulationRFCost / 4

    int chance = tierData.get().pristineChance

    mods.gregtech.dme_sim_chamber.recipeBuilder()
            .dataItem(item("${LabsValues.DME_MODID}:${modelPath}"), tier)
            .input(item('deepmoblearning:polymer_clay').item)
            .output(living.item)
            .chancedOutput(pristine, chance * 100, 0) // Chanced Outputs are In Per 100 (100 = 1%)
            .EUt(eut as int)
            .duration(300)
            .buildAndRegister()
}