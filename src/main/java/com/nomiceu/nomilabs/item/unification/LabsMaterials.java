package com.nomiceu.nomilabs.item.unification;

import com.nomiceu.nomilabs.util.RegistryNames;
import gregtech.api.fluids.fluidType.FluidTypes;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.material.properties.BlastProperty;
import gregtech.api.unification.material.properties.ToolProperty;

import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;

public class LabsMaterials {

    public static Material Draconium;
    public static Material AwakenedDraconium;
    public static Material DarkSteel;
    public static Material Omnium;
    public static Material Ardite;
    public static Material Mana;
    public static Material Manyullyn;
    public static Material Signalum;
    public static Material ConductiveIron;
    public static Material EnergeticAlloy;
    public static Material VibrantAlloy;
    public static Material PulsatingIron;
    public static Material ElectricalSteel;
    public static Material Lumium;
    public static Material Enderium;
    public static Material ElectrumFlux;
    public static Material Mithril;
    public static Material CrystalMatrix;
    public static Material Soularium;
    public static Material EndSteel;
    public static Material Infinity;
    public static Material Microversium;
    public static Material DraconicSuperconductor;
    public static Material Osmiridium8020;
    public static Material Iridosmine8020;
    public static Material Kaemanite;
    public static Material TungstenTrioxide;
    public static Material BerylliumOxide;
    public static Material NiobiumPentoxide;
    public static Material TantalumPentoxide;
    public static Material Fluorite;
    public static Material ManganeseDifluoride;
    public static Material MolybdenumTrioxide;
    public static Material LeadChloride;
    public static Material Wollastonite;
    public static Material SodiumMetavanadate;
    public static Material VanadiumPentoxide;
    public static Material AmmoniumMetavanadate;
    public static Material PhthalicAnhydride;
    public static Material Ethylanthraquinone;
    public static Material HydrogenPeroxide;
    public static Material Hydrazine;
    public static Material AcetoneAzine;
    public static Material GrapheneOxide;
    public static Material KaptonK;
    public static Material Durene;
    public static Material PyromelliticDianhydride;
    public static Material Dimethylformamide;
    public static Material Aminophenol;
    public static Material Oxydianiline;
    public static Material AntimonyPentafluoride;
    public static Material NaquadahOxide;
    public static Material Pyromorphite;
    public static Material NaquadahHydroxide;
    public static Material Snowchestite;
    public static Material CaesiumHydroxide;
    public static Material Neocryolite;
    public static Material NaquadahOxidePetroSolution;
    public static Material NaquadahOxideAeroSolution;
    public static Material HotNaquadahOxideNeocryoliteSolution;
    public static Material LeadMetasilicate;
    public static Material PlatinumMetallic;
    public static Material PalladiumMetallic;
    public static Material AmmoniumHexachloroplatinate;
    public static Material ChloroplatinicAcid;
    public static Material PotassiumBisulfate;
    public static Material PotassiumPyrosulfate;
    public static Material PotassiumSulfate;
    public static Material ZincSulfate;
    public static Material SodiumNitrate;
    public static Material RhodiumNitrate;
    public static Material SodiumRuthenate;
    public static Material SodiumPeroxide;
    public static Material IridiumDioxideResidue;
    public static Material AmmoniumHexachloroiridiate;
    public static Material PlatinumGroupResidue;
    public static Material PalladiumRichAmmonia;
    public static Material CrudePlatinumResidue;
    public static Material CrudePalladiumResidue;
    public static Material IridiumGroupSludge;
    public static Material RhodiumSulfateSolution;
    public static Material CrudeRhodiumResidue;
    public static Material RhodiumSalt;
    public static Material AcidicIridiumDioxideSolution;
    public static Material PlatinumPalladiumLeachate;
    public static Material MethylFormate;
    public static Material FormicAcid;
    public static Material SodiumMethoxide;
    public static Material HexafluorosilicicAcid;
    public static Material DirtyHexafluorosilicicAcid;
    public static Material StoneResidue;
    public static Material UncommonResidue;
    public static Material OxidisedResidue;
    public static Material RefinedResidue;
    public static Material CleanInertResidue;
    public static Material UltraacidicResidue;
    public static Material XenicAcid;
    public static Material DustyHelium;
    public static Material TaraniumEnrichedHelium;
    public static Material TaraniumDepletedHelium;
    public static Material TritiumHydride;
    public static Material HeliumHydride;
    public static Material DioxygenDifluoride;
    public static Material Taranium;
    public static Material Darmstadtite;
    public static Material Dulysite;
    public static Material Butanol;
    public static Material PhosphorusTrichloride;
    public static Material PhosphorylChloride;
    public static Material TributylPhosphate;

