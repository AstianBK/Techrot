package com.the_blood_knight.techrot.common.tile_block;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.api.INutritionBlock;
import com.the_blood_knight.techrot.common.block.BioPipeBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;

public class BioPipeTileBlock extends TileEntity implements ITickable,INutritionBlock {

    public Map<EnumFacing, BlockPos> connections = Techrot.main.getMapEmpty();
    public List<BlockPos> cores = new ArrayList<>();
    public List<BlockPos> features = new ArrayList<>();
    public EnumFacing directionActually = null;
    @Override
    public void update() {

    }

    public int getCountConnection(){
        int i = 0;
        for (BlockPos pos1: this.connections.values()){
            if(pos1!=null){
                i++;
            }
        }
        return i;
    }

    public void addFeature(BlockPos pos){
        if(!this.features.contains(pos)){
            this.features.add(pos);
        }
    }
    public void addCore(BlockPos pos){
        if(!this.cores.contains(pos)){
            this.cores.add(pos);
        }
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);


    }

    public BlockPos[] getConnectedPipes() {
        List<BlockPos> list = new ArrayList<>();
        for (EnumFacing f : EnumFacing.VALUES) {
            BlockPos p = pos.offset(f);
            if (world.isBlockLoaded(p)) {
                TileEntity te = world.getTileEntity(p);
                if (te instanceof BioPipeTileBlock) list.add(p);
            }
        }
        return list.toArray(new BlockPos[0]);
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<EnumFacing,BlockPos> facings : connections.entrySet()){
            if(facings.getValue()==null)continue;
            NBTTagCompound blockpos = new NBTTagCompound();
            blockpos.setTag("pos",NBTUtil.createPosTag(facings.getValue()));
            blockpos.setString("facing",facings.getKey().getName());
            list.appendTag(blockpos);
        }
        compound.setTag("connections",list);
        NBTTagList cores = new NBTTagList();
        for (BlockPos coresPos : this.cores){
            cores.appendTag(NBTUtil.createPosTag(coresPos));
        }
        compound.setTag("cores",cores);
        return super.writeToNBT(compound);
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
            if (te instanceof BioPipeTileBlock) {
                int got = ((BioPipeTileBlock)te).requestNutrients(amount, f, visited);
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
    @Override
    public int getNutrition() {
        return 0;
    }

    @Override
    public void setNutrition(int value) {

    }

    @Override
    public int extractNutrition(EnumFacing facing) {

        return 10;
    }

    @Override
    public boolean canExtract(BlockPos pos, EnumFacing facing) {
        return false;
    }

    @Override
    public boolean canInsert(BlockPos pos, EnumFacing facing) {
        return this.isConnectTo(pos,facing);
    }

    public boolean isConnectTo(BlockPos pos,EnumFacing facing){
        IBlockState state = this.world.getBlockState(this.pos);
        if(facing!=EnumFacing.NORTH && state.getValue(BioPipeBlock.NORTH) && this.pos.north().equals(pos)){
            return true;
        }
        if(facing!=EnumFacing.SOUTH &&state.getValue(BioPipeBlock.SOUTH) && this.pos.south().equals(pos)){
            return true;
        }
        if(facing!=EnumFacing.EAST &&state.getValue(BioPipeBlock.EAST) && this.pos.east().equals(pos)){
            return true;
        }
        if(facing!=EnumFacing.WEST &&state.getValue(BioPipeBlock.WEST) && this.pos.west().equals(pos)){
            return true;
        }
        if(facing!=EnumFacing.UP &&state.getValue(BioPipeBlock.UP) && this.pos.up().equals(pos)){
            return true;
        }
        return facing!=EnumFacing.DOWN && state.getValue(BioPipeBlock.DOWN) && this.pos.down().equals(pos);
    }
}
