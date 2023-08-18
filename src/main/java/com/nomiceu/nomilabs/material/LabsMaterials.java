package com.nomiceu.nomilabs.material;

import gregtech.api.fluids.fluidType.FluidTypes;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.material.properties.BlastProperty;
import gregtech.api.unification.material.properties.ToolProperty;

import static com.nomiceu.nomilabs.util.RegistryNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;

public class LabsMaterials {
    /**
     * Elemental materials
     */
    public static Material Draconium; // 32001
    public static Material AwakenedDraconium; // 32002
    public static Material Omnium; // 32004
    public static Material Taranium; // 32109; HM only


    /**
    * Naq Line Materials
    */
    public static Material NaquadahOxide; // 32057; HM only
    public static Material Pyromorphite; // 32058; HM only
    public static Material NaquadahHydroxide; // 32059; HM only
    public static Material CaesiumHydroxide; // 32061; HM only
    public static Material Neocryolite; // 32062; HM only
    public static Material NaquadahOxidePetroSolution; // 32063; HM only
    public static Material NaquadahOxideAeroSolution; // 32064; HM only
    public static Material HotNaquadahOxideNeocryoliteSolution; // 32065; HM only


    /**
    * Endgame Materials
    */
    public static Material CrystalMatrix; // 32023
    public static Material Infinity; // 32026
    public static Material DraconicSuperconductor; // 32028
    public static Material KaptonK; // 32050; HM only


    /**
    * Taranium Line Materials
    */
    public static Material HexafluorosilicicAcid; // 32094
    public static Material DirtyHexafluorosilicicAcid; // 32095
    public static Material StoneResidue; // 32096; HM only
    public static Material UncommonResidue; // 32097; HM only
    public static Material OxidisedResidue; // 32098; HM only
    public static Material RefinedResidue; // 32099; HM only
    public static Material CleanInertResidue; // 32100; HM only
    public static Material UltraacidicResidue; // 32101
    public static Material XenicAcid; // 32102
    public static Material DustyHelium; // 32103
    public static Material TaraniumEnrichedHelium; // 32104
    public static Material TaraniumDepletedHelium; // 32105
    public static Material TritiumHydride; // 32106
    public static Material HeliumHydride; // 32107
    public static Material DioxygenDifluoride; // 32108


    /**
    * Platinum Line Materials
    */
    public static Material PlatinumMetallic; // 32067; HM only
    public static Material PalladiumMetallic; // 32068; HM only
    public static Material AmmoniumHexachloroplatinate; // 32069; HM only
    public static Material ChloroplatinicAcid; // 32070
    public static Material PotassiumBisulfate; // 32071; HM only
    public static Material PotassiumPyrosulfate; // 32072; HM only
    public static Material PotassiumSulfate; // 32073; HM only
    public static Material ZincSulfate; // 32074; HM only
    public static Material SodiumNitrate; // 32075; HM only
    public static Material RhodiumNitrate; // 32076; HM only
    public static Material SodiumRuthenate; // 32077; HM only
    public static Material SodiumPeroxide; // 32078; HM only
    public static Material IridiumDioxideResidue; // 32079; HM only
    public static Material AmmoniumHexachloroiridiate; // 32080; HM only
    public static Material PlatinumGroupResidue; // 32081; HM only
    public static Material PalladiumRichAmmonia; // 32082
    public static Material CrudePlatinumResidue; // 32083; HM only
    public static Material CrudePalladiumResidue; // 32084; HM only
    public static Material IridiumGroupSludge; // 32085; HM only
    public static Material RhodiumSulfateSolution; // 32086
    public static Material CrudeRhodiumResidue; // 32087; HM only
    public static Material RhodiumSalt; // 32088; HM only
    public static Material AcidicIridiumDioxideSolution; // 32089
    public static Material PlatinumPalladiumLeachate; // 32090
    public static Material MethylFormate; // 32091
    public static Material FormicAcid; // 32092
    public static Material SodiumMethoxide; // 32093; HM only


