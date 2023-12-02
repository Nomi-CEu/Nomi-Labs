<p align="center"><img src="https://github.com/Nomi-CEu/Nomi-CEu/assets/103940576/672808a8-0ad0-4d07-809e-08336a928909" alt="Logo"></p>
<h1 align="center">Nomi Labs</h1>
<p align="center"><b><i>The coremod for <a href="https://github.com/Nomi-CEu/Nomi-CEu"> Nomifactory CEu</a>, providing custom GT changes, items, recipe changes, and more!</i></b></p>
<h1 align="center">
    <a href="https://github.com/Nomi-CEu/Nomi-Labs/issues"><img src="https://img.shields.io/github/issues/Nomi-CEu/Nomi-Labs?style=for-the-badge&color=orange" alt="Issues"></a>
    <a href="https://github.com/Nomi-CEu/Nomi-Labs/blob/master/LICENSE"><img src="https://img.shields.io/github/license/Nomi-CEu/Nomi-Labs?style=for-the-badge" alt="License"></a>
    <a href="https://discord.com/invite/zwQzqP8b6q"><img src="https://img.shields.io/discord/927050775073534012?color=5464ec&label=Discord&style=for-the-badge" alt="Discord"></a>
    <br>
    <a href="https://www.curseforge.com/minecraft/mc-mods/nomi-labs"><img src="https://cf.way2muchnoise.eu/932060.svg?badge_style=for_the_badge" alt="CurseForge"></a>
    <a href="https://www.curseforge.com/minecraft/mc-mods/nomi-labs"><img src="https://cf.way2muchnoise.eu/versions/For%20MC_932060_all.svg?badge_style=for_the_badge" alt="MC Versions"></a>
    <a href="https://github.com/Nomi-CEu/Nomi-Labs/releases"><img src="https://img.shields.io/github/downloads/Nomi-CEu/Nomi-Labs/total?sort=semver&logo=github&label=&style=for-the-badge&color=2d2d2d&labelColor=545454&logoColor=FFFFFF" alt="GitHub"></a>
</h1>

## Features
- All custom items, blocks and fluids from Nomifactory, re-implemented with code cleanliness in mind, and with many improvements, such as:
  - Hand Framing Tool able to be framed/decorated in the Framing Table
  - Excitation Coil Improvements: now can be placed in any direction, and on your head
  - More placeable blocks!
- Compat Mixin allowing NuclearCraft to be used with GregTech CEu, without having to resort to unstable Banoukou patches
- GroovyScript Helpers:
  - Allow replacing of GregTech CEu crafting recipes, with the recycling recipes and OreDictUnifier Data updating automatically
  - Allow refreshing of GregTech CEu recycling recipes at any time, allowing for these to be changed with GroovyScript's reload feature
  - More planned!
- Configs, allowing for disabling of different features, allowing for a wider range of usage
- More to come, such as custom GT content, improved custom multiblocks, new multiblocks (a greenhouse, which alllows for better automation of plant-related materials over EIO Farmers, is already available), and more custom textures!
  
## Why a core mod?
We have chosen to implement most custom features into a coremod for Nomi-CEu, and the rest will be refactored into GroovyScript. This allows much more extensibility over the current CraftTweaker setup, and allows for a much needed code cleanup. We have chosen not to use GroovyScript for custom content as it is very fiddly, especially with client only features, such as custom overlays, and we believe that these will be useful for the community (especially the GroovyScript helpers).

## Dependencies:
This mod requires [GregTech CEu](https://github.com/GregTechCEu/GregTech). Other versions of GT, such as GTCE, will not work.

## Credits:
- [GTCEu Buildscripts](https://github.com/GregTechCEu/Buildscripts) for the amazing buildscripts
- [Actually Additions](https://github.com/Ellpeck/ActuallyAdditions) for part of the custom fluid code
- [Content Tweaker](https://github.com/CraftTweaker/ContentTweaker) for base textures of custom fluids
- [Nomifactory](https://github.com/Nomifactory/Nomifactory) for textures of items, blocks, base code, and the original pack
- [GregTech CEu](https://github.com/GregTechCEu/GregTech) for some textures
