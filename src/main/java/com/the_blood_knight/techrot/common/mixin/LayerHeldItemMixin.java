package com.the_blood_knight.techrot.common.mixin;

import com.the_blood_knight.techrot.Util;
import com.the_blood_knight.techrot.client.model.TechrotArmImplant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.the_blood_knight.techrot.client.layer.ImplantLayer.TEXTURE;

@Mixin(LayerHeldItem.class)
public class LayerHeldItemMixin {
    @Shadow @Final protected RenderLivingBase<?> livingEntityRenderer;

    @Inject(method = "renderHeldItem",at = @At("HEAD"),cancellable = true)
    private void renderArmTechrot(EntityLivingBase p_renderHeldItem_1_, ItemStack p_renderHeldItem_2_, ItemCameraTransforms.TransformType p_renderHeldItem_3_, EnumHandSide p_renderHeldItem_4_, CallbackInfo ci){
        if(p_renderHeldItem_4_==EnumHandSide.RIGHT && Util.hasTechrotArm(Minecraft.getMinecraft().player)){
            ci.cancel();
        }
    }
}
