package com.the_blood_knight.techrot.common.block;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;


public class BlockBase extends Block {

	protected String name;

	public BlockBase(Material material) {
		super(material);
	}
	public BlockBase(Material material, String name) {
		super(material);

		setTranslationKey(name);

		setRegistryName(name);

		this.name = name;
		this.setCreativeTab(TRegistry.TECHROT_TAB);

		TRegistry.BLOCKS.add(this);
	}
	public void registerItemModel(Item itemBlock) {
		Techrot.proxy.registerItemRenderer(itemBlock, 0, name);
	}
	
	public Item createItemBlock() {
		ItemBlock itemBlock = new ItemBlock(this);
		itemBlock.setRegistryName(getRegistryName());

		return itemBlock;
	}



	@Override
	public BlockBase setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

}