package com.nomiceu.nomilabs.gregtech.material.registry;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.material.LabsProperties;
import com.nomiceu.nomilabs.gregtech.material.registry.register.*;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconSet;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.unification.material.Materials.Neutronium;
import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_PLATE;

@SuppressWarnings("unused")
public class LabsMaterials {

    /**
     * Deprecated Materials
     */
    public static Material Infinity; // ID: 26

    /**
     * Elemental materials
     */
    public static Material Draconium; // ID: 1
    public static Material AwakenedDraconium; // ID: 2
    public static Material Omnium; // ID: 4
    public static Material Taranium; // ID: 109; HM only
    public static Material Scandium; // ID: 118
    public static Material Ytterbium; // ID: 119
    public static Material Praseodymium; // ID: 120
    public static Material Gadolinium; // ID: 121

    /**
     * Chemical Materials
     */
    public static Material TungstenTrioxide; // ID: 32; HM only
    public static Material BerylliumOxide; // ID: 33; HM only
    public static Material NiobiumPentoxide; // ID: 34; HM only
    public static Material TantalumPentoxide; // ID: 35; HM only
    public static Material ManganeseDifluoride; // ID: 37; HM only
    public static Material MolybdenumTrioxide; // ID: 38; HM only
    public static Material LeadChloride; // ID: 39; HM only
    public static Material Wollastonite; // ID: 40; HM only
    public static Material SodiumMetavanadate; // ID: 41; HM only
    public static Material VanadiumPentoxide; // ID: 42; HM only
    public static Material AmmoniumMetavanadate; // ID: 43; HM only
    public static Material PhthalicAnhydride; // ID: 44; HM only
    public static Material Ethylanthraquinone; // ID: 45; HM only
    public static Material HydrogenPeroxide; // ID: 46; HM only
    public static Material Hydrazine; // ID: 47; HM only
    public static Material AcetoneAzine; // ID: 48; HM only
    public static Material GrapheneOxide; // ID: 49; HM only
    public static Material Durene; // ID: 51; HM only
    public static Material PyromelliticDianhydride; // ID: 52; HM only
    public static Material Dimethylformamide; // ID: 53; HM only
    public static Material Aminophenol; // ID: 54; HM only
    public static Material Oxydianiline; // ID: 55; HM only
    public static Material AntimonyPentafluoride; // ID: 56; HM only
    public static Material LeadMetasilicate; // ID: 66; HM only
    public static Material Butanol; // ID: 112; HM only
    public static Material PhosphorusTrichloride; // ID: 113; HM only
    public static Material PhosphorylChloride; // ID: 114; HM only
    public static Material TributylPhosphate; // ID: 115; HM only

    /**
     * Naquadah Line Materials
     */
    public static Material NaquadahOxide; // ID: 57; HM only
    public static Material Pyromorphite; // ID: 58; HM only
    public static Material NaquadahHydroxide; // ID: 59; HM only
    public static Material CaesiumHydroxide; // ID: 61; HM only
    public static Material Neocryolite; // ID: 62; HM only
    public static Material NaquadahOxidePetroSolution; // ID: 63; HM only
    public static Material NaquadahOxideAeroSolution; // ID: 64; HM only
    public static Material HotNaquadahOxideNeocryoliteSolution; // ID: 65; HM only

    /**
     * Taranium Line Materials
     */
    public static Material HexafluorosilicicAcid; // ID: 94; HM only
    public static Material DirtyHexafluorosilicicAcid; // ID: 95; HM only
    public static Material StoneResidue; // ID: 96; HM only
    public static Material UncommonResidue; // ID: 97; HM only
    public static Material OxidisedResidue; // ID: 98; HM only
    public static Material RefinedResidue; // ID: 99; HM only
    public static Material CleanInertResidue; // ID: 100; HM only
    public static Material UltraacidicResidue; // ID: 101; HM only
    public static Material XenicAcid; // ID: 102; HM only
    public static Material DustyHelium; // ID: 103; HM only
    public static Material TaraniumEnrichedHelium; // ID: 104; HM only
    public static Material TaraniumDepletedHelium; // ID: 105; HM only
    public static Material TritiumHydride; // ID: 106; HM only
    public static Material HeliumHydride; // ID: 107; HM only
    public static Material DioxygenDifluoride; // ID: 108; HM only

