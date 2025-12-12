package com.the_blood_knight.techrot.common.entity;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRegistry;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ToxicFogEntity extends EntityAreaEffectCloud {
    public ToxicFogEntity(World p_i46809_1_) {
        super(p_i46809_1_);
    }
    public ToxicFogEntity(World p_i46810_1_, double p_i46810_2_, double p_i46810_4_, double p_i46810_6_) {
        this(p_i46810_1_);
        this.setRadius(5.0F);
        this.setPosition((int)p_i46810_2_, (int)p_i46810_4_,(int) p_i46810_6_);
        this.addEffect(new PotionEffect(TRegistry.TECHROT_EFFECT,100,0));
        this.setWaitTime(5);
        this.setRadiusPerTick((float) -5 /300);
        this.setRadiusOnUse(-0.5F);
        this.setDuration(200);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.world.isRemote){
            if(this.ticksExisted%5 == 0){
                Techrot.spawnPeste(this.world,this.getPosition(),rand,getRadius());
            }
        }
    }
}
