package com.the_blood_knight.techrot.common.container;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.client.gui.BlackoutOverlay;
import com.the_blood_knight.techrot.common.TRSounds;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
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
        this.addSlotToContainer(new Slot(furnaceInventory, 1,35 ,27 ){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == TRegistry.ROTPLATE_ARM;
            }
        });

        this.addSlotToContainer(new Slot(furnaceInventory, 2,112 ,18 ){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == TRegistry.ROTPLATE_CHEST
                        || stack.getItem() == TRegistry.ROTPLATE_WINGS;

            }
        });
        this.addSlotToContainer(new Slot(furnaceInventory, 3, 108,56 ){
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

    private int findFreeChestSlot(ITechRotPlayer cap) {
        for (int i = 2; i < cap.getInventory().getSlots(); i++) {
            if (cap.getInventory().getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }


    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index != 1 && index != 0 && index != 2)
            {
                if (itemstack1.getItem() == TRegistry.ROTPLATE_HEAD)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem() == TRegistry.ROTPLATE_ARM)
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }else if (itemstack1.getItem() == TRegistry.ROTPLATE_CHEST
                        || itemstack1.getItem() == TRegistry.ROTPLATE_WINGS)
                {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }

                else if (index >= 4 && index < 32)
                {
                    if (!this.mergeItemStack(itemstack1, 32, 41, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 32 && index < 41 && !this.mergeItemStack(itemstack1, 3, 32, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 41, false))
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
        super.onContainerClosed(playerIn);

        ITechRotPlayer cap = playerIn.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES, null);
        if (cap != null) {
            int rot = 0;
            int currentRot = cap.getHeartRot();

            for (int i = 0; i < 6; i++) {
                ItemStack stack = this.tileFurnace.getStackInSlot(i);
                if (stack.isEmpty()) continue;

                int targetSlot = -1;


                if (stack.getItem() == TRegistry.ROTPLATE_HEAD) {
                    targetSlot = 0;
                    rot += 2;
                }


                else if (stack.getItem() == TRegistry.ROTPLATE_ARM) {
                    targetSlot = 1;
                    rot += 2;
                }

                // CHEST IMPLANTS
                else if (stack.getItem() == TRegistry.ROTPLATE_CHEST) {
                    targetSlot = findFreeChestSlot(cap);
                    rot += 6;
                }
                else if (stack.getItem() == TRegistry.ROTPLATE_WINGS) {
                    targetSlot = findFreeChestSlot(cap);
                    rot += 8;
                }

                if (targetSlot != -1 && cap.getInventory().getStackInSlot(targetSlot).isEmpty()) {
                    cap.getInventory().setStackInSlot(targetSlot, stack.copy());
                    stack.shrink(1);
                }
            }


            if (rot > 0) {
                cap.setHeartRot(rot + currentRot);
                cap.setDirty();

                playerIn.attackEntityFrom(DamageSource.OUT_OF_WORLD, 4.0F);

                playerIn.world.playSound(
                        null,
                        playerIn.posX,
                        playerIn.posY,
                        playerIn.posZ,
                        TRSounds.BIOIMPLANTER_USE,
                        SoundCategory.BLOCKS,
                        1.0F,
                        1.0F
                );

                if (playerIn.world.isRemote) {
                    BlackoutOverlay overlay = new BlackoutOverlay();
                    overlay.start();
                }
            }

        }
    }


}


