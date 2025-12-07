package com.the_blood_knight.techrot;

import com.the_blood_knight.techrot.client.particles.BioGasParticle;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.proxy.CommonProxy;
import com.the_blood_knight.techrot.common.tile_block.BioFleshClonerTileBlock;
import com.the_blood_knight.techrot.common.tile_block.BioFurnaceTileBlock;
import com.the_blood_knight.techrot.common.tile_block.BioPastemakerTileBlock;
import com.the_blood_knight.techrot.common.tile_block.BioPipeTileBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = "techrot", name = Techrot.NAME, version = Techrot.VERSION)
public class Techrot
{
    public static final String MODID = "techrot";
    public static final String NAME = "Techrot";
    public static final String VERSION = "1.0";
    @SidedProxy(serverSide = "com.the_blood_knight.techrot.common.proxy.CommonProxy", clientSide = "com.the_blood_knight.techrot.common.proxy.ClientProxy")
    public static CommonProxy proxy;
    public static Logger logger;
    @Mod.Instance
    public static Techrot main;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        GameRegistry.registerTileEntity(BioPipeTileBlock.class,new ResourceLocation(MODID,"biopipe"));
        GameRegistry.registerTileEntity(BioPastemakerTileBlock.class,new ResourceLocation(MODID,"biopastemaker"));
        GameRegistry.registerTileEntity(BioFurnaceTileBlock.class,new ResourceLocation(MODID,"biofurnace"));
        GameRegistry.registerTileEntity(BioFleshClonerTileBlock.class,new ResourceLocation(MODID,"biofleshcloner"));

        NetworkRegistry.INSTANCE.registerGuiHandler(Techrot.this, new GuiHandler());
    }
    public Map<EnumFacing, BlockPos> getMapEmpty(){
        Map<EnumFacing,BlockPos> map = new HashMap<>();
        map.put(EnumFacing.DOWN,null);
        map.put(EnumFacing.UP,null);
        map.put(EnumFacing.WEST,null);
        map.put(EnumFacing.EAST,null);
        map.put(EnumFacing.SOUTH,null);
        map.put(EnumFacing.NORTH,null);
        return map;
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
    @Mod.EventBusSubscriber
    public static class RegistrationHandler{

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            TRegistry.registerBlocks(event.getRegistry());
        }


        @SubscribeEvent
        public static void registerItem(RegistryEvent.Register<Item> event) throws Exception {
            try {
                IForgeRegistry<Item> registry = event.getRegistry();
                TRegistry.registerItems(registry);
                TRegistry.registerItemBlocks(registry);
            }
            catch(Throwable ex) {
                String message = ex.getMessage();
                throw ex;
            }
        }

        @SubscribeEvent
        public static void registerItems(ModelRegistryEvent event) {
            TRegistry.registerModels();
        }
    }
}
