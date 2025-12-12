package com.the_blood_knight.techrot.client.renderer;

import com.the_blood_knight.techrot.common.entity.ToxicFogEntity;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ToxicFogRenderer extends Render<ToxicFogEntity> {
    public ToxicFogRenderer(RenderManager p_i46554_1_) {
        super(p_i46554_1_);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(ToxicFogEntity p_getEntityTexture_1_) {
        return null;
    }
}
