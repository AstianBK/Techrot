package com.the_blood_knight.techrot.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ToxicBombEntity extends EntityThrowable {
    public Entity owner = null;
    public ToxicBombEntity(World world){
        super(world);
    }
    public ToxicBombEntity(World p_i1776_1_,Entity owner) {
        super(p_i1776_1_, (EntityLivingBase) owner);
        this.owner = owner;
    }

    @Override
    protected void onImpact(RayTraceResult rayTraceResult) {
        if(!this.world.isRemote){
            if(rayTraceResult.entityHit instanceof EntityLivingBase && rayTraceResult.entityHit!=owner){
                Entity living = rayTraceResult.entityHit;
                ToxicFogEntity toxicFog = new ToxicFogEntity(world,living.posX,living.posY,living.posZ,owner);
                world.spawnEntity(toxicFog);
                this.setDead();
            }

            ToxicFogEntity toxicFog = new ToxicFogEntity(world,rayTraceResult.getBlockPos().getX(),rayTraceResult.getBlockPos().getY(),rayTraceResult.getBlockPos().getZ(),owner);
            world.spawnEntity(toxicFog);
            this.setDead();

        }
    }
}
