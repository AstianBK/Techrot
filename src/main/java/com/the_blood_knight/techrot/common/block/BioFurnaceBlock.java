package com.the_blood_knight.techrot.common.block;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.client.particles.BioGasParticle;
import com.the_blood_knight.techrot.client.particles.ToxicFogParticle;
import com.the_blood_knight.techrot.common.TRSounds;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.item.ToxicLaucheritem;
import com.the_blood_knight.techrot.common.tile_block.BioFurnaceTileBlock;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BioFurnaceBlock extends BlockTileBase {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private final boolean isBurning;
    private static boolean keepInventory;
    public BioFurnaceBlock(boolean isBurning,Material material,String name) {
        super(material,name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.isBurning = isBurning;
        if(isBurning){
            this.setLightLevel(0.875F);
        }
    }


    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(TRegistry.BIOFURNACE);
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
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.north());
            IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
            IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
            IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.isBurning) {
            Techrot.spawnPeste(worldIn,pos,rand,4);
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            IBlockState current = worldIn.getBlockState(pos);
            if(current.getBlock() == TRegistry.LIT_BIOFURNACE){
                ItemStack stack = playerIn.getHeldItem(hand);
                if(stack.getItem()==TRegistry.TOXIC_CANISTER_EMPTY){
                    ItemStack bullet = ToxicLaucheritem.getBullet(playerIn);
                    if(!bullet.isEmpty() && bullet.getCount()<63){
                        bullet.grow(1);
                        stack.shrink(1);
                        return true;

                    }else {
                        if(!playerIn.inventory.add(playerIn.inventory.getFirstEmptyStack(),new ItemStack(TRegistry.TOXIC_CANISTER))){
                            playerIn.dropItem(new ItemStack(TRegistry.TOXIC_CANISTER),false);
                        }

                        stack.shrink(1);
                        return true;

                    }

                }
            }
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof BioFurnaceTileBlock) {

                worldIn.playSound(
                        null,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        TRSounds.BLOCK_INTERACT,
                        SoundCategory.BLOCKS,
                        1.0F,
                        1.0F
                );

                playerIn.openGui(
                        Techrot.main,
                        0,
                        worldIn,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ()
                );

                playerIn.addStat(StatList.FURNACE_INTERACTION);
            }


            return true;
        }
    }

    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;

        if (active)
        {
            worldIn.setBlockState(pos, TRegistry.LIT_BIOFURNACE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, TRegistry.LIT_BIOFURNACE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }
        else
        {
            worldIn.setBlockState(pos, TRegistry.BIOFURNACE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, TRegistry.BIOFURNACE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }

        keepInventory = false;

        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }


    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new BioFurnaceTileBlock();
    }


    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }


    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof BioFurnaceTileBlock)
            {
                ((BioFurnaceTileBlock)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }


    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!keepInventory)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof BioFurnaceTileBlock)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (BioFurnaceTileBlock)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }


    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }


    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Blocks.FURNACE);
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
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
