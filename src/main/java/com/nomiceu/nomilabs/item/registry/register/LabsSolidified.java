package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsSolidified {
    public static void initSolidified() {
        SOLIDIFIED_ARGON = createItem(new ItemBase(makeLabsName("solidifiedargon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_CHLORINE = createItem(new ItemBase(makeLabsName("solidifiedchlorine"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_FLUORINE = createItem(new ItemBase(makeLabsName("solidifiedfluorine"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_HELIUM = createItem(new ItemBase(makeLabsName("solidifiedhelium"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_HYDROGEN = createItem(new ItemBase(makeLabsName("solidifiedhydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_KRYPTON = createItem(new ItemBase(makeLabsName("solidifiedkrypton"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_MERCURY = createItem(new ItemBase(makeLabsName("solidifiedmercury"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_NEON = createItem(new ItemBase(makeLabsName("solidifiedneon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_NITROGEN = createItem(new ItemBase(makeLabsName("solidifiednitrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_OXYGEN = createItem(new ItemBase(makeLabsName("solidifiedoxygen"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_RADON = createItem(new ItemBase(makeLabsName("solidifiedradon"), LabsCreativeTabs.TAB_NOMI_LABS));
        SOLIDIFIED_XENON = createItem(new ItemBase(makeLabsName("solidifiedxenon"), LabsCreativeTabs.TAB_NOMI_LABS));
    }
    public static void initStabilized() {
        STABILIZED_EINSTEINIUM = createItem(new ItemBase(makeLabsName("stabilizedeinsteinium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_BERKELIUM = createItem(new ItemBase(makeLabsName("stabilizedberkelium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_NEPTUNIUM = createItem(new ItemBase(makeLabsName("stabilizedneptunium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_PLUTONIUM = createItem(new ItemBase(makeLabsName("stabilizedplutonium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_URANIUM = createItem(new ItemBase(makeLabsName("stabilizeduranium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_CURIUM = createItem(new ItemBase(makeLabsName("stabilizedcurium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_CALIFORNIUM = createItem(new ItemBase(makeLabsName("stabilizedcalifornium"), LabsCreativeTabs.TAB_NOMI_LABS));
        STABILIZED_AMERICIUM = createItem(new ItemBase(makeLabsName("stabilizedamericium"), LabsCreativeTabs.TAB_NOMI_LABS));
    }
}
