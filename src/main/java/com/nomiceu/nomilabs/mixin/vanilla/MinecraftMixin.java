package com.nomiceu.nomilabs.mixin.vanilla;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.common.MinecraftForge;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.config.LabsVersionConfig;
import com.nomiceu.nomilabs.event.LabsResourcesRefreshedEvent;
import com.nomiceu.nomilabs.mixinhelper.ResourcesObserver;

/**
 * Allows Setting of Window Title and Icon. Also calls an event on Resources Reload.
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "refreshResources", at = @At("RETURN"))
    private void callResourcesRefreshedEvent(CallbackInfo ci) {
        if (ResourcesObserver.shouldCallEvent())
            MinecraftForge.EVENT_BUS.post(new LabsResourcesRefreshedEvent());
    }

    @Redirect(method = "createDisplay",
              at = @At(value = "INVOKE",
                       target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V",
                       remap = false),
              require = 1)
    private void setCustomTitle(String title) {
        if (LabsConfig.advanced.windowOverrides.windowTitleOverride.isEmpty()) Display.setTitle(title);
        else Display.setTitle(LabsConfig.advanced.windowOverrides.windowTitleOverride
                .replace("{version}", LabsVersionConfig.formattedVersion));
    }

    @Inject(method = "setWindowIcon", at = @At("HEAD"), cancellable = true)
    public void setCustomWindowIcon(CallbackInfo ci) {
        var x16 = LabsConfig.advanced.windowOverrides.windowLogo16xOverride;
        var x32 = LabsConfig.advanced.windowOverrides.windowLogo32xOverride;
        var x256 = LabsConfig.advanced.windowOverrides.windowLogo256xOverride;
        if (x16.isEmpty() || x32.isEmpty() || x256.isEmpty()) return;

        // From Random Patches (See Below)
        // https://github.com/TheRandomLabs/RandomPatches/blob/1.12/src/main/java/com/therandomlabs/randompatches/client/WindowIconHandler.java
        InputStream stream16 = null;
        InputStream stream32 = null;
        InputStream stream256 = null;
        var osX = Util.getOSType() == Util.EnumOS.OSX;

        try {
            stream16 = new FileInputStream(x16);
            stream32 = new FileInputStream(x32);

            if (osX) {
                stream256 = new FileInputStream(x256);
                Display.setIcon(new ByteBuffer[] {
                        readImageToBuffer(stream16, 16),
                        readImageToBuffer(stream32, 32),
                        readImageToBuffer(stream256, 256)
                });
            } else {
                Display.setIcon(new ByteBuffer[] {
                        readImageToBuffer(stream16, 16),
                        readImageToBuffer(stream32, 32)
                });
            }
        } catch (IOException ex) {
            NomiLabs.LOGGER.error("Failed to Read Custom Window Icon! Is the Path Correct?", ex);
        } finally {
            IOUtils.closeQuietly(stream16);
            IOUtils.closeQuietly(stream32);

            if (osX) {
                IOUtils.closeQuietly(stream256);
            }
        }
        ci.cancel();
    }

    /**
     * From <a href=
     * "https://github.com/TheRandomLabs/RandomPatches/blob/1.12/src/main/java/com/therandomlabs/randompatches/client/WindowIconHandler.java">RandomPatches</a>.
     */
    @Unique
    private static ByteBuffer readImageToBuffer(InputStream stream, int dimensions)
                                                                                    throws IOException {
        BufferedImage image = ImageIO.read(stream);

        if (image.getWidth() != dimensions || image.getHeight() != dimensions) {
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();

            GraphicsDevice device = environment.getDefaultScreenDevice();

            GraphicsConfiguration gc = device.getDefaultConfiguration();

            BufferedImage resized = gc.createCompatibleImage(
                    dimensions,
                    dimensions,
                    image.getTransparency());

            Graphics2D graphics = resized.createGraphics();

            graphics.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            graphics.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            graphics.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            graphics.drawImage(image, 0, 0, dimensions, dimensions, null);

            graphics.dispose();

            image = resized;
        }

        int[] rgb = image.getRGB(0, 0, dimensions, dimensions, null, 0, dimensions);
        ByteBuffer buffer = ByteBuffer.allocate(rgb.length * 4);

        for (int i : rgb) {
            buffer.putInt(i << 8 | i >> 24 & 255);
        }

        buffer.flip();
        return buffer;
    }
}
