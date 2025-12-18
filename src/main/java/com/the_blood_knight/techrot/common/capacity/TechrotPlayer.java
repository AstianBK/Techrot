package com.the_blood_knight.techrot.common.capacity;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.Util;
import com.the_blood_knight.techrot.common.TRSounds;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import com.the_blood_knight.techrot.common.entity.ToxicFogEntity;
import com.the_blood_knight.techrot.messager.PacketHandler;
import com.the_blood_knight.techrot.messager.SyncDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.ItemStackHandler;

public class TechrotPlayer implements ITechRotPlayer {
    private final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            dirty = true;
        }

    };
    public int combustibleAmount = 0;
    public int regTimer = 0;
    public int heartRot = 0;
    public boolean fly = false;

    public ToxicFogEntity flyingFog = null;
    public boolean dirty = false;
    public int firstSpace = 0;
    public boolean startFly = false;
    public int soundWings = 0;

    private WingLoopSound wingLoopSound = null;


    @Override
    public ToxicFogEntity getFog() {
        return this.flyingFog;
    }

    @Override
    public void clearFog() {
        this.flyingFog = null;
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public void tick(EntityPlayer player) {
        if (!player.world.isRemote) {
            if (this.dirty) {
                this.dirty = false;
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("Inv", this.getInventory().serializeNBT());
                tag.setInteger("rotHealth", this.getHeartRot());
                tag.setBoolean("fly", this.isFly());
                tag.setInteger("combustible", this.combustibleAmount);
                PacketHandler.sendTo(new SyncDataPacket(tag), (EntityPlayerMP) player);
            }
            if (this.regTimer > 0) this.regTimer--;


            if (this.fly ) {
                player.fallDistance = 0.0F;
                if (this.flyingFog == null || this.flyingFog.isDead) {
                    this.flyingFog = new ToxicFogEntity(player.world, player.posX, player.posY, player.posZ, player);
                    this.flyingFog.setRadius(3.0F);
                    this.flyingFog.setDuration(9999);
                    this.flyingFog.setRadiusPerTick(0.0F);
                    player.world.spawnEntity(this.flyingFog);
                } else {

                    this.flyingFog.setLocationAndAngles(player.posX, player.posY, player.posZ, 0, 0);


                    this.flyingFog.setEntityBoundingBox(this.flyingFog.getEntityBoundingBox().expand(0.5, 1.0, 0.5));


                    for (EntityLivingBase entity : player.world.getEntitiesWithinAABB(EntityLivingBase.class, this.flyingFog.getEntityBoundingBox())) {
                        if (entity == player) continue;
                        if (entity instanceof EntityPlayer && Util.hasTechrotHead((EntityPlayer) entity)) continue; // skip head-implanted players
                        if (!this.flyingFog.reapplicationDelayMap.containsKey(entity)) {
                            entity.addPotionEffect(new PotionEffect(TRegistry.TECHROT_EFFECT, 50, 0, false, true));
                            entity.attackEntityFrom(DamageSource.GENERIC, 1);
                            this.flyingFog.reapplicationDelayMap.put(entity, this.flyingFog.ticksExisted + this.flyingFog.reapplicationDelay);
                        }
                    }
                }
            }
        }else{
            if (Util.hasTechrotWings(player) && !player.capabilities.isCreativeMode) {
                boolean jumpPressed = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();

                if (jumpPressed && !this.fly) {
                    if (this.firstSpace - player.ticksExisted < 10) {
                        this.fly = true;
                        player.capabilities.isFlying = true;
                        player.capabilities.allowFlying = true;
                        player.capabilities.setFlySpeed(0.04F);
                        player.sendPlayerAbilities();

                        if (player.world.isRemote && (wingLoopSound == null || wingLoopSound.isDonePlaying())) {
                            wingLoopSound = new WingLoopSound(player, TRSounds.ROTPLATE_WINGS_LOOP);
                            Minecraft.getMinecraft().getSoundHandler().playSound(wingLoopSound);
                        }


                        this.setDirty();
                        PacketHandler.sendToServer(new SyncDataPacket(getData()));
                    }
                    this.firstSpace = player.ticksExisted;
                }

                if (this.fly) {
                    float rotY = (float) Math.toRadians(player.renderYawOffset);
                    double cos = MathHelper.cos(rotY);
                    double sin = MathHelper.sin(rotY);
                    double xOffset = sin * 0.43F;
                    double zOffset = -cos * 0.43F;

                    Vec3d delta = new Vec3d(player.motionX, 0, player.motionZ).normalize().scale(0.1F);
                    Particle particle = Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.FLAME.getParticleID(), player.posX + xOffset, player.posY + 0.65F, player.posZ + zOffset, -delta.x, -0.1F, -delta.z);

                    for (int i = 0; i < 4; i++) {
                        if(particle!=null){
                            particle.setRBGColorF(125.0f/255.0F,252.0f/255.0F,15.0f/255.0F);
                            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                        }
                        player.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, player.posX + xOffset, player.posY + 0.65F, player.posZ + zOffset, -delta.x, -0.1F, -delta.z);

                    }
                }

                if (!jumpPressed || player.onGround) {
                    if (this.fly) {
                        this.fly = false;
                        player.capabilities.isFlying = false;
                        player.capabilities.allowFlying = false;
                        player.capabilities.setFlySpeed(0.05F);
                        player.sendPlayerAbilities();

                        if (wingLoopSound != null) {
                            wingLoopSound.stop();
                            wingLoopSound = null;
                        }


                        PacketHandler.sendToServer(new SyncDataPacket(getData()));
                    }
                }
            }
        }
    }

    public NBTTagCompound getData(){
        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("Inv", this.getInventory().serializeNBT());
        tag.setInteger("rotHealth",this.getHeartRot());
        tag.setBoolean("fly",this.isFly());
        tag.setInteger("combustible",this.combustibleAmount);
        return tag;
    }
    @Override
    public void setDirty() {
        this.dirty = true;
    }

    @Override
    public int getHeartRot() {
        return this.heartRot;
    }

    @Override
    public int getCombustible() {
        return this.combustibleAmount;
    }

    @Override
    public int getRegTimer() {
        return this.regTimer;
    }

    @Override
    public boolean isFly() {
        return this.fly;
    }

    @Override
    public void reg(EntityPlayer player) {
        if (player.isDead || player.getHealth() <= 0.0F) return;

        if (player.getHealth() < this.heartRot) {
            if (this.regTimer <= 0) {
                player.world.playSound(
                        null,
                        player.posX, player.posY, player.posZ,
                        TRSounds.IMPLANTEDPLAYER_BREATHE,
                        SoundCategory.PLAYERS,
                        1.0F,
                        1.0F
                );

                this.regTimer = 80;
                player.setHealth(Math.min(player.getHealth() + 1, player.getMaxHealth()));
            }
        }
    }

    @Override
    public void setHeartRot(int value) {
        this.heartRot = value;
    }

    @Override
    public void setCombustible(int value) {
        this.combustibleAmount = value;
    }

    @Override
    public void setFly(boolean value) {
        this.fly = value;
    }

    public boolean canStartFly(){
        return this.combustibleAmount >10;
    }
    public static class TechrotPlayerStorage implements Capability.IStorage<ITechRotPlayer>{
        @Override
        public NBTBase writeNBT(Capability<ITechRotPlayer> capability, ITechRotPlayer instance, EnumFacing side) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("Inv", instance.getInventory().serializeNBT());
            tag.setInteger("rotHealth",instance.getHeartRot());
            tag.setBoolean("fly",instance.isFly());
            return tag;
        }

        @Override
        public void readNBT(Capability<ITechRotPlayer> capability, ITechRotPlayer instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tag = (NBTTagCompound) nbt;
            instance.getInventory().deserializeNBT(tag.getCompoundTag("Inv"));
            instance.setHeartRot(tag.getInteger("rotHealth"));
            instance.setFly(tag.getBoolean("fly"));
            instance.setDirty();
        }
    }

    public static class TechrotPlayerProvider implements ICapabilitySerializable<NBTTagCompound> {

        private ITechRotPlayer instance = new TechrotPlayer();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == Techrot.CapabilityRegistry.PLAYER_UPGRADES;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            if (capability == Techrot.CapabilityRegistry.PLAYER_UPGRADES)
                return Techrot.CapabilityRegistry.PLAYER_UPGRADES.cast(instance);
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {

            return (NBTTagCompound)
                    Techrot.CapabilityRegistry.PLAYER_UPGRADES.getStorage()
                            .writeNBT(Techrot.CapabilityRegistry.PLAYER_UPGRADES, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {

            Techrot.CapabilityRegistry.PLAYER_UPGRADES.getStorage()
                    .readNBT(Techrot.CapabilityRegistry.PLAYER_UPGRADES, instance, null, nbt);
        }
    }
}
