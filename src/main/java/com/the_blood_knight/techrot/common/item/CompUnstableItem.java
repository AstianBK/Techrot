package com.the_blood_knight.techrot.common.item;

        import com.the_blood_knight.techrot.common.entity.ToxicFogEntity;
        import net.minecraft.client.util.ITooltipFlag;
        import net.minecraft.entity.Entity;
        import net.minecraft.entity.player.EntityPlayer;
        import net.minecraft.item.ItemStack;
        import net.minecraft.nbt.NBTTagCompound;
        import net.minecraft.util.text.TextFormatting;
        import net.minecraft.world.World;
        import net.minecraftforge.fml.relauncher.Side;
        import net.minecraftforge.fml.relauncher.SideOnly;
        import javax.annotation.Nullable;
        import java.util.List;

public class CompUnstableItem extends ItemBase {

    private static final int DETONATION_TICKS = 130;

    public CompUnstableItem(String name) {
        super(name);
        this.setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        if (world.isRemote) return;
        if (!(entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) entity;

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            tag.setInteger("Fuse", 0);
            stack.setTagCompound(tag);
        }

        int fuse = tag.getInteger("Fuse") + 1;
        tag.setInteger("Fuse", fuse);

        if (fuse >= DETONATION_TICKS) {
            detonate(world, player);
            stack.shrink(1);
        }
    }

    private void detonate(World world, EntityPlayer player) {
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;

        world.createExplosion(null, x, y, z, 4.0F, true);

        ToxicFogEntity fog = new ToxicFogEntity(world, x, y, z, player);
        fog.setRadius(6.0F);
        fog.setDuration(300);
        world.spawnEntity(fog);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextFormatting.DARK_RED + "Highly Unstable: will detonate violently after 7 seconds.");
    }

}
