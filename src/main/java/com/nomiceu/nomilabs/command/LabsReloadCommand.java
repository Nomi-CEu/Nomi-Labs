package com.nomiceu.nomilabs.command;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.server.command.CommandTreeBase;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.sandbox.LoadStage;
import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.network.LabsLangReloadMessage;
import com.nomiceu.nomilabs.network.LabsNetworkHandler;

/**
 * Provides three variations to the GroovyScript reload command:
 * <ul>
 * <li>Lang: which reloads language files as well as GrS and JEI, useful for tooltip changes.</li>
 * <li>Fast: which reloads JEI faster by reusing the existing Ingredient Filter. Can reduce JEI reload times by
 * around 60-80%, and works for recipe modifications, but does not update added/removed items via GrS.</li>
 * <li>No JEI: which doesn't reload JEI. Does not update recipe modifications' view in JEI, but can be useful
 * in specific situations.</li>
 * </ul>
 */
public class LabsReloadCommand extends CommandTreeBase {

    public LabsReloadCommand() {
        addSubcommand(new SimpleCommand("lang",
                (server, sender, args) -> runReload(sender, server, new LabsLangReloadMessage())));
    }

    /**
     * Mostly from {@link com.cleanroommc.groovyscript.command.GSCommand#runReload(EntityPlayerMP, MinecraftServer)},
     * but allows for custom packets for client handling.
     */
    public static void runReload(ICommandSender sender, MinecraftServer server, IMessage reloadMsg) {
        if (!(sender instanceof EntityPlayerMP player)) return;

        if (server.isDedicatedServer()) {
            player.sendMessage(
                    new TextComponentString("Reloading in multiplayer is currently not allowed to avoid desync."));
            return;
        }

        GroovyLog.get().info("========== Reloading Groovy scripts ==========");

        // noinspection UnstableApiUsage
        long time = GroovyScript.runGroovyScriptsInLoader(LoadStage.POST_INIT);
        GroovyScript.postScriptRunResult(player, false, true, false, time);
        LabsNetworkHandler.NETWORK_HANDLER.sendTo(reloadMsg, player);
    }

    @Override
    @NotNull
    public String getName() {
        return "labsGsReload";
    }

    @Override
    @NotNull
    public List<String> getAliases() {
        return ImmutableList.of("lr", "labsReload");
    }

    @Override
    @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "/lr <lang/fast/noJei>";
    }
}