    /**
    * Thermal Materials
    */
    public static Material Ardite; // 32006
    public static Material Mana; // 32007
    public static Material Manyullyn; // 32008
    public static Material Signalum; // 32010
    public static Material Lumium; // 32017
    public static Material Enderium; // 32018
    public static Material ElectrumFlux; // 32019
    public static Material Mithril; // 32021


    /**
    * EIO Materials
    */
    public static Material DarkSteel; // 32003
    public static Material ConductiveIron; // 32011
    public static Material EnergeticAlloy; // 32012
    public static Material VibrantAlloy; // 32013
    public static Material PulsatingIron; // 32014
    public static Material ElectricalSteel; // 32015
    public static Material Soularium; // 32024
    public static Material EndSteel; // 32025


    /**
    * Chemical Line Materials
    */
    public static Material TungstenTrioxide; // 32032; HM only
    public static Material BerylliumOxide; // 32033; HM only
    public static Material NiobiumPentoxide; // 32034; HM only
    public static Material TantalumPentoxide; // 32035; HM only
    public static Material ManganeseDifluoride; // 32037; HM only
    public static Material MolybdenumTrioxide; // 32038; HM only
    public static Material LeadChloride; // 32039; HM only
    public static Material Wollastonite; // 32040; HM only
    public static Material SodiumMetavanadate; // 32041; HM only
    public static Material VanadiumPentoxide; // 32042; HM only
    public static Material AmmoniumMetavanadate; // 32043; HM only
    public static Material PhthalicAnhydride; // 32044; HM only
    public static Material Ethylanthraquinone; // 32045; HM only
    public static Material HydrogenPeroxide; // 32046; HM only
    public static Material Hydrazine; // 32047; HM only
    public static Material AcetoneAzine; // 32048; HM only
    public static Material GrapheneOxide; // 32049; HM only
    public static Material Durene; // 32051; HM only
    public static Material PyromelliticDianhydride; // 32052; HM only
    public static Material Dimethylformamide; // 32053; HM only
    public static Material Aminophenol; // 32054; HM only
    public static Material Oxydianiline; // 32055; HM only
    public static Material AntimonyPentafluoride; // 32056; HM only
    public static Material LeadMetasilicate; // 32066; HM only
    public static Material Butanol; // 32112
    public static Material PhosphorusTrichloride; // 32113
    public static Material PhosphorylChloride; // 32114
    public static Material TributylPhosphate; // 32115


    /**
    * Microverse Materials
    */
    public static Material Microversium; // 32027
    public static Material Osmiridium8020; // 32029
    public static Material Iridosmine8020; // 32030
    public static Material Kaemanite; // 32031
    public static Material Fluorite; // 32036
    public static Material Snowchestite; // 32060; HM only
    public static Material Darmstadtite; // 32110
    public static Material Dulysite; // 32111


    public static void init() {
        LabsElements.init();
        initNaqLine();
        initEndgame();
        initElement();
        initTaraniumLine();
        initPlatLine();
        initThermal();
        initEIO();
        initChemLine();
        initMicroverse();
    }

    private static void initNaqLine() {
        NaquadahOxide = new Material.Builder(32057, makeLabsName("naquadah_oxide")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x17ddd3).iconSet(MaterialIconSet.ROUGH)
                .components(Naquadah, 2, Oxygen, 3)
                .build();

        Pyromorphite = new Material.Builder(32058, makeLabsName("pyromorphite")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd3ed28).iconSet(MaterialIconSet.ROUGH)
                .components(Lead, 5, Phosphate, 3, Chlorine, 1)
                .build();

        NaquadahHydroxide = new Material.Builder(32059, makeLabsName("naquadah_hydroxide")) // Hardmode Material
                .dust()
                .color(0x1941a6).iconSet(MaterialIconSet.DULL)
                .components(Naquadah, 1, Hydrogen, 3, Oxygen, 3)
                .build();

        NaquadahHydroxide.setFormula("Nq(OH)3", true);

        CaesiumHydroxide = new Material.Builder(32061, makeLabsName("caesium_hydroxide")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xbd8340).iconSet(MaterialIconSet.DULL)
                .components(Caesium, 1, Oxygen, 1, Hydrogen, 1)
                .build();

        Neocryolite = new Material.Builder(32062, makeLabsName("neocryolite")) // Hardmode Material
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x3fd1aa)
                .components(Caesium, 3, Naquadah, 1, Fluorine, 6)
                .build();

