package com.the_blood_knight.techrot.common.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BioPipeBlock extends BlockBase{
    public static final PropertyEnum<BioPipeBlock.EnumPipeDirection> SHAPE = PropertyEnum.<BioPipeBlock.EnumPipeDirection>create("shape", BioPipeBlock.EnumPipeDirection.class);
    protected static final AxisAlignedBB FLAT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB ASCENDING_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BioPipeBlock(Material material,String name) {
        super(material,name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE,EnumPipeDirection.NORTH_SOUTH));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    protected void updateState(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        if (blockIn.getDefaultState().canProvidePower() && (new BioPipeBlock.Pipe(worldIn, pos, state)).countAdjacentPipes() == 3)
        {
            this.updateDir(worldIn, pos, state, false);
        }
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    

    /**
     * @deprecated call via {@link IBlockState#getBoundingBox(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumPipeDirection blockrailbase$enumraildirection = state.getBlock() == this ? getPipeDirection(source, pos, state, null) : null;
        return blockrailbase$enumraildirection != null && blockrailbase$enumraildirection.isAscending() ? ASCENDING_AABB : FLAT_AABB;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }


    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Checks if this block can be placed exactly at the given position.
     */
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP);
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            state = this.updateDir(worldIn, pos, state, true);
            
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            final IBlockState currentState = worldIn.getBlockState(pos);
            EnumPipeDirection blockrailbase$enumraildirection = getPipeDirection(worldIn, pos, currentState.getBlock() == this ? currentState : state, null);
            boolean flag = false;

            if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP))
            {
                flag = true;
            }

            if (blockrailbase$enumraildirection == EnumPipeDirection.UP_EAST && !worldIn.getBlockState(pos.east()).isSideSolid(worldIn, pos.east(), EnumFacing.UP))
            {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == EnumPipeDirection.UP_WEST && !worldIn.getBlockState(pos.west()).isSideSolid(worldIn, pos.west(), EnumFacing.UP))
            {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == EnumPipeDirection.UP_NORTH && !worldIn.getBlockState(pos.north()).isSideSolid(worldIn, pos.north(), EnumFacing.UP))
            {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == EnumPipeDirection.UP_SOUTH && !worldIn.getBlockState(pos.south()).isSideSolid(worldIn, pos.south(), EnumFacing.UP))
            {
                flag = true;
            }

            if (flag && !currentState.getBlock().isAir(currentState, worldIn, pos))
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            else
            {
                this.updateState(state, worldIn, pos, blockIn);
            }
        }
    }

    protected IBlockState updateDir(World worldIn, BlockPos pos, IBlockState state, boolean initialPlacement)
    {
        return worldIn.isRemote ? state : (new BioPipeBlock.Pipe(worldIn, pos, state)).place(worldIn.isBlockPowered(pos), initialPlacement).getBlockState();
    }
    
    public EnumPushReaction getPushReaction(IBlockState state)
    {
        return EnumPushReaction.NORMAL;
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (getPipeDirection(worldIn, pos, state, null).isAscending())
        {
            worldIn.notifyNeighborsOfStateChange(pos.up(), this, false);
        }

        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);

    }
    

    /**
     * Returns true if the rail can make up and down slopes.
     * Used by placement logic.
     * @param world The world.
     * @param pos Block's position in world
     * @return True if the rail can make slopes.
     */
    public boolean canMakeSlopes(IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    /**
     * Return the rail's direction.
     * Can be used to make the cart think the rail is a different shape,
     * for example when making diamond junctions or switches.
     * The cart parameter will often be null unless it it called from EntityMinecart.
     *
     * @param world The world.
     * @param pos Block's position in world
     * @param state The BlockState
     * @param cart The cart asking for the metadata, null if it is not called by EntityMinecart.
     * @return The direction.
     */
    public EnumPipeDirection getPipeDirection(IBlockAccess world, BlockPos pos, IBlockState state, @javax.annotation.Nullable net.minecraft.entity.item.EntityMinecart cart)
    {
        return state.getValue(getShapeProperty());
    }

    /**
     * Returns the max speed of the rail at the specified position.
     * @param world The world.
     * @param cart The cart on the rail, may be null.
     * @param pos Block's position in world
     * @return The max speed of the current rail.
     */
    public float getPipeMaxSpeed(World world, net.minecraft.entity.item.EntityMinecart cart, BlockPos pos)
    {
        return 0.4f;
    }

    /**
     * This function is called by any minecart that passes over this rail.
     * It is called once per update tick that the minecart is on the rail.
     * @param world The world.
     * @param cart The cart on the rail.
     * @param pos Block's position in world
     */
    public void onMinecartPass(World world, net.minecraft.entity.item.EntityMinecart cart, BlockPos pos)
    {
    }

    /**
     * Rotate the block. For vanilla blocks this rotates around the axis passed in (generally, it should be the "face" that was hit).
     * Note: for mod blocks, this is up to the block and modder to decide. It is not mandated that it be a rotation around the
     * face, but could be a rotation to orient *to* that face, or a visiting of possible rotations.
     * The method should return true if the rotation was successful though.
     *
     * @param world The world
     * @param pos Block position in world
     * @param axis The axis to rotate around
     * @return True if the rotation was successful, False if the rotation failed, or is not possible
     */
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        IBlockState state = world.getBlockState(pos);
        for (IProperty prop : state.getProperties().keySet())
        {
            if (prop.getName().equals("shape"))
            {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        }
        return false;
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
                    case UP_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_WEST);
                    case UP_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_EAST);
                    case UP_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_SOUTH);
                    case UP_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_NORTH);
                    case DOWN_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_WEST);
                    case DOWN_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_EAST);
                    case DOWN_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_SOUTH);
                    case DOWN_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_NORTH);
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
                    case UP_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_NORTH);
                    case UP_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_SOUTH);
                    case UP_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_WEST);
                    case UP_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_EAST);
                    case DOWN_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_NORTH);
                    case DOWN_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_SOUTH);
                    case DOWN_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_WEST);
                    case DOWN_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_EAST);
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
                    case UP_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_SOUTH);
                    case UP_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_NORTH);
                    case UP_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_EAST);
                    case UP_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_WEST);
                    case DOWN_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_SOUTH);
                    case DOWN_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_NORTH);
                    case DOWN_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_EAST);
                    case DOWN_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_WEST);
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
                    case UP_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_SOUTH);
                    case UP_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_NORTH);
                    case DOWN_NORTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_SOUTH);
                    case DOWN_SOUTH:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.DOWN_NORTH);
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
                    case UP_EAST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_WEST);
                    case UP_WEST:
                        return state.withProperty(SHAPE, BioPipeBlock.EnumPipeDirection.UP_EAST);
                    case UP_NORTH:
                    case UP_SOUTH:
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
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,SHAPE);
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



    public static boolean isPipeBlock(World worldIn, BlockPos pos)
    {
        return isPipeBlock(worldIn.getBlockState(pos));
    }

    public static boolean isPipeBlock(IBlockState state)
    {
        Block block = state.getBlock();
        return block instanceof BioPipeBlock;
    }
    public static enum EnumPipeDirection implements IStringSerializable
    {
        NORTH_SOUTH(0, "north_south"),
        EAST_WEST(1, "east_west"),
        UP_EAST(2, "up_east"),
        UP_WEST(3, "up_west"),
        UP_NORTH(4, "up_north"),
        UP_SOUTH(5, "up_south"),
        SOUTH_EAST(6, "south_east"),
        SOUTH_WEST(7, "south_west"),
        NORTH_WEST(8, "north_west"),
        NORTH_EAST(9, "north_east"),
        DOWN_EAST(10, "down_east"),
        DOWN_WEST(11, "down_west"),
        DOWN_NORTH(12, "down_north"),
        DOWN_SOUTH(13, "down_south"),
        UP_DOWN(14, "up_down");

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
            return this == UP_NORTH || this == UP_EAST || this == UP_SOUTH || this == UP_WEST;
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
            this.isPowered = false;
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
                case UP_EAST:
                    this.connectedPipes.add(this.pos.west());
                    this.connectedPipes.add(this.pos.east().up());
                    break;
                case UP_WEST:
                    this.connectedPipes.add(this.pos.west().up());
                    this.connectedPipes.add(this.pos.east());
                    break;
                case UP_NORTH:
                    this.connectedPipes.add(this.pos.north().up());
                    this.connectedPipes.add(this.pos.south());
                    break;
                case UP_SOUTH:
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
                    blockrailbase$enumraildirection = EnumPipeDirection.UP_NORTH;
                }

                if (BioPipeBlock.isPipeBlock(this.world, blockpos1.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.UP_SOUTH;
                }
            }

            if (blockrailbase$enumraildirection == EnumPipeDirection.EAST_WEST && canMakeSlopes)
            {
                if (BioPipeBlock.isPipeBlock(this.world, blockpos3.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.UP_EAST;
                }

                if (BioPipeBlock.isPipeBlock(this.world, blockpos2.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.UP_WEST;
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
                    blockrailbase$enumraildirection = EnumPipeDirection.UP_NORTH;
                }

                if (BioPipeBlock.isPipeBlock(this.world, blockpos1.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.UP_SOUTH;
                }
            }

            if (blockrailbase$enumraildirection == EnumPipeDirection.EAST_WEST && canMakeSlopes)
            {
                if (BioPipeBlock.isPipeBlock(this.world, blockpos3.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.UP_EAST;
                }

                if (BioPipeBlock.isPipeBlock(this.world, blockpos2.up()))
                {
                    blockrailbase$enumraildirection = EnumPipeDirection.UP_WEST;
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
