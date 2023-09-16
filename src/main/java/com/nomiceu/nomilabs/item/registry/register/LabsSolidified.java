package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;

import static com.nomiceu.nomilabs.util.LabsNames.makeCTName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsSolidified {
    public static void initSolidified() {
        SOLIDIFIED_ARGON = createItem(new ItemBase(makeCTName("solidifiedargon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_CHLORINE = createItem(new ItemBase(makeCTName("solidifiedchlorine"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_FLUORINE = createItem(new ItemBase(makeCTName("solidifiedfluorine"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_HELIUM = createItem(new ItemBase(makeCTName("solidifiedhelium"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_HYDROGEN = createItem(new ItemBase(makeCTName("solidifiedhydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_KRYPTON = createItem(new ItemBase(makeCTName("solidifiedkrypton"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_MERCURY = createItem(new ItemBase(makeCTName("solidifiedmercury"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_NEON = createItem(new ItemBase(makeCTName("solidifiedneon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_NITROGEN = createItem(new ItemBase(makeCTName("solidifiednitrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_OXYGEN = createItem(new ItemBase(makeCTName("solidifiedoxygen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_RADON = createItem(new ItemBase(makeCTName("solidifiedradon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_XENON = createItem(new ItemBase(makeCTName("solidifiedxenon"), LabsCreativeTabs.TAB_NOMI_LABS));
    }
    public static void initStabilized() {
        STABILIZED_EINSTEINIUM = createItem(new ItemBase(makeCTName("stabilizedeinsteinium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_BERKELIUM = createItem(new ItemBase(makeCTName("stabilizedberkelium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_NEPTUNIUM = createItem(new ItemBase(makeCTName("stabilizedneptunium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_PLUTONIUM = createItem(new ItemBase(makeCTName("stabilizedplutonium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_URANIUM = createItem(new ItemBase(makeCTName("stabilizeduranium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_CURIUM = createItem(new ItemBase(makeCTName("stabilizedcurium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_CALIFORNIUM = createItem(new ItemBase(makeCTName("stabilizedcalifornium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_AMERICIUM = createItem(new ItemBase(makeCTName("stabilizedamericium"), LabsCreativeTabs.TAB_NOMI_LABS));
    }
}
