package com.the_blood_knight.techrot.common.proxy;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.client.particles.BioGasParticle;
import com.the_blood_knight.techrot.client.particles.ToxicFogParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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