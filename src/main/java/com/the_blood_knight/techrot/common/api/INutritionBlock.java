package com.the_blood_knight.techrot.common.api;

import com.the_blood_knight.techrot.common.tile_block.BioPipeExtractTileBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;

public interface INutritionBlock {
    default int requestNutrient(int amount, BlockPos pos, World world) {
        for (EnumFacing facing : getValidFacingConnect()){
            BlockPos offset = pos.offset(facing);
            TileEntity tile = world.getTileEntity(offset);
            if(tile instanceof BioPipeExtractTileBlock){
                return ((BioPipeExtractTileBlock)tile).requestNutrients(amount,facing,new HashSet<>());
            }
        }
        return 0;
    }
    List<EnumFacing> getValidFacingConnect();
}
