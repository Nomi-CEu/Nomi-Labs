package com.nomiceu.nomilabs.gregtech.material.registry.register;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconSet;

import static com.nomiceu.nomilabs.util.RegistryNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;

public class LabsChemicals {
    public static void initChemicals() {
        TungstenTrioxide = new Material.Builder(32032, makeLabsName("tungsten_trioxide")) // Hardmode Material
                .dust()
                .color(0xC7D300).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Tungsten, 1, Oxygen, 3)
                .build();

        BerylliumOxide = new Material.Builder(32033, makeLabsName("beryllium_oxide")) // Hardmode Material
                .ingot()
                .color(0x54C757).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_ROD, GENERATE_RING)
                .components(Beryllium, 1, Oxygen, 1)
                .build();

        NiobiumPentoxide = new Material.Builder(32034, makeLabsName("niobium_pentoxide")) // Hardmode Material
                .dust()
                .color(0xBAB0C3).iconSet(MaterialIconSet.ROUGH)
                .components(Niobium, 2, Oxygen, 5)
                .build();

        TantalumPentoxide = new Material.Builder(32035, makeLabsName("tantalum_pentoxide")) // Hardmode Material
                .dust()
                .color(0x72728A).iconSet(MaterialIconSet.ROUGH)
                .components(Tantalum, 2, Oxygen, 5)
                .build();

        ManganeseDifluoride = new Material.Builder(32037, makeLabsName("manganese_difluoride")) // Hardmode Material
                .dust()
                .color(0xEF4B3D).iconSet(MaterialIconSet.ROUGH)
                .components(Manganese, 1, Fluorine, 2)
                .build();

        MolybdenumTrioxide = new Material.Builder(32038, makeLabsName("molybdenum_trioxide")) // Hardmode Material
                .dust()
                .color(0xCBCFDA).iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Molybdenum, 1, Oxygen, 3)
                .build();

        LeadChloride = new Material.Builder(32039, makeLabsName("lead_chloride")) // Hardmode Material
                .dust()
                .color(0xF3F3F3).iconSet(MaterialIconSet.ROUGH)
                .components(Lead, 1, Chlorine, 2)
                .build();

        Wollastonite = new Material.Builder(32040, makeLabsName("wollastonite")) // Hardmode Material
                .dust()
                .color(0xF7F7E7).iconSet(MaterialIconSet.BRIGHT)
                .components(Calcium, 1, Silicon, 1, Oxygen, 3)
                .build();

        SodiumMetavanadate = new Material.Builder(32041, makeLabsName("sodium_metavanadate")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xe6bb22).iconSet(MaterialIconSet.DULL)
                .components(Sodium, 1, Vanadium, 1, Oxygen, 3)
                .build();

        VanadiumPentoxide = new Material.Builder(32042, makeLabsName("vanadium_pentoxide")) // Hardmode Material
                .dust()
                .color(0xffcf33).iconSet(MaterialIconSet.ROUGH)
                .components(Vanadium, 2, Oxygen, 5)
                .build();

        AmmoniumMetavanadate = new Material.Builder(32043, makeLabsName("ammonium_metavanadate")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xf7e37e).iconSet(MaterialIconSet.DULL)
                .components(Nitrogen, 1, Hydrogen, 4, Vanadium, 1, Oxygen, 3)
                .build();

        PhthalicAnhydride = new Material.Builder(32044, makeLabsName("phthalic_anhydride")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xeeaaee).iconSet(MaterialIconSet.DULL)
                .components(Carbon, 8, Hydrogen, 4, Oxygen, 3)
                .build();

        PhthalicAnhydride.setFormula("C6H4(CO)2O", true);

        Ethylanthraquinone = new Material.Builder(32045, makeLabsName("ethylanthraquinone")) // Hardmode Material
                .dust()
                .color(0xf1e181)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 16, Hydrogen, 12, Oxygen, 2)
                .build();

        Ethylanthraquinone.setFormula("C6H4(CO)2C6H3(CH2CH3)", true);

        HydrogenPeroxide = new Material.Builder(32046, makeLabsName("hydrogen_peroxide")) // Hardmode Material
                .fluid()
                .color(0xd2ffff)
                .components(Hydrogen, 2, Oxygen, 2)
                .build();

        Hydrazine = new Material.Builder(32047, makeLabsName("hydrazine")) // Hardmode Material
                .fluid()
                .color(0xb50707)
                .components(Nitrogen, 2, Hydrogen, 4)
                .build();

        AcetoneAzine = new Material.Builder(32048, makeLabsName("acetone_azine")) // Hardmode Material
                .fluid()
                .color(0xa1e1e1)
                .components(Carbon, 6, Hydrogen, 12, Nitrogen, 2)
                .build();

        AcetoneAzine.setFormula("((CH3)2(CN))2", true);

        GrapheneOxide = new Material.Builder(32049, makeLabsName("graphene_oxide")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x777777).iconSet(MaterialIconSet.ROUGH)
                .components(Graphene, 1, Oxygen, 1)
                .build();

        Durene = new Material.Builder(32051, makeLabsName("durene")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x336040).iconSet(MaterialIconSet.FINE)
                .components(Carbon, 10, Hydrogen, 14)
                .build();

        Durene.setFormula("C6H2(CH3)4", true);

        PyromelliticDianhydride = new Material.Builder(32052, makeLabsName("pyromellitic_dianhydride")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xf0ead6).iconSet(MaterialIconSet.ROUGH)
                .components(Carbon, 10, Hydrogen, 2, Oxygen, 6)
                .build();

        PyromelliticDianhydride.setFormula("C6H2(C2O3)2", true);

        Dimethylformamide = new Material.Builder(32053, makeLabsName("dimethylformamide")) // Hardmode Material
                .fluid()
                .color(0x42bdff)
                .components(Carbon, 3, Hydrogen, 7, Nitrogen, 1, Oxygen, 1)
                .build();

        Aminophenol = new Material.Builder(32054, makeLabsName("aminophenol")) // Hardmode Material
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xff7f50)
                .components(Carbon, 6, Hydrogen, 7, Nitrogen, 1, Oxygen, 1)
                .build();

        Oxydianiline = new Material.Builder(32055, makeLabsName("oxydianiline")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xf0e130).iconSet(MaterialIconSet.DULL)
                .components(Carbon, 12, Hydrogen, 12, Nitrogen, 2, Oxygen, 1)
                .build();

        Oxydianiline.setFormula("O(C6H4NH2)2", true);

        AntimonyPentafluoride = new Material.Builder(32056, makeLabsName("antimony_pentafluoride")) // Hardmode Material
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xe3f1f1)
                .components(Antimony, 1, Fluorine, 5)
                .build();

        LeadMetasilicate = new Material.Builder(32066, makeLabsName("lead_metasilicate")) // Hardmode Material
                .dust()
                .color(0xF7F7E7).iconSet(MaterialIconSet.DULL)
                .components(Lead, 1, Silicon, 1, Oxygen, 3)
                .build();

        Butanol = new Material.Builder(32112, makeLabsName("butanol"))
                .fluid()
                .color(0xc7af2e)
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 1)
                .build();

        Butanol.setFormula("C4H9OH", true);

        PhosphorusTrichloride = new Material.Builder(32113, makeLabsName("phosphorus_trichloride"))
                .fluid()
                .color(0xe8c474)
                .components(Phosphorus, 1, Chlorine, 3)
                .build();

        PhosphorylChloride = new Material.Builder(32114, makeLabsName("phosphoryl_chloride"))
                .fluid()
                .color(0xe8bb5b)
                .components(Phosphorus, 1, Oxygen, 1, Chlorine, 3)
                .build();

        TributylPhosphate = new Material.Builder(32115, makeLabsName("tributyl_phosphate"))
                .fluid()
                .color(0xe8c4a0)
                .components(Carbon, 12, Hydrogen, 27, Oxygen, 4, Phosphorus, 1)
                .build();

        TributylPhosphate.setFormula("(C4H9O)3PO", true);
    }
}
