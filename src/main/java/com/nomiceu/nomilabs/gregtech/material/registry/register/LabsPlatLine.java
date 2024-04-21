package com.nomiceu.nomilabs.gregtech.material.registry.register;

import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;

import gregtech.api.unification.material.Material;

public class LabsPlatLine {

    public static void initPlatLine() {
        PlatinumMetallic = new Material.Builder(67, makeLabsName("platinum_metallic")) // Hardmode Material
                .dust()
                .color(0xfffbc5).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Platinum, 1, RareEarth, 1)
                .build();

        PalladiumMetallic = new Material.Builder(68, makeLabsName("palladium_metallic")) // Hardmode Material
                .dust()
                .color(0x808080).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Palladium, 1, RareEarth, 1)
                .build();

        AmmoniumHexachloroplatinate = new Material.Builder(69, makeLabsName("ammonium_hexachloroplatinate")) // Hardmode
                                                                                                             // Material
                .dust()
                .color(0xfef0c2).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Nitrogen, 2, Hydrogen, 8, Platinum, 1, Chlorine, 6)
                .build().setFormula("(NH4)2PtCl6", true);

        ChloroplatinicAcid = new Material.Builder(70, makeLabsName("chloroplatinic_acid"))
                .liquid()
                .color(0xfef0c2)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Platinum, 1, Chlorine, 6)
                .build();

        PotassiumBisulfate = new Material.Builder(71, makeLabsName("potassium_bisulfate")) // Hardmode Material
                .dust()
                .color(0xfdbd68)
                .components(Potassium, 1, Hydrogen, 1, Sulfur, 1, Oxygen, 4)
                .build();

        PotassiumPyrosulfate = new Material.Builder(72, makeLabsName("potassium_pyrosulfate")) // Hardmode Material
                .dust()
                .color(0xfbbb66)
                .components(Potassium, 2, Sulfur, 2, Oxygen, 7)
                .build();

        PotassiumSulfate = new Material.Builder(73, makeLabsName("potassium_sulfate")) // Hardmode Material
                .dust()
                .color(0xf0b064).iconSet(METALLIC)
                .components(Potassium, 2, Sulfur, 1, Oxygen, 4)
                .build();

        ZincSulfate = new Material.Builder(74, makeLabsName("zinc_sulfate")) // Hardmode Material
                .dust()
                .color(0x846649).iconSet(FINE)
                .components(Zinc, 1, Sulfur, 1, Oxygen, 4)
                .build();

        SodiumNitrate = new Material.Builder(75, makeLabsName("sodium_nitrate")) // Hardmode Material
                .dust()
                .color(0x846684).iconSet(ROUGH)
                .components(Sodium, 1, Nitrogen, 1, Oxygen, 3)
                .build();

        RhodiumNitrate = new Material.Builder(76, makeLabsName("rhodium_nitrate")) // Hardmode Material
                .dust()
                .color(0x776649).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 1, Nitrogen, 3, Oxygen, 9)
                .build().setFormula("Rh(NO3)3", true);

        SodiumRuthenate = new Material.Builder(77, makeLabsName("sodium_ruthenate")) // Hardmode Material
                .dust()
                .color(0x3a40cb).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 2, Ruthenium, 1, Oxygen, 4)
                .build();

        SodiumPeroxide = new Material.Builder(78, makeLabsName("sodium_peroxide")) // Hardmode Material
                .dust()
                .color(0xecff80).iconSet(ROUGH)
                .components(Sodium, 2, Oxygen, 2)
                .build();

        IridiumDioxideResidue = new Material.Builder(79, makeLabsName("iridium_dioxide_residue")) // Hardmode Material
                .dust()
                .color(0x17182e).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Oxygen, 2, RareEarth, 1)
                .build();

        AmmoniumHexachloroiridiate = new Material.Builder(80, makeLabsName("ammonium_hexachloroiridiate")) // Hardmode
                                                                                                           // Material
                .dust()
                .color(0x644629).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Nitrogen, 2, Hydrogen, 8, Iridium, 1, Chlorine, 6)
                .build().setFormula("(NH4)2IrCl6", true);

        PlatinumGroupResidue = new Material.Builder(81, makeLabsName("platinum_group_residue")) // Hardmode Material
                .dust()
                .color(0x64632e).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Osmium, 1, Rhodium, 1, Ruthenium, 1, RareEarth, 1)
                .build();

        PalladiumRichAmmonia = new Material.Builder(82, makeLabsName("palladium_rich_ammonia"))
                .liquid()
                .color(0x808080)
                .flags(DISABLE_DECOMPOSITION)
                .components(Ammonia, 2, Palladium, 1, Chlorine, 1)
                .build();

        CrudePlatinumResidue = new Material.Builder(83, makeLabsName("crude_platinum_residue")) // Hardmode Material
                .dust()
                .color(0xfffbc5).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(PlatinumRaw, 1)
                .build();

        CrudePalladiumResidue = new Material.Builder(84, makeLabsName("crude_palladium_residue")) // Hardmode Material
                .dust()
                .color(0x909090).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(PalladiumRaw, 1)
                .build();

        IridiumGroupSludge = new Material.Builder(85, makeLabsName("iridium_group_sludge")) // Hardmode Material
                .dust()
                .color(0x644629).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Osmium, 1, Ruthenium, 1, RareEarth, 1)
                .build();

        RhodiumSulfateSolution = new Material.Builder(86, makeLabsName("rhodium_sulfate_solution"))
                .liquid()
                .color(0xffbb66)
                .flags(DISABLE_DECOMPOSITION)
                .components(RhodiumSulfate, 1, Water, 1)
                .build();

        CrudeRhodiumResidue = new Material.Builder(87, makeLabsName("crude_rhodium_residue")) // Hardmode Material
                .dust()
                .color(0x666666).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 2, Water, 1)
                .build();

        RhodiumSalt = new Material.Builder(88, makeLabsName("rhodium_salt")) // Hardmode Material
                .dust()
                .color(0x848484).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Salt, 2, Rhodium, 2, Chlorine, 6)
                .build().setFormula("(NaCl)2(RhCl3)2", true);

        AcidicIridiumDioxideSolution = new Material.Builder(89, makeLabsName("acidic_iridium_dioxide_solution"))
                .liquid()
                .color(0x27284e)
                .flags(DISABLE_DECOMPOSITION)
                .components(IridiumDioxideResidue, 1, HydrochloricAcid, 4)
                .build();

        PlatinumPalladiumLeachate = new Material.Builder(90, makeLabsName("platinum_palladium_leachate"))
                .liquid()
                .color(0xffffc5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Platinum, 1, Palladium, 1, AquaRegia, 1)
                .build();

        MethylFormate = new Material.Builder(91, makeLabsName("methyl_formate"))
                .liquid()
                .color(0xffaaaa)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 2)
                .build().setFormula("HCOOCH3", true);

        FormicAcid = new Material.Builder(92, makeLabsName("formic_acid"))
                .liquid()
                .color(0xffffc5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 1, Hydrogen, 2, Oxygen, 2)
                .build().setFormula("HCOOH", true);

        SodiumMethoxide = new Material.Builder(93, makeLabsName("sodium_methoxide")) // Hardmode Material
                .dust()
                .color(0xd0d0f0).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 1, Hydrogen, 3, Oxygen, 1, Sodium, 1)
                .build();
    }
}
