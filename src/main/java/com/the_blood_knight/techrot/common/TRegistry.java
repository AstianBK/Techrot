package com.the_blood_knight.techrot.common;

import com.the_blood_knight.techrot.common.api.IRegisterable;
import com.the_blood_knight.techrot.common.block.*;
import com.the_blood_knight.techrot.common.item.BioExtractorItem;
import com.the_blood_knight.techrot.common.item.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

public class TRegistry {
    public static final CreativeTabs TECHROT_TAB = new CreativeTabs("techrot_tab") {

        @Override
        public ItemStack createIcon() {
            return new ItemStack(TRegistry.BIO_EXTRACTOR); // ícono del tab
        }
    };
    public static final ArrayList<IRegisterable> ITEMS = new ArrayList<>();

    public static final ArrayList<BlockBase> BLOCKS = new ArrayList<BlockBase>();
    public static final BioExtractorItem BIO_EXTRACTOR = new BioExtractorItem("bio_extractor");
    public static final ItemBase BIO_CUBE = new ItemBase("bio_cube");
    public static final ItemBase BIO_WRENCH = new ItemBase("bio_wrench");

    public static final BlockBase BIOFURNACE = new BioFurnaceBlock(false,Material.ROCK,"biofurnace");

    public static final BlockBase LIT_BIOFURNACE = new BioFurnaceBlock(true,Material.ROCK,"lit_biofurnace");
    public static final BlockBase BIOPASTEMAKER = new BioPastemakerBlock(Material.CACTUS,"biopastemaker");
    public static final BlockBase BIOPIPE = new BioPipeBlock(Material.CACTUS,"biopipe");
    public static final BlockBase BIOFLESHCLONER = new BioFleshClonerBlock(Material.CACTUS,"biofleshcloner");
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
        for(IRegisterable item : ITEMS) {
            item.registerItemModel();
        }
    }


    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(BIO_EXTRACTOR,BIO_WRENCH,BIO_CUBE);
    }




}
