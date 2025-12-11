package com.the_blood_knight.techrot.common.block;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.tile_block.BioPipeTileBlock;
import net.minecraft.block.Block;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;

public class BioPipeBlock extends BlockTileBase{
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    protected static final AxisAlignedBB FLAT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB ASCENDING_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BioPipeBlock(Material material,String name) {
        super(material,name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP,false).withProperty(DOWN,false).withProperty(SOUTH,false).withProperty(NORTH,false).withProperty(EAST,false).withProperty(WEST,false));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return super.withRotation(state, rot);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new BioPipeTileBlock();
    }


    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return ASCENDING_AABB;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }


    public boolean isFullCube(IBlockState state)
    {
        return false;
    }


    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote) {
        }
    }
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        Pipe newPipe = this.updateDir(worldIn, pos, this.getStateFromMeta(meta), true,false);
        IBlockState state = newPipe.state;
        IBlockState sourceBlock = worldIn.getBlockState(pos.offset(facing.getOpposite()));
        if (sourceBlock.getBlock() instanceof BlockTileBase){
            if(sourceBlock.getBlock() instanceof BioPipeBlock){
                Pipe pipe = (new BioPipeBlock.Pipe(worldIn, pos.offset(facing.getOpposite()), sourceBlock));
                pipe.connectFacing(facing);
            }
            Pipe pipeSource = (new BioPipeBlock.Pipe(worldIn, pos, state));
            pipeSource.connectToPipe(pos.offset(facing.getOpposite()),facing);
            state = pipeSource.state;
            return worldIn.isRemote ? state : pipeSource.state;
        }else {
            //state = ;
        }

        return state;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            final IBlockState currentState = worldIn.getBlockState(pos);
            boolean flag = false;
        }
    }
    

    protected Pipe updateDir(World worldIn, BlockPos pos, IBlockState state, boolean initialPlacement,boolean multi) {
        return (new BioPipeBlock.Pipe(worldIn, pos, state)).place(multi, initialPlacement);
    }
    
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.NORMAL;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity entity = worldIn.getTileEntity(pos);
        if(entity instanceof BioPipeTileBlock && !worldIn.isRemote){
            //((BioPipeTileBlock) entity).requestNutrients(true);
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }


    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {



        Pipe pipe = new Pipe(worldIn,pos,state);
        for (EnumFacing facing : EnumFacing.VALUES){
            BlockPos offSet = pos.offset(facing);
            Pipe breakPie = pipe.findPipeAt(offSet);
            if(breakPie!=null){
                breakPie.breakConnect(worldIn,facing);
            }
        }

        super.breakBlock(worldIn, pos, state);
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);

    }
    






    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        return false;
    }




    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {UP, NORTH, EAST, WEST, SOUTH,DOWN});
    }

    

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public static boolean isPipeBlock(World worldIn, BlockPos pos) {
        return isPipeBlock(worldIn.getBlockState(pos));
    }
    public static boolean isBlockValid(World level,EnumFacing facing, BlockPos pos, int maxValue){
        IBlockState state = level.getBlockState(pos);

        if(!(state.getBlock() instanceof BlockTileBase)){
            return false;
        }

        if(((BlockTileBase)state.getBlock()).validConnectPipe(state,facing)){

            return true;
        }
        if(!isPipeBlock(level.getBlockState(pos)))return false;
        BioPipeTileBlock bioPipe = (BioPipeTileBlock)level.getTileEntity(pos) ;
        return isPipeBlock(state) && bioPipe.getCountConnection()<=maxValue;
    }

    private static boolean isCore(World level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof BioPastemakerBlock;
    }

    public static boolean isPipeValid(World level, BlockPos pos, int maxValue){
        IBlockState state = level.getBlockState(pos);

        if(!(state.getBlock() instanceof BioPipeBlock)){
            return false;
        }
        BioPipeTileBlock bioPipe = (BioPipeTileBlock) level.getTileEntity(pos);
        return isPipeBlock(state) && bioPipe.getCountConnection()<=maxValue;
    }

    public static boolean isPipeBlock(IBlockState state)
    {
        Block block = state.getBlock();
        return block instanceof BioPipeBlock;
    }

    public static class Pipe {
        private final World world;
        private final BlockPos pos;
        private final BioPipeBlock block;
        private IBlockState state;
        private final BioPipeTileBlock pipe;
        public Pipe(World worldIn, BlockPos pos, IBlockState state)
        {
            this.world = worldIn;
            this.pos = pos;
            this.state = state;
            this.block = (BioPipeBlock)state.getBlock();
            this.pipe = (BioPipeTileBlock) worldIn.getTileEntity(pos);
        }


        
        @Nullable
        private BioPipeBlock.Pipe findPipeAt(BlockPos pos) {
            IBlockState iblockstate = this.world.getBlockState(pos);

            if (BioPipeBlock.isPipeBlock(iblockstate)) {
                return new BioPipeBlock.Pipe(this.world, pos, iblockstate);
            } else {
                return null;
            }
        }

        
        private boolean canConnectTo(BioPipeBlock.Pipe rail) {
            boolean maxDirection = this.pipe.getCountConnection() < 2;

            return maxDirection;
        }
        private void connectToPipe(BlockPos sourcePos,EnumFacing facing){
            boolean ns= facing==EnumFacing.NORTH || facing == EnumFacing.SOUTH;
            boolean we = facing == EnumFacing.EAST || facing == EnumFacing.WEST;
            boolean du = facing ==EnumFacing.DOWN || facing ==EnumFacing.UP;
            BlockPos posNorth = this.pos.north();
            BlockPos posSouth = this.pos.south();
            BlockPos posWest = this.pos.west();
            BlockPos posEast = this.pos.east();
            BlockPos posUp = this.pos.up();
            BlockPos posDown = this.pos.down();
            boolean west = false;
            boolean east = false;
            boolean north = false;
            boolean south = false;
            boolean up = false;
            boolean down = false;
            Map<EnumFacing,BlockPos> map = Techrot.main.getMapEmpty();
            map.put(facing,sourcePos);
            if(ns){
                if(isBlockValid(world,EnumFacing.SOUTH,posNorth, 6)){
                    map.put(EnumFacing.NORTH,posNorth);
                    north = true;
                }
                if(isBlockValid(world,EnumFacing.NORTH,posSouth, 6)){
                    map.put(EnumFacing.SOUTH,posSouth);
                    south = true;
                }
            }
            if(we){
                if(isBlockValid(world,EnumFacing.EAST,posWest, 6)){
                    map.put(EnumFacing.WEST,posWest);
                    west = true;
                }
                if(isBlockValid(world,EnumFacing.WEST,posEast, 6)){
                    map.put(EnumFacing.EAST,posEast);
                    east = true;
                }
            }
            if(du){
                if(isBlockValid(world,EnumFacing.UP,posDown, 6)){
                    map.put(EnumFacing.DOWN,posDown);
                    down = true;
                }
                if(isBlockValid(world,EnumFacing.DOWN,posUp, 6)){
                    map.put(EnumFacing.UP,posUp);
                    up = true;
                }
            }
            this.state = this.state.withProperty(EAST,east).withProperty(WEST,west).withProperty(NORTH,north).withProperty(SOUTH,south).withProperty(UP,up).withProperty(DOWN,down);
            this.world.setBlockState(pos,state,3);

            ((BioPipeTileBlock)world.getTileEntity(pos)).connections=map;
        }
        public void connectFacing(EnumFacing facing) {
            boolean north = this.state.getValue(NORTH);
            boolean south = this.state.getValue(SOUTH);
            boolean west = this.state.getValue(WEST);
            boolean east = this.state.getValue(EAST);
            boolean up = this.state.getValue(UP);
            boolean down = this.state.getValue(DOWN);
            Map<EnumFacing,BlockPos> map = ((BioPipeTileBlock)this.world.getTileEntity(pos)).connections;
            map.put(facing,this.pos.offset(facing));
            if(this.pipe.getCountConnection()<1){
                if(facing == EnumFacing.EAST || facing == EnumFacing.WEST){
                    west = true;
                    east = true;
                    north = false;
                    south = false;
                    up = false;
                    down = false;
                }
                if(facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH){
                    south = true;
                    north = true;
                    west = false;
                    east = false;
                    up = false;
                    down = false;
                }
                if(facing == EnumFacing.DOWN || facing == EnumFacing.UP){
                    south = false;
                    north = false;
                    west = false;
                    east = false;
                    up = true;
                    down = true;
                }
            }else {
                if(facing == EnumFacing.WEST){
                    west = true;
                }
                if(facing == EnumFacing.SOUTH){
                    south = true;
                }
                if(facing == EnumFacing.NORTH){
                    north = true;
                }
                if(facing == EnumFacing.EAST){
                    east = true;
                }
                if(facing == EnumFacing.UP){
                    up = true;
                }
                if(facing == EnumFacing.DOWN){
                    down = true;
                }
            }

            this.state = this.state.withProperty(EAST,east).withProperty(WEST,west).withProperty(NORTH,north).withProperty(SOUTH,south).withProperty(UP,up).withProperty(DOWN,down);
            this.world.setBlockState(pos,state,3);

            ((BioPipeTileBlock)world.getTileEntity(pos)).connections=map;


        }
        private void connectTo(BioPipeBlock.Pipe rail,EnumFacing facing) {
            BlockPos posNorth = this.pos.north();
            BlockPos posSouth = this.pos.south();
            BlockPos posWest = this.pos.west();
            BlockPos posEast = this.pos.east();
            BlockPos posUp = this.pos.up();
            BlockPos posDown = this.pos.down();
            BlockPos sourcePos = rail.pos;
            boolean sinConexion = this.pipe.getCountConnection()==0;
            boolean north = this.state.getValue(NORTH);
            boolean south = this.state.getValue(SOUTH);
            boolean west = this.state.getValue(WEST);
            boolean east = this.state.getValue(EAST);
            boolean up = this.state.getValue(UP);
            boolean down = this.state.getValue(DOWN);
            
            Map<EnumFacing,BlockPos> map = Techrot.main.getMapEmpty();
            EnumFacing opposite = facing.getOpposite();
            if(down){
                if(isPipeValid(world,posDown,6)){
                    map.put(EnumFacing.EAST, posDown);
                }else {
                    down = false;
                }

                if(opposite == EnumFacing.NORTH){
                    map.put(opposite,sourcePos);
                    north = true;
                }
                if(opposite == EnumFacing.SOUTH){
                    map.put(opposite,sourcePos);
                    south = true;
                }
                if(opposite == EnumFacing.WEST){
                    map.put(opposite,sourcePos);
                    west = true;
                }
                if(opposite == EnumFacing.EAST){
                    map.put(opposite,sourcePos);
                    east = true;
                }
                if(opposite == EnumFacing.UP){
                    map.put(opposite,sourcePos);
                    up = true;
                }
            } else if(up){
                if(isPipeValid(world,posUp,6)){
                    map.put(EnumFacing.EAST, posUp);
                }else {
                    up = false;
                }

                if(opposite == EnumFacing.NORTH){
                    map.put(opposite,sourcePos);
                    north = true;
                }
                if(opposite == EnumFacing.SOUTH){
                    map.put(opposite,sourcePos);
                    south = true;
                }
                if(opposite == EnumFacing.WEST){
                    map.put(opposite,sourcePos);
                    west = true;
                }
                if(opposite == EnumFacing.EAST){
                    map.put(opposite,sourcePos);
                    east = true;
                }
                if(opposite == EnumFacing.DOWN){
                    map.put(opposite,sourcePos);
                    down = true;
                }
            }else if(east){
                if(isPipeValid(world,posEast,6)){
                    map.put(EnumFacing.EAST, posEast);
                }else {
                    east = false;
                }

                if(opposite == EnumFacing.NORTH){
                    map.put(opposite,sourcePos);
                    north = true;
                }
                if(opposite == EnumFacing.SOUTH){
                    map.put(opposite,sourcePos);
                    south = true;
                }
                if(opposite == EnumFacing.WEST){
                    map.put(opposite,sourcePos);
                    west = true;
                }
                if(opposite == EnumFacing.UP){
                    map.put(opposite,sourcePos);
                    up = true;
                }
                if(opposite == EnumFacing.DOWN){
                    map.put(opposite,sourcePos);
                    down = true;
                }
            }else if(west){
                if(isPipeValid(world,posWest,6)){
                    map.put(EnumFacing.WEST, posWest);
                }else {
                    west = false;
                }
                if(opposite == EnumFacing.NORTH){
                    map.put(opposite,posNorth);
                    north = true;
                }
                if(opposite == EnumFacing.SOUTH){
                    map.put(opposite,posSouth);
                    south = true;
                }
                if(opposite == EnumFacing.EAST){
                    map.put(opposite,posEast);
                    east = true;
                }
                if(opposite == EnumFacing.UP){
                    map.put(opposite,sourcePos);
                    up = true;
                }
                if(opposite == EnumFacing.DOWN){
                    map.put(opposite,sourcePos);
                    down = true;
                }
            }else if(north){
                if(isPipeValid(world,posNorth,6)){
                    map.put(EnumFacing.NORTH, posNorth);
                }else {
                    north = false;
                }
                if(opposite == EnumFacing.EAST){
                    map.put(opposite,posEast);
                    east = true;
                }
                if(opposite == EnumFacing.SOUTH){
                    map.put(opposite,posSouth);
                    south = true;
                }
                if(opposite == EnumFacing.WEST){
                    map.put(opposite,posWest);
                    west = true;
                }
                if(opposite == EnumFacing.UP){
                    map.put(opposite,sourcePos);
                    up = true;
                }
                if(opposite == EnumFacing.DOWN){
                    map.put(opposite,sourcePos);
                    down = true;
                }
            }else if(south){
                if(isPipeValid(world,posSouth,6)){
                    map.put(EnumFacing.SOUTH, posSouth);
                }else {
                    south = false;
                }

                if(opposite == EnumFacing.NORTH){
                    map.put(opposite,posNorth);
                    north = true;
                }
                if(opposite == EnumFacing.EAST){
                    map.put(opposite,posEast);
                    east = true;
                }
                if(opposite == EnumFacing.WEST){
                    map.put(opposite,posWest);
                    west = true;
                }
                if(opposite == EnumFacing.UP){
                    map.put(opposite,sourcePos);
                    up = true;
                }
                if(opposite == EnumFacing.DOWN){
                    map.put(opposite,sourcePos);
                    down = true;
                }
            }
            if(sinConexion){

                if(facing==EnumFacing.EAST){
                    west=true;
                    map.put(EnumFacing.WEST,sourcePos);
                }
                if(facing==EnumFacing.WEST){
                    east=true;
                    map.put(EnumFacing.EAST,sourcePos);
                }
                if(facing==EnumFacing.SOUTH){
                    north=true;
                    map.put(EnumFacing.NORTH,sourcePos);
                }
                if(facing==EnumFacing.NORTH){
                    south=true;
                    map.put(EnumFacing.SOUTH,sourcePos);
                }
                if(facing == EnumFacing.UP){
                    map.put(EnumFacing.DOWN,sourcePos);
                    up = true;
                }
                if(facing == EnumFacing.DOWN){
                    map.put(EnumFacing.UP,sourcePos);
                    down = true;
                }
            }

            this.state = this.state.withProperty(EAST,east).withProperty(WEST,west).withProperty(NORTH,north).withProperty(SOUTH,south).withProperty(UP,up).withProperty(DOWN,down);
            this.world.setBlockState(pos,state,3);


            ((BioPipeTileBlock)this.world.getTileEntity(pos)).connections=map;
        }

        private boolean hasNeighborPipe(BlockPos posIn,EnumFacing facing,boolean canConnect) {
            BioPipeBlock.Pipe blockrailbase$rail = this.findPipeAt(posIn);
            if(blockrailbase$rail==null){
                return isBlockValid(world,facing,posIn, 4);
            }
            return blockrailbase$rail.pipe.getCountConnection()<2 || (hasConnect(facing,posIn) && canConnect);
        }

        private boolean hasConnect(EnumFacing facing,BlockPos pos) {
            IBlockState neighborState = world.getBlockState(pos);
            switch (facing){
                case EAST:{
                    return neighborState.getValue(EAST);
                }
                case WEST:{
                    return neighborState.getValue(WEST);
                }
                case SOUTH:{
                    return neighborState.getValue(SOUTH);
                }
                case NORTH:{
                    return neighborState.getValue(NORTH);
                }
                default:{
                    return false;
                }
            }
        }

        private boolean hasNeighborBioBlock(BlockPos posIn) {
            return world.getBlockState(posIn).getBlock() instanceof BioFurnaceBlock;
        }

        public BioPipeBlock.Pipe place(boolean canConnect, boolean initialPlacement) {
            BlockPos blockposNorth = this.pos.north();
            BlockPos blockposSouth = this.pos.south();
            BlockPos blockposWest = this.pos.west();
            BlockPos blockposEast = this.pos.east();
            BlockPos blockposUp = this.pos.up();
            BlockPos blockposDown = this.pos.down();
            boolean north = this.hasNeighborPipe(blockposNorth,EnumFacing.NORTH,canConnect);
            boolean south = this.hasNeighborPipe(blockposSouth,EnumFacing.SOUTH,canConnect);
            boolean west = this.hasNeighborPipe(blockposWest,EnumFacing.WEST,canConnect);
            boolean east = this.hasNeighborPipe(blockposEast,EnumFacing.EAST,canConnect);
            boolean up = this.hasNeighborPipe(blockposUp,EnumFacing.UP,canConnect);
            boolean down = this.hasNeighborPipe(blockposUp,EnumFacing.DOWN,canConnect);
            Map<EnumFacing,BlockPos> map = Techrot.main.getMapEmpty();

            if(east){
                map.put(EnumFacing.EAST,blockposEast);
            }

            if(west){
                map.put(EnumFacing.WEST,blockposWest);
            }
            if (north){
                map.put(EnumFacing.NORTH,blockposNorth);
            }
            if(south){
                map.put(EnumFacing.SOUTH,blockposSouth);
            }
            if(up){
                map.put(EnumFacing.UP,blockposUp);
            }
            if(down){
                map.put(EnumFacing.DOWN,blockposDown);
            }
            if(!east && !west && !north && !south && !up && !down){
                east = true;
            }

            this.state = this.state.withProperty(EAST,east).withProperty(WEST,west).withProperty(NORTH,north).withProperty(SOUTH,south).withProperty(UP,up).withProperty(DOWN,down);

            if (initialPlacement || this.world.getBlockState(this.pos) != this.state) {
                this.world.setBlockState(this.pos, this.state, 3);
                for (Map.Entry<EnumFacing,BlockPos> entry : map.entrySet()){
                    if(entry.getValue()==null)continue;
                    BioPipeBlock.Pipe pipe = this.findPipeAt(entry.getValue());
                    if(pipe!=null && pipe.canConnectTo(this)){
                        pipe.connectTo(this,entry.getKey());
                    }
                }
            }
            ((BioPipeTileBlock)world.getTileEntity(pos)).connections=map;
            return this;
        }


        public IBlockState getBlockState() {
            return this.state;
        }

        public void breakConnect(World worldIn,EnumFacing facing) {
            boolean north = this.state.getValue(NORTH);
            boolean south =this.state.getValue(SOUTH);
            boolean west = this.state.getValue(WEST);
            boolean east = this.state.getValue(EAST);
            boolean up = this.state.getValue(UP);
            boolean down = this.state.getValue(DOWN);
            if(this.pipe.getCountConnection()<2)return;
            Map<EnumFacing,BlockPos> map = this.pipe.connections;
            map.put(facing.getOpposite(),null);
            if(facing == EnumFacing.EAST){
                west = false;
            }
            if(facing == EnumFacing.NORTH){
                south = false;
            }
            if(facing == EnumFacing.SOUTH){
                north = false;
            }
            if(facing == EnumFacing.WEST){
                east = false;
            }
            if(facing == EnumFacing.DOWN){
                up = false;
            }
            if(facing == EnumFacing.UP){
                down = false;
            }
            this.state = this.state.withProperty(EAST,east).withProperty(WEST,west).withProperty(NORTH,north).withProperty(SOUTH,south).withProperty(UP,up).withProperty(DOWN,down);
            this.world.setBlockState(this.pos, this.state, 3);

            ((BioPipeTileBlock)this.world.getTileEntity(pos)).connections = map;
        }


    }
}
