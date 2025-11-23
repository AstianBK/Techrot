package com.the_blood_knight.techrot.common.api;

import net.minecraft.creativetab.CreativeTabs;

public interface ICreativeTabbable<T> {
	T setCreativeTab(CreativeTabs tab);
}