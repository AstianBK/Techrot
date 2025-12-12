package com.the_blood_knight.techrot;

import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class Util {
    public static boolean hasTechrotArm(AbstractClientPlayer player){
        ITechRotPlayer cap = player.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES,null);
        if(cap!=null){
            return findItem(cap.getInventory(), TRegistry.ROTPLATE_ARM);
        }
        return false;
    }
    public static boolean hasTechrotHead(AbstractClientPlayer player){
        ITechRotPlayer cap = player.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES,null);
        if(cap!=null){
            return findItem(cap.getInventory(), TRegistry.ROTPLATE_HEAD);
        }
        return false;
    }
    public static boolean hasTechrotChest(AbstractClientPlayer player){
        ITechRotPlayer cap = player.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES,null);
        if(cap!=null){
            return findItem(cap.getInventory(), TRegistry.ROTPLATE_CHEST);
        }
        return false;
    }
    public static boolean findItem(ItemStackHandler inventory, Item item){
        for (int i = 0 ; i < inventory.getSlots() ; i++){
            ItemStack stack = inventory.getStackInSlot(i);
            if(stack.getItem()==item){
                return true;
            }
        }
        return false;
    }
}
