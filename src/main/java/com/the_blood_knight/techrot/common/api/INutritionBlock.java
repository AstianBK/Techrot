package com.the_blood_knight.techrot.common.api;

import net.minecraft.util.math.BlockPos;

public interface INutritionBlock {
    int getNutrition();
    void setNutrition(int value);
    void extractNutrition();
    boolean canExtract(BlockPos pos);
}
