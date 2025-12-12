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
        this(p_i1776_1_);
        this.owner = owner;
    }

    @Override
    protected void onImpact(RayTraceResult rayTraceResult) {
        if(!this.world.isRemote){
            if(rayTraceResult.entityHit instanceof EntityLivingBase && rayTraceResult.entityHit!=owner){
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
}
