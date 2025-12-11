package com.the_blood_knight.techrot.common.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public interface IBioCrafterRecipe extends IRecipe {
    ItemStack getResult();

    List<ItemStack> getCraftingItems();

    int getNeedNutrient();
}
