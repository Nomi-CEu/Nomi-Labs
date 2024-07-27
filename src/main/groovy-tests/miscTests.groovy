import com.nomiceu.nomilabs.groovy.NCActiveCoolerHelper

import static nc.enumm.MetaEnums.CoolerType.*

/**
 * Change a NuclearCraft Active Cooler Recipe.<br>
 * Inputs:<br>
 * FluidStack newFluid: The new fluid which should be accepted as input to Active Fluid Cooler<br>
 * CoolerType type: The type of active cooler fluid which this recipe should replace, having that recipe removed as
 * well as using the cooling and consumption from that type.
 */

NCActiveCoolerHelper.changeCoolerRecipe(fluid('liquid_helium'), HELIUM)

NCActiveCoolerHelper.changeCoolerRecipe(fluid('moltenempoweredemeradic'), EMERALD)
