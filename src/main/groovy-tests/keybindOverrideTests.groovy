import com.nomiceu.nomilabs.util.LabsSide
import net.minecraftforge.client.settings.KeyModifier
import org.lwjgl.input.Keyboard

import static com.nomiceu.nomilabs.groovy.GroovyHelpers.KeyBindingHelpers.*

// Override Default Keybinds! (Goes in Post Init)

// Doesn't matter on Server
// IMPORTANT! This stops the script from crashing on servers!
if (LabsSide.isDedicatedServer()) return

// Change Default Sprint Keybind to 'W' (Same as forwards, essentially toggle-sprint)
addOverride('key.sprint', Keyboard.KEY_W)

// Change Default Advancements Keybind to None
addOverride('key.advancements', Keyboard.KEY_NONE)

// Change Drop key to 'CTRL + Q'
addOverride('key.drop', KeyModifier.CONTROL, Keyboard.KEY_Q)
