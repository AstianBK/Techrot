package com.the_blood_knight.techrot.common.container;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BioImplanterContainer extends Container {
    private final IInventory tileFurnace;
    private int currentNutrition = 0;
    public BioImplanterContainer(InventoryPlayer playerInventory, IInventory furnaceInventory)
    {
        this.tileFurnace = furnaceInventory;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 52, -2){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == TRegistry.ROTPLATE_HEAD;
            }
        });
        this.addSlotToContainer(new Slot(furnaceInventory, 1, 108, 56){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == TRegistry.ROTPLATE_ARM;
            }
        });

        this.addSlotToContainer(new Slot(furnaceInventory, 2, 112, 18){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == TRegistry.ROTPLATE_CHEST;
            }
        });
        this.addSlotToContainer(new Slot(furnaceInventory, 3, 35, 27){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        this.addSlotToContainer(new Slot(furnaceInventory, 4, 42, 57){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });


        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 119 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 177));
        }
    }
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileFurnace);
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



            if (this.currentNutrition != this.tileFurnace.getField(0)) {
                icontainerlistener.sendWindowProperty(this, 0, this.tileFurnace.getField(0));
            }

        }
        this.currentNutrition = this.tileFurnace.getField(0);
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

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        if(!playerIn.world.isRemote){

            ITechRotPlayer cap = playerIn.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES,null);
            if(cap!=null){
                int rot = 0;
                for (int i = 0 ; i < 6 ; i++){
                    ItemStack stack = this.tileFurnace.getStackInSlot(i);
                    if(stack.getItem() == TRegistry.ROTPLATE_HEAD){
                        rot +=2;
                    }
                    if(stack.getItem() == TRegistry.ROTPLATE_ARM){
                        rot +=2;
                    }
                    if(stack.getItem() == TRegistry.ROTPLATE_CHEST){
                        rot +=6;
                    }
                    if(cap.getInventory().getStackInSlot(i).isEmpty()){
                        cap.getInventory().setStackInSlot(i,stack.copy());
                        stack.shrink(1);
                    }
                }
                cap.setHeartRot(rot);
            }
        }
    }
}
