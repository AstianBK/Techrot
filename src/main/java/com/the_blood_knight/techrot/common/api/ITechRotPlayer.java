package com.the_blood_knight.techrot.common.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.ItemStackHandler;

public interface ITechRotPlayer {
    ItemStackHandler getInventory();
    void tick(EntityPlayer player);
    void setDirty();
    int getHeartRot();
    int getRegTimer();
    void reg(EntityPlayer player);
    void setHeartRot(int value);
}
