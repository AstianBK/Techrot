package com.the_blood_knight.techrot;

import com.the_blood_knight.techrot.client.screen.BioFurnaceScreen;
import com.the_blood_knight.techrot.client.screen.BioPastemakerScreen;
import com.the_blood_knight.techrot.common.block.BioFurnaceBlock;
import com.the_blood_knight.techrot.common.container.BioPastemakerContainer;
import com.the_blood_knight.techrot.common.tile_block.BioFurnaceTileBlock;
import com.the_blood_knight.techrot.common.tile_block.BioPastemakerTileBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int BIOPIPE = 0;
    public static final int BIOPASTEMAKER = 1;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if(id == 0){
            return new ContainerFurnace(player.inventory, ((BioFurnaceTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
        }
        if(id == 1){
            return new BioPastemakerContainer(player.inventory,((BioPastemakerTileBlock)world.getTileEntity(new BlockPos(x, y, z))));
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
        return null;
    }
}
