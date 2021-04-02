package com.github.galatynf.sihywtcamd;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;

public class Sihywtcamd implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }

    public static boolean isZombieType(EntityType<?> entityType) {
        return entityType.equals(EntityType.ZOMBIE)
                || entityType.equals(EntityType.ZOMBIE_VILLAGER)
                || entityType.equals(EntityType.ZOMBIFIED_PIGLIN)
                || entityType.equals(EntityType.ZOMBIE_HORSE)
                || entityType.equals(EntityType.DROWNED)
                || entityType.equals(EntityType.HUSK)
                || entityType.equals(EntityType.GIANT);
    }
}
