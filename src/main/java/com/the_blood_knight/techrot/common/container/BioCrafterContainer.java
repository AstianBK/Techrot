package com.the_blood_knight.techrot.common.container;

import com.google.common.collect.Lists;
import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.api.IBioCrafterRecipe;
import com.the_blood_knight.techrot.common.item.BioExtractorItem;
import com.the_blood_knight.techrot.common.recipes.BioCrafterRecipe;
import com.the_blood_knight.techrot.common.slot.BioCrafterSlot;
import com.the_blood_knight.techrot.common.tile_block.BioCrafterTileBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BioCrafterContainer extends Container {
    private final IInventory tileFurnace;
    private int currentNutrition = 0;
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public InventoryCraftResult craftResult = new InventoryCraftResult();

    public World world;
    public EntityPlayer player;
    //private int clonerTime = 0;
    public BioCrafterContainer(InventoryPlayer playerInventory, IInventory furnaceInventory)
    {
        this.tileFurnace = furnaceInventory;
        this.world = playerInventory.player.world;
        this.player = playerInventory.player;
        this.addSlotToContainer(new BioCrafterSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35){
            @Override
            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {

                return super.onTake(thePlayer, stack);
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileFurnace);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        super.onCraftMatrixChanged(inventoryIn);
        this.slotChangedCraftingGrid(world,this.player,this.craftMatrix,this.craftResult);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.tileFurnace.setField(id,data);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);


            /*if (this.clonerTime != this.tileFurnace.getField(1)) {
                icontainerlistener.sendWindowProperty(this, 1, this.tileFurnace.getField(1));
            }*/


            if (this.currentNutrition != this.tileFurnace.getField(0)) {
                icontainerlistener.sendWindowProperty(this, 0, this.tileFurnace.getField(0));
            }

        }
        this.currentNutrition = this.tileFurnace.getField(0);
        //this.clonerTime = this.tileFurnace.getField(1);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (!FurnaceRecipes.instance().getSmeltingResult(itemstack1).isEmpty())
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (TileEntityFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    protected void slotChangedCraftingGrid(World p_192389_1_, EntityPlayer p_192389_2_, InventoryCrafting p_192389_3_, InventoryCraftResult p_192389_4_)
    {
    }
}
