package com.the_blood_knight.techrot.client.layer;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.client.model.TechrotArmImplant;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ImplantLayer<T extends EntityPlayer> implements LayerRenderer<T> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Techrot.MODID,"textures/entity/bioarmors.png");

    public final TechrotArmImplant modelArm = new TechrotArmImplant();
    public final RenderPlayer renderer;
    public ImplantLayer(RenderPlayer renderer){
        this.renderer = renderer;
    }
    @Override
    public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        GlStateManager.pushMatrix();

        //renderer.getMainModel().bipedRightArm.postRender(scale);
        ITechRotPlayer cap = entitylivingbaseIn.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES,null);
        if(cap!=null){
            for (int i = 0 ; i < cap.getInventory().getSlots() ; i++){
                ItemStack stack = cap.getInventory().getStackInSlot(i);

                if(stack.getItem() == TRegistry.ROTPLATE_ARM){
                    modelArm.renderRightArm(this.renderer.getMainModel().bipedRightArm);
                    modelArm.right_arm.render(scale);
                }
                if(stack.getItem() == TRegistry.ROTPLATE_CHEST){
                    modelArm.renderChest(this.renderer.getMainModel().bipedBody);
                    modelArm.torso.render(scale);
                }
                if(stack.getItem() == TRegistry.ROTPLATE_HEAD){
                    modelArm.renderHead(this.renderer.getMainModel().bipedHead);
                    modelArm.head.render(scale);
                }
            }
        }
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
