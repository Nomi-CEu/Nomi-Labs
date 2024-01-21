package com.nomiceu.nomilabs.remap;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.StartupQuery;

import static com.nomiceu.nomilabs.util.LabsTranslate.format;
import static com.nomiceu.nomilabs.util.LabsTranslate.translateWithBackup;

public class LabsMessageHelper {
    public static void sendMessage(MessageType type, String[] message) {
        var finalMessage = String.join("\n\n", message);

        switch (type) {
            case NOTIFY -> StartupQuery.notify(finalMessage);
            case CONFIRM -> {
                if (!StartupQuery.confirm(finalMessage)) LabsRemapHelper.abort();
            }
        }
    }

    public enum MessageType {
        NOTIFY,
        CONFIRM
    }

    // Don't forget to update this in en_us.lang as well!
    public static class Components {
        public static String[] getIntro() {
            return new String[] {
                    translateWithBackup("nomilabs.fixer.intro.1", "This world must be remapped."),
                    translateWithBackup("nomilabs.fixer.intro.2", format("A Backup will be made.", TextFormatting.BOLD)),
                    translateWithBackup("nomilabs.fixer.intro.3", "Pressing 'No' will cancel world loading."),
                    translateWithBackup("nomilabs.fixer.intro.4",
                            format("Note that after the world is loaded with this, you CANNOT undo this!.", TextFormatting.RED)),
                    translateWithBackup("nomilabs.fixer.intro.5",
                            String.format("You %s have to load from the backup in order to load in a previous version!",
                                    format("WILL", TextFormatting.UNDERLINE)))
            };
        }

        public static String[] getIntroAddition() {
            return new String[] {
                    translateWithBackup("nomilabs.fixer.intro.6",
                            format("The changes that must be made via Data Fixers have been printed to your log.",
                                    TextFormatting.GRAY))
            };
        }

        public static String[] getModeCheck(String mode) {
            return new String[] {
                    translateWithBackup("nomilabs.fixer.mode_check.1",
                            String.format("Are you sure you previously loaded this world with the pack mode '%s' ?",
                                    format(mode, TextFormatting.YELLOW)), mode),
                    translateWithBackup("nomilabs.fixer.mode_check.2",
                            format(String.format("Launching with the wrong mode %s void items and/or blocks!",
                                            format("WILL", TextFormatting.UNDERLINE) + TextFormatting.RED),
                                    TextFormatting.RED)),
                    translateWithBackup("nomilabs.fixer.mode_check.3",
                            format("If you did not change it in your old instance, the default mode is 'Normal'.", TextFormatting.GRAY)),
                    translateWithBackup("nomilabs.fixer.mode_check.4",
                            "Press 'No' if you are not sure! (It will cancel world loading)")
            };
        }

        public static String[] getDoNotExit() {
            return new String[] {
                    translateWithBackup("nomilabs.fixer.do_not_exit.1",
                            format("Do not interrupt the loading process!", TextFormatting.RED)),
                    translateWithBackup("nomilabs.fixer.do_not_exit.2",
                            "If interrupted, load again from the backup, as your world may be corrupted!")
            };
        }
    }
}
