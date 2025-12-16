package com.the_blood_knight.techrot.common.tile_block;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.api.INutritionBlock;
import com.the_blood_knight.techrot.common.block.BioPipeBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;

public class BioPipeExtractTileBlock extends TileEntity implements ITickable {

    public Map<EnumFacing, BlockPos> connections = Techrot.main.getMapEmpty();
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
                .withProperty(BioPipeBlock.NORTH, this.north)
                .withProperty(BioPipeBlock.SOUTH, this.south)
                .withProperty(BioPipeBlock.EAST,  this.east)
                .withProperty(BioPipeBlock.WEST,  this.west)
                .withProperty(BioPipeBlock.UP,    this.up)
                .withProperty(BioPipeBlock.DOWN,  this.down);

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
                if (te instanceof BioPipeExtractTileBlock || te instanceof BioPastemakerTileBlock || te instanceof INutritionBlock) list.add(p);
            }
        }
        return list.toArray(new BlockPos[0]);
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setBoolean("north", world.getBlockState(pos).getValue(BioPipeBlock.NORTH));
        tag.setBoolean("south", world.getBlockState(pos).getValue(BioPipeBlock.SOUTH));
        tag.setBoolean("east",  world.getBlockState(pos).getValue(BioPipeBlock.EAST));
        tag.setBoolean("west",  world.getBlockState(pos).getValue(BioPipeBlock.WEST));
        tag.setBoolean("up",    world.getBlockState(pos).getValue(BioPipeBlock.UP));
        tag.setBoolean("down",  world.getBlockState(pos).getValue(BioPipeBlock.DOWN));

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
    public BioPastemakerTileBlock getConnectedNucleus() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity tile = world.getTileEntity(pos.offset(facing));
            if (tile instanceof BioPastemakerTileBlock) return (BioPastemakerTileBlock) tile;
        }
        return null;
    }
    public int requestNutrients(int amount, @Nullable EnumFacing from,Set<BlockPos> visited) {
        if(visited.size()>50){
            return 0;
        }
        if(visited.contains(this.pos)){
            return 0;
        }else {
            visited.add(this.pos);
        }
        BioPastemakerTileBlock core = getConnectedNucleus();
        if (core != null && from != null) {
            return core.extractNutrients(amount);
        }

        for (EnumFacing f : getConnectionFacing()) {
            if (from != null && f == from.getOpposite()) continue;

            BlockPos nextPos = pos.offset(f);
            if (!world.isBlockLoaded(nextPos)) continue;

            TileEntity te = world.getTileEntity(nextPos);
            if (te instanceof BioPipeExtractTileBlock) {
                int got = ((BioPipeExtractTileBlock)te).requestNutrients(amount, f, visited);
                if (got > 0) return got;
            }

            if (te instanceof BioPastemakerTileBlock) {
                return ((BioPastemakerTileBlock)te).extractNutrients(amount);
            }
        }

        return 0;
    }

    public List<EnumFacing> getConnectionFacing(){
        List<EnumFacing> list = new ArrayList<>();
        IBlockState state = this.world.getBlockState(pos);
        if(state.getValue(BioPipeBlock.NORTH)){
            list.add(EnumFacing.NORTH);
        }
        if(state.getValue(BioPipeBlock.SOUTH)){
            list.add(EnumFacing.SOUTH);
        }
        if(state.getValue(BioPipeBlock.EAST)){
            list.add(EnumFacing.EAST);
        }
        if(state.getValue(BioPipeBlock.WEST)){
            list.add(EnumFacing.WEST);
        }
        if(state.getValue(BioPipeBlock.UP)){
            list.add(EnumFacing.UP);
        }
        if(state.getValue(BioPipeBlock.DOWN)){
            list.add(EnumFacing.DOWN);
        }
        return list;
    }

}
