package com.the_blood_knight.techrot.common.tile_block;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.api.INutritionBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class BioPipeTileBlock extends TileEntity implements ITickable {

    public Map<EnumFacing, BlockPos> connections = Techrot.main.getMapEmpty();
    public List<BlockPos> cores = new ArrayList<>();

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

    public void addCore(BlockPos pos){
        if(!this.cores.contains(pos)){
            this.cores.add(pos);
        }
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        return super.writeToNBT(compound);
    }

}
