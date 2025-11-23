package com.the_blood_knight.techrot.client.particles;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToxicFogParticle extends Particle {
    public ToxicFogParticle(World worldIn, double posXIn, double posYIn, double posZI, double p_i1221_8_, double p_i1221_10_, double p_i1221_12_n) {
        super(worldIn, posXIn, posYIn,posZI,p_i1221_8_,p_i1221_10_,p_i1221_12_n);
        particleScale = 10;
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ToxicFogParticle(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
