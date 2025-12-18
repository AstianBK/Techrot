package com.the_blood_knight.techrot.common.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.ItemStackHandler;

public interface ITechRotPlayer {
    ItemStackHandler getInventory();
    void tick(EntityPlayer player);
    void setDirty();
    int getHeartRot();
    int getCombustible();
    int getRegTimer();
    boolean isFly();
    void reg(EntityPlayer player);
    void setHeartRot(int value);
    void setCombustible(int value);
    void setFly(boolean value);
}
