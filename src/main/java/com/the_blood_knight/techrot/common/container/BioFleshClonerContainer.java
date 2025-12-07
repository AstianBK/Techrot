package com.the_blood_knight.techrot.common.container;

import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.item.BioExtractorItem;
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

public class BioFleshClonerContainer extends Container {
    private final IInventory tileFurnace;
    private int maxNutrition = 1000;
    private int currentNutrition = 0;
    private int clonerTime = 0;
    public BioFleshClonerContainer(InventoryPlayer playerInventory, IInventory furnaceInventory)
    {
        this.tileFurnace = furnaceInventory;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 39, 24){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() instanceof BioExtractorItem;
            }
        });
        this.addSlotToContainer(new Slot(furnaceInventory, 1, 74, 24){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == TRegistry.BIO_CUBE;
            }
        });

        this.addSlotToContainer(new Slot(furnaceInventory, 2, 126, 24));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 162));
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

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);


            if (this.clonerTime != this.tileFurnace.getField(2)) {
                icontainerlistener.sendWindowProperty(this, 2, this.tileFurnace.getField(2));
            }

            if (this.maxNutrition != this.tileFurnace.getField(1)) {
                icontainerlistener.sendWindowProperty(this, 1, this.tileFurnace.getField(1));
            }

            if (this.currentNutrition != this.tileFurnace.getField(0)) {
                icontainerlistener.sendWindowProperty(this, 0, this.tileFurnace.getField(0));
            }

        }

        this.maxNutrition = this.tileFurnace.getField(1);
        this.currentNutrition = this.tileFurnace.getField(0);
        this.clonerTime = this.tileFurnace.getField(2);
        //this.totalEatTime = this.tileFurnace.getField(1);
    }

}
