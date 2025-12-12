package com.the_blood_knight.techrot;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.the_blood_knight.techrot.client.layer.ImplantLayer;
import com.the_blood_knight.techrot.client.particles.ToxicFogParticle;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import com.the_blood_knight.techrot.common.capacity.TechrotPlayer;
import com.the_blood_knight.techrot.common.proxy.CommonProxy;
import com.the_blood_knight.techrot.common.recipes.BioCrafterRecipe;
import com.the_blood_knight.techrot.common.tile_block.*;
import com.the_blood_knight.techrot.messager.PacketHandler;
import com.the_blood_knight.techrot.messager.SyncDataPacket;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.util.*;

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
    public static final List<BioCrafterRecipe> RECIPES = new ArrayList<>();
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        GameRegistry.registerTileEntity(BioEggMakerTileBlock.class,new ResourceLocation(MODID,"bioeggmaker"));
        GameRegistry.registerTileEntity(BioCrafterTileBlock.class,new ResourceLocation(MODID,"biocrafter"));
        GameRegistry.registerTileEntity(BioImplanterTileBlock.class,new ResourceLocation(MODID,"bioimplanter"));
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

    public static void spawnPeste(World worldIn , BlockPos pos, Random rand,int radius){
        for (int pass = 0; pass < 15; pass++) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(pos);
            float theta = (float) (2 * Math.PI * rand.nextFloat());
            float phi = (float) Math.acos(2 * rand.nextFloat() - 1);
            double x = radius * Math.sin(phi) * Math.cos(theta);
            double y = radius * Math.sin(phi) * Math.sin(theta);
            double z = radius * Math.cos(phi);

            mutableBlockPos.setPos(x + pos.getX(), y + pos.getY(), z + pos.getZ());
            if (worldIn.getHeight(mutableBlockPos.getX(), mutableBlockPos.getZ()) > pos.getY())
                continue;
            double height = worldIn.getHeight(mutableBlockPos.getX(),mutableBlockPos.getZ());
            Minecraft.getMinecraft().effectRenderer.addEffect(new ToxicFogParticle(worldIn, mutableBlockPos.getX(), height + rand.nextFloat(), mutableBlockPos.getZ(), 0, 0, 0));
        }
    }



    public static void damageTick(World world,BlockPos pos,int radius){
        for (EntityLivingBase living : world.getEntities(EntityLivingBase.class, e-> e.isEntityAlive() && e.getDistance(pos.getX(),pos.getY(),pos.getZ())<radius)){
            living.attackEntityFrom(DamageSource.FALL,1.0F);
            living.addPotionEffect(new PotionEffect(MobEffects.POISON,100,0));
        }
    }
    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        CapabilityRegistry.register();
        PacketHandler.registerNetwork();
    }
    @Mod.EventBusSubscriber
    public static class RegistrationHandler{
        private static boolean layerAdded = false;

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            TRegistry.registerBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerPotion(RegistryEvent.Register<Potion> event) throws Exception {
            try {
                TRegistry.registerMobEffect(event);
            }
            catch(Throwable ex) {
                String message = ex.getMessage();
                throw ex;
            }
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
        @SubscribeEvent
        public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof EntityPlayer) {
                event.addCapability(
                        CapabilityRegistry.PLAYER_UPGRADES_ID,new TechrotPlayer.TechrotPlayerProvider());
            }
        }
        @SubscribeEvent
        public static void onHeal(LivingHealEvent event) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                ITechRotPlayer cap = player.getCapability(CapabilityRegistry.PLAYER_UPGRADES,null);
                if(cap!=null){
                    if (player.getHealth()<=cap.getHeartRot()) {
                        event.setCanceled(true);
                    }
                }
            }
        }
        @SubscribeEvent
        public static void onTick(TickEvent.PlayerTickEvent event){
            if(event.side == Side.SERVER){
                ITechRotPlayer cap = event.player.getCapability(
                        CapabilityRegistry.PLAYER_UPGRADES,
                        null
                );
                if(cap!=null){
                    cap.tick(event.player);
                }

            }
        }
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
            if (!layerAdded) {
                event.getRenderer().addLayer(new ImplantLayer<>(event.getRenderer()));
                layerAdded = true;
            }
        }



        @SubscribeEvent
        public static void onPlayerJoin(EntityJoinWorldEvent event) {
            if (event.getEntity() instanceof EntityPlayerMP) {
                EntityPlayerMP mp = (EntityPlayerMP) event.getEntity();
                ITechRotPlayer cap = mp.getCapability(CapabilityRegistry.PLAYER_UPGRADES, null);

                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("Inv", cap.getInventory().serializeNBT());

                PacketHandler.sendTo(new SyncDataPacket(tag), mp);
            }
        }
    }
    @Mod.EventBusSubscriber
    public static class CapabilityRegistry {

        public static final ResourceLocation PLAYER_UPGRADES_ID =
                new ResourceLocation("techrot", "player_upgrades");

        @CapabilityInject(ITechRotPlayer.class)
        public static Capability<ITechRotPlayer> PLAYER_UPGRADES = null;

        public static void register() {
            CapabilityManager.INSTANCE.register(
                    ITechRotPlayer.class,
                    new TechrotPlayer.TechrotPlayerStorage(),
                    TechrotPlayer::new
            );
        }
    }



}
