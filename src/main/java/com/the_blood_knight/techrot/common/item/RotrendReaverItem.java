package com.the_blood_knight.techrot.common.item;

import com.google.common.collect.Multimap;
import com.the_blood_knight.techrot.Util;
import com.the_blood_knight.techrot.common.item.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class RotrendReaverItem extends ItemBase {

    public RotrendReaverItem(String name) {
        super(name);
        this.maxStackSize = 1;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isRemote) return;
        if (!(entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) entity;

        boolean holding =
                player.getHeldItemMainhand() == stack ||
                        player.getHeldItemOffhand() == stack;

        if (holding && !Util.hasTechrotArm(player)) {
            player.addPotionEffect(new PotionEffect(
                    MobEffects.SLOWNESS,
                    20,
                    1,  // WEAKNESS 2
                    true,
                    false
            ));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return TextFormatting.GREEN
                + super.getItemStackDisplayName(stack);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

        if (!(attacker instanceof EntityPlayer)) {
            return false;
        }

        EntityPlayer player = (EntityPlayer) attacker;


        if (!Util.hasTechrotArm(player)) {
            if (!player.world.isRemote) {
                player.sendMessage(new TextComponentString("I don't know how to use this"));
            }
            return false;
        }


        float maxHealth = player.getMaxHealth();
        float currentHealth = player.getHealth();


        float missingRatio = 1.0F - (currentHealth / maxHealth);

        float minDamage = 8.0F;
        float maxDamage = 30.0F;

        float damage = minDamage + (maxDamage - minDamage) * missingRatio;


        target.attackEntityFrom(
                DamageSource.causePlayerDamage(player),
                damage
        );


        target.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0));

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        tooltip.add(TextFormatting.ITALIC + "Rot-Weapon");
        tooltip.add(TextFormatting.GRAY + "Injects poison into the wound, damage ramps up as wielder's health decreases.");

        EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;
        if (player != null) {

            float maxHealth = player.getMaxHealth();
            float currentHealth = player.getHealth();
            float missingRatio = 1.0F - (currentHealth / maxHealth);

            float minDamage = 8.0F;
            float maxDamage = 30.0F;
            float damage = minDamage + (maxDamage - minDamage) * missingRatio;

            tooltip.add(""); // spacing
            tooltip.add(TextFormatting.GRAY + "Damage:");
            tooltip.add(TextFormatting.DARK_RED + "  " +
                    String.format("%.1f", damage) +
                    TextFormatting.GRAY);
            tooltip.add(TextFormatting.DARK_GRAY +
                    "  Min: " + minDamage + "  Max: " + maxDamage);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.MAINHAND) {

            modifiers.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());

            modifiers.put(
                    SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.4, 0)
            );
        }
        return modifiers;
    }


}
