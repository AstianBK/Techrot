package com.the_blood_knight.techrot.common.proxy;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.client.particles.BioGasParticle;
import com.the_blood_knight.techrot.client.particles.ToxicFogParticle;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.item.BioExtractorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy{
	@Override
	public void registerItemRenderer(Item item, int metadataValue, String itemId) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Techrot.MODID + ":" + itemId,"inventory"));

	}
	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(new ResourceLocation("techrot:particles/bio_gas"));
		event.getMap().registerSprite(new ResourceLocation("techrot:particles/toxic_fog"));

	}
	@Override
	public void init() {
		super.init();
		TRegistry.BIO_EXTRACTOR.addPropertyOverride(
				new ResourceLocation("fill"),
				new IItemPropertyGetter() {
					@Override
					@SideOnly(Side.CLIENT)
					public float apply(ItemStack stack, World world, EntityLivingBase entity) {
						float id = stack.hasTagCompound() && !BioExtractorItem.getADN(stack).equals("none") ? 1.0F : 0.0F;
						return id;
					}
				}
		);
		Minecraft.getMinecraft().effectRenderer.registerParticle(
				49,
				new ToxicFogParticle.Factory()
		);
		Minecraft.getMinecraft().effectRenderer.registerParticle(
				50,
				new BioGasParticle.Factory()
		);

	}
}