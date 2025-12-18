package com.the_blood_knight.techrot.common.block;

import com.the_blood_knight.techrot.common.TRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BioImplanterTopBlock extends BlockTileBase {

    public BioImplanterTopBlock(Material material, String name) {
        super(material, name);
        setHardness(85.0F);
        setResistance(85.0F);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasItemBlock() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
                                    EntityPlayer playerIn, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos below = pos.down();
        IBlockState bottomState = worldIn.getBlockState(below);

        if (bottomState.getBlock() == TRegistry.BIOIMPLANTER) {
            return bottomState.getBlock().onBlockActivated(
                    worldIn, below, bottomState,
                    playerIn, hand, facing, hitX, hitY, hitZ
            );
        }
        return true;
    }



}
