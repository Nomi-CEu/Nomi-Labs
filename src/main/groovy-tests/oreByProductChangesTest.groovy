import static com.nomiceu.nomilabs.groovy.OreByProductChanger.*
import static gregtech.api.unification.material.Materials.*

// Change GT's Ore Processing Info JEI Tab.
// Goes in Post Init.
// You MAY put side only, client, on these changes to save computation; but it is NOT required.
// Most computation is skipped on server side regardless.

/* Nickel Changes */
// Yellow Limonite has the same processing steps. Double check with that to make sure all slot idxs are correct.

// Change Ore (also test oredicts)
changeOre(Nickel, ore('treeSapling'))

// Change machines (excl sifter) to concrete
Machines.FURNACE.change(Nickel, item('minecraft:concrete', 0))
Machines.MACERATOR_CRUSH.change(Nickel, item('minecraft:concrete', 1))
Machines.MACERATOR_IMPURE.change(Nickel, item('minecraft:concrete', 2))
Machines.CENTRIFUGE_IMPURE.change(Nickel, item('minecraft:concrete', 3))
Machines.ORE_WASHER.change(Nickel, item('minecraft:concrete', 4))
Machines.THERMAL_CENTRIFUGE.change(Nickel, item('minecraft:concrete', 5))
Machines.MACERATOR_CENTRIFUGED.change(Nickel, item('minecraft:concrete', 6))
Machines.MACERATOR_PURIFIED.change(Nickel, item('minecraft:concrete', 7))
Machines.CENTRIFUGE_PURIFIED.change(Nickel, item('minecraft:concrete', 8))
Machines.CAULDRON_SIMPLE_WASHER.change(Nickel, item('minecraft:concrete', 9))
Machines.CAULDRON_IMPURE.change(Nickel, item('minecraft:concrete', 10))
Machines.CAULDRON_PURE.change(Nickel, item('minecraft:concrete', 11))
Machines.CHEMICAL_BATH.change(Nickel, item('minecraft:concrete', 12))
Machines.ELECTROMAGNETIC_SEPARATOR.change(Nickel, item('minecraft:concrete', 13))

// Change outputs (excl sifter) (and test adding chances)
BasicProcessing.SMElT_RESULT.change(Nickel, item('minecraft:dye', 0))
BasicProcessing.SMElT_RESULT.changeChance(Nickel, 100, 10)

BasicProcessing.CRUSHED.change(Nickel, item('minecraft:dye', 1))
BasicProcessing.CRUSHED.changeChance(Nickel, 200, 20)
BasicProcessing.CRUSHED_BYPRODUCT.change(Nickel, item('minecraft:dye', 2))
BasicProcessing.CRUSHED_BYPRODUCT.changeChance(Nickel, 300, 30)

BasicProcessing.IMPURE_DUST.change(Nickel, item('minecraft:dye', 3))
BasicProcessing.IMPURE_DUST.changeChance(Nickel, 400, 40)
BasicProcessing.IMPURE_DUST_BYPRODUCT.change(Nickel, item('minecraft:dye', 4))
BasicProcessing.IMPURE_DUST_BYPRODUCT.changeChance(Nickel, 500, 50)

BasicProcessing.IMPURE_CENTRIFUGE.change(Nickel, item('minecraft:dye', 5))
BasicProcessing.IMPURE_CENTRIFUGE.changeChance(Nickel, 600, 60)
BasicProcessing.IMPURE_CENTRIFUGE_BYPRODUCT.change(Nickel, item('minecraft:dye', 6))
BasicProcessing.IMPURE_CENTRIFUGE_BYPRODUCT.changeChance(Nickel, 700, 70)

BasicProcessing.PURIFIED_WASHER.change(Nickel, item('minecraft:dye', 7))
BasicProcessing.PURIFIED_WASHER.changeChance(Nickel, 800, 80)
BasicProcessing.PURIFIED_WASHER_BYPRODUCT.change(Nickel, item('minecraft:dye', 8))
BasicProcessing.PURIFIED_WASHER_BYPRODUCT.changeChance(Nickel, 900, 90)

BasicProcessing.CENTRIFUGED.change(Nickel, item('minecraft:dye', 9))
BasicProcessing.CENTRIFUGED.changeChance(Nickel, 1000, 100)
BasicProcessing.CENTRIFUGED_BYPRODUCT.change(Nickel, item('minecraft:dye', 10))
BasicProcessing.CENTRIFUGED_BYPRODUCT.changeChance(Nickel, 1100, 110)

BasicProcessing.CENTRIFUGED_DUST.change(Nickel, item('minecraft:dye', 11))
BasicProcessing.CENTRIFUGED_DUST.changeChance(Nickel, 1200, 120)
BasicProcessing.CENTRIFUGED_DUST_BYPRODUCT.change(Nickel, item('minecraft:dye', 12))
BasicProcessing.CENTRIFUGED_DUST_BYPRODUCT.changeChance(Nickel, 1300, 130)

