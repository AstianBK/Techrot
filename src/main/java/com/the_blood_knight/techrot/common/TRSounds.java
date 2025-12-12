package com.the_blood_knight.techrot.common;

import com.the_blood_knight.techrot.Techrot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

@SuppressWarnings("EmptyMethod")
public class TRSounds {
    public static final SoundEvent IMPLANTEDPLAYER_BREATHE = createSoundEvent("implantedplayer_breathe");
    public static final SoundEvent IMPLANTEDPLAYER_TALK = createSoundEvent("implantedplayer_talk");

    private static SoundEvent createSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(Techrot.MODID, name);
        return new SoundEvent(id).setRegistryName(id);
    }
}