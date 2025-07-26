<p align="center"><img src="https://github.com/Nomi-CEu/Nomi-CEu/assets/103940576/672808a8-0ad0-4d07-809e-08336a928909" alt="Logo"></p>
<h1 align="center">Nomi Labs</h1>
<p align="center"><b><i>The coremod for <a href="https://github.com/Nomi-CEu/Nomi-CEu"> Nomifactory CEu</a>, providing custom GT changes, items, recipe changes, and more!</i></b></p>
<h1 align="center">
    <a href="https://github.com/Nomi-CEu/Nomi-Labs/"><img src="https://img.shields.io/github/stars/Nomi-CEu/Nomi-Labs?style=for-the-badge&logo=github&logoColor=white" alt="Stars"></a>
    <a href="https://nightly.link/Nomi-CEu/Nomi-Labs/workflows/build/main/Built%20Jars"><img src="https://img.shields.io/github/last-commit/Nomi-CEu/Nomi-Labs/main?style=for-the-badge&logo=githubactions&logoColor=white&label=Nightly%20Builds&color=%238a67ab" alt="Builds"></a>
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
- Compat Mixin allowing NuclearCraft to be used with GregTech CEu, without having to resort to unstable Bansoukou patches
- GroovyScript Helpers:
  - Allow replacing of GregTech CEu crafting recipes, with the recycling recipes and OreDictUnifier Data updating automatically
  - Allow refreshing of GregTech CEu recycling recipes at any time, allowing for these to be changed with GroovyScript's reload feature
  - More planned!
- Configs, allowing for disabling of different features, allowing for a wider range of usage
- More to come, such as custom GT content, improved custom multiblocks, new multiblocks (a growth chamber, which allows for better automation of plant-related materials over EIO Farmers, is already available), and more custom textures!

## Translations
Translations are offered for Nomi Labs, in a separate 'language pack'. This is available to download [here](https://nightly.link/Nomi-CEu/Nomi-CEu-Translations/workflows/pushbuildpack/main?preview) (download the 'nomi-labs' zip for Nomi Labs).

For more information, including supported languages and their status, see [the GitHub Page](https://github.com/Nomi-CEu/Nomi-CEu-Translations/).
  
## Why a core mod?
We have chosen to implement most custom features into a coremod for Nomi-CEu, and the rest will be refactored into GroovyScript. This allows much more extensibility over the current CraftTweaker setup, and allows for a much needed code cleanup. We have chosen not to use GroovyScript for custom content as it is very fiddly, especially with client only features, such as custom overlays, and we believe that these will be useful for the community (especially the GroovyScript helpers).

## Dependencies:
This mod requires [GregTech CEu](https://github.com/GregTechCEu/GregTech). Other versions of GT, such as GTCE, will not work.

## Licensing
This whole project is licensed under the GNU GPLv3, however, most files are licensed under the GNU LGPLv3.

Files in `src/main/java/org/cicirello/` are licensed under the GNU GPLv3.  
Files elsewhere, including, but not limited to, the code source files (`src/main/java/com/nomiceu/nomilabs/`), resources files (`src/main/resources/`), and more, are licensed under the GNU LGPLv3.

A copy of the GNU GPLv3 can be found in this root directory, and in `src/main/java/org/cicirello`.  
A copy of the GNU LGPLv3 can be found in `src/main/java/com/nomiceu/nomilabs/`.

## Credits:
- [GTCEu Buildscripts](https://github.com/GregTechCEu/Buildscripts) for the amazing buildscripts
- [GregicProbeCEu](https://github.com/Supernoobv/GregicProbeCEu) for base FluidStack element and base recipe outputs implementation (integrated in Labs for localization and display improvements and easier future GT porting)
- [Actually Additions](https://github.com/Ellpeck/ActuallyAdditions) for part of the custom fluid code
- [Content Tweaker](https://github.com/CraftTweaker/ContentTweaker) for base textures of custom fluids
- [Nomifactory](https://github.com/Nomifactory/Nomifactory) for textures of items, blocks, base code, and the original pack
- [GregTech CEu](https://github.com/GregTechCEu/GregTech) for some textures
- [GregTech CE](https://github.com/GregTechCE/GregTech) & [Data Fixer Example](https://github.com/gabor7d2/DataFixerExampleMod) for sample & base code in Data Fixes
- [GT-Expert-Core](https://github.com/GTModpackTeam/GTExpert-Core/tree/master) for development environment runtime fix for EnderCore
- [GTNH's Fork of ArchitectureCraft](https://github.com/GTNewHorizons/ArchitectureCraft) for the basis of our ArchitectureCraft Integration
- [ρμ (Rho-Mu)](https://github.com/cicirello/rho-mu/) for fast functions relating to generating random variates from a binomial distribution
