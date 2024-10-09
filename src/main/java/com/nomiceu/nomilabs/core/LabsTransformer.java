package com.nomiceu.nomilabs.core;

import java.util.*;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.*;

import com.nomiceu.nomilabs.config.LabsConfig;

@SuppressWarnings({ "unused" })
public class LabsTransformer implements IClassTransformer, Opcodes {

    private static final Set<String> excludedTwoLongPackages;
    private static final Set<String> transformableClasses;

    static {
        excludedTwoLongPackages = new HashSet<>();
        excludedTwoLongPackages.add("net/minecraft");
        excludedTwoLongPackages.add("net/minecraftforge");

        // Seperated from To Remove, to not load ObfMapping too early.
        transformableClasses = new HashSet<>();
        String[] methodsRemove;
        String[] fieldsRemove;
        if (FMLLaunchHandler.side() == Side.SERVER) {
            // Remove CLIENT side methods & fields
            fieldsRemove = LabsConfig.advanced.clientSideFields;
            methodsRemove = LabsConfig.advanced.clientSideMethods;
        } else {
            // Remove SERVER side methods & fields
            fieldsRemove = LabsConfig.advanced.serverSideFields;
            methodsRemove = LabsConfig.advanced.serverSideMethods;
        }

        for (var field : fieldsRemove) {
            var parts = field.split("@");
            if (parts.length != 2)
                throw new IllegalStateException("Invalid Field Remove: " + field +
                        "! Must have Two Parts, Seperated by '@', Got " + parts.length + "!");

            transformableClasses.add(parts[0]);
        }

        for (var method : methodsRemove) {
            var parts = method.split("@");
            if (parts.length != 3)
                throw new IllegalStateException("Invalid Method Remove: " + method +
                        "! Must have Three Parts, Seperated by '@', Got " + parts.length + "!");

            transformableClasses.add(parts[0]);
        }

        LogManager.getLogger("LabsASM").debug("Computed Transformable Classes: {}", transformableClasses);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        String internalName = transformedName.replace('.', '/');
        String[] parts = internalName.split("/");

        // Check the first two parts against the excluded packages
        // Don't transform Minecraft or Forge classes, causes errors due to late loading
        // Late loading required for compat with SpongeForge
        if (parts.length >= 2) {
            if (excludedTwoLongPackages.add(parts[0] + "/" + parts[1]))
                return basicClass;
        }

        if (!transformableClasses.contains(internalName)) return basicClass;

        return SideSpecificRemover.handleTransform(internalName, basicClass);
    }
}
