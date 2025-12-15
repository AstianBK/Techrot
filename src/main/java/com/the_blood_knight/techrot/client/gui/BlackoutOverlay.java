package com.the_blood_knight.techrot.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;

public class BlackoutOverlay extends Gui {

    private final Minecraft mc = Minecraft.getMinecraft();
    private long startTime = -1L;
    private int durationMs = 3200; // fade back over 2 seconds

    public BlackoutOverlay() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (startTime < 0 || event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > durationMs) {
            startTime = -1;
            return;
        }

        float alpha = 1.0f - (float) elapsed / durationMs; // 1 → 0
        if (alpha < 0) alpha = 0;

        drawRect(
                0, 0,
                mc.displayWidth, mc.displayHeight,
                (int) (alpha * 255) << 24 // black with alpha
        );
    }
}

