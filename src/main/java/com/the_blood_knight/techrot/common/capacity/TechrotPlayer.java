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
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
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
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("Inv", this.getInventory().serializeNBT());
                PacketHandler.sendTo(new SyncDataPacket(tag), (EntityPlayerMP) player);
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

    public static class TechrotPlayerStorage implements Capability.IStorage<ITechRotPlayer>{
        @Override
        public NBTBase writeNBT(Capability<ITechRotPlayer> capability, ITechRotPlayer instance, EnumFacing side) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("Inv", instance.getInventory().serializeNBT());
            return tag;
        }

        @Override
        public void readNBT(Capability<ITechRotPlayer> capability, ITechRotPlayer instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tag = (NBTTagCompound) nbt;
            instance.getInventory().deserializeNBT(tag.getCompoundTag("Inv"));
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
            // Forge -> llama writeNBT()
            return (NBTTagCompound)
                    Techrot.CapabilityRegistry.PLAYER_UPGRADES.getStorage()
                            .writeNBT(Techrot.CapabilityRegistry.PLAYER_UPGRADES, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            // Forge -> llama readNBT()
            Techrot.CapabilityRegistry.PLAYER_UPGRADES.getStorage()
                    .readNBT(Techrot.CapabilityRegistry.PLAYER_UPGRADES, instance, null, nbt);
        }
    }
}
