package com.the_blood_knight.techrot.common.recipes;

import com.google.common.collect.Lists;
import com.the_blood_knight.techrot.common.api.IBioCrafterRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BioCrafterRecipe implements IBioCrafterRecipe {
    private final ItemStack result;
    private final NonNullList<Ingredient> craftingItems;
    private final int needNutrient;
    private final boolean isSimple;

    public BioCrafterRecipe(NonNullList<Ingredient> items,ItemStack result,int nutrient){
        this.result = result;
        this.craftingItems = items;
        this.needNutrient = nutrient;
        boolean simple = true;
        for (Ingredient i : getIngredients())
            simple &= i.isSimple();
        this.isSimple = simple;
    }
    @Override
    public ItemStack getResult() {
        return this.result;
    }

    @Override
    public List<ItemStack> getCraftingItems() {
        return null;
    }

    @Override
    public int getNeedNutrient() {
        return this.needNutrient;
    }
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        for (int i = 0; i <= inv.getWidth() - 3; ++i)
        {
            for (int j = 0; j <= inv.getHeight() - 3; ++j)
            {
                if (this.checkMatch(inv, i, j, true))
                {
                    return true;
                }

                if (this.checkMatch(inv, i, j, false))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkMatch(InventoryCrafting craftingInventory, int p_77573_2_, int p_77573_3_, boolean p_77573_4_)
    {
        for (int i = 0; i < craftingInventory.getWidth(); ++i)
        {
            for (int j = 0; j < craftingInventory.getHeight(); ++j)
            {
                int k = i - p_77573_2_;
                int l = j - p_77573_3_;
                Ingredient ingredient = Ingredient.EMPTY;

                if (k >= 0 && l >= 0 && k < 3 && l < 3)
                {
                    if (p_77573_4_)
                    {
                        ingredient = this.craftingItems.get(3 - k - 1 + l * 3);
                    }
                    else
                    {
                        ingredient = this.craftingItems.get(k + l * 3);
                    }
                }

                if (!ingredient.apply(craftingInventory.getStackInRowAndColumn(i, j)))
                {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.craftingItems;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return null;
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return null;
    }


    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);

            nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }

        return nonnulllist;
    }
}
