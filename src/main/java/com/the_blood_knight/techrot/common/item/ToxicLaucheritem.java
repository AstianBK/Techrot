package com.the_blood_knight.techrot.common.item;

import com.the_blood_knight.techrot.Util;
import com.the_blood_knight.techrot.common.TRSounds;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.entity.ToxicBombEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

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
                    world.playSound((EntityPlayer)null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, TRSounds.TOXICLAUNCHER_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
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
                }
            }else {
                entityPlayer.sendMessage(new TextComponentString("Hello baby !!!"));
            }

        }

        return new ActionResult<>(EnumActionResult.PASS,handItem);
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
