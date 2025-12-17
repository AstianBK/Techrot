package com.the_blood_knight.techrot.client.particles;

import com.the_blood_knight.techrot.Techrot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class ToxicFogParticle extends Particle {
    public float roll = 0.0F;
    public float oRoll = 0.0F;
    float xdxd;
    float zdzd;
    public final ResourceLocation tex = new ResourceLocation(Techrot.MODID,"textures/particles/toxic_fog.png");
    public ToxicFogParticle(World worldIn, double posXIn, double posYIn, double posZI, double p_i1221_8_, double p_i1221_10_, double p_i1221_12_n) {
        super(worldIn, posXIn, posYIn,posZI,p_i1221_8_,p_i1221_10_,p_i1221_12_n);
        particleScale = 4;
        this.roll = (float) (Math.PI/2.0F * worldIn.rand.nextInt(4));
        this.oRoll = this.roll;
        this.motionX = (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionY = 0;
        this.motionZ = 0 ;
        float f = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX = this.motionX / (double)f1 * (double)f * 0.4000000059604645D;
        this.motionZ = this.motionZ / (double)f1 * (double)f * 0.4000000059604645D;
        this.particleAlpha = 0.2F;
        this.particleRed = (105.0F-rand.nextFloat()*49)/255.0F;
        this.particleGreen = (153.0F-rand.nextFloat()*59)/255.0F;
        this.particleBlue = (26.0F-rand.nextFloat()*3)/255.0F;

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

        this.motionY -= 0.04D * (double)this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }



    @Override
    public int getFXLayer() {
        return 3;
    }
    private float flipIfBackfaced(float rot, float prevRot, float partialTicks, Vec3d toCamera) {
        float realRot = prevRot + (rot - prevRot) * partialTicks;

        Vec3d normal = new Vec3d(0, 1, 0);

        double dot = normal.dotProduct(toCamera);

        if (dot > 0) {
            realRot += Math.PI;
        }

        return realRot;
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks,
                               float rotationX, float rotationZ, float rotationYZ,
                               float rotationXY, float rotationXZ) {
        GlStateManager.color(1F, 1F, 1F, 1F);


        GlStateManager.enableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();

        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
        int brightness = this.getBrightnessForRender(partialTicks);
        int lx = brightness >> 16 & 65535;
        int ly = brightness & 65535;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lx, ly);


        Tessellator tess = Tessellator.getInstance();
        BufferBuilder vb = tess.getBuffer();
        vb.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

        double x = this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX;
        double y = this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY;
        double z = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ;

        float size = this.particleScale;

        Entity cam = Minecraft.getMinecraft().getRenderViewEntity();

        Vec3d toCamera = new Vec3d(
                cam.posX - this.posX,
                cam.posY - this.posY,
                cam.posZ - this.posZ
        ).normalize();

        float finalRot = flipIfBackfaced(this.particleAngle, this.prevParticleAngle, partialTicks, toCamera);

        float cos = MathHelper.cos(finalRot);
        float sin = MathHelper.sin(finalRot);

        // Quad en el plano horizontal (XZ)
        float x1 = -size, z1 = -size;
        float x2 =  size, z2 = -size;
        float x3 =  size, z3 =  size;
        float x4 = -size, z4 =  size;

        // Rotar quad horizontal
        float rx1 = x1 * cos - z1 * sin;
        float rz1 = x1 * sin + z1 * cos;

        float rx2 = x2 * cos - z2 * sin;
        float rz2 = x2 * sin + z2 * cos;

        float rx3 = x3 * cos - z3 * sin;
        float rz3 = x3 * sin + z3 * cos;

        float rx4 = x4 * cos - z4 * sin;
        float rz4 = x4 * sin + z4 * cos;
        vb.pos(x + rx1, y, z + rz1).tex(0, 1)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(lx, ly).endVertex();

        vb.pos(x + rx4, y, z + rz4).tex(0, 0)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(lx, ly).endVertex();

        vb.pos(x + rx3, y, z + rz3).tex(1, 0)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(lx, ly).endVertex();

        vb.pos(x + rx2, y, z + rz2).tex(1, 1)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(lx, ly).endVertex();

        tess.draw();

        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }
    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ToxicFogParticle(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
