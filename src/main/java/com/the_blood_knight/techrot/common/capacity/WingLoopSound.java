package com.the_blood_knight.techrot.common.capacity;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundCategory;
import net.minecraft.entity.player.EntityPlayer;

public class WingLoopSound extends MovingSound {
    private final EntityPlayer player;

    public WingLoopSound(EntityPlayer player, SoundEvent sound) {
        super(sound, SoundCategory.PLAYERS);
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.xPosF = (float) player.posX;
        this.yPosF = (float) player.posY;
        this.zPosF = (float) player.posZ;
    }

    public void stop() {
        this.donePlaying = true;
    }


    @Override
    public void update() {
        if (player.isDead || !player.capabilities.isFlying) {
            this.donePlaying = true;
        } else {
            this.xPosF = (float) player.posX;
            this.yPosF = (float) player.posY;
            this.zPosF = (float) player.posZ;
        }
    }
}

