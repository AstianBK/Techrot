package com.the_blood_knight.techrot.common.mixin;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.TRegistry;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin {
    @Shadow public abstract ModelPlayer getMainModel();

    @Inject(method = "setModelVisibilities", at = @At("TAIL"))
    private void injectVisibility(AbstractClientPlayer player, CallbackInfo ci) {
        ITechRotPlayer cap = player.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES,null);
        if(cap!=null){
            for (int i = 0 ; i < cap.getInventory().getSlots() ; i++){
                ItemStack stack = cap.getInventory().getStackInSlot(i);
                if(stack.getItem() == TRegistry.ROTPLATE_ARM){
                    getMainModel().bipedRightArm.showModel = false;
                }
                if(stack.getItem() == TRegistry.ROTPLATE_CHEST){
                    getMainModel().bipedBody.showModel = false;
                }
                if(stack.getItem() == TRegistry.ROTPLATE_HEAD){
                    getMainModel().bipedHead.showModel = false;
                }
            }
        }
    }
}
