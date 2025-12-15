package com.the_blood_knight.techrot.common.item;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ICreativeTabbable;
import com.the_blood_knight.techrot.common.api.IRegisterable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ImplantItemBase extends Item implements IRegisterable, ICreativeTabbable {

    private String name;
    private int decayLevel;

    public ImplantItemBase(String name, int decayLevel) {
        this.name = name;
        this.decayLevel = decayLevel;
        this.setMaxStackSize(1);
        this.setCreativeTab(TRegistry.TECHROT_TAB);
        updateRegistryAndLocalizedName(name);
    }

    public int getDecayLevel() {
        return decayLevel;
    }

    @Override
    public void registerItemModel() {
        Techrot.proxy.registerItemRenderer(this, 0, name);
    }


    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return net.minecraft.util.text.TextFormatting.DARK_GREEN
                + super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn,
                               List<String> tooltip, ITooltipFlag flagIn) {

        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextFormatting.ITALIC + "Bio-Implant");

        String key = this.getTranslationKey(stack) + ".tooltip";
        if (net.minecraft.client.resources.I18n.hasKey(key)) {
            tooltip.add(
                    net.minecraft.util.text.TextFormatting.GRAY +
                            net.minecraft.client.resources.I18n.format(key)
            );
        }

        tooltip.add(
                net.minecraft.util.text.TextFormatting.DARK_RED +
                        "Decay Level: " + decayLevel
        );
    }
    @Override
    public void updateRegistryAndLocalizedName(String name) {
        this.setTranslationKey(Techrot.MODID + "." + name);
        this.setRegistryName(name);
        TRegistry.ITEMS.add(this);
    }


}

