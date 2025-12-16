package com.the_blood_knight.techrot.client.screen;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.container.BioFleshClonerContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class BioFleshClonerScreen extends NutrientContainer {
    private static final ResourceLocation[] FRAMES = new ResourceLocation[]{
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofleshcloner_gui_0.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofleshcloner_gui_1.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofleshcloner_gui_2.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofleshcloner_gui_3.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofleshcloner_gui_4.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofleshcloner_gui_5.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofleshcloner_gui_6.png"),
            new ResourceLocation(Techrot.MODID,"textures/gui/container/biofleshcloner_gui_7 .png")
    };
    private final InventoryPlayer playerInventory;
    private final IInventory tileFurnace;

    public BioFleshClonerScreen(InventoryPlayer playerInv, IInventory furnaceInv)
    {
        super(new BioFleshClonerContainer(playerInv, furnaceInv));
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
        this.drawTexturedModalRect(i, j, 0, 0, 176, 186);

        int k = this.getNutrition(49);
        this.drawTexturedModalRect(i + 60, j + 72, 176, 20, k, 18);
        this.nutrient.render(TextFormatting.BLUE +"Paste : "+TextFormatting.GREEN +this.tileFurnace.getField(0) +" lts",fontRenderer,mouseX,mouseY,i + 60, j + 72,49,18);


        int l = this.getCloning(34);
        this.drawTexturedModalRect(i + 91, j + 22, 176, 0, l, 19);
    }

    private int getNutrition(int pixels) {
        int i = this.tileFurnace.getField(0);
        int j = 1000;
        return j != 0 && i != 0 ? (int) (((float) i / (float) j) * pixels) : 0;
    }

    private int getCloning(int pixels) {
        return (int) (((float)this.tileFurnace.getField(1) / 200.0F) * pixels);
    }
}
