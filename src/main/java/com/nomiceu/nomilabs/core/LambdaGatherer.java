package com.nomiceu.nomilabs.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.minecraftforge.fml.common.FMLLog;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @apiNote net.minecraftforge.fml.common.asm.transformers.SideTransformer.LambdaGatherer
 */
@SuppressWarnings("deprecation")
public class LambdaGatherer extends MethodVisitor {

    private static final Handle META_FACTORY = new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory",
            "metafactory",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
            false);
    private final List<Handle> dynamicLambdaHandles = new ArrayList<>();

    public LambdaGatherer() {
        super(Opcodes.ASM5);
    }

    public void accept(MethodNode method) {
        ListIterator<AbstractInsnNode> insnNodeIterator = method.instructions.iterator();
        while (insnNodeIterator.hasNext()) {
            AbstractInsnNode insnNode = insnNodeIterator.next();
            if (insnNode.getType() == AbstractInsnNode.INVOKE_DYNAMIC_INSN) {
                insnNode.accept(this);
            }
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        if (META_FACTORY.equals(bsm)) {
            Handle dynamicLambdaHandle = (Handle) bsmArgs[1];
            dynamicLambdaHandles.add(dynamicLambdaHandle);
        }
    }

    public List<Handle> getDynamicLambdaHandles() {
        return dynamicLambdaHandles;
    }

    /**
     * Remove dynamic synthetic lambda methods that are inside of removed methods.
     */
    public static void removeDynamicLambdaMethods(LambdaGatherer gatherer, ClassNode classNode) {
        for (List<Handle> dynamicLambdaHandles = gatherer.getDynamicLambdaHandles(); !dynamicLambdaHandles
                .isEmpty(); dynamicLambdaHandles = gatherer.getDynamicLambdaHandles()) {
            gatherer = new LambdaGatherer();
            var methods = classNode.methods.iterator();
            while (methods.hasNext()) {
                MethodNode method = methods.next();
                if ((method.access & Opcodes.ACC_SYNTHETIC) == 0) continue;

                for (Handle dynamicLambdaHandle : dynamicLambdaHandles) {
                    if (method.name.equals(dynamicLambdaHandle.getName()) &&
                            method.desc.equals(dynamicLambdaHandle.getDesc())) {

                        FMLLog.log("LabsASM", Level.WARN, "Removing Method: %s.%s%s", classNode.name, method.name,
                                method.desc);

                        methods.remove();
                        gatherer.accept(method);
                    }
                }
            }
        }
    }
}
