package com.the_blood_knight.techrot.common.tile_block;

import com.the_blood_knight.techrot.common.api.IBioContainer;
import com.the_blood_knight.techrot.common.api.INutritionBlock;
import com.the_blood_knight.techrot.common.block.BioPipeTransportItemBlock;
import com.the_blood_knight.techrot.common.block.BioPipeTransportItemBlock;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BioPipeTransportItemTileBlock extends TileEntity implements ITickable {

    public boolean loaded = false;
    public boolean north = false;
    public boolean south = false;
    public boolean east = false;
    public boolean west = false;
    public boolean up = false;
    public boolean down = false;
    @Override
    public void update() {
        if (!loaded || world == null) return;

        loaded = false;
        IBlockState state = world.getBlockState(pos)
                .withProperty(BioPipeTransportItemBlock.NORTH, this.north)
                .withProperty(BioPipeTransportItemBlock.SOUTH, this.south)
                .withProperty(BioPipeTransportItemBlock.EAST,  this.east)
                .withProperty(BioPipeTransportItemBlock.WEST,  this.west)
                .withProperty(BioPipeTransportItemBlock.UP,    this.up)
                .withProperty(BioPipeTransportItemBlock.DOWN,  this.down);

        world.setBlockState(pos, state, 3);
        markDirty();
    }

    public int getCountConnection(){
        return getConnectedPipes().length;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.north = compound.getBoolean("north");
        this.south = compound.getBoolean("south");
        this.east =  compound.getBoolean("east");
        this.west = compound.getBoolean("west");
        this.up = compound.getBoolean("up");
        this.down = compound.getBoolean("down");
        this.loaded = true;
    }

    public BlockPos[] getConnectedPipes() {
        List<BlockPos> list = new ArrayList<>();
        for (EnumFacing f : getConnectionFacing()) {
            BlockPos p = pos.offset(f);
            if (world.isBlockLoaded(p)) {
                TileEntity te = world.getTileEntity(p);
                if (te instanceof BioPipeTransportItemTileBlock || te instanceof BioPastemakerTileBlock || te instanceof INutritionBlock) list.add(p);
            }
        }
        return list.toArray(new BlockPos[0]);
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setBoolean("north", world.getBlockState(pos).getValue(BioPipeTransportItemBlock.NORTH));
        tag.setBoolean("south", world.getBlockState(pos).getValue(BioPipeTransportItemBlock.SOUTH));
        tag.setBoolean("east",  world.getBlockState(pos).getValue(BioPipeTransportItemBlock.EAST));
        tag.setBoolean("west",  world.getBlockState(pos).getValue(BioPipeTransportItemBlock.WEST));
        tag.setBoolean("up",    world.getBlockState(pos).getValue(BioPipeTransportItemBlock.UP));
        tag.setBoolean("down",  world.getBlockState(pos).getValue(BioPipeTransportItemBlock.DOWN));

        return tag;
    }
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        writeToNBT(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }
    public TileEntity getConnectedNucleus() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity tile = world.getTileEntity(pos.offset(facing));
            if (tile instanceof IBioContainer || tile instanceof TileEntityChest) return tile;
        }
        return null;
    }
    public ItemStack getItemForList(List<Item> requiemItems, IItemHandler singleChestHandler){
        for (int i = 0 ; i < singleChestHandler.getSlots() ; i++){
            if(!singleChestHandler.getStackInSlot(i).isEmpty() && requiemItems.contains(singleChestHandler.getStackInSlot(i).getItem())){
                return singleChestHandler.getStackInSlot(i);
            }
        }
        return ItemStack.EMPTY;
    }
    public ItemStack requestNutrients(@Nullable EnumFacing from, Set<BlockPos> visited,List<Item> items) {
        if(visited.size()>50){
            return ItemStack.EMPTY;
        }
        if(visited.contains(this.pos)){
            return ItemStack.EMPTY;
        }else {
            visited.add(this.pos);
        }
        TileEntity core = getConnectedNucleus();
        if (core != null && from != null) {
            if(core instanceof IBioContainer){
                ItemStack extract = ((IBioContainer)core).getExtractItem();
                ItemStack copy = extract.copy();
                extract.shrink(1);
                return copy;
            }
            return core instanceof TileEntityChest ? getItemForList(items,((TileEntityChest) core).getSingleChestHandler()) : ItemStack.EMPTY;
        }

        for (EnumFacing f : getConnectionFacing()) {
            if (from != null && f == from.getOpposite()) continue;

            BlockPos nextPos = pos.offset(f);
            if (!world.isBlockLoaded(nextPos)) continue;

            TileEntity te = world.getTileEntity(nextPos);
            if (te instanceof BioPipeTransportItemTileBlock) {
                ItemStack got = ((BioPipeTransportItemTileBlock)te).requestNutrients(f, visited,items);
                if (!got.isEmpty()) return got;
            }

            if (te instanceof IBioContainer) {
                if(core instanceof TileEntityChest){
                    return getItemForList(items,((TileEntityChest) core).getSingleChestHandler());
                }
                return ((IBioContainer)core).getExtractItem();
            }
        }

        return ItemStack.EMPTY;

    }

    public List<EnumFacing> getConnectionFacing(){
        List<EnumFacing> list = new ArrayList<>();
        IBlockState state = this.world.getBlockState(pos);
        if(state.getValue(BioPipeTransportItemBlock.NORTH)){
            list.add(EnumFacing.NORTH);
        }
        if(state.getValue(BioPipeTransportItemBlock.SOUTH)){
            list.add(EnumFacing.SOUTH);
        }
        if(state.getValue(BioPipeTransportItemBlock.EAST)){
            list.add(EnumFacing.EAST);
        }
        if(state.getValue(BioPipeTransportItemBlock.WEST)){
            list.add(EnumFacing.WEST);
        }
        if(state.getValue(BioPipeTransportItemBlock.UP)){
            list.add(EnumFacing.UP);
        }
        if(state.getValue(BioPipeTransportItemBlock.DOWN)){
            list.add(EnumFacing.DOWN);
        }
        return list;
    }

}
