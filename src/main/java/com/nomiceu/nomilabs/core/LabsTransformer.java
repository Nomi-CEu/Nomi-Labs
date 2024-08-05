package com.nomiceu.nomilabs.core;

import java.util.*;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.nomiceu.nomilabs.config.LabsConfig;

/**
 * @apiNote Inspired by net.minecraftforge.fml.common.asm.transformers.SideTransformer
 *          <p>
 *          Allows for Client/Server Only Methods to be specified via Config.
 */
@SuppressWarnings({ "unused", "deprecation" })
public class LabsTransformer implements IClassTransformer, Opcodes {

    // Map of Class -> Pair Of Obf Mapping Lists, Left is Fields, Right is Methods
    private static Map<String, Pair<List<ObfMapping>, List<ObfMapping>>> toRemove;

    /**
     * Must setup here so FMLCommonHandler has time to load.
     */
    private static void setupToRemove() {
        if (toRemove != null) return;

        // Can't use Object2ObjectOpenHashMap as in ASM stage
        toRemove = new HashMap<>();
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

            toRemove.computeIfAbsent(parts[0], (k) -> Pair.of(new ArrayList<>(), new ArrayList<>()))
                    .getLeft().add(new ObfMapping(parts[0], parts[1]));
        }

        for (var method : methodsRemove) {
            var parts = method.split("@");
            if (parts.length != 3)
                throw new IllegalStateException("Invalid Method Remove: " + method +
                        "! Must have Three Parts, Seperated by '@', Got " + parts.length + "!");

            toRemove.computeIfAbsent(parts[0], (k) -> Pair.of(new ArrayList<>(), new ArrayList<>()))
                    .getRight().add(new ObfMapping(parts[0], parts[1], parts[2]));
        }

        FMLLog.log("LabsASM", Level.DEBUG, "Computed To Remove: %s", toRemove);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        setupToRemove();

        String internalName = transformedName.replace('.', '/');
        if (!toRemove.containsKey(internalName)) return basicClass;

        var pair = toRemove.get(internalName);
        var fieldsRemove = pair.getLeft();
        var methodsRemove = pair.getRight();

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        for (var toRemove : fieldsRemove) {
            Iterator<FieldNode> fields = classNode.fields.iterator();
            boolean found = false;
            while (fields.hasNext()) {
                FieldNode field = fields.next();
                if (!toRemove.s_name.equals(field.name)) continue;

                FMLLog.log("LabsASM", Level.WARN, "Removing Field: %s.%s", classNode.name, field.name);
                fields.remove();
                found = true;
                break;
            }

            if (!found)
                throw new IllegalStateException("Could not find Field " + classNode.name + "." + toRemove.s_name + "!");
        }

        var gatherer = new LambdaGatherer();
        for (var toRemove : methodsRemove) {
            Iterator<MethodNode> methods = classNode.methods.iterator();
            boolean found = false;
            while (methods.hasNext()) {
                MethodNode method = methods.next();
                if (!toRemove.matches(method)) continue;

                FMLLog.log("LabsASM", Level.WARN, "Removing Method: %s.%s%s", classNode.name, method.name,
                        method.desc);
                methods.remove();
                gatherer.accept(method);
                found = true;
                break;
            }

            if (!found)
                throw new IllegalStateException(
                        "Could not find Method " + classNode.name + "." + toRemove.s_name + toRemove.s_desc + "!");
        }

        LambdaGatherer.removeDynamicLambdaMethods(gatherer, classNode);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);

        return writer.toByteArray();
    }
}
