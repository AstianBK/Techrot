package com.the_blood_knight.techrot;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.the_blood_knight.techrot.client.particles.BioGasParticle;
import com.the_blood_knight.techrot.client.particles.ToxicFogParticle;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.proxy.CommonProxy;
import com.the_blood_knight.techrot.common.recipes.BioCrafterRecipe;
import com.the_blood_knight.techrot.common.tile_block.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
    public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting craftMatrix, World worldIn)
    {
        for (IRecipe irecipe : RECIPES)
        {
            if (irecipe.matches(craftMatrix, worldIn))
            {
                return irecipe.getRemainingItems(craftMatrix);
            }
        }

        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(craftMatrix.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            nonnulllist.set(i, craftMatrix.getStackInSlot(i));
        }

        return nonnulllist;
    }
    public static void damageTick(World world,BlockPos pos,int radius){
        for (EntityLiving living : world.getEntities(EntityLiving.class, e-> e.isEntityAlive() && e.getDistance(pos.getX(),pos.getY(),pos.getZ())<radius)){
            living.attackEntityFrom(DamageSource.FALL,1.0F);
            living.addPotionEffect(new PotionEffect(MobEffects.POISON,100,0));
        }
    }
    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        loadAll();

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
    public static void loadAll() {
        try {
            File folder = new File( "C:/Users/Usuario/Desktop/Proyectos/In working progress/techrot/src/main/resources/assets/techrot/biocrafter_recipes");
            Techrot.logger.info("Load all Recipes "+folder);
            if (!folder.exists()) return;

            File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
            if (files == null) return;
            for (File f : files) {
                loadRecipe(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadRecipe(File file) throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(new FileReader(file)).getAsJsonObject();

        BioCrafterRecipe recipe = deserialize(json);
        addRecipe(recipe);

        System.out.println("Loaded BioFurnace recipe: " + file.getName());
    }

    public static void addRecipe(BioCrafterRecipe recipe) {
        RECIPES.add(recipe);
    }

    @Nullable
    public static BioCrafterRecipe getMatch(InventoryCrafting inv, World world) {
        for (BioCrafterRecipe r : RECIPES) {
            if (r.matches(inv,world))
                return r;
        }
        return null;
    }

    public static BioCrafterRecipe deserialize(JsonObject json)
    {
        Map<String, Ingredient> map = deserializeKey(JsonUtils.getJsonObject(json, "key"));
        String[] astring = shrink(patternFromJson(JsonUtils.getJsonArray(json, "pattern")));
        int i = astring[0].length();
        int j = astring.length;
        int nutrient = json.has("need_nutrient") ? json.get("need_nutrient").getAsInt() : 0;
        NonNullList<Ingredient> nonnulllist = deserializeIngredients(astring, map, i, j);
        ItemStack itemstack = ShapedRecipes.deserializeItem(JsonUtils.getJsonObject(json, "result"), true);
        return new BioCrafterRecipe(nonnulllist, itemstack,nutrient);
    }

    private static Map<String, Ingredient> deserializeKey(JsonObject json)
    {
        Map<String, Ingredient> map = Maps.<String, Ingredient>newHashMap();

        for (Map.Entry<String, JsonElement> entry : json.entrySet())
        {
            if (((String)entry.getKey()).length() != 1)
            {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey()))
            {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), ShapedRecipes.deserializeIngredient(entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }
    private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight)
    {
        NonNullList<Ingredient> nonnulllist = NonNullList.<Ingredient>withSize(patternWidth * patternHeight, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(keys.keySet());
        set.remove(" ");

        for (int i = 0; i < pattern.length; ++i)
        {
            for (int j = 0; j < pattern[i].length(); ++j)
            {
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = keys.get(s);

                if (ingredient == null)
                {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + patternWidth * i, ingredient);
            }
        }

        if (!set.isEmpty())
        {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        }
        else
        {
            return nonnulllist;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... toShrink)
    {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < toShrink.length; ++i1)
        {
            String s = toShrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);

            if (j1 < 0)
            {
                if (k == i1)
                {
                    ++k;
                }

                ++l;
            }
            else
            {
                l = 0;
            }
        }

        if (toShrink.length == l)
        {
            return new String[0];
        }
        else
        {
            String[] astring = new String[toShrink.length - l - k];

            for (int k1 = 0; k1 < astring.length; ++k1)
            {
                astring[k1] = toShrink[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    private static int firstNonSpace(String str)
    {
        int i;

        for (i = 0; i < str.length() && str.charAt(i) == ' '; ++i)
        {
            ;
        }

        return i;
    }

    private static int lastNonSpace(String str)
    {
        int i;

        for (i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i)
        {
            ;
        }

        return i;
    }

    private static String[] patternFromJson(JsonArray jsonArr)
    {
        String[] astring = new String[jsonArr.size()];

        if (astring.length > 3)
        {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        }
        else if (astring.length == 0)
        {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        }
        else
        {
            for (int i = 0; i < astring.length; ++i)
            {
                String s = JsonUtils.getString(jsonArr.get(i), "pattern[" + i + "]");

                if (s.length() > 3)
                {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }

                if (i > 0 && astring[0].length() != s.length())
                {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

}