    public static void init() {

        Draconium = new Material.Builder(32001, RegistryNames.makeLabsName("draconium"))
                .ingot().fluid().ore()
                .element(LabsElements.Dc)
                .color(0xbe49ed).iconSet(MaterialIconSet.METALLIC)
                .blastTemp(6800, BlastProperty.GasTier.HIGHER)
                .cableProperties(524288, 1, 0, true)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_DENSE)
                .build();

        AwakenedDraconium = new Material.Builder(32002, RegistryNames.makeLabsName("awakened_draconium"))
                .ingot().fluid()
                .element(LabsElements.ADc) // this is erroring for some reason?
                .color(0xf58742).iconSet(MaterialIconSet.METALLIC)
                .flags(NO_SMELTING, GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR)
                .build();

        DarkSteel = new Material.Builder(32003, RegistryNames.makeLabsName("dark_steel"))
                .ingot().fluid()
                .color(0x414751).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .components(Iron , 1)
                .build();

        Omnium = new Material.Builder(32004, RegistryNames.makeLabsName("omnium"))
                .ingot().fluid()
                .element(LabsElements.Nm)
                .color(0x84053e).iconSet(MaterialIconSet.SHINY)
                .cableProperties(2147483647, 64, 0, true)
                .build();

        Ardite = new Material.Builder(32006, RegistryNames.makeLabsName("ardite"))
                .ingot().fluid()
                .color(0xad2f05).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE)
                .components(RedSteel , 3, Blaze , 1)
                .build();


        Mana = new Material.Builder(32007, RegistryNames.makeLabsName("mana"))
                .flags(DISABLE_DECOMPOSITION)
                .build();

        Manyullyn = new Material.Builder(32008, RegistryNames.makeLabsName("manyullyn"))
                .ingot().fluid()
                .color(0x9949cc).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE)
                .components(Ardite, 4, Cobalt, 4, Mana, 1)
                .build();

        Signalum = new Material.Builder(32010, RegistryNames.makeLabsName("signalum"))
                .ingot().fluid()
                .color(0xff7f0f).iconSet(MaterialIconSet.SHINY)
                .blastTemp(4000, BlastProperty.GasTier.MID, 120, 12800)
                .flags(GENERATE_PLATE, GENERATE_DENSE, GENERATE_ROD, GENERATE_GEAR)
                .components(AnnealedCopper, 4, Ardite, 2, RedAlloy, 2)
                .cableProperties(32768, 1, 0, true)
                .build();

        ConductiveIron = new Material.Builder(32011, RegistryNames.makeLabsName("conductive_iron"))
                .ingot().fluid()
                .color(0xf7b29b).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .components(Iron, 1, Redstone, 1)
                .cableProperties(32, 1, 0, true)
                .build();

        EnergeticAlloy = new Material.Builder(32012, RegistryNames.makeLabsName("energetic_alloy"))
                .ingot().fluid()
                .color(0xffb545).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .blastTemp(1250, BlastProperty.GasTier.LOW, 120, 400)
                .components(Gold, 2, Redstone, 1, Glowstone, 1)
                .cableProperties(128, 1, 0, true)
                .build();

        VibrantAlloy = new Material.Builder(32013, RegistryNames.makeLabsName("vibrant_alloy"))
                .ingot().fluid()
                .color(0xa4ff70).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_ROD, GENERATE_BOLT_SCREW)
                .blastTemp(1350, BlastProperty.GasTier.LOW, 120, 600)
                .components(EnergeticAlloy, 1, EnderPearl, 1)
                .cableProperties(512, 1, 0, true)
                .build();

        PulsatingIron = new Material.Builder(32014, RegistryNames.makeLabsName("pulsating_iron"))
                .ingot().fluid()
                .color(0x6ae26e).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .components(Iron, 1)
                .cableProperties(8, 1, 0, true)
                .build();

        ElectricalSteel = new Material.Builder(32015, RegistryNames.makeLabsName("electrical_steel"))
                .ingot().fluid()
                .color(0xb2c0c1).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .components(Steel, 1, Silicon, 1)
                .build();

        Lumium = new Material.Builder(32017, RegistryNames.makeLabsName("lumium"))
                .ingot().fluid()
                .color(0xf6ff99).iconSet(MaterialIconSet.BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_FINE_WIRE)
                .blastTemp(4500, BlastProperty.GasTier.MID, 120, 14400)
                .components(TinAlloy, 4, SterlingSilver, 2)
                .cableProperties(8192, 1, 0, true)
                .build();

        Enderium = new Material.Builder(32018, RegistryNames.makeLabsName("enderium"))
                .ingot().fluid()
                .color(0x1f6b62).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_FINE_WIRE)
                .blastTemp(6400, BlastProperty.GasTier.HIGHEST, 120, 20800)
                .components(Lead, 4, Platinum, 2, BlueSteel, 1, Osmium, 1)
                .cableProperties(131072, 1, 0, true)
                .build();

        ElectrumFlux = new Material.Builder(32019, RegistryNames.makeLabsName("electrum_flux"))
                .ingot().fluid()
                .color(0xf7be20).iconSet(MaterialIconSet.BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .blastTemp(1100)
                .components(Electrum, 6, Lumium, 1, Signalum, 1)
                .build();

        Mithril = new Material.Builder(32021, RegistryNames.makeLabsName("mithril"))
                .ingot()
                .color(0x428fdb).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE, GENERATE_GEAR, NO_UNIFICATION)
                .components(Titanium, 1, Mana, 1)
                .build();

        CrystalMatrix = new Material.Builder(32023, RegistryNames.makeLabsName("crystal_matrix"))
                .ingot().fluid()
                .color(0x70ecff).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE)
                .build();

        Soularium = new Material.Builder(32024, RegistryNames.makeLabsName("soularium"))
                .ingot().fluid()
                .color(0x7c674d).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE)
                .components(Gold, 1)
                .build();

        EndSteel = new Material.Builder(32025, RegistryNames.makeLabsName("end_steel"))
                .ingot().fluid()
                .color(0xd6d980).iconSet(MaterialIconSet.METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .toolStats(new ToolProperty(4.0f, 3.5f, 1024, 3))
                .cableProperties(2048,1,0,true)
                .build();

        Infinity = new Material.Builder(32026, RegistryNames.makeLabsName("infinity"))
                .ingot()
                .color(0x000000).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE)
                .components(Neutronium, 5)
                .build();

        Infinity.setFormula("∞");

        Microversium = new Material.Builder(32027, RegistryNames.makeLabsName("microversium"))
                .ingot()
                .color(0x9b61b8).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE, GENERATE_FRAME)
                .build();

        DraconicSuperconductor = new Material.Builder(32028, RegistryNames.makeLabsName("draconic_superconductor"))
                .ingot()
                .color(0xf5f0f4).iconSet(MaterialIconSet.SHINY)
                .cableProperties(2147483647, 4, 0, true)
                .build();

        Osmiridium8020 = new Material.Builder(32029, RegistryNames.makeLabsName("osmiridium_8020"))
                .dust().ore()
                .components(Osmium, 4, Iridium, 1)
                .colorAverage()
                .addOreByproducts(Osmium, Iridium, Ruthenium)
                .build();

        Iridosmine8020 = new Material.Builder(32030, RegistryNames.makeLabsName("iridosmine_8020"))
                .dust().ore()
                .components(Iridium, 4, Osmium, 1)
                .colorAverage()
                .addOreByproducts(Iridium, Osmium, Rhodium)
                .build();

        Kaemanite = new Material.Builder(32031, RegistryNames.makeLabsName("kaemanite"))
                .dust().ore()
                .components(Trinium, 1, Tantalum, 1, Oxygen, 4)
                .color(0xe7413c).iconSet(MaterialIconSet.BRIGHT)
                .addOreByproducts(Niobium, TriniumSulfide, Trinium)
                .build();

        TungstenTrioxide = new Material.Builder(32032, RegistryNames.makeLabsName("tungsten_trioxide"))
                .dust()
                .color(0xC7D300).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Tungsten, 1, Oxygen, 3)
                .build();

        BerylliumOxide = new Material.Builder(32033, RegistryNames.makeLabsName("beryllium_oxide"))
                .ingot()
                .color(0x54C757).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_ROD, GENERATE_RING)
                .components(Beryllium, 1, Oxygen, 1)
                .build();

        NiobiumPentoxide = new Material.Builder(32034, RegistryNames.makeLabsName("niobium_pentoxide"))
                .dust()
                .color(0xBAB0C3).iconSet(MaterialIconSet.ROUGH)
                .components(Niobium, 2, Oxygen, 5)
                .build();

        TantalumPentoxide = new Material.Builder(32035, RegistryNames.makeLabsName("tantalum_pentoxide"))
                .dust()
                .color(0x72728A).iconSet(MaterialIconSet.ROUGH)
                .components(Tantalum, 2, Oxygen, 5)
                .build();

        Fluorite = new Material.Builder(32036, RegistryNames.makeLabsName("fluorite"))
                .dust().ore()
                .color(0xFFFC9E).iconSet(MaterialIconSet.ROUGH)
                .components(Calcium, 1, Fluorine, 2)
                .addOreByproducts(Sphalerite, Bastnasite, Topaz)
                .build();

        ManganeseDifluoride = new Material.Builder(32037, RegistryNames.makeLabsName("manganese_difluoride"))
                .dust()
                .color(0xEF4B3D).iconSet(MaterialIconSet.ROUGH)
                .components(Manganese, 1, Fluorine, 2)
                .build();

        MolybdenumTrioxide = new Material.Builder(32038, RegistryNames.makeLabsName("molybdenum_trioxide"))
                .dust()
                .color(0xCBCFDA).iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Molybdenum, 1, Oxygen, 3)
                .build();

        LeadChloride = new Material.Builder(32039, RegistryNames.makeLabsName("lead_chloride"))
                .dust()
                .color(0xF3F3F3).iconSet(MaterialIconSet.ROUGH)
                .components(Lead, 1, Chlorine, 2)
                .build();

        Wollastonite = new Material.Builder(32040, RegistryNames.makeLabsName("wollastonite"))
                .dust()
                .color(0xF7F7E7).iconSet(MaterialIconSet.BRIGHT)
                .components(Calcium, 1, Silicon, 1, Oxygen, 3)
                .build();

        SodiumMetavanadate = new Material.Builder(32041, RegistryNames.makeLabsName("sodium_metavanadate"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xe6bb22).iconSet(MaterialIconSet.DULL)
                .components(Sodium, 1, Vanadium, 1, Oxygen, 3)
                .build();

        VanadiumPentoxide = new Material.Builder(32042, RegistryNames.makeLabsName("vanadium_pentoxide"))
                .dust()
                .color(0xffcf33).iconSet(MaterialIconSet.ROUGH)
                .components(Vanadium, 2, Oxygen, 5)
                .build();

        AmmoniumMetavanadate = new Material.Builder(32043, RegistryNames.makeLabsName("ammonium_metavanadate"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xf7e37e).iconSet(MaterialIconSet.DULL)
                .components(Nitrogen, 1, Hydrogen, 4, Vanadium, 1, Oxygen, 3)
                .build();

        PhthalicAnhydride = new Material.Builder(32044, RegistryNames.makeLabsName("phthalic_anhydride"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xeeaaee).iconSet(MaterialIconSet.DULL)
                .components(Carbon, 8, Hydrogen, 4, Oxygen, 3)
                .build();

        PhthalicAnhydride.setFormula("C6H4(CO)2O", true);

        Ethylanthraquinone = new Material.Builder(32045, RegistryNames.makeLabsName("ethylanthraquinone"))
                .dust()
                .color(0xf1e181)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 16, Hydrogen, 12, Oxygen, 2)
                .build();

        Ethylanthraquinone.setFormula("C6H4(CO)2C6H3(CH2CH3)", true);

        HydrogenPeroxide = new Material.Builder(32046, RegistryNames.makeLabsName("hydrogen_peroxide"))
                .fluid()
                .color(0xd2ffff)
                .components(Hydrogen, 2, Oxygen, 2)
                .build();

        Hydrazine = new Material.Builder(32047, RegistryNames.makeLabsName("hydrazine"))
                .fluid()
                .color(0xb50707)
                .components(Nitrogen, 2, Hydrogen, 4)
                .build();

        AcetoneAzine = new Material.Builder(32048, RegistryNames.makeLabsName("acetone_azine"))
                .fluid()
                .color(0xa1e1e1)
                .components(Carbon, 6, Hydrogen, 12, Nitrogen, 2)
                .build();

        AcetoneAzine.setFormula("((CH3)2(CN))2", true);

        GrapheneOxide = new Material.Builder(32049, RegistryNames.makeLabsName("graphene_oxide"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x777777).iconSet(MaterialIconSet.ROUGH)
                .components(Graphene, 1, Oxygen, 1)
                .build();

        KaptonK = new Material.Builder(32050, RegistryNames.makeLabsName("kapton_k"))
                .ingot().fluid()
                .color(0xffce52).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .components(Carbon, 22, Hydrogen, 10, Nitrogen, 2, Oxygen, 5)
                .build();

        KaptonK.setFormula("C6H2((CO)2N)2C6H4OC6H4", true);

        Durene = new Material.Builder(32051, RegistryNames.makeLabsName("durene"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x336040).iconSet(MaterialIconSet.FINE)
                .components(Carbon, 10, Hydrogen, 14)
                .build();

        Durene.setFormula("C6H2(CH3)4", true);

        PyromelliticDianhydride = new Material.Builder(32052, RegistryNames.makeLabsName("pyromellitic_dianhydride"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xf0ead6).iconSet(MaterialIconSet.ROUGH)
                .components(Carbon, 10, Hydrogen, 2, Oxygen, 6)
                .build();

        PyromelliticDianhydride.setFormula("C6H2(C2O3)2", true);

        Dimethylformamide = new Material.Builder(32053, RegistryNames.makeLabsName("dimethylformamide"))
                .fluid()
                .color(0x42bdff)
                .components(Carbon, 3, Hydrogen, 7, Nitrogen, 1, Oxygen, 1)
                .build();

        Aminophenol = new Material.Builder(32054, RegistryNames.makeLabsName("aminophenol"))
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xff7f50)
                .components(Carbon, 6, Hydrogen, 7, Nitrogen, 1, Oxygen, 1)
                .build();

        Oxydianiline = new Material.Builder(32055, RegistryNames.makeLabsName("oxydianiline"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xf0e130).iconSet(MaterialIconSet.DULL)
                .components(Carbon, 12, Hydrogen, 12, Nitrogen, 2, Oxygen, 1)
                .build();

        Oxydianiline.setFormula("O(C6H4NH2)2", true);

        AntimonyPentafluoride = new Material.Builder(32056, RegistryNames.makeLabsName("antimony_pentafluoride"))
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xe3f1f1)
                .components(Antimony, 1, Fluorine, 5)
                .build();

        NaquadahOxide = new Material.Builder(32057, RegistryNames.makeLabsName("naquadah_oxide"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x17ddd3).iconSet(MaterialIconSet.ROUGH)
                .components(Naquadah, 2, Oxygen, 3)
                .build();

        Pyromorphite = new Material.Builder(32058, RegistryNames.makeLabsName("pyromorphite"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd3ed28).iconSet(MaterialIconSet.ROUGH)
                .components(Lead, 5, Phosphate, 3, Chlorine, 1)
                .build();

        NaquadahHydroxide = new Material.Builder(32059, RegistryNames.makeLabsName("naquadah_hydroxide"))
                .dust()
                .color(0x1941a6).iconSet(MaterialIconSet.DULL)
                .components(Naquadah, 1, Hydrogen, 3, Oxygen, 3)
                .build();

        NaquadahHydroxide.setFormula("Nq(OH)3", true);

        Snowchestite = new Material.Builder(32060, RegistryNames.makeLabsName("snowchestite"))
                .dust().ore()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x274c9f).iconSet(MaterialIconSet.SHINY)
                .components(NaquadahOxide, 3, Pyromorphite, 1)
                .addOreByproducts(Chalcopyrite, VanadiumMagnetite, NaquadahHydroxide)
                .build();

        CaesiumHydroxide = new Material.Builder(32061, RegistryNames.makeLabsName("caesium_hydroxide"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xbd8340).iconSet(MaterialIconSet.DULL)
                .components(Caesium, 1, Oxygen, 1, Hydrogen, 1)
                .build();

        Neocryolite = new Material.Builder(32062, RegistryNames.makeLabsName("neocryolite"))
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x3fd1aa)
                .components(Caesium, 3, Naquadah, 1, Fluorine, 6)
                .build();

        NaquadahOxidePetroSolution = new Material.Builder(32063, RegistryNames.makeLabsName("naquadah_oxide_petro_solution"))
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x595c70)
                .build();

        NaquadahOxideAeroSolution = new Material.Builder(32064, RegistryNames.makeLabsName("naquadah_oxide_aero_solution"))
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x6f7059)
                .build();

        HotNaquadahOxideNeocryoliteSolution = new Material.Builder(32065, RegistryNames.makeLabsName("hot_naquadah_oxide_neocryolite_solution"))
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x658280)
                .fluidTemp(4700)
                .build();

        LeadMetasilicate = new Material.Builder(32066, RegistryNames.makeLabsName("lead_metasilicate"))
                .dust()
                .color(0xF7F7E7).iconSet(MaterialIconSet.DULL)
                .components(Lead, 1, Silicon, 1, Oxygen, 3)
                .build();

        PlatinumMetallic = new Material.Builder(32067, RegistryNames.makeLabsName("platinum_metallic"))
                .dust()
                .color(0xfffbc5).iconSet(MaterialIconSet.METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Platinum, 1, RareEarth, 1)
                .build();

        PalladiumMetallic = new Material.Builder(32068, RegistryNames.makeLabsName("palladium_metallic"))
                .dust()
                .color(0x808080).iconSet(MaterialIconSet.METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Palladium, 1, RareEarth, 1)
                .build();

        AmmoniumHexachloroplatinate = new Material.Builder(32069, RegistryNames.makeLabsName("ammonium_hexachloroplatinate"))
                .dust()
                .color(0xfef0c2).iconSet(MaterialIconSet.METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Nitrogen, 2, Hydrogen, 8, Platinum, 1, Chlorine, 6)
                .build();

        AmmoniumHexachloroplatinate.setFormula("(NH4)2PtCl6", true);

        ChloroplatinicAcid = new Material.Builder(32070, RegistryNames.makeLabsName("chloroplatinic_acid"))
                .fluid()
                .color(0xfef0c2)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Platinum, 1, Chlorine, 6)
                .build();

        PotassiumBisulfate = new Material.Builder(32071, RegistryNames.makeLabsName("potassium_bisulfate"))
                .dust()
                .color(0xfdbd68)
                .components(Potassium, 1, Hydrogen, 1, Sulfur, 1, Oxygen, 4)
                .build();

        PotassiumPyrosulfate = new Material.Builder(32072, RegistryNames.makeLabsName("potassium_pyrosulfate"))
                .dust()
                .color(0xfbbb66)
                .components(Potassium, 2, Sulfur, 2, Oxygen, 7)
                .build();

        PotassiumSulfate = new Material.Builder(32073, RegistryNames.makeLabsName("potassium_sulfate"))
                .dust()
                .color(0xf0b064).iconSet(MaterialIconSet.METALLIC)
                .components(Potassium, 2, Sulfur, 1, Oxygen, 4)
                .build();

        ZincSulfate = new Material.Builder(32074, RegistryNames.makeLabsName("zinc_sulfate"))
                .dust()
                .color(0x846649).iconSet(MaterialIconSet.FINE)
                .components(Zinc, 1, Sulfur, 1, Oxygen, 4)
                .build();

        SodiumNitrate = new Material.Builder(32075, RegistryNames.makeLabsName("sodium_nitrate"))
                .dust()
                .color(0x846684).iconSet(MaterialIconSet.ROUGH)
                .components(Sodium, 1, Nitrogen, 1, Oxygen, 3)
                .build();

        RhodiumNitrate = new Material.Builder(32076, RegistryNames.makeLabsName("rhodium_nitrate"))
                .dust()
                .color(0x776649).iconSet(MaterialIconSet.FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 1, Nitrogen, 3, Oxygen, 9)
                .build();

        RhodiumNitrate.setFormula("Rh(NO3)3", true);

        SodiumRuthenate = new Material.Builder(32077, RegistryNames.makeLabsName("sodium_ruthenate"))
                .dust()
                .color(0x3a40cb).iconSet(MaterialIconSet.SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 2, Ruthenium, 1, Oxygen, 4)
                .build();

        SodiumPeroxide = new Material.Builder(32078, RegistryNames.makeLabsName("sodium_peroxide"))
                .dust()
                .color(0xecff80).iconSet(MaterialIconSet.ROUGH)
                .components(Sodium, 2, Oxygen, 2)
                .build();

        IridiumDioxideResidue = new Material.Builder(32079, RegistryNames.makeLabsName("iridium_dioxide_residue"))
                .dust()
                .color(0x17182e).iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Oxygen, 2, RareEarth, 1)
                .build();

        AmmoniumHexachloroiridiate = new Material.Builder(32080, RegistryNames.makeLabsName("ammonium_hexachloroiridiate"))
                .dust()
                .color(0x644629).iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Nitrogen, 2, Hydrogen, 8, Iridium, 1, Chlorine, 6)
                .build();

        AmmoniumHexachloroiridiate.setFormula("(NH4)2IrCl6", true);

        PlatinumGroupResidue = new Material.Builder(32081, RegistryNames.makeLabsName("platinum_group_residue"))
                .dust()
                .color(0x64632e).iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Osmium, 1, Rhodium, 1, Ruthenium, 1, RareEarth, 1)
                .build();

        PalladiumRichAmmonia = new Material.Builder(32082, RegistryNames.makeLabsName("palladium_rich_ammonia"))
                .fluid()
                .color(0x808080)
                .flags(DISABLE_DECOMPOSITION)
                .components(Ammonia, 2, Palladium, 1, Chlorine, 1)
                .build();

        CrudePlatinumResidue = new Material.Builder(32083, RegistryNames.makeLabsName("crude_platinum_residue"))
                .dust()
                .color(0xfffbc5).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(PlatinumRaw, 1)
                .build();

        CrudePalladiumResidue = new Material.Builder(32084, RegistryNames.makeLabsName("crude_palladium_residue"))
                .dust()
                .color(0x909090).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(PalladiumRaw, 1)
                .build();

        IridiumGroupSludge = new Material.Builder(32085, RegistryNames.makeLabsName("iridium_group_sludge"))
                .dust()
                .color(0x644629).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Osmium, 1, Ruthenium, 1, RareEarth, 1)
                .build();

        RhodiumSulfateSolution = new Material.Builder(32086, RegistryNames.makeLabsName("rhodium_sulfate_solution"))
                .fluid()
                .color(0xffbb66)
                .flags(DISABLE_DECOMPOSITION)
                .components(RhodiumSulfate, 1, Water, 1)
                .build();

        CrudeRhodiumResidue = new Material.Builder(32087, RegistryNames.makeLabsName("crude_rhodium_residue"))
                .dust()
                .color(0x666666).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 2, Water, 1)
                .build();

        RhodiumSalt = new Material.Builder(32088, RegistryNames.makeLabsName("rhodium_salt"))
                .dust()
                .color(0x848484).iconSet(MaterialIconSet.SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Salt, 2, Rhodium, 2, Chlorine, 6)
                .build();

        RhodiumSalt.setFormula("(NaCl)2(RhCl3)2", true);

        AcidicIridiumDioxideSolution = new Material.Builder(32089, RegistryNames.makeLabsName("acidic_iridium_dioxide_solution"))
                .fluid()
                .color(0x27284e)
                .flags(DISABLE_DECOMPOSITION)
                .components(IridiumDioxideResidue, 1, HydrochloricAcid, 4)
                .build();

        PlatinumPalladiumLeachate = new Material.Builder(32090, RegistryNames.makeLabsName("platinum_palladium_leachate"))
                .fluid()
                .color(0xffffc5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Platinum, 1, Palladium, 1, AquaRegia, 1)
                .build();

        MethylFormate = new Material.Builder(32091, RegistryNames.makeLabsName("methyl_formate"))
                .fluid()
                .color(0xffaaaa)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 2)
                .build();

        MethylFormate.setFormula("HCOOCH3", true);

        FormicAcid = new Material.Builder(32092, RegistryNames.makeLabsName("formic_acid"))
                .fluid()
                .color(0xffffc5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 1, Hydrogen, 2, Oxygen, 2)
                .build();

        FormicAcid.setFormula("HCOOH", true);

        SodiumMethoxide = new Material.Builder(32093, RegistryNames.makeLabsName("sodium_methoxide"))
                .dust()
                .color(0xd0d0f0).iconSet(MaterialIconSet.DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 1, Hydrogen, 3, Oxygen, 1, Sodium, 1)
                .build();

        HexafluorosilicicAcid = new Material.Builder(32094, RegistryNames.makeLabsName("hexafluorosilicic_acid"))
                .fluid()
                .color(0xd00010)
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6)
                .build();

        DirtyHexafluorosilicicAcid = new Material.Builder(32095, RegistryNames.makeLabsName("dirty_hexafluorosilicic_acid"))
                .fluid()
                .color(0xe00030)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6, RareEarth, 1)
                .build();

        StoneResidue = new Material.Builder(32096, RegistryNames.makeLabsName("stone_residue"))
                .dust()
                .color(0x4d4d4d).iconSet(MaterialIconSet.ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        UncommonResidue = new Material.Builder(32097, RegistryNames.makeLabsName("uncommon_residue"))
                .dust()
                .color(0x4d4ded).iconSet(MaterialIconSet.FINE)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        OxidisedResidue = new Material.Builder(32098, RegistryNames.makeLabsName("oxidised_residue"))
                .dust()
                .color(0xad4d4d).iconSet(MaterialIconSet.FINE)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        RefinedResidue = new Material.Builder(32099, RegistryNames.makeLabsName("refined_residue"))
                .dust()
                .color(0x2a8a21).iconSet(MaterialIconSet.SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        CleanInertResidue = new Material.Builder(32100, RegistryNames.makeLabsName("clean_inert_residue"))
                .dust()
                .color(0x3bbd2f).iconSet(MaterialIconSet.SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        UltraacidicResidue = new Material.Builder(32101, RegistryNames.makeLabsName("ultraacidic_residue"))
                .fluid()
                .color(0xb0babf)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        XenicAcid = new Material.Builder(32102, RegistryNames.makeLabsName("xenic_acid"))
                .fluid()
                .color(0xa567db)
                .components(Xenon, 1, Water, 1, Oxygen, 5, HydrogenPeroxide, 1)
                .build();

        XenicAcid.setFormula("H2XeO4", true);

        DustyHelium = new Material.Builder(32103, RegistryNames.makeLabsName("dusty_helium"))
                .fluid(FluidTypes.GAS)
                .color(0xa040af)
                .flags(DISABLE_DECOMPOSITION)
                .components(Helium3, 1, RareEarth, 1)
                .build();

        TaraniumEnrichedHelium = new Material.Builder(32104, RegistryNames.makeLabsName("taranium_enriched_helium"))
                .fluid(FluidTypes.GAS).plasma()
                .color(0x10c050)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        TaraniumDepletedHelium = new Material.Builder(32105, RegistryNames.makeLabsName("taranium_depleted_helium"))
                .fluid(FluidTypes.GAS)
                .color(0x006010)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        TritiumHydride = new Material.Builder(32106, RegistryNames.makeLabsName("tritium_hydride"))
                .fluid(FluidTypes.GAS)
                .color(0xd01010)
                .flags(DISABLE_DECOMPOSITION)
                .components(Tritium, 1, Hydrogen, 1)
                .build();

        HeliumHydride = new Material.Builder(32107, RegistryNames.makeLabsName("helium_hydride"))
                .fluid(FluidTypes.GAS)
                .color(0xe6d62e)
                .flags(DISABLE_DECOMPOSITION)
                .components(Helium3, 1, Hydrogen, 1)
                .build();

        DioxygenDifluoride = new Material.Builder(32108, RegistryNames.makeLabsName("dioxygen_difluoride"))
                .fluid().fluidTemp(80)
                .colorAverage()
                .components(Oxygen, 2, Fluorine, 2)
                .build();

        Taranium = new Material.Builder(32109, RegistryNames.makeLabsName("taranium"))
                .element(LabsElements.Tn)
                .ingot().fluid()
                .color(0xff00ff).iconSet(MaterialIconSet.BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_DENSE)
                .blastTemp(10800)
                .build();

        Taranium.setFormula("Tn");

        Darmstadtite = new Material.Builder(32110, RegistryNames.makeLabsName("darmstadtite"))
                .dust().ore(2, 1)
                .colorAverage().iconSet(MaterialIconSet.DULL)
                .components(Darmstadtium, 2, Sulfur, 3)
                .addOreByproducts(RareEarth, RhodiumSulfate, Darmstadtium)
                .build();

        Dulysite = new Material.Builder(32111, RegistryNames.makeLabsName("dulysite"))
                .gem().ore(2, 1)
                .colorAverage().iconSet(MaterialIconSet.RUBY)
                .components(Duranium, 1, Chlorine, 3)
                .addOreByproducts(Sphalerite, Duranium, Europium)
                .build();

        Butanol = new Material.Builder(32112, RegistryNames.makeLabsName("butanol"))
                .fluid()
                .color(0xc7af2e)
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 1)
                .build();

        Butanol.setFormula("C4H9OH", true);

        PhosphorusTrichloride = new Material.Builder(32113, RegistryNames.makeLabsName("phosphorus_trichloride"))
                .fluid()
                .color(0xe8c474)
                .components(Phosphorus, 1, Chlorine, 3)
                .build();

        PhosphorylChloride = new Material.Builder(32114, RegistryNames.makeLabsName("phosphoryl_chloride"))
                .fluid()
                .color(0xe8bb5b)
                .components(Phosphorus, 1, Oxygen, 1, Chlorine, 3)
                .build();

        TributylPhosphate = new Material.Builder(32115, RegistryNames.makeLabsName("tributyl_phosphate"))
                .fluid()
                .color(0xe8c4a0)
                .components(Carbon, 12, Hydrogen, 27, Oxygen, 4, Phosphorus, 1)
                .build();

        TributylPhosphate.setFormula("(C4H9O)3PO", true);;
    }
}
