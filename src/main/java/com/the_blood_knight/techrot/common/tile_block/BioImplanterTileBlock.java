package com.the_blood_knight.techrot.common.tile_block;

import com.google.common.collect.Lists;
import com.the_blood_knight.techrot.common.block.BioFurnaceBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.List;

public class BioImplanterTileBlock extends TileEntity implements ITickable, ISidedInventory {
    public int currentNutrient = 0;
    public NonNullList<ItemStack> container = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return this.container.size();
    }

    @Override
    public boolean isEmpty() {
        return this.container.isEmpty();
    }

    public ItemStack getStackInSlot(int index)
    {
        return this.container.get(index);
    }

    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.container, index, count);
    }


    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.container, index);
    }


    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = this.container.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.container.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            // this.totalCookTime = this.getCookTime(stack);
            //this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        if(id==0){
            return this.currentNutrient;
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        if(id==0){
            this.currentNutrient = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public void clear() {

    }

    @Override
    public void update() {
        if(!this.world.isRemote){
            if(this.currentNutrient>=1000)return;
            this.currentNutrient+=requestNutrient(10);
        }
    }

    private int requestNutrient(int amount) {
        for (EnumFacing facing : getValidFacingConnect()){
            BlockPos offset = this.pos.offset(facing);
            TileEntity tile = this.world.getTileEntity(offset);
            if(tile instanceof BioPipeTileBlock){
                return ((BioPipeTileBlock)tile).requestNutrients(amount,facing,new HashSet<>());
            }
        }
        return 0;
    }

    public List<EnumFacing> getValidFacingConnect(){
        IBlockState state = this.world.getBlockState(this.pos);
        switch (state.getValue(BioFurnaceBlock.FACING)){
            case EAST:{

                return Lists.newArrayList(EnumFacing.WEST,EnumFacing.SOUTH,EnumFacing.NORTH);
            }
            case WEST:{
                return Lists.newArrayList(EnumFacing.EAST,EnumFacing.SOUTH,EnumFacing.NORTH);

            }
            case SOUTH:{
                return Lists.newArrayList(EnumFacing.WEST,EnumFacing.EAST,EnumFacing.NORTH);

            }
            case NORTH:{
                return Lists.newArrayList(EnumFacing.WEST,EnumFacing.SOUTH,EnumFacing.EAST);

            }
            default:{
                return Lists.newArrayList();
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.container = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.container);

        this.currentNutrient = compound.getInteger("currentNutrient");

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("currentNutrient",this.currentNutrient);
        ItemStackHelper.saveAllItems(compound, this.container);

        return super.writeToNBT(compound);
    }
    @Override
    public String getName() {
        return "Bio Crafter";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
