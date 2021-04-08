package com.github.galatynf.sihywtcamd;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;

public class Sihywtcamd implements ModInitializer {
    public static boolean areConfigsInit = false;

    @Override
    public void onInitialize() {
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
