package com.nomiceu.nomilabs.mixin;

import com.nomiceu.nomilabs.config.LabsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngameForge.class, remap = false)
public abstract class GuiIngameForgeMixin extends GuiIngame {
    @Shadow
    public static int left_height;

    @Shadow
    public abstract void renderHealth(int width, int height);

    @Shadow
    private ScaledResolution res;

    /**
     * Default Ignored Constructor
     */
    public GuiIngameForgeMixin(Minecraft mcIn) {
        super(mcIn);
    }

    @Inject(method = "renderGameOverlay(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/GuiIngameForge;renderHealth(II)V", shift = At.Shift.AFTER, remap = false), remap = true)
    public void renderHealthAgain(float partialTicks, CallbackInfo ci) {
        if (LabsConfig.advanced.doubleHealthMode == LabsConfig.Advanced.DoubleHealthMode.NONE) return;
        if (LabsConfig.advanced.doubleHealthMode == LabsConfig.Advanced.DoubleHealthMode.SPACE) left_height += 10;
        renderHealth(res.getScaledWidth(), res.getScaledHeight());
    }
    /*
    @Inject(method = "renderHealth", at = @At("TAIL"))
    public void renderSecondHealthLogic(int width, int height, CallbackInfo ci) {
        renderSecondHealthOld(width, height);
    }

    @Unique
    public void renderSecondHealth(int width, int height) {
        if (LabsConfig.advanced.doubleHealthMode == LabsConfig.Advanced.DoubleHealthMode.NONE) return;
        bind(ICONS);
        if (pre(HEALTH)) return;
        mc.profiler.startSection("secondHealth");
        GlStateManager.enableBlend();

        EntityPlayer player = (EntityPlayer) this.mc.getRenderViewEntity();
        int health = MathHelper.ceil(player.getHealth());
        boolean highlight = healthUpdateCounter > (long) updateCounter && (healthUpdateCounter - (long) updateCounter) / 3L % 2L == 1L;

        if (health < this.playerHealth && player.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = (long) (this.updateCounter + 20);
        } else if (health > this.playerHealth && player.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = (long) (this.updateCounter + 10);
        }

        if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
            this.playerHealth = health;
            this.lastPlayerHealth = health;
            this.lastSystemTime = Minecraft.getSystemTime();
        }

        this.playerHealth = health;
        int healthLast = this.lastPlayerHealth;

        IAttributeInstance attrMaxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        float healthMax = (float) attrMaxHealth.getAttributeValue();
        float absorb = MathHelper.ceil(player.getAbsorptionAmount());

        int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        this.rand.setSeed((long) (updateCounter * 312871));

        int left = width / 2 - 91;
        int top = height - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10) left_height += 10 - rowHeight;

        int regen = -1;
        if (player.isPotionActive(MobEffects.REGENERATION)) {
            regen = updateCounter % 25;
        }

        final int TOP = 9 * (mc.world.getWorldInfo().isHardcoreModeEnabled() ? 5 : 0);
        final int BACKGROUND = (highlight ? 25 : 16);
        int MARGIN = 16;
        if (player.isPotionActive(MobEffects.POISON)) MARGIN += 36;
        else if (player.isPotionActive(MobEffects.WITHER)) MARGIN += 72;
        float absorbRemaining = absorb;
        int hearts = MathHelper.ceil((healthMax + absorb) / 2.0F);
        int rows = MathHelper.ceil(hearts / 10.0F);

        for (int i = hearts - 1; i >= 0; --i) {
            //int b0 = (highlight ? 1 : 0);
            int row = MathHelper.ceil((float) (i + 1) / 10.0F);
            if (LabsConfig.advanced.doubleHealthMode == LabsConfig.Advanced.DoubleHealthMode.NO_SPACE) row -= 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4) y += rand.nextInt(2);
            if (i == regen) y -= 2;

            drawTexturedModalRect(x, y, BACKGROUND, TOP, 9, 9);

            if (highlight) {
                if (i * 2 + 1 < healthLast)
                    drawTexturedModalRect(x, y, MARGIN + 54, TOP, 9, 9); //6
                else if (i * 2 + 1 == healthLast)
                    drawTexturedModalRect(x, y, MARGIN + 63, TOP, 9, 9); //7
            }

            if (absorbRemaining > 0.0F) {
                if (absorbRemaining == absorb && absorb % 2.0F == 1.0F) {
                    drawTexturedModalRect(x, y, MARGIN + 153, TOP, 9, 9); //17
                    absorbRemaining -= 1.0F;
                } else {
                    drawTexturedModalRect(x, y, MARGIN + 144, TOP, 9, 9); //16
                    absorbRemaining -= 2.0F;
                }
            } else {
                if (i * 2 + 1 < health)
                    drawTexturedModalRect(x, y, MARGIN + 36, TOP, 9, 9); //4
                else if (i * 2 + 1 == health)
                    drawTexturedModalRect(x, y, MARGIN + 45, TOP, 9, 9); //5
            }
        }

        GlStateManager.disableBlend();
        mc.profiler.endSection();
        post(HEALTH);
    }

     */
}
