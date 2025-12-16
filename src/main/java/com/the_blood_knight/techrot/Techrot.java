package com.the_blood_knight.techrot;

import com.the_blood_knight.techrot.client.layer.ImplantLayer;
import com.the_blood_knight.techrot.client.particles.ToxicFogParticle;
import com.the_blood_knight.techrot.common.TRSounds;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import com.the_blood_knight.techrot.common.capacity.TechrotPlayer;
import com.the_blood_knight.techrot.common.entity.TEntityRegister;
import com.the_blood_knight.techrot.common.proxy.CommonProxy;
import com.the_blood_knight.techrot.common.tile_block.*;
import com.the_blood_knight.techrot.common.world.WorldGenRotOre;
import com.the_blood_knight.techrot.messager.PacketHandler;
import com.the_blood_knight.techrot.messager.SyncDataPacket;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
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
import net.minecraftforge.event.ServerChatEvent;
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


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        GameRegistry.registerTileEntity(BioEggMakerTileBlock.class,new ResourceLocation(MODID,"bioeggmaker"));
        GameRegistry.registerTileEntity(BioCrafterTileBlock.class,new ResourceLocation(MODID,"biocrafter"));
        GameRegistry.registerTileEntity(BioImplanterTileBlock.class,new ResourceLocation(MODID,"bioimplanter"));
        GameRegistry.registerTileEntity(BioPipeExtractTileBlock.class,new ResourceLocation(MODID,"biopipe"));
        GameRegistry.registerTileEntity(BioPastemakerTileBlock.class,new ResourceLocation(MODID,"biopastemaker"));
        GameRegistry.registerTileEntity(BioFurnaceTileBlock.class,new ResourceLocation(MODID,"biofurnace"));
        GameRegistry.registerTileEntity(BioFleshClonerTileBlock.class,new ResourceLocation(MODID,"biofleshcloner"));
        NetworkRegistry.INSTANCE.registerGuiHandler(Techrot.this, new GuiHandler());
        GameRegistry.registerWorldGenerator(new WorldGenRotOre(), 0);
        TEntityRegister.registerEntities();
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

    public static void spawnPeste(World worldIn, BlockPos pos, Random rand, double radius) {
        for (int pass = 0; pass < 15; pass++) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(pos);

            float theta = (float) (2 * Math.PI * rand.nextFloat());
            float phi = (float) Math.acos(2 * rand.nextFloat() - 1);

            double x = radius * Math.sin(phi) * Math.cos(theta);
            double y = radius * Math.sin(phi) * Math.sin(theta);
            double z = radius * Math.cos(phi);

            mutableBlockPos.setPos(
                    pos.getX() + x,
                    pos.getY() + y,
                    pos.getZ() + z
            );

            double height = pos.getY() + 1.0D;

            Minecraft.getMinecraft().effectRenderer.addEffect(
                    new ToxicFogParticle(
                            worldIn,
                            pos.getX() + x + 0.5D,
                            height + rand.nextFloat() * 0.3D,
                            pos.getZ() + z + 0.5D,
                            0, 0, 0
                    )
            );
        }
    }




    public static void damageTick(World world,BlockPos pos,int radius){
        for (EntityLivingBase living : world.getEntities(EntityLivingBase.class, e-> e.isEntityAlive() && e.getDistance(pos.getX(),pos.getY(),pos.getZ())<radius)){
            boolean hasTechrotHead = living instanceof EntityPlayer && Util.hasTechrotHead((EntityPlayer)living);
            boolean hasTechrotChest = living instanceof EntityPlayer && Util.hasTechrotChest((EntityPlayer)living);
            if(hasTechrotChest){
                Util.getCap((EntityPlayer)living).reg((EntityPlayer)living);
            }
            if(hasTechrotHead)continue;
            living.attackEntityFrom(DamageSource.FALL,1.0F);
            living.addPotionEffect(new PotionEffect(TRegistry.TECHROT_EFFECT,60,0));
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
        public static void registerSound(RegistryEvent.Register<SoundEvent> event) throws Exception {
            try {
                TRSounds.registerSound(event);
            }
            catch(Throwable ex) {
                String message = ex.getMessage();
                throw ex;
            }
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
        public void onPlayerChat(ServerChatEvent event) {
            net.minecraft.entity.player.EntityPlayerMP player = event.getPlayer();

            player.world.playSound(
                    null,
                    player.posX,
                    player.posY,
                    player.posZ,
                    TRSounds.IMPLANTEDPLAYER_TALK,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
            );
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
        public static void onTick(TickEvent.PlayerTickEvent event) {
            if (event.side != Side.SERVER) return;

            EntityPlayer player = event.player;

            if (Util.hasTechrotChest(player)) {

                if (player.isInWater()) {
                    player.addPotionEffect(new PotionEffect(
                            MobEffects.WATER_BREATHING,
                            90,
                            0,
                            true,
                            false
                    ));
                }
            }
            ITechRotPlayer cap = player.getCapability(
                    CapabilityRegistry.PLAYER_UPGRADES,
                    null
            );
            if (cap != null) {
                cap.tick(player);
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

        @SubscribeEvent
        public static void onPotionApplicable(net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event) {
            if (!(event.getEntityLiving() instanceof EntityPlayer)) {
                return;
            }

            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            if (event.getPotionEffect().getPotion() == MobEffects.BLINDNESS) {

                if (Util.hasTechrotHead(player)) {
                    event.setResult(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);
                }
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
