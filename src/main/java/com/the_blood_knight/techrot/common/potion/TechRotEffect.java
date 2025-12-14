package com.the_blood_knight.techrot.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class TechRotEffect extends Potion {
    public TechRotEffect() {
        super(false, 0x8A2BE2);
        this.setPotionName("effect.techrot");
        this.setRegistryName("techrot");
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {

    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
