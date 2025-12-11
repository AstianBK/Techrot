package com.the_blood_knight.techrot.common.tile_block;

import com.google.common.collect.Lists;
import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.INutritionBlock;
import com.the_blood_knight.techrot.common.block.BioEggMakerBlock;
import com.the_blood_knight.techrot.common.block.BioFurnaceBlock;
import com.the_blood_knight.techrot.common.container.BioEggMakerContainer;
import com.the_blood_knight.techrot.common.container.BioFleshClonerContainer;
import com.the_blood_knight.techrot.common.item.BioExtractorItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.SpawnEggNames;
import net.minecraft.util.datafix.fixes.SpawnerEntityTypes;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.List;

public class BioEggMakerTileBlock extends TileEntityLockable implements ITickable, ISidedInventory, INutritionBlock {
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    public int currentNutrient = 0;
    public int clonerTimer = 0;
    public int maxClonerTimer = 0;
    private NonNullList<ItemStack> container = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);

    public BioEggMakerTileBlock() {
        super();
    }

    @Override
    public void update() {
        boolean flag = false;
        boolean flag1 = this.maxClonerTimer>0;

        if(!this.world.isRemote){
            if(this.maxClonerTimer>0 && this.clonerTimer%10==0){
                Techrot.damageTick(world,pos,3);
            }
            if(this.currentNutrient<1000){
                this.currentNutrient+=this.requestNutrient(1);
            }
            if(currentNutrient>0 && canCloneFlesh()){
                if(this.maxClonerTimer<=0){
                    flag = true;
                    this.maxClonerTimer = 200;
                }else {
                    if(this.clonerTimer%10 == 0){
                        this.currentNutrient-=5;
                    }
                    this.clonerTimer++;
                    if(this.clonerTimer==this.maxClonerTimer){
                        this.maxClonerTimer = 200;
                        this.clonerTimer = 0;
                        this.cloneFlesh();
                        flag = true;
                    }
                }
            }else if(this.clonerTimer>0){
                this.clonerTimer = 0;
            }
            if(flag1!=this.clonerTimer>0){
                flag = true;
                BioEggMakerBlock.setState(this.clonerTimer>0,this.world,this.pos);
            }
        }

        if(flag){
            this.markDirty();
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
    public static boolean equalSpawnEgg(ItemStack first,ItemStack second){
        if(first.getTagCompound() == null || second.getTagCompound()==null){
            return false;
        }
        if(first.getTagCompound().hasKey("EntityTag") && second.getTagCompound().hasKey("EntityTag")){
            return first.getTagCompound().getCompoundTag("EntityTag").getString("id").equals(second.getTagCompound().getCompoundTag("EntityTag").getString("id"));
        }
        return false;
    }
    public static ItemStack createSpawnEgg(ResourceLocation entityId, int primaryColor, int secondaryColor) {
        ItemStack egg = new ItemStack(Items.SPAWN_EGG);

        NBTTagCompound entityTag = new NBTTagCompound();
        entityTag.setString("id", entityId.toString());

        NBTTagCompound root = new NBTTagCompound();
        root.setTag("EntityTag", entityTag);

        egg.setTagCompound(root);

        return egg;
    }
    private void cloneFlesh() {
        this.container.get(1).shrink(1);
        ItemStack extract = this.container.get(0);
        ItemStack clone = getCloneForADN(extract);
        ItemStack result = this.container.get(2);

        if(clone!=null){
            int damage = extract.getItemDamage();
            if(result.isEmpty()){
                this.container.set(2,clone);
            }else if(result.getItem()==clone.getItem() && result.getCount()<64){
                this.container.get(2).grow(1);
            }
            if(damage<14){
                extract.setItemDamage(damage+1);
            }else {
                this.container.set(0,new ItemStack(TRegistry.BIO_EXTRACTOR));
            }
        }
    }
    public ItemStack getCloneForADN(ItemStack extract){
        if(!BioExtractorItem.getADN(extract).contains(":"))return ItemStack.EMPTY;
        String adn = BioExtractorItem.getADN(extract);
        return createSpawnEgg(new ResourceLocation(adn),0xA0E0A0, 0xFF0000 );
    }




    public boolean canCloneFlesh(){
        boolean flag = !BioExtractorItem.getADN(this.container.get(0)).equals("none") && this.container.get(1).getItem().equals(Items.EGG);
        if(!flag){
            return false;
        }
        ItemStack clone = getCloneForADN(this.container.get(0));
        ItemStack result = this.container.get(2);
        return (result.isEmpty() || equalSpawnEgg(clone,result)) && result.getCount()<64;
    }



    @Override
    public int getNutrition() {
        return 0;
    }

    @Override
    public void setNutrition(int value) {

    }

    @Override
    public int extractNutrition(EnumFacing facing) {
        this.currentNutrient++;
        this.markDirty();
        return 1;
    }

    @Override
    public boolean canExtract(BlockPos pos, EnumFacing facing) {
        return false;
    }

    @Override
    public boolean canInsert(BlockPos pos, EnumFacing facing) {
        return this.currentNutrient<1000;
    }

    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN)
        {
            return SLOTS_BOTTOM;
        }
        else
        {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }



    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }



    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (direction == EnumFacing.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getSizeInventory() {
        return 3;
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
        return false;
    }

    @Override
    public int getField(int id) {
        switch (id)
        {
            case 0:
                return this.currentNutrient;
            case 1:
                return this.clonerTimer;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id)
        {
            case 0:
                this.currentNutrient = value;
                break;
            case 1:
                this.clonerTimer = value;
                break;
            default:

        }
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public void clear() {
        this.container.clear();
    }

    @Override
    public String getName() {
        return "Egg Maker";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.currentNutrient = compound.getInteger("currentNutrient");
        this.clonerTimer = compound.getInteger("clonerTimer");
        this.maxClonerTimer = compound.getInteger("maxClonerTimer");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("currentNutrient",this.currentNutrient);
        compound.setInteger("clonerTimer",this.clonerTimer);
        compound.setInteger("maxClonerTimer",this.maxClonerTimer);
        return super.writeToNBT(compound);
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new BioEggMakerContainer(playerInventory,this);
    }

    @Override
    public String getGuiID() {
        return "techrot:bioeggmaker";
    }

    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.WEST);

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable EnumFacing facing)
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
