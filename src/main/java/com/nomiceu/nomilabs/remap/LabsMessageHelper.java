package com.nomiceu.nomilabs.remap;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;

import java.util.Arrays;

public class LabsMessageHelper {
    /**
     * Send Message. If Type is Confirm, and user presses no, Startup will abort.
     * @param type Type of Message
     * @param texts Texts, which will be joined together.
     */
    public static void sendTranslatableMessage(MessageType type, Translatable[] texts) {
        var backup = Arrays.stream(texts).map((text) -> text.backup).toArray(String[]::new);
        var joiners = Arrays.stream(texts).map((text) -> text.joiner).toArray(String[]::new);

        // If is dedicated server, I18n definitely doesn't exist
        // Just use backup strings.
        if (FMLCommonHandler.instance().getSide().isServer()) sendMessage(backup, joiners, type);

        // Try Translating
        try {
            Class.forName("net.minecraft.client.resources.I18n"); // Try to get I18n Class
            // Exists, use it!
            var translated = Arrays.stream(texts)
                    .map((text) -> I18n.format(text.key, text.substitutions))
                    .toArray(String[]::new);
            sendMessage(translated, joiners, type);
        } catch (ClassNotFoundException e) {
            // I18n not found, use backup
            sendMessage(backup, joiners, type);
        }
    }

    private static void sendMessage(String[] message, String[] joiners, MessageType type) {
        // Loop do this for all elements except for last
        for (int i = 0; i < message.length - 1; i++)
            message[i] = message[i].concat(joiners[i]);

        var finalMessage = String.join("", message);
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

    public static class Translatable {
        public final String backup;
        public final String key;
        public final Object[] substitutions;
        /**
         * What will be appended to the next translatable obj.
         */
        public String joiner = "\n\n";

        public Translatable(String backup, String key) {
            this.backup = backup;
            this.key = key;
            this.substitutions = new Object[0];
        }
        public Translatable(String backup, String key, Object... substitutions) {
            this.backup = backup;
            this.key = key;
            this.substitutions = substitutions;
        }
        public Translatable setJoiner(String joiner) {
            this.joiner = joiner;
            return this;
        }
    }

    // Don't forget to update this in en_us.lang as well!
    public static class Components {
        public static Translatable[] getIntro() {
            return new Translatable[] {
                    new Translatable("This world must be remapped.", "nomilabs.fixer.intro.1"),
                    new Translatable(TextFormatting.BOLD + "A Backup will be made.", "nomilabs.fixer.intro.2")
                            .setJoiner("\n"),
                    new Translatable("Pressing 'No' will cancel world loading.", "nomilabs.fixer.intro.3"),
                    new Translatable(TextFormatting.RED + "Note that after the world is loaded with this, you CANNOT undo this!.",
                            "nomilabs.fixer.intro.4").setJoiner("\n"),
                    new Translatable(String.format("You %sWILL%s have to load from the backup in order to load in a previous version!",
                            TextFormatting.UNDERLINE, TextFormatting.RESET),
                            "nomilabs.fixer.intro.5"),
            };
        }

        public static Translatable[] getIntroAddition() {
            return new Translatable[] {
                    new Translatable(TextFormatting.GRAY + "The changes that must be made via Data Fixers have been printed to your log.",
                            "nomilabs.fixer.intro.6")
            };
        }

        public static Translatable[] getModeCheck(String mode) {
            return new Translatable[] {
                    new Translatable(
                            String.format("Are you sure you previously loaded this world with the pack mode '%s' ?",
                                    TextFormatting.YELLOW + mode + TextFormatting.RESET),
                            "nomilabs.fixer.mode_check.1",
                            mode),
                    new Translatable(
                            String.format(TextFormatting.RED + "Launching with the wrong mode %sWILL%s void items and/or blocks!",
                                    TextFormatting.UNDERLINE, TextFormatting.RESET),
                            "nomilabs.fixer.mode_check.2"
                    ),
                    new Translatable(
                            TextFormatting.GRAY + "If you did not change it in your old instance, the default mode is 'Normal'.",
                            "nomilabs.fixer.mode_check.3"
                    ),
                    new Translatable(
                            "Press 'No' if you are not sure! (It will cancel world loading)",
                            "nomilabs.fixer.mode_check.4"
                    )
            };
        }

        public static Translatable[] getDoNotExit() {
            return new Translatable[] {
                    new Translatable(TextFormatting.RED + "Do not interrupt the loading process!",
                            "nomilabs.fixer.do_not_exit.1"),
                    new Translatable("If interrupted, load again from the backup, as your world may be corrupted!",
                            "nomilabs.fixer.do_not_exit.2")
            };
        }
    }
}