        NaquadahOxidePetroSolution = new Material.Builder(32063, makeLabsName("naquadah_oxide_petro_solution")) // Hardmode Material
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x595c70)
                .build();

        NaquadahOxideAeroSolution = new Material.Builder(32064, makeLabsName("naquadah_oxide_aero_solution")) // Hardmode Material
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x6f7059)
                .build();

        HotNaquadahOxideNeocryoliteSolution = new Material.Builder(32065, makeLabsName("hot_naquadah_oxide_neocryolite_solution")) // Hardmode Material
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x658280)
                .fluidTemp(4700)
                .build();
    }

    private static void initEndgame() {
        CrystalMatrix = new Material.Builder(32023, makeLabsName("crystal_matrix"))
                .ingot().fluid()
                .color(0x70ecff).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE)
                .build();

        Infinity = new Material.Builder(32026, makeLabsName("infinity"))
                .ingot()
                .color(0x000000).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE)
                .components(Neutronium, 5)
                .build();

        Infinity.setFormula("∞");

        DraconicSuperconductor = new Material.Builder(32028, makeLabsName("draconic_superconductor"))
                .ingot()
                .color(0xf5f0f4).iconSet(MaterialIconSet.SHINY)
                .cableProperties(2147483647, 4, 0, true)
                .build();

        KaptonK = new Material.Builder(32050, makeLabsName("kapton_k")) // Hardmode Material
                .ingot().fluid()
                .color(0xffce52).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .components(Carbon, 22, Hydrogen, 10, Nitrogen, 2, Oxygen, 5)
                .build();

        KaptonK.setFormula("C6H2((CO)2N)2C6H4OC6H4", true);
    }

    private static void initTaraniumLine() {
        HexafluorosilicicAcid = new Material.Builder(32094, makeLabsName("hexafluorosilicic_acid"))
                .fluid()
                .color(0xd00010)
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6)
                .build();

        DirtyHexafluorosilicicAcid = new Material.Builder(32095, makeLabsName("dirty_hexafluorosilicic_acid"))
                .fluid()
                .color(0xe00030)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6, RareEarth, 1)
                .build();

        StoneResidue = new Material.Builder(32096, makeLabsName("stone_residue")) // Hardmode Material
                .dust()
                .color(0x4d4d4d).iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        UncommonResidue = new Material.Builder(32097, makeLabsName("uncommon_residue")) // Hardmode Material
                .dust()
                .color(0x4d4ded).iconSet(MaterialIconSet.FINE)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        OxidisedResidue = new Material.Builder(32098, makeLabsName("oxidised_residue")) // Hardmode Material
                .dust()
                .color(0xad4d4d).iconSet(MaterialIconSet.FINE)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        RefinedResidue = new Material.Builder(32099, makeLabsName("refined_residue")) // Hardmode Material
                .dust()
                .color(0x2a8a21).iconSet(MaterialIconSet.SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        CleanInertResidue = new Material.Builder(32100, makeLabsName("clean_inert_residue")) // Hardmode Material
                .dust()
                .color(0x3bbd2f).iconSet(MaterialIconSet.SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        UltraacidicResidue = new Material.Builder(32101, makeLabsName("ultraacidic_residue"))
                .fluid()
                .color(0xb0babf)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        XenicAcid = new Material.Builder(32102, makeLabsName("xenic_acid"))
                .fluid()
                .color(0xa567db)
                .components(Xenon, 1, Water, 1, Oxygen, 5, HydrogenPeroxide, 1)
                .build();

        XenicAcid.setFormula("H2XeO4", true);

        DustyHelium = new Material.Builder(32103, makeLabsName("dusty_helium"))
                .fluid(FluidTypes.GAS)
                .color(0xa040af)
                .flags(DISABLE_DECOMPOSITION)
                .components(Helium3, 1, RareEarth, 1)
                .build();

        TaraniumEnrichedHelium = new Material.Builder(32104, makeLabsName("taranium_enriched_helium"))
                .fluid(FluidTypes.GAS).plasma()
                .color(0x10c050)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        TaraniumDepletedHelium = new Material.Builder(32105, makeLabsName("taranium_depleted_helium"))
                .fluid(FluidTypes.GAS)
                .color(0x006010)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        TritiumHydride = new Material.Builder(32106, makeLabsName("tritium_hydride"))
                .fluid(FluidTypes.GAS)
                .color(0xd01010)
                .flags(DISABLE_DECOMPOSITION)
                .components(Tritium, 1, Hydrogen, 1)
                .build();

        HeliumHydride = new Material.Builder(32107, makeLabsName("helium_hydride"))
                .fluid(FluidTypes.GAS)
                .color(0xe6d62e)
                .flags(DISABLE_DECOMPOSITION)
                .components(Helium3, 1, Hydrogen, 1)
                .build();

        DioxygenDifluoride = new Material.Builder(32108, makeLabsName("dioxygen_difluoride"))
                .fluid().fluidTemp(80)
                .colorAverage()
                .components(Oxygen, 2, Fluorine, 2)
                .build();
    }

    private static void initPlatLine() {
        PlatinumMetallic = new Material.Builder(32067, makeLabsName("platinum_metallic")) // Hardmode Material
                .dust()
                .color(0xfffbc5).iconSet(MaterialIconSet.METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Platinum, 1, RareEarth, 1)
                .build();

        PalladiumMetallic = new Material.Builder(32068, makeLabsName("palladium_metallic")) // Hardmode Material
                .dust()
                .color(0x808080).iconSet(MaterialIconSet.METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Palladium, 1, RareEarth, 1)
                .build();

        AmmoniumHexachloroplatinate = new Material.Builder(32069, makeLabsName("ammonium_hexachloroplatinate")) // Hardmode Material
                .dust()
                .color(0xfef0c2).iconSet(MaterialIconSet.METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Nitrogen, 2, Hydrogen, 8, Platinum, 1, Chlorine, 6)
                .build();

        AmmoniumHexachloroplatinate.setFormula("(NH4)2PtCl6", true);

        ChloroplatinicAcid = new Material.Builder(32070, makeLabsName("chloroplatinic_acid"))
                .fluid()
                .color(0xfef0c2)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Platinum, 1, Chlorine, 6)
                .build();

        PotassiumBisulfate = new Material.Builder(32071, makeLabsName("potassium_bisulfate")) // Hardmode Material
                .dust()
                .color(0xfdbd68)
                .components(Potassium, 1, Hydrogen, 1, Sulfur, 1, Oxygen, 4)
                .build();

        PotassiumPyrosulfate = new Material.Builder(32072, makeLabsName("potassium_pyrosulfate")) // Hardmode Material
                .dust()
                .color(0xfbbb66)
                .components(Potassium, 2, Sulfur, 2, Oxygen, 7)
                .build();

        PotassiumSulfate = new Material.Builder(32073, makeLabsName("potassium_sulfate")) // Hardmode Material
                .dust()
                .color(0xf0b064).iconSet(MaterialIconSet.METALLIC)
                .components(Potassium, 2, Sulfur, 1, Oxygen, 4)
                .build();

        ZincSulfate = new Material.Builder(32074, makeLabsName("zinc_sulfate")) // Hardmode Material
                .dust()
                .color(0x846649).iconSet(MaterialIconSet.FINE)
                .components(Zinc, 1, Sulfur, 1, Oxygen, 4)
                .build();

        SodiumNitrate = new Material.Builder(32075, makeLabsName("sodium_nitrate")) // Hardmode Material
                .dust()
                .color(0x846684).iconSet(MaterialIconSet.ROUGH)
                .components(Sodium, 1, Nitrogen, 1, Oxygen, 3)
                .build();

        RhodiumNitrate = new Material.Builder(32076, makeLabsName("rhodium_nitrate")) // Hardmode Material
                .dust()
                .color(0x776649).iconSet(MaterialIconSet.FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 1, Nitrogen, 3, Oxygen, 9)
                .build();

        RhodiumNitrate.setFormula("Rh(NO3)3", true);

        SodiumRuthenate = new Material.Builder(32077, makeLabsName("sodium_ruthenate")) // Hardmode Material
                .dust()
                .color(0x3a40cb).iconSet(MaterialIconSet.SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 2, Ruthenium, 1, Oxygen, 4)
                .build();

        SodiumPeroxide = new Material.Builder(32078, makeLabsName("sodium_peroxide")) // Hardmode Material
                .dust()
                .color(0xecff80).iconSet(MaterialIconSet.ROUGH)
                .components(Sodium, 2, Oxygen, 2)
                .build();

        IridiumDioxideResidue = new Material.Builder(32079, makeLabsName("iridium_dioxide_residue")) // Hardmode Material
                .dust()
                .color(0x17182e).iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Oxygen, 2, RareEarth, 1)
                .build();

        AmmoniumHexachloroiridiate = new Material.Builder(32080, makeLabsName("ammonium_hexachloroiridiate")) // Hardmode Material
                .dust()
                .color(0x644629).iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Nitrogen, 2, Hydrogen, 8, Iridium, 1, Chlorine, 6)
                .build();

        AmmoniumHexachloroiridiate.setFormula("(NH4)2IrCl6", true);

        PlatinumGroupResidue = new Material.Builder(32081, makeLabsName("platinum_group_residue")) // Hardmode Material
                .dust()
                .color(0x64632e).iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Osmium, 1, Rhodium, 1, Ruthenium, 1, RareEarth, 1)
                .build();

        PalladiumRichAmmonia = new Material.Builder(32082, makeLabsName("palladium_rich_ammonia"))
                .fluid()
                .color(0x808080)
                .flags(DISABLE_DECOMPOSITION)
                .components(Ammonia, 2, Palladium, 1, Chlorine, 1)
                .build();

        CrudePlatinumResidue = new Material.Builder(32083, makeLabsName("crude_platinum_residue")) // Hardmode Material
                .dust()
                .color(0xfffbc5).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(PlatinumRaw, 1)
                .build();

        CrudePalladiumResidue = new Material.Builder(32084, makeLabsName("crude_palladium_residue")) // Hardmode Material
                .dust()
                .color(0x909090).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(PalladiumRaw, 1)
                .build();

        IridiumGroupSludge = new Material.Builder(32085, makeLabsName("iridium_group_sludge")) // Hardmode Material
                .dust()
                .color(0x644629).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Osmium, 1, Ruthenium, 1, RareEarth, 1)
                .build();

        RhodiumSulfateSolution = new Material.Builder(32086, makeLabsName("rhodium_sulfate_solution"))
                .fluid()
                .color(0xffbb66)
                .flags(DISABLE_DECOMPOSITION)
                .components(RhodiumSulfate, 1, Water, 1)
                .build();

        CrudeRhodiumResidue = new Material.Builder(32087, makeLabsName("crude_rhodium_residue")) // Hardmode Material
                .dust()
                .color(0x666666).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 2, Water, 1)
                .build();

        RhodiumSalt = new Material.Builder(32088, makeLabsName("rhodium_salt")) // Hardmode Material
                .dust()
                .color(0x848484).iconSet(MaterialIconSet.SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Salt, 2, Rhodium, 2, Chlorine, 6)
                .build();

        RhodiumSalt.setFormula("(NaCl)2(RhCl3)2", true);

        AcidicIridiumDioxideSolution = new Material.Builder(32089, makeLabsName("acidic_iridium_dioxide_solution"))
                .fluid()
                .color(0x27284e)
                .flags(DISABLE_DECOMPOSITION)
                .components(IridiumDioxideResidue, 1, HydrochloricAcid, 4)
                .build();

        PlatinumPalladiumLeachate = new Material.Builder(32090, makeLabsName("platinum_palladium_leachate"))
                .fluid()
                .color(0xffffc5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Platinum, 1, Palladium, 1, AquaRegia, 1)
                .build();

        MethylFormate = new Material.Builder(32091, makeLabsName("methyl_formate"))
                .fluid()
                .color(0xffaaaa)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 2)
                .build();

        MethylFormate.setFormula("HCOOCH3", true);

        FormicAcid = new Material.Builder(32092, makeLabsName("formic_acid"))
                .fluid()
                .color(0xffffc5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 1, Hydrogen, 2, Oxygen, 2)
                .build();

        FormicAcid.setFormula("HCOOH", true);

        SodiumMethoxide = new Material.Builder(32093, makeLabsName("sodium_methoxide")) // Hardmode Material
                .dust()
                .color(0xd0d0f0).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 1, Hydrogen, 3, Oxygen, 1, Sodium, 1)
                .build();
    }

    private static void initThermal() {
        Ardite = new Material.Builder(32006, makeLabsName("ardite"))
                .ingot().fluid()
                .color(0xad2f05).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE)
                .components(RedSteel, 3, Blaze, 1)
                .build();

        Mana = new Material.Builder(32007, makeLabsName("mana"))
                .flags(DISABLE_DECOMPOSITION)
                .build();

        Manyullyn = new Material.Builder(32008, makeLabsName("manyullyn"))
                .ingot().fluid()
                .color(0x9949cc).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE)
                .components(Ardite, 4, Cobalt, 4, Mana, 1)
                .build();

        Signalum = new Material.Builder(32010, makeLabsName("signalum"))
                .ingot().fluid()
                .color(0xff7f0f).iconSet(MaterialIconSet.SHINY)
                .blastTemp(4000, BlastProperty.GasTier.MID, 120, 12800)
                .flags(GENERATE_PLATE, GENERATE_DENSE, GENERATE_ROD, GENERATE_GEAR)
                .components(AnnealedCopper, 4, Ardite, 2, RedAlloy, 2)
                .cableProperties(32768, 1, 0, true)
                .build();

        Lumium = new Material.Builder(32017, makeLabsName("lumium"))
                .ingot().fluid()
                .color(0xf6ff99).iconSet(MaterialIconSet.BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_FINE_WIRE)
                .blastTemp(4500, BlastProperty.GasTier.MID, 120, 14400)
                .components(TinAlloy, 4, SterlingSilver, 2)
                .cableProperties(8192, 1, 0, true)
                .build();

        Enderium = new Material.Builder(32018, makeLabsName("enderium"))
                .ingot().fluid()
                .color(0x1f6b62).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_FINE_WIRE)
                .blastTemp(6400, BlastProperty.GasTier.HIGHEST, 120, 20800)
                .components(Lead, 4, Platinum, 2, BlueSteel, 1, Osmium, 1)
                .cableProperties(131072, 1, 0, true)
                .build();

        ElectrumFlux = new Material.Builder(32019, makeLabsName("electrum_flux"))
                .ingot().fluid()
                .color(0xf7be20).iconSet(MaterialIconSet.BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .blastTemp(1100)
                .components(Electrum, 6, Lumium, 1, Signalum, 1)
                .build();

        Mithril = new Material.Builder(32021, makeLabsName("mithril"))
                .ingot()
                .color(0x428fdb).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE, GENERATE_GEAR, NO_UNIFICATION)
                .components(Titanium, 1, Mana, 1)
                .build();
    }

    private static void initEIO() {
        DarkSteel = new Material.Builder(32003, makeLabsName("dark_steel"))
                .ingot().fluid()
                .color(0x414751).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .components(Iron, 1)
                .build();

        ConductiveIron = new Material.Builder(32011, makeLabsName("conductive_iron"))
                .ingot().fluid()
                .color(0xf7b29b).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .components(Iron, 1, Redstone, 1)
                .cableProperties(32, 1, 0, true)
                .build();

        EnergeticAlloy = new Material.Builder(32012, makeLabsName("energetic_alloy"))
                .ingot().fluid()
                .color(0xffb545).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .blastTemp(1250, BlastProperty.GasTier.LOW, 120, 400)
                .components(Gold, 2, Redstone, 1, Glowstone, 1)
                .cableProperties(128, 1, 0, true)
                .build();

        VibrantAlloy = new Material.Builder(32013, makeLabsName("vibrant_alloy"))
                .ingot().fluid()
                .color(0xa4ff70).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_ROD, GENERATE_BOLT_SCREW)
                .blastTemp(1350, BlastProperty.GasTier.LOW, 120, 600)
                .components(EnergeticAlloy, 1, EnderPearl, 1)
                .cableProperties(512, 1, 0, true)
                .build();

        PulsatingIron = new Material.Builder(32014, makeLabsName("pulsating_iron"))
                .ingot().fluid()
                .color(0x6ae26e).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .components(Iron, 1)
                .cableProperties(8, 1, 0, true)
                .build();

        ElectricalSteel = new Material.Builder(32015, makeLabsName("electrical_steel"))
                .ingot().fluid()
                .color(0xb2c0c1).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .components(Steel, 1, Silicon, 1)
                .build();

        Soularium = new Material.Builder(32024, makeLabsName("soularium"))
                .ingot().fluid()
                .color(0x7c674d).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE)
                .components(Gold, 1)
                .build();

        EndSteel = new Material.Builder(32025, makeLabsName("end_steel"))
                .ingot().fluid()
                .color(0xd6d980).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .toolStats(new ToolProperty(4.0f, 3.5f, 1024, 3))
                .cableProperties(2048,1,0,true)
                .build();
    }

    private static void initChemLine() {
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

    private static void initMicroverse() {
        Microversium = new Material.Builder(32027, makeLabsName("microversium"))
                .ingot()
                .color(0x9b61b8).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE, GENERATE_FRAME)
                .build();

        Osmiridium8020 = new Material.Builder(32029, makeLabsName("osmiridium_8020"))
                .dust().ore()
                .components(Osmium, 4, Iridium, 1)
                .colorAverage()
                .addOreByproducts(Osmium, Iridium, Ruthenium)
                .build();

        Iridosmine8020 = new Material.Builder(32030, makeLabsName("iridosmine_8020"))
                .dust().ore()
                .components(Iridium, 4, Osmium, 1)
                .colorAverage()
                .addOreByproducts(Iridium, Osmium, Rhodium)
                .build();

        Kaemanite = new Material.Builder(32031, makeLabsName("kaemanite"))
                .dust().ore()
                .components(Trinium, 1, Tantalum, 1, Oxygen, 4)
                .color(0xe7413c).iconSet(MaterialIconSet.BRIGHT)
                .addOreByproducts(Niobium, TriniumSulfide, Trinium)
                .build();

        Fluorite = new Material.Builder(32036, makeLabsName("fluorite"))
                .dust().ore()
                .color(0xFFFC9E).iconSet(MaterialIconSet.ROUGH)
                .components(Calcium, 1, Fluorine, 2)
                .addOreByproducts(Sphalerite, Bastnasite, Topaz)
                .build();

        Snowchestite = new Material.Builder(32060, makeLabsName("snowchestite")) // Hardmode Material
                .dust().ore()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x274c9f).iconSet(MaterialIconSet.SHINY)
                .components(NaquadahOxide, 3, Pyromorphite, 1)
                .addOreByproducts(Chalcopyrite, VanadiumMagnetite, NaquadahHydroxide)
                .build();

        Darmstadtite = new Material.Builder(32110, makeLabsName("darmstadtite"))
                .dust().ore(2, 1)
                .colorAverage().iconSet(MaterialIconSet.DULL)
                .components(Darmstadtium, 2, Sulfur, 3)
                .addOreByproducts(RareEarth, RhodiumSulfate, Darmstadtium)
                .build();

        Dulysite = new Material.Builder(32111, makeLabsName("dulysite"))
                .gem().ore(2, 1)
                .colorAverage().iconSet(MaterialIconSet.RUBY)
                .components(Duranium, 1, Chlorine, 3)
                .addOreByproducts(Sphalerite, Duranium, Europium)
                .build();
    }
}
