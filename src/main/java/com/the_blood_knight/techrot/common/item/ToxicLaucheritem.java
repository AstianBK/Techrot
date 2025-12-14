package com.the_blood_knight.techrot.common.item;

import com.the_blood_knight.techrot.Util;
import com.the_blood_knight.techrot.common.TRSounds;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.entity.ToxicBombEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ToxicLaucheritem extends ItemBase{
    public ToxicLaucheritem(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand hand) {
        ItemStack handItem = entityPlayer.getHeldItem(hand);
        if(handItem.getItem() == this) {
            if(Util.hasTechrotArm(entityPlayer)){
                ItemStack stack = getBullet(entityPlayer);
                if(!stack.isEmpty()){
                    world.playSound((EntityPlayer)null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ,
                            TRSounds.TOXICLAUNCHER_SHOOT, SoundCategory.NEUTRAL,
                            1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));

                    if (!entityPlayer.capabilities.isCreativeMode) {
                        stack.shrink(1);
                        entityPlayer.getCooldownTracker().setCooldown(handItem.getItem(),20);
                    }

                    if(!world.isRemote){
                        ToxicBombEntity bullet = new ToxicBombEntity(world,entityPlayer);
                        bullet.shoot(entityPlayer,entityPlayer.rotationPitch ,entityPlayer.rotationYaw,0.0F,1.5F,1.0F);
                        world.spawnEntity(bullet);
                    }

                    return ActionResult.newResult(EnumActionResult.SUCCESS,handItem);

                } else {

                    world.playSound(
                            (EntityPlayer)null,
                            entityPlayer.posX,
                            entityPlayer.posY,
                            entityPlayer.posZ,
                            SoundEvents.ITEM_FLINTANDSTEEL_USE,
                            SoundCategory.PLAYERS,
                            1.0F,
                            1.0F
                    );
                }

            }else {
                entityPlayer.sendMessage(new TextComponentString("I don't know how to use this"));
            }

        }
        return new ActionResult<>(EnumActionResult.PASS,handItem);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return TextFormatting.GREEN
                + super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        String key = this.getTranslationKey(stack) + ".tooltip";

        if (net.minecraft.client.resources.I18n.hasKey(key)) {
            tooltip.add(net.minecraft.util.text.TextFormatting.GRAY +
                    net.minecraft.client.resources.I18n.format(key));
        }
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
                    1,  // Slowness II
                    true,
                    false
            ));
        }
    }

    public static ItemStack getBullet(EntityPlayer entityPlayer){
        IInventory container = entityPlayer.inventory;
        for (int i = 0 ; i< container.getSizeInventory() ; i++){
            if(container.getStackInSlot(i).getItem() == TRegistry.TOXIC_CANISTER){
                return container.getStackInSlot(i);
            }
        }
        return ItemStack.EMPTY;
    }
    public static ItemStack getFreeBullet(EntityPlayer entityPlayer){
        IInventory container = entityPlayer.inventory;
        for (int i = 0 ; i< container.getSizeInventory() ; i++){
            if(container.getStackInSlot(i).getItem() == TRegistry.TOXIC_CANISTER){
                return container.getStackInSlot(i);
            }
        }
        return ItemStack.EMPTY;
    }
}
