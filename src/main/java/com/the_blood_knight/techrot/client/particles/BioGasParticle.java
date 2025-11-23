package com.the_blood_knight.techrot.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

@SideOnly(Side.CLIENT)
public class BioGasParticle extends Particle {
    public BioGasParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1221_8_, double p_i1221_10_, double p_i1221_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i1221_8_, p_i1221_10_, p_i1221_12_);
        this.particleRed = 0.0F;
        this.particleGreen = 0.75F;
        this.particleBlue = 0.0F;
        this.particleAlpha = 0.1F;
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9599999785423279D;
        this.motionY *= 1;
        this.motionZ *= 0.9599999785423279D;
        EntityPlayer entityplayer = this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 2.0D, false);

        if (entityplayer != null)
        {
            AxisAlignedBB axisalignedbb = entityplayer.getEntityBoundingBox();

            if (this.posY > axisalignedbb.minY)
            {
                this.posY += (axisalignedbb.minY - this.posY) * 0.2D;
                this.motionY += (entityplayer.motionY - this.motionY) * 0.2D;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks,
                               float rotationX, float rotationZ, float rotationYZ,
                               float rotationXY, float rotationXZ)
    {
        // POSICIÓN INTERPOLADA
        double px = this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX;
        double py = this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY;
        double pz = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ;

        float s = 1F;

        GlStateManager.pushMatrix();
        GlStateManager.translate(px, py, pz);

        // Rota según la cámara
        GlStateManager.rotate(0, 0, 1, 0);
        GlStateManager.rotate(0, 1, 0, 0);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.color(0f, 1f, 0f, 0.2f); // Verde transparente

        //----------------------------------------------------
        // CUBO 3D con OpenGL inmediato (seguro para partículas)
        //----------------------------------------------------
        GL11.glBegin(GL11.GL_QUADS);

        // Frente
        GL11.glVertex3f(-s, -s,  s);
        GL11.glVertex3f( s, -s,  s);
        GL11.glVertex3f( s,  s,  s);
        GL11.glVertex3f(-s,  s,  s);

        // Atrás
        GL11.glVertex3f(-s, -s, -s);
        GL11.glVertex3f(-s,  s, -s);
        GL11.glVertex3f( s,  s, -s);
        GL11.glVertex3f( s, -s, -s);

        // Izquierda
        GL11.glVertex3f(-s, -s, -s);
        GL11.glVertex3f(-s, -s,  s);
        GL11.glVertex3f(-s,  s,  s);
        GL11.glVertex3f(-s,  s, -s);

        // Derecha
        GL11.glVertex3f( s, -s, -s);
        GL11.glVertex3f( s,  s, -s);
        GL11.glVertex3f( s,  s,  s);
        GL11.glVertex3f( s, -s,  s);

        // Abajo
        GL11.glVertex3f(-s, -s, -s);
        GL11.glVertex3f( s, -s, -s);
        GL11.glVertex3f( s, -s,  s);
        GL11.glVertex3f(-s, -s,  s);

        // Arriba
        GL11.glVertex3f(-s,  s, -s);
        GL11.glVertex3f(-s,  s,  s);
        GL11.glVertex3f( s,  s,  s);
        GL11.glVertex3f( s,  s, -s);

        GL11.glEnd();

        //----------------------------------------------------

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }


    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new BioGasParticle(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
