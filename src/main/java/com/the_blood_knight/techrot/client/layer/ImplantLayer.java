package com.the_blood_knight.techrot.client.layer;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.client.model.TechrotArmImplant;
import com.the_blood_knight.techrot.client.model.TechrotPackImplant;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ImplantLayer<T extends EntityPlayer> implements LayerRenderer<T> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Techrot.MODID,"textures/entity/bioarmors.png");

    public final TechrotArmImplant modelArm = new TechrotArmImplant();
    public final TechrotPackImplant modelWings = new TechrotPackImplant();

    public final RenderPlayer renderer;
    public ImplantLayer(RenderPlayer renderer){
        this.renderer = renderer;
    }
    @Override
    public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();

        ITechRotPlayer cap = entitylivingbaseIn.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES,null);
        if(cap!=null){
            boolean hasArm = false;
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            for (int i = 0 ; i < cap.getInventory().getSlots() ; i++){
                ItemStack stack = cap.getInventory().getStackInSlot(i);

                if(stack.getItem() == TRegistry.ROTPLATE_ARM){
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
                    modelWings.renderRightArm(this.renderer.getMainModel().bipedRightArm);
                    modelWings.right_arm.render(scale);
                    hasArm = true;
                    GlStateManager.enableBlend();
                    int frame = (int) ((0.25F * (partialTicks+entitylivingbaseIn.ticksExisted)) % 7);
                    ResourceLocation location = new ResourceLocation(Techrot.MODID,"textures/entity/bioglowing_"+frame+".png");
                    Minecraft.getMinecraft().getTextureManager().bindTexture(location);

                    GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE); // additive glow
                    GlStateManager.disableLighting();

                    modelWings.right_arm.render(scale);

                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                }
                if(stack.getItem() == TRegistry.ROTPLATE_CHEST){
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
                    modelWings.renderChest(this.renderer.getMainModel().bipedBody);
                    modelWings.torso.render(scale);
                    GlStateManager.enableBlend();
                    int frame = (int) ((0.25F * (partialTicks+entitylivingbaseIn.ticksExisted)) % 7);
                    ResourceLocation location = new ResourceLocation(Techrot.MODID,"textures/entity/bioglowing_"+frame+".png");
                    Minecraft.getMinecraft().getTextureManager().bindTexture(location);

                    GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE); // additive glow
                    GlStateManager.disableLighting();

                    modelWings.torso.render(scale);

                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                }
                if (stack.getItem() == TRegistry.ROTPLATE_WINGS) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);



                    modelWings.copyPose(this.renderer.getMainModel().bipedBody,entitylivingbaseIn.isSneaking());

                    modelWings.torsopack.render(scale);

                    if(cap.isFly()){
                        GlStateManager.enableBlend();

                        int frame = (int) ((1.25F * (partialTicks+entitylivingbaseIn.ticksExisted)) % 7);

                        ResourceLocation location = new ResourceLocation(Techrot.MODID,"textures/entity/bioglowing_"+frame+".png");

                        Minecraft.getMinecraft().getTextureManager().bindTexture(location);

                        GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
                        GlStateManager.disableLighting();

                        modelWings.torsopack.render(scale);

                        GlStateManager.enableLighting();
                        GlStateManager.disableBlend();
                    }
                }
                if(stack.getItem() == TRegistry.ROTPLATE_HEAD){
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

                    modelWings.renderHead(this.renderer.getMainModel().bipedHead);
                    modelWings.head.render(scale);
                    GlStateManager.enableBlend();

                    int frame = (int) ((0.25F * (partialTicks+entitylivingbaseIn.ticksExisted)) % 7);

                    ResourceLocation location = new ResourceLocation(Techrot.MODID,"textures/entity/bioglowing_"+frame+".png");

                    Minecraft.getMinecraft().getTextureManager().bindTexture(location);

                    GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE); // additive glow
                    GlStateManager.disableLighting();

                    modelWings.head.render(scale);

                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                }
            }
            if(hasArm){
                renderHeldItem(entitylivingbaseIn,entitylivingbaseIn.getHeldItemMainhand(), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,EnumHandSide.RIGHT);
            }
        }
        GlStateManager.popMatrix();
    }

    private void renderHeldItem(EntityLivingBase p_renderHeldItem_1_, ItemStack p_renderHeldItem_2_, ItemCameraTransforms.TransformType p_renderHeldItem_3_, EnumHandSide p_renderHeldItem_4_) {
        if (!p_renderHeldItem_2_.isEmpty()) {
            GlStateManager.pushMatrix();
            if (p_renderHeldItem_1_.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.modelWings.right_arm.postRender(0.0625F);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            boolean flag = p_renderHeldItem_4_ == EnumHandSide.LEFT;
            GlStateManager.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(p_renderHeldItem_1_, p_renderHeldItem_2_, p_renderHeldItem_3_, flag);
            GlStateManager.popMatrix();
        }

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