BasicProcessing.PURIFIED_DUST.change(Nickel, item('minecraft:dye', 13))
BasicProcessing.PURIFIED_DUST.changeChance(Nickel, 1400, 140)
BasicProcessing.PURIFIED_DUST_BYPRODUCT.change(Nickel, item('minecraft:dye', 14))
BasicProcessing.PURIFIED_DUST_BYPRODUCT.changeChance(Nickel, 1500, 150)

BasicProcessing.PURIFIED_CENTRIFUGE.change(Nickel, item('minecraft:bed', 0))
BasicProcessing.PURIFIED_CENTRIFUGE.changeChance(Nickel, 1600, 160)
BasicProcessing.PURIFIED_CENTRIFUGE_BYPRODUCT.change(Nickel, item('minecraft:bed', 1))
// Test removing chances
BasicProcessing.PURIFIED_CENTRIFUGE_BYPRODUCT.removeChance(Nickel)

BasicProcessing.CRUSHED_SIMPLE.change(Nickel, item('minecraft:bed', 2))
BasicProcessing.CRUSHED_SIMPLE.changeChance(Nickel, 1700, 170)

BasicProcessing.PURIFIED_SIMPLE.change(Nickel, item('minecraft:bed', 3))
BasicProcessing.PURIFIED_SIMPLE.changeChance(Nickel, 1800, 180)

BasicProcessing.IMPURE_SIMPLE.change(Nickel, item('minecraft:bed', 4))
BasicProcessing.IMPURE_SIMPLE.changeChance(Nickel, 1900, 190)

BasicProcessing.IMPURE_DUST_SIMPLE.change(Nickel, item('minecraft:bed', 5))
BasicProcessing.IMPURE_DUST_SIMPLE.changeChance(Nickel, 2000, 200)

BasicProcessing.PURE_SIMPLE.change(Nickel, item('minecraft:bed', 6))
BasicProcessing.PURE_SIMPLE.changeChance(Nickel, 2100, 210)

BasicProcessing.PURE_DUST_SIMPLE.change(Nickel, item('minecraft:bed', 7))
BasicProcessing.PURE_DUST_SIMPLE.changeChance(Nickel, 2200, 220)

AdvancedProcessing.CHEMICAL_BATH.change(Nickel, item('minecraft:bed', 8))
AdvancedProcessing.CHEMICAL_BATH.changeChance(Nickel, 2300, 230)
AdvancedProcessing.CHEMICAL_BATH_BYPRODUCT.change(Nickel, item('minecraft:bed', 9))
AdvancedProcessing.CHEMICAL_BATH_BYPRODUCT.changeChance(Nickel, 2400, 240)

AdvancedProcessing.ELECTROMAGNETIC_SEPARATOR.change(Nickel, item('minecraft:bed', 10))
AdvancedProcessing.ELECTROMAGNETIC_SEPARATOR.changeChance(Nickel, 2500, 250)
AdvancedProcessing.ELECTROMAGNETIC_SEPARATOR_BYPRODUCT_1.change(Nickel, item('minecraft:bed', 11))
AdvancedProcessing.ELECTROMAGNETIC_SEPARATOR_BYPRODUCT_1.changeChance(Nickel, 2600, 260)
AdvancedProcessing.ELECTROMAGNETIC_SEPARATOR_BYPRODUCT_2.change(Nickel, item('minecraft:bed', 12))
AdvancedProcessing.ELECTROMAGNETIC_SEPARATOR_BYPRODUCT_2.changeChance(Nickel, 2700, 270)

/* Sapphire Changes (to test Sifter) */
// Check with Emerald that all idxs are correct
Machines.SIFTER.change(Sapphire, ore('paneGlass'))

AdvancedProcessing.SIFTER_1.change(Sapphire, item('minecraft:stained_glass_pane', 0))
AdvancedProcessing.SIFTER_1.removeChance(Sapphire)
AdvancedProcessing.SIFTER_2.change(Sapphire, item('minecraft:stained_glass_pane', 1))
AdvancedProcessing.SIFTER_2.changeChance(Sapphire, 1000, 100)
AdvancedProcessing.SIFTER_3.change(Sapphire, item('minecraft:stained_glass_pane', 2))
AdvancedProcessing.SIFTER_3.removeChance(Sapphire)
AdvancedProcessing.SIFTER_4.change(Sapphire, item('minecraft:stained_glass_pane', 3))
AdvancedProcessing.SIFTER_4.changeChance(Sapphire, 2000, 200)
AdvancedProcessing.SIFTER_5.change(Sapphire, item('minecraft:stained_glass_pane', 4))
AdvancedProcessing.SIFTER_5.removeChance(Sapphire)
AdvancedProcessing.SIFTER_6.change(Sapphire, item('minecraft:stained_glass_pane', 5))
AdvancedProcessing.SIFTER_6.changeChance(Sapphire, 3000, 300)
