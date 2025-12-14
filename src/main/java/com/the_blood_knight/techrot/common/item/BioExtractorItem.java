package com.the_blood_knight.techrot.common.item;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRSounds;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import com.the_blood_knight.techrot.common.entity.ToxicBombEntity;
import com.the_blood_knight.techrot.common.entity.ToxicFogEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.datafix.walkers.EntityTag;
import net.minecraft.util.text.TextFormatting;
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

        this.addPropertyOverride(
                new ResourceLocation("minecraft", "fill"),
                (stack, world, entity) ->
                        getADN(stack).equals("none") ? 0.0F : 1.0F
        );

    }



    @Override
    public int getDamage(ItemStack stack) {
        return getADN(stack).equals("none") ? 1 : super.getDamage(stack);
    }


    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        ItemStack extract = playerIn.getHeldItem(hand);


        if (getADN(extract).equals("none") && !(target instanceof EntityPlayer)) {

            playerIn.world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                    TRSounds.BIO_EXTRACTOR_USE, SoundCategory.NEUTRAL,
                    1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));


            addADN(extract, EntityList.getKey(target).toString());


            target.attackEntityFrom(DamageSource.causePlayerDamage(playerIn), 1.0F);
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }



    public static void addADN(ItemStack stack,String adn){
        stack.getOrCreateSubCompound("store").setString("adn",adn);
    }
    public static String getADN(ItemStack stack) {
        if (!stack.hasTagCompound()) return "none";

        NBTTagCompound store = stack.getTagCompound().getCompoundTag("store");
        if (!store.hasKey("adn")) return "none";

        String adn = store.getString("adn");
        return adn.isEmpty() ? "none" : adn;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (player.isSneaking()) {
            if (!getADN(stack).equals("none")) {

                if (!world.isRemote) {
                    stack.getOrCreateSubCompound("store").removeTag("adn");

                    world.playSound(
                            null,
                            player.posX,
                            player.posY,
                            player.posZ,
                            SoundEvents.ITEM_FLINTANDSTEEL_USE,
                            SoundCategory.PLAYERS,
                            0.8F,
                            1.2F
                    );
                }

                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        }

        return super.onItemRightClick(world, player, hand);
    }



    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String adn = getADN(stack);
        if(adn.equals("none") || !adn.contains(":"))return;
        String creatureName = adn.split(":")[1];
        tooltip.add(TextFormatting.GREEN + "Genome: " + creatureName);

    }
}
