package com.the_blood_knight.techrot.common.capacity;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import com.the_blood_knight.techrot.messager.PacketHandler;
import com.the_blood_knight.techrot.messager.SyncDataPacket;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;

public class TechrotPlayer implements ITechRotPlayer {
    private final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            dirty = true;
        }
    };


    public boolean dirty = false;

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public void tick(EntityPlayer player) {
        if(!player.world.isRemote){
            if(this.dirty){
                this.dirty=false;
                PacketHandler.sendTo(new SyncDataPacket(this.getInventory().serializeNBT()), (EntityPlayerMP) player);
            }
        }

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if ((stack.getItem() == TRegistry.BIO_EXTRACTOR)) {

                player.getEntityAttribute(SharedMonsterAttributes.ARMOR)
                        .setBaseValue(2.0);
            }
        }
    }
    @Override
    public void setDirty() {
        this.dirty = true;
    }

    public static class TechrotPlayerProvider implements Capability.IStorage<ITechRotPlayer>{
        @Override
        public NBTBase writeNBT(Capability<ITechRotPlayer> capability, ITechRotPlayer instance, EnumFacing side) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("Inv", instance.getInventory().serializeNBT());
            Techrot.logger.info("save :"+tag);
            return tag;
        }

        @Override
        public void readNBT(Capability<ITechRotPlayer> capability, ITechRotPlayer instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tag = (NBTTagCompound) nbt;
            instance.getInventory().deserializeNBT(tag.getCompoundTag("Inv"));
            instance.setDirty();
            Techrot.logger.info("read :"+tag);
        }
    }
}
