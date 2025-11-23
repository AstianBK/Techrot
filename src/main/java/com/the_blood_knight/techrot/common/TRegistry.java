package com.the_blood_knight.techrot.common;

import com.the_blood_knight.techrot.common.api.IRegisterable;
import com.the_blood_knight.techrot.common.block.BioFurnaceBlock;
import com.the_blood_knight.techrot.common.block.BioPastemakerBlock;
import com.the_blood_knight.techrot.common.block.BioPipeBlock;
import com.the_blood_knight.techrot.common.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

public class TRegistry {
    public static final ArrayList<IRegisterable> ITEMS = new ArrayList<>();

    public static final ArrayList<BlockBase> BLOCKS = new ArrayList<BlockBase>();
    public static final BlockBase BIOFURNACE = new BioFurnaceBlock(false,Material.ROCK,"biofurnace");

    public static final BlockBase LIT_BIOFURNACE = new BioFurnaceBlock(true,Material.ROCK,"lit_biofurnace");
    public static final BlockBase BIOPASTEMAKER = new BioPastemakerBlock(Material.CACTUS,"biopastemaker");
    public static final BlockBase BIOPIPE = new BioPipeBlock(Material.CACTUS,"biopipe");

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        for(BlockBase block : BLOCKS) {
            registry.register(block);
        }
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for(BlockBase block : BLOCKS) {
            registry.register(block.createItemBlock());
        }
    }

    public static void registerModels() {
        for(BlockBase block : BLOCKS) {
            block.registerItemModel(Item.getItemFromBlock(block));
        }
        /*for(IRegisterable item : ITEMS) {
            item.registerItemModel();
        }*/
    }


    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll();
    }




}
