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
            return new ItemStack(TRegistry.BIO_CUBE); // ícono del tab
        }
    };
    public static final ArrayList<IRegisterable> ITEMS = new ArrayList<>();

    public static final ArrayList<BlockBase> BLOCKS = new ArrayList<BlockBase>();

    //DECORATIVE-BLOCKS

    public static final BlockBase BIOCUBE_BLOCK = new BlockBase(Material.IRON,"biocube_block");

    public static final BlockBase ROTPLATE_BLOCK = new BlockBase(Material.IRON,"rotplate_block");

    public static final BlockBase ROTPLATE_SECTIONED_BLOCK = new BlockBase(Material.IRON,"rotplate_sectioned_block");

    public static final BlockBase ROTPLATE_PATTERN_BLOCK = new BlockBase(Material.IRON,"rotplate_pattern_block");

    public static final BlockBase ROTPLATE_SOLID_BLOCK = new BlockBase(Material.IRON,"rotplate_solid_block");

    public static final BlockBase ROTPLATE_ORE = new BlockBase(Material.IRON,"rotplate_ore");

    //FUNCTIONAL-BLOCKS

    public static final BlockBase BIOIMPLANTER = new BioImplanterBlock(Material.ROCK,"bioimplanter",false);
    public static final BlockBase BIOFURNACE = new BioFurnaceBlock(false,Material.ROCK,"biofurnace");
    public static final BlockBase LIT_BIOFURNACE = new BioFurnaceBlock(true,Material.ROCK,"lit_biofurnace");
    public static final BlockBase BIOPASTEMAKER = new BioPastemakerBlock(Material.CACTUS,"biopastemaker",false);
    public static final BlockBase LIT_BIOPASTEMAKER = new BioPastemakerBlock(Material.CACTUS,"lit_biopastemaker",true);

    public static final BlockBase BIOPIPE = new BioPipeBlock(Material.CACTUS,"biopipe");
    public static final BlockBase BIOEGGMAKER = new BioEggMakerBlock(Material.CACTUS,"bioeggmaker",false);
    public static final BlockBase LIT_BIOEGGMAKER = new BioEggMakerBlock(Material.CACTUS,"lit_bioeggmaker",true);

    public static final BlockBase BIOCRAFTER = new BioCrafterBlock(Material.CACTUS,"biocrafter");
    public static final BlockBase BIOFLESHCLONER = new BioFleshClonerBlock(Material.CACTUS,"biofleshcloner",false);
    public static final BlockBase LIT_BIOFLESHCLONER = new BioFleshClonerBlock(Material.CACTUS,"lit_biofleshcloner",true);

    //ITEMS
    public static final BioExtractorItem BIO_EXTRACTOR = new BioExtractorItem("bio_extractor");
    public static final ItemBase BIO_CUBE = new ItemBase("bio_cube");
    public static final ItemBase BIO_CHUNK = new ItemBase("bio_chunk");
    public static final ItemBase ROTPLATE = new ItemBase("rotplate");
    public static final ItemBase BIO_WRENCH = new ItemBase("bio_wrench");
    public static final ItemBase ROTPLATE_ARM = new ItemBase("rotplate_arm");
    public static final ItemBase ROTPLATE_CHEST = new ItemBase("rotplate_chest");
    public static final ItemBase ROTPLATE_HEAD = new ItemBase("rotplate_head");

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
        registry.registerAll(BIO_EXTRACTOR,BIO_WRENCH,BIO_CUBE,BIO_CHUNK,ROTPLATE,ROTPLATE_HEAD,ROTPLATE_CHEST,ROTPLATE_ARM);
    }




}
