package com.the_blood_knight.techrot.common.entity;

import com.the_blood_knight.techrot.Techrot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class TEntityRegister {
    private static int ENTITY_ID = 0;

    public static void registerEntities() {
        EntityRegistry.registerModEntity(
                new ResourceLocation(Techrot.MODID,"toxic_bomb_entity"),
                ToxicBombEntity.class,
                "Toxic Bomb Entity",
                ENTITY_ID++,
                Techrot.main,
                64,
                1,
                true
        );
        EntityRegistry.registerModEntity(
                new ResourceLocation(Techrot.MODID,"toxic_fog"),
                ToxicFogEntity.class,
                "Toxic Fog",
                ENTITY_ID++,
                Techrot.main,
                64,
                1,
                true
        );
    }
}
