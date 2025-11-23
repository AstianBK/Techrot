package com.the_blood_knight.techrot.common.api;

public interface IRegisterable<T> {
	void registerItemModel();
	void updateRegistryAndLocalizedName(String name);
}