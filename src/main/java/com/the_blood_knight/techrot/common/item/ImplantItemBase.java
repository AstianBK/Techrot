package com.the_blood_knight.techrot.common.item;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ICreativeTabbable;
import com.the_blood_knight.techrot.common.api.IRegisterable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

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
        setTranslationKey(Techrot.MODID + "." + name);

        setRegistryName(name);

        TRegistry.ITEMS.add(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        String key = this.getTranslationKey(stack) + ".tooltip";

        if (net.minecraft.client.resources.I18n.hasKey(key)) {
            tooltip.add(net.minecraft.util.text.TextFormatting.GRAY +
                    net.minecraft.client.resources.I18n.format(key));
        }
    }



}