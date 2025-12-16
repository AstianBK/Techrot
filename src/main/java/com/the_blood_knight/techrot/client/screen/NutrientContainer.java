package com.the_blood_knight.techrot.client.screen;

import com.the_blood_knight.techrot.client.screen.gui_component.OverlayNutrient;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class NutrientContainer extends GuiContainer {
    public final OverlayNutrient nutrient = new OverlayNutrient();
    public NutrientContainer(Container p_i1072_1_) {
        super(p_i1072_1_);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
    }


}
