package com.nomiceu.nomilabs.remap;

import net.minecraftforge.fml.common.StartupQuery;

import static com.nomiceu.nomilabs.util.LabsTranslate.translate;

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
                    translate("nomilabs.fixer.intro.1"),
                    translate("nomilabs.fixer.intro.2"),
                    translate("nomilabs.fixer.intro.3"),
                    "",
                    translate("nomilabs.fixer.do_not_revert.intro_addition"),
                    translate("nomilabs.fixer.do_not_revert.1"),
                    translate("nomilabs.fixer.do_not_revert.2")
            };
        }

        public static String[] getIntroAddition() {
            return new String[] {
                    "",
                    translate("nomilabs.fixer.intro.addition")
            };
        }

        public static String[] getModeCheck(String mode) {
            return new String[] {
                    translate("nomilabs.fixer.mode_check.1", mode),
                    translate("nomilabs.fixer.mode_check.2"),
                    translate("nomilabs.fixer.mode_check.3"),
                    translate("nomilabs.fixer.mode_check.4")
            };
        }

        public static String[] getDoNotExit() {
            return new String[] {
                    translate("nomilabs.fixer.do_not_exit.1"),
                    translate("nomilabs.fixer.do_not_exit.2"),
                    "",
                    translate("nomilabs.fixer.do_not_revert.do_not_exit_addition"),
                    translate("nomilabs.fixer.do_not_revert.1"),
                    translate("nomilabs.fixer.do_not_revert.2")
            };
        }
    }
}
