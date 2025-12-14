package com.the_blood_knight.techrot.common.item;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ICreativeTabbable;
import com.the_blood_knight.techrot.common.api.IRegisterable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ImplantItemBase extends Item implements IRegisterable, ICreativeTabbable {
    protected String name;

    public ImplantItemBase(String name) {
        this.name = name;
        this.setCreativeTab(TRegistry.TECHROT_TAB);

        updateRegistryAndLocalizedName(name);
    }
    public ImplantItemBase(ToolMaterial material, String name) {
        this.name = name;
    }
    public void registerItemModel() {
        Techrot.proxy.registerItemRenderer(this, 0, name);
    }


    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return net.minecraft.util.text.TextFormatting.DARK_GREEN
                + super.getItemStackDisplayName(stack);
    }

    public ImplantItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    public void updateRegistryAndLocalizedName(String name) {
        setTranslationKey(name);

        setRegistryName(name);

        TRegistry.ITEMS.add(this);
    }
}