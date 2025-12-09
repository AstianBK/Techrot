package com.the_blood_knight.techrot;

import com.the_blood_knight.techrot.client.screen.*;
import com.the_blood_knight.techrot.common.block.BioFurnaceBlock;
import com.the_blood_knight.techrot.common.container.*;
import com.the_blood_knight.techrot.common.tile_block.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int BIOPIPE = 0;
    public static final int BIOPASTEMAKER = 1;
    public static final int BIOFLESHCLONER = 2;
    public static final int BIOEGGMAKER = 3;
    public static final int BIOCRAFTER = 4;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if(id == 0){
            return new BioFurnaceContainer(player.inventory, ((BioFurnaceTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        if(id == 1){
            return new BioPastemakerContainer(player.inventory,((BioPastemakerTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        if(id == 2){
            return new BioFleshClonerContainer(player.inventory,((BioFleshClonerTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        if(id == 3){
            return new BioEggMakerContainer(player.inventory,((BioEggMakerTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        if(id == 4){
            return new BioCrafterContainer(player.inventory,((BioCrafterTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if(id == 0){
            return new BioFurnaceScreen(player.inventory, ((BioFurnaceTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        if(id == 1){
            return new BioPastemakerScreen(player.inventory, ((BioPastemakerTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        if(id == 2){
            return new BioFleshClonerScreen(player.inventory, ((BioFleshClonerTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        if(id == 3){
            return new BioEggMakerScreen(player.inventory, ((BioEggMakerTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        if(id == 4){
            return new BioCrafterScreen(player.inventory, ((BioCrafterTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        return null;
    }
}
