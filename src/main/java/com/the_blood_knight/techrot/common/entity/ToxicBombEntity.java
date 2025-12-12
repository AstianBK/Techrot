package com.the_blood_knight.techrot.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ToxicBombEntity extends EntityThrowable {
    public ToxicBombEntity(World p_i1776_1_) {
        super(p_i1776_1_);
    }

    @Override
    protected void onImpact(RayTraceResult rayTraceResult) {
        if(rayTraceResult.entityHit instanceof EntityLivingBase){
            Entity living = rayTraceResult.entityHit;
            ToxicFogEntity toxicFog = new ToxicFogEntity(world,living.posX,living.posY,living.posZ);
            world.spawnEntity(toxicFog);
            this.setDead();
        }else if(rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK){
            ToxicFogEntity toxicFog = new ToxicFogEntity(world,rayTraceResult.getBlockPos().getX(),rayTraceResult.getBlockPos().getY(),rayTraceResult.getBlockPos().getZ());
            world.spawnEntity(toxicFog);
            this.setDead();
        }
    }
}
