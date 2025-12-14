package com.the_blood_knight.techrot.common.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.Util;
import com.the_blood_knight.techrot.client.particles.ToxicFogParticle;
import com.the_blood_knight.techrot.common.TRegistry;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.PotionTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ToxicFogEntity extends EntityAreaEffectCloud {
    private static final DataParameter<Float> RADIUS;
    private static final DataParameter<Integer> COLOR;
    private static final DataParameter<Boolean> IGNORE_RADIUS;
    private static final DataParameter<Integer> PARTICLE;
    private static final DataParameter<Integer> PARTICLE_PARAM_1;
    private static final DataParameter<Integer> PARTICLE_PARAM_2;
    private PotionType potion;
    private final List<PotionEffect> effects;
    private final Map<Entity, Integer> reapplicationDelayMap;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private boolean colorSet;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private EntityLivingBase owner;
    private UUID ownerUniqueId;

    public ToxicFogEntity(World p_i46809_1_) {
        super(p_i46809_1_);
        this.potion = PotionTypes.EMPTY;
        this.effects = Lists.newArrayList(new PotionEffect(TRegistry.TECHROT_EFFECT,50,0));
        this.reapplicationDelayMap = Maps.newHashMap();
        this.duration = 300;
        this.waitTime = 5;
        this.reapplicationDelay = 10;
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setRadius(5.0F);

    }

    public ToxicFogEntity(World p_i46810_1_, double posX, double posY, double posZ, Entity owner) {
        this(p_i46810_1_);
        this.setPosition(posX, posY, posZ);
    }

    protected void entityInit() {
        this.getDataManager().register(COLOR, 0);
        this.getDataManager().register(RADIUS, 0.5F);
        this.getDataManager().register(IGNORE_RADIUS, false);
        this.getDataManager().register(PARTICLE, EnumParticleTypes.SPELL_MOB.getParticleID());
        this.getDataManager().register(PARTICLE_PARAM_1, 0);
        this.getDataManager().register(PARTICLE_PARAM_2, 0);
    }

    public void setRadius(float p_setRadius_1_) {
        double lvt_2_1_ = this.posX;
        double lvt_4_1_ = this.posY;
        double lvt_6_1_ = this.posZ;
        this.setSize(p_setRadius_1_ * 2.0F, 0.5F);
        this.setPosition(lvt_2_1_, lvt_4_1_, lvt_6_1_);
        if (!this.world.isRemote) {
            this.getDataManager().set(RADIUS, p_setRadius_1_);
        }

    }

    public float getRadius() {
        return (Float)this.getDataManager().get(RADIUS);
    }

    public void setPotion(PotionType p_setPotion_1_) {
        this.potion = p_setPotion_1_;
        if (!this.colorSet) {
            this.updateFixedColor();
        }

    }

    private void updateFixedColor() {
        if (this.potion == PotionTypes.EMPTY && this.effects.isEmpty()) {
            this.getDataManager().set(COLOR, 0);
        } else {
            this.getDataManager().set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.effects)));
        }

    }

    public void addEffect(PotionEffect p_addEffect_1_) {
        this.effects.add(p_addEffect_1_);
        if (!this.colorSet) {
            this.updateFixedColor();
        }

    }

    public int getColor() {
        return (Integer)this.getDataManager().get(COLOR);
    }

    public void setColor(int p_setColor_1_) {
        this.colorSet = true;
        this.getDataManager().set(COLOR, p_setColor_1_);
    }

    public EnumParticleTypes getParticle() {
        return EnumParticleTypes.getParticleFromId((Integer)this.getDataManager().get(PARTICLE));
    }

    public void setParticle(EnumParticleTypes p_setParticle_1_) {
        this.getDataManager().set(PARTICLE, p_setParticle_1_.getParticleID());
    }

    public int getParticleParam1() {
        return (Integer)this.getDataManager().get(PARTICLE_PARAM_1);
    }

    public void setParticleParam1(int p_setParticleParam1_1_) {
        this.getDataManager().set(PARTICLE_PARAM_1, p_setParticleParam1_1_);
    }

    public int getParticleParam2() {
        return (Integer)this.getDataManager().get(PARTICLE_PARAM_2);
    }

    public void setParticleParam2(int p_setParticleParam2_1_) {
        this.getDataManager().set(PARTICLE_PARAM_2, p_setParticleParam2_1_);
    }

    protected void setIgnoreRadius(boolean p_setIgnoreRadius_1_) {
        this.getDataManager().set(IGNORE_RADIUS, p_setIgnoreRadius_1_);
    }

    public boolean shouldIgnoreRadius() {
        return (Boolean)this.getDataManager().get(IGNORE_RADIUS);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int p_setDuration_1_) {
        this.duration = p_setDuration_1_;
    }

    public void onUpdate() {
        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }

        this.onEntityUpdate();
        boolean lvt_1_1_ = this.shouldIgnoreRadius();
        float lvt_2_1_ = this.getRadius();
        if (this.world.isRemote) {


            if (this.ticksExisted % 3 != 0) return;

            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;


            if (player == null) return;


            double maxDistSq = 64 * 64;
            if (player.getDistanceSq(this) > maxDistSq) return;

            float radius = this.getRadius();


            int particleCount = Math.max(4, (int)(radius * 3));


            if (mc.gameSettings.particleSetting > 0) {
                particleCount /= 2;
            }

            for (int i = 0; i < particleCount; i++) {
                double angle = rand.nextDouble() * Math.PI * 2;
                double dist = rand.nextDouble() * radius;

                double x = this.posX + Math.cos(angle) * dist;
                double z = this.posZ + Math.sin(angle) * dist;
                double y = this.posY + rand.nextDouble() * 0.5D;

                mc.effectRenderer.addEffect(
                        new ToxicFogParticle(
                                world,
                                x,
                                y,
                                z,
                                0, 0, 0
                        )
                );
            }
        }
        else {
            if (this.ticksExisted >= this.waitTime + this.duration) {
                this.setDead();
                return;
            }

            boolean lvt_3_2_ = this.ticksExisted < this.waitTime;
            if (lvt_1_1_ != lvt_3_2_) {
                this.setIgnoreRadius(lvt_3_2_);
            }

            if (lvt_3_2_) {
                return;
            }

            if (this.radiusPerTick != 0.0F) {
                lvt_2_1_ += this.radiusPerTick;
                if (lvt_2_1_ < 0.5F) {
                    this.setDead();
                    return;
                }

                this.setRadius(lvt_2_1_);
            }

            if (this.ticksExisted % 5 == 0) {
                Iterator<Map.Entry<Entity, Integer>> lvt_4_2_ = this.reapplicationDelayMap.entrySet().iterator();

                while(lvt_4_2_.hasNext()) {
                    Map.Entry<Entity, Integer> lvt_5_3_ = (Map.Entry)lvt_4_2_.next();
                    if (this.ticksExisted >= (Integer)lvt_5_3_.getValue()) {
                        lvt_4_2_.remove();
                    }
                }

                List<PotionEffect> lvt_4_3_ = Lists.newArrayList();
                Iterator var22 = this.potion.getEffects().iterator();

                while(var22.hasNext()) {
                    PotionEffect lvt_6_3_ = (PotionEffect)var22.next();
                    lvt_4_3_.add(new PotionEffect(lvt_6_3_.getPotion(), lvt_6_3_.getDuration() / 4, lvt_6_3_.getAmplifier(), lvt_6_3_.getIsAmbient(), lvt_6_3_.doesShowParticles()));
                }

                lvt_4_3_.addAll(this.effects);
                if (lvt_4_3_.isEmpty()) {
                    this.reapplicationDelayMap.clear();
                } else {
                    List<EntityLivingBase> lvt_5_4_ = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox());
                    if (!lvt_5_4_.isEmpty()) {
                        Iterator var25 = lvt_5_4_.iterator();

                        while(true) {
                            EntityLivingBase lvt_7_3_;
                            double lvt_12_3_;
                            do {
                                do {
                                    do {
                                        if (!var25.hasNext()) {
                                            return;
                                        }

                                        lvt_7_3_ = (EntityLivingBase)var25.next();
                                    } while(this.reapplicationDelayMap.containsKey(lvt_7_3_));
                                } while(!lvt_7_3_.canBeHitWithPotion());

                                double lvt_8_3_ = lvt_7_3_.posX - this.posX;
                                double lvt_10_3_ = lvt_7_3_.posZ - this.posZ;
                                lvt_12_3_ = lvt_8_3_ * lvt_8_3_ + lvt_10_3_ * lvt_10_3_;
                            } while(!(lvt_12_3_ <= (double)(lvt_2_1_ * lvt_2_1_)));

                            this.reapplicationDelayMap.put(lvt_7_3_, this.ticksExisted + this.reapplicationDelay);
                            Iterator var31 = lvt_4_3_.iterator();

                            while(var31.hasNext()) {
                                PotionEffect lvt_15_1_ = (PotionEffect)var31.next();
                                if(lvt_7_3_ instanceof EntityPlayer && Util.hasTechrotChest(((EntityPlayer) lvt_7_3_))){
                                    Util.getCap(((EntityPlayer) lvt_7_3_)).reg(((EntityPlayer) lvt_7_3_));
                                }
                                if(lvt_7_3_ instanceof EntityPlayer && Util.hasTechrotHead(((EntityPlayer)lvt_7_3_)))continue;
                                if (lvt_15_1_.getPotion().isInstant()) {
                                    lvt_15_1_.getPotion().affectEntity(this, this.getOwner(), lvt_7_3_, lvt_15_1_.getAmplifier(), 0.5);
                                } else {
                                    lvt_7_3_.addPotionEffect(new PotionEffect(lvt_15_1_));
                                }
                                lvt_7_3_.attackEntityFrom(DamageSource.GENERIC,1);
                            }

                            if (this.radiusOnUse != 0.0F) {
                                lvt_2_1_ += this.radiusOnUse;
                                if (lvt_2_1_ < 0.5F) {
                                    this.setDead();
                                    return;
                                }

                                this.setRadius(lvt_2_1_);
                            }

                            if (this.durationOnUse != 0) {
                                this.duration += this.durationOnUse;
                                if (this.duration <= 0) {
                                    this.setDead();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public void setRadiusOnUse(float p_setRadiusOnUse_1_) {
        this.radiusOnUse = p_setRadiusOnUse_1_;
    }

    public void setRadiusPerTick(float p_setRadiusPerTick_1_) {
        this.radiusPerTick = p_setRadiusPerTick_1_;
    }

    public void setWaitTime(int p_setWaitTime_1_) {
        this.waitTime = p_setWaitTime_1_;
    }

    public void setOwner(@Nullable EntityLivingBase p_setOwner_1_) {
        this.owner = p_setOwner_1_;
        this.ownerUniqueId = p_setOwner_1_ == null ? null : p_setOwner_1_.getUniqueID();
    }

    @Nullable
    public EntityLivingBase getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof WorldServer) {
            Entity lvt_1_1_ = ((WorldServer)this.world).getEntityFromUuid(this.ownerUniqueId);
            if (lvt_1_1_ instanceof EntityLivingBase) {
                this.owner = (EntityLivingBase)lvt_1_1_;
            }
        }

        return this.owner;
    }

    protected void readEntityFromNBT(NBTTagCompound p_readEntityFromNBT_1_) {
        this.ticksExisted = p_readEntityFromNBT_1_.getInteger("Age");
        this.duration = p_readEntityFromNBT_1_.getInteger("Duration");
        this.waitTime = p_readEntityFromNBT_1_.getInteger("WaitTime");
        this.reapplicationDelay = p_readEntityFromNBT_1_.getInteger("ReapplicationDelay");
        this.durationOnUse = p_readEntityFromNBT_1_.getInteger("DurationOnUse");
        this.radiusOnUse = p_readEntityFromNBT_1_.getFloat("RadiusOnUse");
        this.radiusPerTick = p_readEntityFromNBT_1_.getFloat("RadiusPerTick");
        this.setRadius(p_readEntityFromNBT_1_.getFloat("Radius"));
        this.ownerUniqueId = p_readEntityFromNBT_1_.getUniqueId("OwnerUUID");
        if (p_readEntityFromNBT_1_.hasKey("Particle", 8)) {
            EnumParticleTypes lvt_2_1_ = EnumParticleTypes.getByName(p_readEntityFromNBT_1_.getString("Particle"));
            if (lvt_2_1_ != null) {
                this.setParticle(lvt_2_1_);
                this.setParticleParam1(p_readEntityFromNBT_1_.getInteger("ParticleParam1"));
                this.setParticleParam2(p_readEntityFromNBT_1_.getInteger("ParticleParam2"));
            }
        }

        if (p_readEntityFromNBT_1_.hasKey("Color", 99)) {
            this.setColor(p_readEntityFromNBT_1_.getInteger("Color"));
        }

        if (p_readEntityFromNBT_1_.hasKey("Potion", 8)) {
            this.setPotion(PotionUtils.getPotionTypeFromNBT(p_readEntityFromNBT_1_));
        }

        if (p_readEntityFromNBT_1_.hasKey("Effects", 9)) {
            NBTTagList lvt_2_2_ = p_readEntityFromNBT_1_.getTagList("Effects", 10);
            this.effects.clear();

            for(int lvt_3_1_ = 0; lvt_3_1_ < lvt_2_2_.tagCount(); ++lvt_3_1_) {
                PotionEffect lvt_4_1_ = PotionEffect.readCustomPotionEffectFromNBT(lvt_2_2_.getCompoundTagAt(lvt_3_1_));
                if (lvt_4_1_ != null) {
                    this.addEffect(lvt_4_1_);
                }
            }
        }

    }

    protected void writeEntityToNBT(NBTTagCompound p_writeEntityToNBT_1_) {
        p_writeEntityToNBT_1_.setInteger("Age", this.ticksExisted);
        p_writeEntityToNBT_1_.setInteger("Duration", this.duration);
        p_writeEntityToNBT_1_.setInteger("WaitTime", this.waitTime);
        p_writeEntityToNBT_1_.setInteger("ReapplicationDelay", this.reapplicationDelay);
        p_writeEntityToNBT_1_.setInteger("DurationOnUse", this.durationOnUse);
        p_writeEntityToNBT_1_.setFloat("RadiusOnUse", this.radiusOnUse);
        p_writeEntityToNBT_1_.setFloat("RadiusPerTick", this.radiusPerTick);
        p_writeEntityToNBT_1_.setFloat("Radius", this.getRadius());
        p_writeEntityToNBT_1_.setString("Particle", this.getParticle().getParticleName());
        p_writeEntityToNBT_1_.setInteger("ParticleParam1", this.getParticleParam1());
        p_writeEntityToNBT_1_.setInteger("ParticleParam2", this.getParticleParam2());
        if (this.ownerUniqueId != null) {
            p_writeEntityToNBT_1_.setUniqueId("OwnerUUID", this.ownerUniqueId);
        }

        if (this.colorSet) {
            p_writeEntityToNBT_1_.setInteger("Color", this.getColor());
        }

        if (this.potion != PotionTypes.EMPTY && this.potion != null) {
            p_writeEntityToNBT_1_.setString("Potion", ((ResourceLocation)PotionType.REGISTRY.getNameForObject(this.potion)).toString());
        }

        if (!this.effects.isEmpty()) {
            NBTTagList lvt_2_1_ = new NBTTagList();
            Iterator var3 = this.effects.iterator();

            while(var3.hasNext()) {
                PotionEffect lvt_4_1_ = (PotionEffect)var3.next();
                lvt_2_1_.appendTag(lvt_4_1_.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            p_writeEntityToNBT_1_.setTag("Effects", lvt_2_1_);
        }

    }

    public void notifyDataManagerChange(DataParameter<?> p_notifyDataManagerChange_1_) {
        if (RADIUS.equals(p_notifyDataManagerChange_1_)) {
            this.setRadius(this.getRadius());
        }

        super.notifyDataManagerChange(p_notifyDataManagerChange_1_);
    }

    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }

    static {
        RADIUS = EntityDataManager.createKey(ToxicFogEntity.class, DataSerializers.FLOAT);
        COLOR = EntityDataManager.createKey(ToxicFogEntity.class, DataSerializers.VARINT);
        IGNORE_RADIUS = EntityDataManager.createKey(ToxicFogEntity.class, DataSerializers.BOOLEAN);
        PARTICLE = EntityDataManager.createKey(ToxicFogEntity.class, DataSerializers.VARINT);
        PARTICLE_PARAM_1 = EntityDataManager.createKey(ToxicFogEntity.class, DataSerializers.VARINT);
        PARTICLE_PARAM_2 = EntityDataManager.createKey(ToxicFogEntity.class, DataSerializers.VARINT);
    }
}
