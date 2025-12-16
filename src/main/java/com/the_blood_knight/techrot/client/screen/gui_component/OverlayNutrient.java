package com.the_blood_knight.techrot.client.screen.gui_component;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OverlayNutrient {
    public OverlayNutrient(){

    }

    public void render(int nutrient,FontRenderer renderer,int mouseX,int mouseY,int posX,int posY,int sizeX,int sizeY){
        if(mouseX > posX && mouseX < posX+sizeX && mouseY > posY && mouseY<posY+sizeY){
            GuiUtils.drawHoveringText(Arrays.asList(TextFormatting.BLUE +"Paste : "+TextFormatting.GREEN +nutrient +" lts"),mouseX+100,mouseY, 176,166, 100, renderer);
        }
    }

}
