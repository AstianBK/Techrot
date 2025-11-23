package com.the_blood_knight.techrot.client.screen;

import com.the_blood_knight.techrot.Techrot;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;

public class BioPastemakerScreen extends GuiContainer {
    private static final ResourceLocation[] FRAMES = new ResourceLocation[]{
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biopastemaker_gui_0.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biopastemaker_gui_1.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biopastemaker_gui_2.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biopastemaker_gui_3.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biopastemaker_gui_4.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biopastemaker_gui_5.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biopastemaker_gui_6.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biopastemaker_gui_7.png")
    };    private final InventoryPlayer playerInventory;
    private final IInventory tileFurnace;

    public BioPastemakerScreen(InventoryPlayer playerInv, IInventory furnaceInv)
    {
        super(new ContainerFurnace(playerInv, furnaceInv));
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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        int frame = (int) ((1.25F * (partialTicks+this.playerInventory.player.ticksExisted)) % 8);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(FRAMES[frame]);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    private int getCookProgressScaled(int pixels)
    {
        int i = this.tileFurnace.getField(2);
        int j = this.tileFurnace.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int i = this.tileFurnace.getField(1);

        if (i == 0)
        {
            i = 200;
        }

        return this.tileFurnace.getField(0) * pixels / i;
    }
}
