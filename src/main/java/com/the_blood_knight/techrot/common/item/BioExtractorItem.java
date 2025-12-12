package com.the_blood_knight.techrot.common.item;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.walkers.EntityTag;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class BioExtractorItem extends ItemBase{
    public BioExtractorItem(String name) {
        super(name);
        this.setMaxDamage(15);
        this.maxStackSize = 1;
    }


    @Override
    public int getDamage(ItemStack stack) {
        return getADN(stack).equals("none") ? 1 : super.getDamage(stack);
    }

    public static void clearADN(ItemStack stack){
        stack.getOrCreateSubCompound("store").setString("adn","none");
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        ItemStack extract = playerIn.getHeldItem(hand);
        if(extract.getTagCompound()==null && !(target instanceof EntityPlayer)){
            addADN(extract,EntityList.getKey(target).toString());
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    public static void addADN(ItemStack stack,String adn){
        stack.getOrCreateSubCompound("store").setString("adn",adn);
    }
    public static String getADN(ItemStack stack){
        return stack.getTagCompound()==null ? "none" : stack.getOrCreateSubCompound("store").getString("adn");
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String adn = getADN(stack);
        if(adn.equals("none") || !adn.contains(":"))return;
        tooltip.add(adn.split(":")[1]);
    }
}
