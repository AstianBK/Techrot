package com.the_blood_knight.techrot.client.screen;

import com.google.common.collect.Maps;
import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.container.BioFurnaceContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

public class BioFurnaceScreen extends NutrientContainer {
    private static final ResourceLocation[] FRAMES = new ResourceLocation[]{
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofurnace_0.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofurnace_1.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofurnace_2.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofurnace_3.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofurnace_4.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofurnace_5.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofurnace_6.png")
    };
    private final InventoryPlayer playerInventory;
    private final IInventory tileFurnace;

    public BioFurnaceScreen(InventoryPlayer playerInv, IInventory furnaceInv)
    {
        super(new BioFurnaceContainer(playerInv, furnaceInv));
        this.playerInventory = playerInv;
        this.tileFurnace = furnaceInv;

    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.tileFurnace.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int frame = (int) ((0.5F * (partialTicks+this.playerInventory.player.ticksExisted)) % 7);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(FRAMES[frame]);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 176, 166);

        int k = this.getBurnLeftScaled(49);
        this.drawTexturedModalRect(i + 45, j + 54, 176, 35, k, 18);

        this.nutrient.render(TextFormatting.BLUE +"Paste : "+TextFormatting.GREEN +this.tileFurnace.getField(0) +" lts",fontRenderer,mouseX,mouseY,i + 45, j + 54,49,18);


        int l = this.getCookProgressScaled(18);
        this.drawTexturedModalRect(i + 79, j + 14, 176, 16, l, 18);


    }

    private int getCookProgressScaled(int pixels)
    {
        int i = this.tileFurnace.getField(2);
        int j = this.tileFurnace.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int i = (int) 1000.0F;

        if (i == 0)
        {
            i = 200;
        }

        return (int) (((float)this.tileFurnace.getField(0) / i) *pixels);
    }
}
