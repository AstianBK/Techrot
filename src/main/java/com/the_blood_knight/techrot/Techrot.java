package com.the_blood_knight.techrot;

import com.the_blood_knight.techrot.client.particles.BioGasParticle;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.proxy.CommonProxy;
import com.the_blood_knight.techrot.common.tile_block.BioFurnaceTileBlock;
import com.the_blood_knight.techrot.common.tile_block.BioPastemakerTileBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
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


        GameRegistry.registerTileEntity(BioPastemakerTileBlock.class,new ResourceLocation(MODID,"biopastemaker"));
        GameRegistry.registerTileEntity(BioFurnaceTileBlock.class,new ResourceLocation(MODID,"biofurnace"));
        NetworkRegistry.INSTANCE.registerGuiHandler(Techrot.this, new GuiHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
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
                //TRegistry.registerItems(registry);
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
