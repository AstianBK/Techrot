package com.the_blood_knight.techrot.client.gui;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.Util;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


import java.util.Random;


public class RotGui extends Gui {
    private static final ResourceLocation HEARTS_TEX = new ResourceLocation(Techrot.MODID, "textures/gui/decaying_hearts.png");


    private final Minecraft mc = Minecraft.getMinecraft();
    private final Random rand = new Random();



    private static final int HEART_W = 9;
    private static final int HEART_H = 9;



    private static final int U_FULL = 36;
    private static final int U_HALF = 63;
    private static final int U_EMPTY = 0;
    private static final int U_GLOW = 27;
    private static final int U_ABS_FULL = 36;
    private static final int U_ABS_HALF = 45;
    private float gasAlpha = 0.0F;
    private static final float FADE_IN_SPEED = 0.04F;
    private static final float FADE_OUT_SPEED = 0.08F;


    long healthBlinkTime = 0;
    long lastHealthTime = 0;
    int displayHealth;
    private float lastHealth = -1f;
    private int healFlashTimer = 0;


    public RotGui() {
        MinecraftForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent
    public void onPreRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
            onRenderOverlay(event);
        }
    }
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        boolean inGas = mc.player.isPotionActive(TRegistry.TECHROT_EFFECT);

        if (inGas) {
            gasAlpha = Math.min(1.0F, gasAlpha + FADE_IN_SPEED);
        } else {
            gasAlpha = Math.max(0.0F, gasAlpha - FADE_OUT_SPEED);
        }


        if (gasAlpha <= 0.01F) return;

        int frame = (int) ((0.07F * (event.getPartialTicks() + mc.player.ticksExisted)) % 7);
        mc.getTextureManager().bindTexture(
                new ResourceLocation(Techrot.MODID, "textures/overlay/toxicgas_overlay_" + frame + ".png")
        );

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
        );
        GlStateManager.color(1F, 1F, 1F, gasAlpha);

        ScaledResolution res = new ScaledResolution(mc);
        Gui.drawModalRectWithCustomSizedTexture(
                0, 0, 0, 0,
                res.getScaledWidth(),
                res.getScaledHeight(),
                res.getScaledWidth(),
                res.getScaledHeight()
        );

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
    }


    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (mc.player == null) return;
        EntityPlayer player = mc.player;

        ITechRotPlayer cap = player.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES,null);
        if(cap!=null){
            event.setCanceled(true);
            GlStateManager.enableBlend();

            ScaledResolution sr = event.getResolution();
            int screenW = sr.getScaledWidth();
            int screenH = sr.getScaledHeight();




            int health = ceil(player.getHealth());
            float absorb = ceil(player.getAbsorptionAmount());
            float healthMax = ceil(player.getMaxHealth());
            int ticks = mc.ingameGUI.getUpdateCounter();
            boolean highlight = this.healthBlinkTime > (long)ticks && (this.healthBlinkTime - (long)ticks) / 3L % 2L == 1L;
            if (health < this.lastHealth && player.hurtResistantTime > 0)
            {
                this.lastHealthTime = System.currentTimeMillis();
                this.healthBlinkTime = (long) (ticks + 20);
            }
            else if (health > this.lastHealth && player.hurtResistantTime> 0)
            {
                this.lastHealthTime = System.currentTimeMillis();
                this.healthBlinkTime = (long) (ticks + 10);
            }

            if (System.currentTimeMillis() - this.lastHealthTime > 1000L)
            {
                this.lastHealth = health;
                this.displayHealth = health;
                this.lastHealthTime = System.currentTimeMillis();
            }
            int absorptionHearts = ceil(absorb / 2.0f) - 1;
            int hearts = ceil(healthMax / 2.0f) - 1;
            int healthRows = ceil((healthMax + absorb) / 2.0F / 10.0F);
            int totalHealthRows = ceil((healthMax + absorb) / 2.0F / 10.0F);
            int rowHeight = Math.max(10 - (healthRows - 2), 3);
            int extraHealthRows = totalHealthRows - healthRows;
            int extraRowHeight = clamp(10 - (healthRows - 2), 3, 10);

            if (lastHealth < 0) lastHealth = health;
            if (health > lastHealth) {
                healFlashTimer = 20;
            }
            this.lastHealth = health;
            int healthLast = this.displayHealth;

            float f = Math.max(player.getMaxHealth(), (float)Math.max(health, healthLast));
            int regen = -1;
            if (player.isPotionActive(MobEffects.REGENERATION)){
                regen = ticks % ceil(f + 5.0F);
            }
            if (healFlashTimer > 0) healFlashTimer--;


            int left = screenW / 2 - 91;
            int top = screenH - 39;
            if (rowHeight != 10){
                //top += 10 - rowHeight;
            }
            final int TOP = 0;
            final int BACKGROUND = (highlight ? 25 : 16);
            int MARGIN = 16;
            rand.setSeed(mc.ingameGUI.getUpdateCounter() * 312871L);
            int[] random = new int[10];
            for (int i = 0; i < random.length; ++i) random[i] = rand.nextInt(2);

            drawVanillaHearts(health,highlight,healthLast,player.getMaxHealth(),rowHeight,left,top,regen,random,TOP,BACKGROUND,MARGIN);

            mc.getTextureManager().bindTexture(HEARTS_TEX);
            healthMax = cap.getHeartRot();

            for (int i = ceil((healthMax) / 2.0F) - 1; i >= 0; -- i) {
                int row = i / 10;
                int heart = i % 10;
                int x = left + heart * 8;
                int y = top - row * rowHeight;
                if (health <= 4) y += random[MathHelper.clamp(i, 0, random.length - 1)];
                if (i == regen) y -= 2;
                drawTexturedModalRect( x, y, highlight ? 9 : 0, 0, 9, 9);
                if (i * 2 + 1 < healthLast && highlight){
                    drawTexturedModalRect( x, y,54 , 0, 9, 9);
                }
                if (i * 2 + 1 < health){
                    drawTexturedModalRect( x, y, 36, 0, 9, 9);
                } else if (i * 2 + 1 == health){
                    drawTexturedModalRect( x, y, 45, 0, 9, 9);
                }
            }
            GlStateManager.disableBlend();
            mc.renderEngine.bindTexture(Gui.ICONS);
        }
    }

    private void drawVanillaHearts(int health, boolean highlight, int healthLast, float healthMax, int rowHeight, int left, int top, int regen, int[] lowHealthBob, int TOP, int BACKGROUND, int MARGIN) {
        float absorb = MathHelper.ceil(Minecraft.getMinecraft().player.getAbsorptionAmount());
        float absorbRemaining = absorb;
        float healthTotal = health + absorb;

        int iStart = MathHelper.ceil((healthMax + absorb) / 2f) - 1;
        for (int i = iStart; i >= 0; --i) {
            int row = MathHelper.ceil((i + 1) / 10f) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4)
                y += lowHealthBob[MathHelper.clamp(i, 0, lowHealthBob.length - 1)];
            if (i == regen)
                y -= 2;

            drawTexturedModalRect(x, y, BACKGROUND, TOP, 9, 9);

            if (highlight) {
                if (i * 2 + 1 < healthLast)
                    drawTexturedModalRect(x, y, MARGIN + 54, TOP, 9, 9);
                else if (i * 2 + 1 == healthLast)
                    drawTexturedModalRect(x, y, MARGIN + 63, TOP, 9, 9);
            }

            if (absorbRemaining > 0f) {
                if (absorbRemaining == absorb && absorb % 2f == 1f) {
                    drawTexturedModalRect(x, y, MARGIN + 153, TOP, 9, 9);
                    absorbRemaining -= 1f;
                } else {
                    if (i * 2 + 1 < healthTotal)
                        drawTexturedModalRect(x, y, MARGIN + 144, TOP, 9, 9);
                    absorbRemaining -= 2f;
                }
            } else {
                if (i * 2 + 1 < health)
                    drawTexturedModalRect(x, y, MARGIN + 36, TOP, 9, 9);
                else if (i * 2 + 1 == health)
                    drawTexturedModalRect(x, y, MARGIN + 45, TOP, 9, 9);
            }
        }
    }
    public static int ceil(float pValue) {
        int i = (int)pValue;
        return pValue > (float)i ? i + 1 : i;
    }
    public static int clamp(int pValue, int pMin, int pMax) {
        return Math.min(Math.max(pValue, pMin), pMax);
    }
}