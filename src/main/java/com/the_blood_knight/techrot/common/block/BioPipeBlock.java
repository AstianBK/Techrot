package com.the_blood_knight.techrot.common.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BioPipeBlock;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BioPipeBlock extends BlockBase{
    public static final PropertyEnum<BioPipeBlock.EnumPipeDirection> SHAPE = PropertyEnum.<BioPipeBlock.EnumPipeDirection>create("shape", BioPipeBlock.EnumPipeDirection.class);

    public BioPipeBlock(Material material,String name) {
        super(material,name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    protected void updateState(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        if (blockIn.getDefaultState().canProvidePower() && (new BlockRailBase.Rail(worldIn, pos, state)).countAdjacentRails() == 3)
        {
            this.updateDir(worldIn, pos, state, false);
        }
    }

    public IProperty<BioPipeBlock.EnumPipeDirection> getShapeProperty()
    {
        return SHAPE;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((BioPipeBlock.EnumPipeDirection)state.getValue(SHAPE)).getMetadata();
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    @SuppressWarnings("incomplete-switch")
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case CLOCKWISE_180:

                switch ((BioPipeBlock.EnumPipeDirection)state.getValue(SHAPE))
                {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.SOUTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.SOUTH_WEST);
                }

            case COUNTERCLOCKWISE_90:

                switch ((BioPipeBlock.EnumPipeDirection)state.getValue(SHAPE))
                {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_WEST);
                    case NORTH_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.EAST_WEST);
                    case EAST_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_SOUTH);
                }

            case CLOCKWISE_90:

                switch ((BioPipeBlock.EnumPipeDirection)state.getValue(SHAPE))
                {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_WEST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.EAST_WEST);
                    case EAST_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_SOUTH);
                }

            default:
                return state;
        }
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    @SuppressWarnings("incomplete-switch")
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        BioPipeBlock.EnumPipeDirection blockrailbase$enumraildirection = (BioPipeBlock.EnumPipeDirection)state.getValue(SHAPE);

        switch (mirrorIn)
        {
            case LEFT_RIGHT:

                switch (blockrailbase$enumraildirection)
                {
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_WEST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.SOUTH_EAST);
                    default:
                        return super.withMirror(state, mirrorIn);
                }

            case FRONT_BACK:

                switch (blockrailbase$enumraildirection)
                {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.NORTH_WEST);
                }
        }

        return super.withMirror(state, mirrorIn);
    }
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,SHAPE);
    }
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    /**
     * @deprecated call via {@link IBlockState#isFullCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Determines if an entity can path through this block
     */
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     * @deprecated call via {@link IBlockState#isOpaqueCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    

    

    public static enum EnumPipeDirection implements IStringSerializable
    {
        NORTH_SOUTH(0, "north_south"),
        EAST_WEST(1, "east_west"),
        ASCENDING_EAST(2, "ascending_east"),
        ASCENDING_WEST(3, "ascending_west"),
        ASCENDING_NORTH(4, "ascending_north"),
        ASCENDING_SOUTH(5, "ascending_south"),
        SOUTH_EAST(6, "south_east"),
        SOUTH_WEST(7, "south_west"),
        NORTH_WEST(8, "north_west"),
        NORTH_EAST(9, "north_east");

        private static final EnumPipeDirection[] META_LOOKUP = new EnumPipeDirection[values().length];
        private final int meta;
        private final String name;

        private EnumPipeDirection(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        public boolean isAscending()
        {
            return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
        }

        public static EnumPipeDirection byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.name;
        }

        static
        {
            for (EnumPipeDirection blockrailbase$enumraildirection : values())
            {
                META_LOOKUP[blockrailbase$enumraildirection.getMetadata()] = blockrailbase$enumraildirection;
            }
        }
    }
    public class Pipe
    {
        private final World world;
        private final BlockPos pos;
        private final BioPipeBlock block;
        private IBlockState state;
        private final boolean isPowered;
        private final List<BlockPos> connectedPipes = Lists.<BlockPos>newArrayList();
        private final boolean canMakeSlopes;

        public Pipe(World worldIn, BlockPos pos, IBlockState state)
        {
            this.world = worldIn;
            this.pos = pos;
            this.state = state;
            this.block = (BioPipeBlock)state.getBlock();
            EnumPipeDirection blockrailbase$enumraildirection = block.getPipeDirection(worldIn, pos, state, null);
            this.isPowered = !this.block.isFlexiblePipe(worldIn, pos);
            this.canMakeSlopes = this.block.canMakeSlopes(worldIn, pos);
            this.updateConnectedPipes(blockrailbase$enumraildirection);
        }

        public List<BlockPos> getConnectedPipes()
        {
            return this.connectedPipes;
        }

        private void updateConnectedPipes(EnumPipeDirection railDirection)
        {
            this.connectedPipes.clear();

            switch (railDirection)
            {
                case NORTH_SOUTH:
                    this.connectedPipes.add(this.pos.north());
                    this.connectedPipes.add(this.pos.south());
                    break;
                case EAST_WEST:
                    this.connectedPipes.add(this.pos.west());
                    this.connectedPipes.add(this.pos.east());
                    break;
                case ASCENDING_EAST:
                    this.connectedPipes.add(this.pos.west());
                    this.connectedPipes.add(this.pos.east().up());
                    break;
                case ASCENDING_WEST:
                    this.connectedPipes.add(this.pos.west().up());
                    this.connectedPipes.add(this.pos.east());
                    break;
                case ASCENDING_NORTH:
                    this.connectedPipes.add(this.pos.north().up());
                    this.connectedPipes.add(this.pos.south());
                    break;
                case ASCENDING_SOUTH:
                    this.connectedPipes.add(this.pos.north());
                    this.connectedPipes.add(this.pos.south().up());
                    break;
                case SOUTH_EAST:
                    this.connectedPipes.add(this.pos.east());
                    this.connectedPipes.add(this.pos.south());
                    break;
                case SOUTH_WEST:
                    this.connectedPipes.add(this.pos.west());
                    this.connectedPipes.add(this.pos.south());
                    break;
                case NORTH_WEST:
                    this.connectedPipes.add(this.pos.west());
                    this.connectedPipes.add(this.pos.north());
                    break;
                case NORTH_EAST:
                    this.connectedPipes.add(this.pos.east());
                    this.connectedPipes.add(this.pos.north());
            }
        }

        private void removeSoftConnections()
        {
            for (int i = 0; i < this.connectedPipes.size(); ++i)
            {
                BioPipeBlock.Pipe blockrailbase$rail = this.findPipeAt(this.connectedPipes.get(i));

                if (blockrailbase$rail != null && blockrailbase$rail.isConnectedToPipe(this))
                {
                    this.connectedPipes.set(i, blockrailbase$rail.pos);
                }
                else
                {
                    this.connectedPipes.remove(i--);
                }
            }
        }

        private boolean hasPipeAt(BlockPos pos)
        {
            return BioPipeBlock.isPipeBlock(this.world, pos) || BioPipeBlock.isPipeBlock(this.world, pos.up()) || BioPipeBlock.isPipeBlock(this.world, pos.down());
        }

        @Nullable
        private BioPipeBlock.Pipe findPipeAt(BlockPos pos)
        {
            IBlockState iblockstate = this.world.getBlockState(pos);

            if (BioPipeBlock.isPipeBlock(iblockstate))
            {
                return BioPipeBlock.this.new Pipe(this.world, pos, iblockstate);
            }
            else
            {
                BlockPos lvt_2_1_ = pos.up();
                iblockstate = this.world.getBlockState(lvt_2_1_);

                if (BioPipeBlock.isPipeBlock(iblockstate))
                {
                    return BioPipeBlock.this.new Pipe(this.world, lvt_2_1_, iblockstate);
                }
                else
                {
                    lvt_2_1_ = pos.down();
                    iblockstate = this.world.getBlockState(lvt_2_1_);
                    return BioPipeBlock.isPipeBlock(iblockstate) ? BioPipeBlock.this.new Pipe(this.world, lvt_2_1_, iblockstate) : null;
                }
            }
        }

        private boolean isConnectedToPipe(BioPipeBlock.Pipe rail)
        {
            return this.isConnectedTo(rail.pos);
        }

        private boolean isConnectedTo(BlockPos posIn)
        {
            for (int i = 0; i < this.connectedPipes.size(); ++i)
            {
                BlockPos blockpos = this.connectedPipes.get(i);

                if (blockpos.getX() == posIn.getX() && blockpos.getZ() == posIn.getZ())
                {
                    return true;
                }
            }

            return false;
        }

        /**
         * Counts the number of rails adjacent to this rail.
         */
        protected int countAdjacentPipes()
        {
            int i = 0;

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                if (this.hasPipeAt(this.pos.offset(enumfacing)))
                {
                    ++i;
                }
            }

            return i;
        }

        private boolean canConnectTo(BioPipeBlock.Pipe rail)
        {
            return this.isConnectedToPipe(rail) || this.connectedPipes.size() != 2;
        }

        private void connectTo(BioPipeBlock.Pipe rail)
        {
            this.connectedPipes.add(rail.pos);
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.isConnectedTo(blockpos);
            boolean flag1 = this.isConnectedTo(blockpos1);
            boolean flag2 = this.isConnectedTo(blockpos2);
            boolean flag3 = this.isConnectedTo(blockpos3);
            EnumPipeDirection blockrailbase$enumraildirection = null;

            if (flag || flag1)
            {
                blockrailbase$enumraildirection = EnumPipeDirection.NORTH_SOUTH;
            }

            if (flag2 || flag3)
            {
                blockrailbase$enumraildirection = EnumPipeDirection.EAST_WEST;
            }

            if (!this.isPowered)
            {
                if (flag1 && flag3 && !flag && !flag2)
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.SOUTH_EAST;
                }

                if (flag1 && flag2 && !flag && !flag3)
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.SOUTH_WEST;
                }

                if (flag && flag2 && !flag1 && !flag3)
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.NORTH_WEST;
                }

                if (flag && flag3 && !flag1 && !flag2)
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.NORTH_EAST;
                }
            }

            if (blockrailbase$enumraildirection == EnumPipeDirection.NORTH_SOUTH && canMakeSlopes)
            {
                if (BioPipeBlock.isPipeBlock(this.world, blockpos.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.ASCENDING_NORTH;
                }

                if (BioPipeBlock.isPipeBlock(this.world, blockpos1.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.ASCENDING_SOUTH;
                }
            }

            if (blockrailbase$enumraildirection == EnumPipeDirection.EAST_WEST && canMakeSlopes)
            {
                if (BioPipeBlock.isPipeBlock(this.world, blockpos3.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.ASCENDING_EAST;
                }

                if (BioPipeBlock.isPipeBlock(this.world, blockpos2.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.ASCENDING_WEST;
                }
            }

            if (blockrailbase$enumraildirection == null)
            {
                blockrailbase$enumraildirection = EnumPipeDirection.NORTH_SOUTH;
            }

            this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
            this.world.setBlockState(this.pos, this.state, 3);
        }

        private boolean hasNeighborPipe(BlockPos posIn)
        {
            BioPipeBlock.Pipe blockrailbase$rail = this.findPipeAt(posIn);

            if (blockrailbase$rail == null)
            {
                return false;
            }
            else
            {
                blockrailbase$rail.removeSoftConnections();
                return blockrailbase$rail.canConnectTo(this);
            }
        }

        public BioPipeBlock.Pipe place(boolean powered, boolean initialPlacement)
        {
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.hasNeighborPipe(blockpos);
            boolean flag1 = this.hasNeighborPipe(blockpos1);
            boolean flag2 = this.hasNeighborPipe(blockpos2);
            boolean flag3 = this.hasNeighborPipe(blockpos3);
            EnumPipeDirection blockrailbase$enumraildirection = null;

            if ((flag || flag1) && !flag2 && !flag3)
            {
                blockrailbase$enumraildirection = EnumPipeDirection.NORTH_SOUTH;
            }

            if ((flag2 || flag3) && !flag && !flag1)
            {
                blockrailbase$enumraildirection = EnumPipeDirection.EAST_WEST;
            }

            if (!this.isPowered)
            {
                if (flag1 && flag3 && !flag && !flag2)
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.SOUTH_EAST;
                }

                if (flag1 && flag2 && !flag && !flag3)
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.SOUTH_WEST;
                }

                if (flag && flag2 && !flag1 && !flag3)
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.NORTH_WEST;
                }

                if (flag && flag3 && !flag1 && !flag2)
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.NORTH_EAST;
                }
            }

            if (blockrailbase$enumraildirection == null)
            {
                if (flag || flag1)
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.NORTH_SOUTH;
                }

                if (flag2 || flag3)
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.EAST_WEST;
                }

                if (!this.isPowered)
                {
                    if (powered)
                    {
                        if (flag1 && flag3)
                        {
                            blockrailbase$enumraildirection = EnumPipeDirection.SOUTH_EAST;
                        }

                        if (flag2 && flag1)
                        {
                            blockrailbase$enumraildirection = EnumPipeDirection.SOUTH_WEST;
                        }

                        if (flag3 && flag)
                        {
                            blockrailbase$enumraildirection = EnumPipeDirection.NORTH_EAST;
                        }

                        if (flag && flag2)
                        {
                            blockrailbase$enumraildirection = EnumPipeDirection.NORTH_WEST;
                        }
                    }
                    else
                    {
                        if (flag && flag2)
                        {
                            blockrailbase$enumraildirection = EnumPipeDirection.NORTH_WEST;
                        }

                        if (flag3 && flag)
                        {
                            blockrailbase$enumraildirection = EnumPipeDirection.NORTH_EAST;
                        }

                        if (flag2 && flag1)
                        {
                            blockrailbase$enumraildirection = EnumPipeDirection.SOUTH_WEST;
                        }

                        if (flag1 && flag3)
                        {
                            blockrailbase$enumraildirection = EnumPipeDirection.SOUTH_EAST;
                        }
                    }
                }
            }

            if (blockrailbase$enumraildirection == EnumPipeDirection.NORTH_SOUTH && canMakeSlopes)
            {
                if (BioPipeBlock.isPipeBlock(this.world, blockpos.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.ASCENDING_NORTH;
                }

                if (BioPipeBlock.isPipeBlock(this.world, blockpos1.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.ASCENDING_SOUTH;
                }
            }

            if (blockrailbase$enumraildirection == EnumPipeDirection.EAST_WEST && canMakeSlopes)
            {
                if (BioPipeBlock.isPipeBlock(this.world, blockpos3.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.ASCENDING_EAST;
                }

                if (BioPipeBlock.isPipeBlock(this.world, blockpos2.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.ASCENDING_WEST;
                }
            }

            if (blockrailbase$enumraildirection == null)
            {
                blockrailbase$enumraildirection = EnumPipeDirection.NORTH_SOUTH;
            }

            this.updateConnectedPipes(blockrailbase$enumraildirection);
            this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);

            if (initialPlacement || this.world.getBlockState(this.pos) != this.state)
            {
                this.world.setBlockState(this.pos, this.state, 3);

                for (int i = 0; i < this.connectedPipes.size(); ++i)
                {
                    BioPipeBlock.Pipe blockrailbase$rail = this.findPipeAt(this.connectedPipes.get(i));

                    if (blockrailbase$rail != null)
                    {
                        blockrailbase$rail.removeSoftConnections();

                        if (blockrailbase$rail.canConnectTo(this))
                        {
                            blockrailbase$rail.connectTo(this);
                        }
                    }
                }
            }

            return this;
        }

        public IBlockState getBlockState()
        {
            return this.state;
        }
    }
}