    /**
     * Platinum Line Materials
     */
    public static Material PlatinumMetallic; // ID: 67; HM only
    public static Material PalladiumMetallic; // ID: 68; HM only
    public static Material AmmoniumHexachloroplatinate; // ID: 69; HM only
    public static Material ChloroplatinicAcid; // ID: 70; HM only
    public static Material PotassiumBisulfate; // ID: 71; HM only
    public static Material PotassiumPyrosulfate; // ID: 72; HM only
    public static Material PotassiumSulfate; // ID: 73; HM only
    public static Material ZincSulfate; // ID: 74; HM only
    public static Material SodiumNitrate; // ID: 75; HM only
    public static Material RhodiumNitrate; // ID: 76; HM only
    public static Material SodiumRuthenate; // ID: 77; HM only
    public static Material SodiumPeroxide; // ID: 78; HM only
    public static Material IridiumDioxideResidue; // ID: 79; HM only
    public static Material AmmoniumHexachloroiridiate; // ID: 80; HM only
    public static Material PlatinumGroupResidue; // ID: 81; HM only
    public static Material PalladiumRichAmmonia; // ID: 82; HM only
    public static Material CrudePlatinumResidue; // ID: 83; HM only
    public static Material CrudePalladiumResidue; // ID: 84; HM only
    public static Material IridiumGroupSludge; // ID: 85; HM only
    public static Material RhodiumSulfateSolution; // ID: 86; HM only
    public static Material CrudeRhodiumResidue; // ID: 87; HM only
    public static Material RhodiumSalt; // ID: 88; HM only
    public static Material AcidicIridiumDioxideSolution; // ID: 89; HM only
    public static Material PlatinumPalladiumLeachate; // ID: 90; HM only
    public static Material MethylFormate; // ID: 91; HM only
    public static Material FormicAcid; // ID: 92; HM only
    public static Material SodiumMethoxide; // ID: 93; HM only

    /**
     * Rare Earth Line Materials
     */

    public static Material RareEarthOxideConcentrate; // ID: 122
    public static Material RoastedRareEarthOxideConcentrate; // ID: 123
    public static Material LeechedRareEarthOxide; // ID: 124
    public static Material TrivalentRareEarths; // ID: 125
    public static Material CeriumConcentrate; // ID: 126
    public static Material DissolvedCeriumConcentrate; // ID: 127
    public static Material VaporousNitricAcid; // ID: 128
    /**
     * Microverse Materials
     */
    public static Material Microversium; // ID: 27
    public static Material Osmiridium8020; // ID: 29
    public static Material Iridosmine8020; // ID: 30
    public static Material Kaemanite; // ID: 31
    public static Material Fluorite; // ID: 36
    public static Material Snowchestite; // ID: 60; HM only
    public static Material Darmstadtite; // ID: 110
    public static Material Dulysite; // ID: 111; HM only
    public static Material Laurite; // ID: 116
    public static Material Cuprorhodsite; // ID: 117

    /**
     * Thermal Materials
     */
    public static Material Ardite; // ID: 6
    public static Material Mana; // ID: 7
    public static Material Manyullyn; // ID: 8
    public static Material Signalum; // ID: 10
    public static Material Lumium; // ID: 17
    public static Material Enderium; // ID: 18
    public static Material ElectrumFlux; // ID: 19
    public static Material Mithril; // ID: 21

    /**
     * EnderIO Materials
     */
    public static Material DarkSteel; // ID: 3
    public static Material ConductiveIron; // ID: 11
    public static Material EnergeticAlloy; // ID: 12
    public static Material VibrantAlloy; // ID: 13
    public static Material PulsatingIron; // ID: 14
    public static Material ElectricalSteel; // ID: 15
    public static Material Soularium; // ID: 24
    public static Material EndSteel; // ID: 25

    /**
     * Endgame Materials
     */
    public static Material CrystalMatrix; // ID: 23
    public static Material DraconicSuperconductor; // ID: 28
    public static Material KaptonK; // ID: 50; HM only

    public static void init() {
        /* Deprecated Materials */
        Infinity = new Material.Builder(26, makeLabsName("infinity"))
                .ingot()
                .color(0x000000).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE)
                .components(Neutronium, 5)
                .build();

        Infinity.setFormula("∞");

        /* Elements */
        LabsElements.init();

        /* Chemical Materials */
        LabsChemicals.initChemicals();

        /* Naquadah Line Materials */
        LabsNaqLine.initNaqLine();

        /* Taranium Line Materials */
        LabsTaraniumLine.initTaraniumLine();

        /* Platinum Line Materials */
        LabsPlatLine.initPlatLine();

        /* Rare Earth Line Materials */
        LabsRareEarthLine.initRareEarthLine();

        /* Microverse Materials */
        LabsMicroverse.initMicroverse();

        /* Thermal Materials */
        LabsThermal.initThermal();

        /* EnderIO Materials */
        LabsEIO.initEIO();

        /* Endgame Materials */
        LabsEndgame.initEndgame();

        /* Fallback Material */
        GregTechAPI.materialManager.getRegistry(LabsValues.LABS_MODID)
                .setFallbackMaterial(Omnium);
    }

    public static void materialChanges() {
        LabsProperties.propertyChanges();
        LabsProperties.flagChanges();
        LabsProperties.miscChanges();
    }
}
