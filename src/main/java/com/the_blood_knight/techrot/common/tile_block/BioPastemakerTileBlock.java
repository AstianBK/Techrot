package com.the_blood_knight.techrot.common.tile_block;

import com.google.common.collect.Lists;
import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.api.INutritionBlock;
import com.the_blood_knight.techrot.common.block.BioFurnaceBlock;
import com.the_blood_knight.techrot.common.block.BioPastemakerBlock;
import com.the_blood_knight.techrot.common.container.BioPastemakerContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class BioPastemakerTileBlock extends TileEntityLockable implements ITickable, ISidedInventory {
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    private int maxNutrition = 10000;
    private int currentNutrition = 0;
    private int eatTime = 0;
    private int totalEatTime = 0;
    private int extractTime = 0;
    private NonNullList<ItemStack> container = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private List<BlockPos> parents = new ArrayList<>();
    private List<Item> foods = Lists.newArrayList(Items.BEEF,Items.MUTTON,Items.SPIDER_EYE,Items.CHICKEN,Items.FISH,Items.PORKCHOP,Items.EGG,Items.ROTTEN_FLESH,Items.RABBIT, Items.RABBIT_FOOT);

    public boolean isHungry(){
        return this.currentNutrition<this.maxNutrition;
    }
    @Override
    public void update() {
        boolean flag = this.isEating();
        boolean flag1 = false;
        if(!this.isHungry())return;
        if (!this.world.isRemote) {
            this.extractTime++;
            ItemStack itemstack = getEatItem();
            if(this.isEating() && this.eatTime%10==0){
                Techrot.damageTick(world,pos,7);
            }
            if(extractTime==10){
                extractTime=0;
                this.extractFood();
            }
            if (this.isEating() || !itemstack.isEmpty()) {

                if (this.totalEatTime<=0 && this.canEat()) {
                    this.eatTime = 0;
                    this.totalEatTime = 200;

                    if (this.isEating()) {
                        flag1 = true;
                    }

                }

                if (this.totalEatTime>0 && this.canEat()) {
                    ++this.eatTime;

                    if (this.eatTime == this.totalEatTime) {
                        this.eatTime = 0;
                        this.totalEatTime = 200;
                        this.eat();
                        flag1 = true;
                    }
                } else {
                    this.eatTime = 0;
                    this.totalEatTime = 0;
                }
            } else if (this.totalEatTime<=0 && this.eatTime > 0) {
                this.eatTime = MathHelper.clamp(this.eatTime - 2, 0, 200);
            }

            if (flag != this.isEating()) {
                flag1 = true;
                BioPastemakerBlock.setState(this.isEating(), this.world, this.pos);
            }

        }
        if (flag1) {
            this.markDirty();
        }
    }

    private void extractFood() {
        if(this.container.get(0).getCount()==64 && this.container.get(1).getCount()==64 && this.container.get(2).getCount()==64){
            return;
        }
        for (EnumFacing facing : getValidFacingConnect()){
            BlockPos offset = pos.offset(facing);
            TileEntity tile = world.getTileEntity(offset);
            if(tile instanceof BioPipeTransportItemTileBlock){
                if(!((BioPipeTransportItemTileBlock)tile).getConnectionFacing().contains(facing.getOpposite()))continue;
                boolean isEmpty0 = this.container.get(0).isEmpty();
                boolean isEmpty1 = this.container.get(1).isEmpty();
                boolean isEmpty2 = this.container.get(2).isEmpty();
                ItemStack currentItem = ((BioPipeTransportItemTileBlock)tile).requestNutrients(null,new HashSet<>(), this.foods);

                if(isEmpty0){
                    ItemStack copy = new ItemStack(currentItem.getItem());
                    copy.setCount(1);
                    currentItem.shrink(1);
                    this.container.set(0,copy);
                    return;
                }else {
                    if(this.container.get(0).getCount()<64){
                        ItemStack slot0=((BioPipeTransportItemTileBlock)tile).requestNutrients(null,new HashSet<>(), Arrays.asList(this.container.get(0).getItem()));
                        if (!slot0.isEmpty()){
                            this.container.get(0).grow(1);
                            slot0.shrink(1);
                            return;
                        }
                    }
                }

                if(isEmpty1){
                    ItemStack copy = new ItemStack(currentItem.getItem());
                    currentItem.shrink(1);
                    this.container.set(1,copy);
                    return;
                }else {

                    if(this.container.get(1).getCount()<64){
                        ItemStack slot1=((BioPipeTransportItemTileBlock)tile).requestNutrients(null,new HashSet<>(), Arrays.asList(this.container.get(1).getItem()));

                        if(!slot1.isEmpty()){
                            this.container.get(1).grow(1);
                            slot1.shrink(1);
                            return;
                        }
                    }

                }

                if(isEmpty2){
                    ItemStack copy = new ItemStack(currentItem.getItem());
                    copy.setCount(1);
                    currentItem.shrink(1);
                    this.container.set(2,copy);
                    return;
                }else {
                    if(this.container.get(2).getCount()<64){
                        ItemStack slot2=((BioPipeTransportItemTileBlock)tile).requestNutrients(null,new HashSet<>(), Arrays.asList(this.container.get(2).getItem()));
                        if(!slot2.isEmpty()){
                            this.container.get(2).grow(1);
                            slot2.shrink(1);
                            return;
                        }
                    }
                }
                if(this.container.get(0).getCount()==64 && this.container.get(1).getCount()==64 && this.container.get(2).getCount()==64){
                    return;
                }
            }
        }

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

    public int extractNutrients(int amount) {
        if (amount <= 0) return 0;

        int delivered = Math.min(amount, this.currentNutrition);

        this.currentNutrition -= delivered;

        markDirty();

        return delivered;
    }

    public boolean canEat(){
        return this.container.stream().anyMatch(this::isFood);
    }

    public boolean isEating(){
        return this.eatTime>0;
    }


    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }



    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return true;
    }



    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (direction == EnumFacing.DOWN && index == 1) {
            Item item = stack.getItem();

            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }

        return true;
    }

    @Override
    public int getSizeInventory() {
        return this.container.size();
    }

    public ItemStack getEatItem(){
        ItemStack item0 = this.container.get(0);
        ItemStack item1 = this.container.get(1);
        ItemStack item2 = this.container.get(2);
        if(isFood(item0)){
            return item0;
        }
        if(isFood(item1)){
            return item1;
        }
        if(isFood(item2)){
            return item2;
        }

        return new ItemStack(Items.AIR);
    }
    public boolean isFood(ItemStack food){
        return this.foods.contains(food.getItem());
    }

    public void eat(){
        ItemStack item0 = this.container.get(0);
        ItemStack item1 = this.container.get(1);
        ItemStack item2 = this.container.get(2);
        boolean flag = false;
        if(isFood(item0)){
            item0.shrink(1);
            flag = true;
        }else if(isFood(item1)){
            item1.shrink(1);
            flag = true;
        }else if(isFood(item2)){
            item2.shrink(1);
            flag = true;
        }
        if(flag){
            this.currentNutrition=Math.min(this.maxNutrition,this.currentNutrition+200);

        }
    }

    @Override
    public boolean isEmpty() {
        return this.container.isEmpty();
    }

    public ItemStack getStackInSlot(int index) {
        return this.container.get(index);
    }


    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.container, index, count);
    }


    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.container, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.container.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.container.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {

            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    public int getField(int id) {
        switch (id)
        {
            case 0:
                return this.maxNutrition;
            case 1:
                return this.currentNutrition;
            case 2:
                return this.eatTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.maxNutrition = value;
                break;
            case 1:
                this.currentNutrition = value;
                break;
            case 2:
                this.eatTime = value;
                break;

        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.container = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.container);

        this.eatTime = compound.getInteger("eatTime");
        this.totalEatTime = compound.getInteger("totalEatTime");
        this.maxNutrition = compound.getInteger("maxNutrition");
        this.currentNutrition = compound.getInteger("currentNutrition");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        ItemStackHelper.saveAllItems(compound, this.container);

        compound.setInteger("eatTime",this.eatTime);
        compound.setInteger("totalEatTime",this.totalEatTime);
        compound.setInteger("maxNutrition",this.maxNutrition);
        compound.setInteger("currentNutrition",this.currentNutrition);
        return super.writeToNBT(compound);
    }

    public int getFieldCount() {
        return 3;
    }

    @Override
    public void clear() {
        this.container.clear();
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new BioPastemakerContainer(playerInventory,this);
    }

    @Override
    public String getGuiID() {
        return "techrot:biopastemaker";
    }

    @Override
    public String getName() {
        return "PasteMaker";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing)
    {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerBottom;
            else if (facing == EnumFacing.UP)
                return (T) handlerTop;
            else
                return (T) handlerSide;
        return super.getCapability(capability, facing);
    }


}
