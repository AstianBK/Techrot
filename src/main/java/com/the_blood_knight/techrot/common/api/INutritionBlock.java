package com.the_blood_knight.techrot.common.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface INutritionBlock {
    int getNutrition();
    void setNutrition(int value);
    int extractNutrition(EnumFacing facing);
    boolean canExtract(BlockPos pos,EnumFacing facing);
    boolean canInsert(BlockPos pos,EnumFacing facing);
}
