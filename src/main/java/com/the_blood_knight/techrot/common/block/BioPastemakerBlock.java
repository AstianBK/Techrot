package com.the_blood_knight.techrot.common.block;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRSounds;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.tile_block.BioFurnaceTileBlock;
import com.the_blood_knight.techrot.common.tile_block.BioPastemakerTileBlock;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BioPastemakerBlock extends BlockTileBase{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static boolean keepInventory;
    private final boolean active;
    public BioPastemakerBlock(Material material, String name,boolean active) {
        super(material, name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.active = active;
    }
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.active) {
            worldIn.playSound(null,pos.getX(),pos.getY(),pos.getZ(), TRSounds.IMPLANTEDPLAYER_BREATHE,SoundCategory.PLAYERS,1.0F,1.0F);

            Techrot.spawnPeste(worldIn,pos,rand,7);
        }
    }
    public static void setState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;

        if (active)
        {
            worldIn.setBlockState(pos, TRegistry.LIT_BIOPASTEMAKER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, TRegistry.LIT_BIOPASTEMAKER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }
        else
        {
            worldIn.setBlockState(pos, TRegistry.BIOPASTEMAKER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, TRegistry.BIOPASTEMAKER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }

        keepInventory = false;

        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);


            if (tileentity instanceof BioPastemakerTileBlock) {
                playerIn.openGui(Techrot.main,1,worldIn,pos.getX(),pos.getY(),pos.getZ());
                //playerIn.addStat(StatList.FURNACE_INTERACTION);
            }

            return true;
        }
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new BioPastemakerTileBlock();
    }


    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }


    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName())
        {

        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {


        super.breakBlock(worldIn, pos, state);
    }
    public boolean validConnectPipe(IBlockState state,EnumFacing facing){
        switch (state.getValue(FACING)){
            case EAST:{
                return facing==EnumFacing.WEST || facing==EnumFacing.SOUTH || facing==EnumFacing.NORTH;
            }
            case WEST:{
                return facing==EnumFacing.EAST   || facing==EnumFacing.SOUTH || facing==EnumFacing.NORTH;

            }
            case SOUTH:{
                return facing==EnumFacing.WEST || facing==EnumFacing.EAST || facing==EnumFacing.NORTH;

            }
            case NORTH:{
                return facing==EnumFacing.WEST || facing==EnumFacing.EAST || facing==EnumFacing.SOUTH;

            }
            default:{
                return false;
            }
        }
    }

    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }


    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(TRegistry.BIOPASTEMAKER);
    }


    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }


    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }


    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }


    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }


    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

}
