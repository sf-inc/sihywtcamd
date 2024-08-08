package com.github.galatynf.sihywtcamd.init;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

public class SoundRegistry {
    public static final Identifier SPIDER_SPIT_ID = id("spider_spit");
    public static SoundEvent SPIDER_SPIT_EVENT = SoundEvent.of(SPIDER_SPIT_ID);

    public static void init() {
        if (ModConfig.get().arthropods.generalSpiders.webProjectileGoal) {
            Registry.register(Registries.SOUND_EVENT, SPIDER_SPIT_ID, SPIDER_SPIT_EVENT);
        }
    }
}
