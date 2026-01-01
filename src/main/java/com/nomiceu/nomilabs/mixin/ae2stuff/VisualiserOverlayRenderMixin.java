package com.nomiceu.nomilabs.mixin.ae2stuff;

import net.bdew.ae2stuff.items.visualiser.VLink;
import net.bdew.ae2stuff.items.visualiser.VLinkFlags;
import net.bdew.ae2stuff.items.visualiser.VisualiserOverlayRender$;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import scala.collection.Seq;

/**
 * Fixes an error due to divide by zero.
 */
@Mixin(value = VisualiserOverlayRender$.class, remap = false)
public class VisualiserOverlayRenderMixin {

    /**
     * Fully replace method as its very difficult to inject into scala.
     */
    @Inject(method = "renderLinks", at = @At("HEAD"), cancellable = true)
    private static void newRenderLinksLogic(Seq<VLink> links, float width, boolean onlyP2P, EntityPlayer player,
                                            CallbackInfo ci) {
        ci.cancel();

        var iter = links.iterator();
        while (iter.hasNext()) {
            VLink link = iter.next();
            if (onlyP2P && !link.flags().contains(VLinkFlags.COMPRESSED()))
                continue;

            int x1 = link.node1().x();
            int x2 = link.node2().x();
            int y1 = link.node1().y();
            int y2 = link.node2().y();
            int z1 = link.node1().z();
            int z2 = link.node2().z();

            /* CHANGE: DO AVERAGE FORMULA FOR CENTER INSTEAD OF... WHATEVER WAS THERE BEFORE */
            double cx = (x1 + x2) / 2.0;
            double cy = (y1 + y2) / 2.0;
            double cz = (z1 + z2) / 2.0;

            double dist = player.getDistanceSq(cx, cy, cz);
            float lineWidth = MathHelper.clamp((float) MathHelper.fastInvSqrt(dist) * width, 1.0f, width);

            GL11.glLineWidth(lineWidth);
            GL11.glBegin(1);

            // Color
            if (link.flags().contains(VLinkFlags.COMPRESSED())) {
                GL11.glColor3f(1.0f, 0.0f, 1.0f);
            } else if (link.flags().contains(VLinkFlags.DENSE())) {
                GL11.glColor3f(1.0f, 1.0f, 0.0f);
            } else {
                GL11.glColor3f(0.0f, 0.0f, 1.0f);
            }

            GL11.glVertex3f(x1 + 0.5f, y1 + 0.5f, z1 + 0.5f);
            GL11.glVertex3f(x2 + 0.5f, y2 + 0.5f, z2 + 0.5f);

            GL11.glEnd();
        }
    }
}
