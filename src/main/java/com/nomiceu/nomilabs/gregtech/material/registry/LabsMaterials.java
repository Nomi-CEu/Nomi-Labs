package com.nomiceu.nomilabs.gregtech.material.registry;

import com.nomiceu.nomilabs.gregtech.material.LabsProperties;
import com.nomiceu.nomilabs.gregtech.material.registry.register.*;
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
    public static Material Infinity; // 32026

    /**
     * Elemental materials
     */
    public static Material Draconium; // 32001
    public static Material AwakenedDraconium; // 32002
    public static Material Omnium; // 32004
    public static Material Taranium; // 32109; HM only

    /**
     * Chemical Materials
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
     * Naquadah Line Materials
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
     * EnderIO Materials
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
     * Endgame Materials
     */
    public static Material CrystalMatrix; // 32023
    public static Material DraconicSuperconductor; // 32028
    public static Material KaptonK; // 32050; HM only

    public static void init() {
        /* Deprecated Materials */
        Infinity = new Material.Builder(32026, makeLabsName("infinity"))
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

        /* Microverse Materials */
        LabsMicroverse.initMicroverse();

        /* Thermal Materials */
        LabsThermal.initThermal();

        /* EnderIO Materials */
        LabsEIO.initEIO();

        /* Endgame Materials */
        LabsEndgame.initEndgame();
    }

    public static void materialChanges() {
        LabsProperties.propertyChanges();
        LabsProperties.flagChanges();
        LabsProperties.miscChanges();
    }
}
