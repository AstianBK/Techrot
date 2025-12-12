package com.the_blood_knight.techrot.common;

import com.the_blood_knight.techrot.Techrot;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;

@SuppressWarnings("EmptyMethod")
public class TRSounds {
    public static final SoundEvent IMPLANTEDPLAYER_BREATHE = createSoundEvent("implantedplayer_breathe");

    public static final SoundEvent IMPLANTEDPLAYER_TALK = createSoundEvent("implantedplayer_talk");

    public static final SoundEvent TOXICLAUNCHER_SHOOT = createSoundEvent("toxiclauncher_shoot");

    public static final SoundEvent BIO_EXTRACTOR_USE = createSoundEvent("bio_extractor_use");

    public static final SoundEvent BIOFLESHCLONER_ACTIVE = createSoundEvent("biofleshcloner_active");

    public static final SoundEvent BIOPASTEMAKER_ACTIVE = createSoundEvent("biopastemaker_active");

    public static final SoundEvent BIOFURNACE_ACTIVE = createSoundEvent("biofurnace_active");

    public static final SoundEvent BIOIMPLANTER_USE = createSoundEvent("bioimplanter_block_use");

    public static final SoundEvent BIOPIPE_BLOCK_BREAK = createSoundEvent("biopipe_block_break");

    public static final SoundEvent BIOPIPE_BLOCK_PLACE = createSoundEvent("biopipe_block_place");

    private static SoundEvent createSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(Techrot.MODID, name);
        return new SoundEvent(id).setRegistryName(id);
    }

    public static void registerSound(RegistryEvent.Register<SoundEvent> event){
        event.getRegistry().registerAll(IMPLANTEDPLAYER_BREATHE,IMPLANTEDPLAYER_TALK,TOXICLAUNCHER_SHOOT,BIOFLESHCLONER_ACTIVE,BIOPASTEMAKER_ACTIVE,
                BIOFURNACE_ACTIVE,BIOIMPLANTER_USE,BIOPIPE_BLOCK_BREAK,BIOPIPE_BLOCK_PLACE,BIO_EXTRACTOR_USE);
    }
}