package com.the_blood_knight.techrot.common.mixin;

import com.the_blood_knight.techrot.Util;
import com.the_blood_knight.techrot.client.model.TechrotArmImplant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
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

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow @Final private Minecraft mc;

    @Shadow @Final private RenderManager renderManager;
    @Unique
    public final TechrotArmImplant techrot$modelArm = new TechrotArmImplant();

    @Inject(method = "renderArmFirstPerson",at = @At("HEAD"),cancellable = true)
    private void renderArmTechrot(float p_renderArmFirstPerson_1_, float p_renderArmFirstPerson_2_, EnumHandSide p_renderArmFirstPerson_3_, CallbackInfo ci){
        if(p_renderArmFirstPerson_3_==EnumHandSide.RIGHT && Util.hasTechrotArm(this.mc.player)){
            float f = 1.0F;
            float f1 = MathHelper.sqrt(p_renderArmFirstPerson_2_);
            float f2 = -0.3F * MathHelper.sin(f1 * 3.1415927F);
            float f3 = 0.4F * MathHelper.sin(f1 * 6.2831855F);
            float f4 = -0.4F * MathHelper.sin(p_renderArmFirstPerson_2_ * 3.1415927F);
            GlStateManager.translate(f * (f2 + 0.64000005F), f3 + -0.6F + p_renderArmFirstPerson_1_ * -0.6F, f4 + -0.71999997F);
            GlStateManager.rotate(f * 45.0F, 0.0F, 1.0F, 0.0F);
            float f5 = MathHelper.sin(p_renderArmFirstPerson_2_ * p_renderArmFirstPerson_2_ * 3.1415927F);
            float f6 = MathHelper.sin(f1 * 3.1415927F);
            GlStateManager.rotate(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
            AbstractClientPlayer abstractclientplayer = this.mc.player;
            this.mc.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
            GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
            GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);
            Render renderplayer = this.renderManager.getEntityRenderObject(abstractclientplayer);
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            ((RenderPlayer)renderplayer).renderRightArm(abstractclientplayer);
            techrot$modelArm.renderRightArm(((RenderPlayer)renderplayer).getMainModel().bipedRightArm);
            techrot$modelArm.right_arm.render(0.0625F);

            GlStateManager.popMatrix();
            GlStateManager.enableCull();
        }
    }
}
