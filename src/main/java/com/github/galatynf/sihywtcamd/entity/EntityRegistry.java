package com.github.galatynf.sihywtcamd.entity;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

public class EntityRegistry {
    public static EntityType<CobwebProjectileEntity> COBWEB;

    public static void init() {
        if (ModConfig.get().arthropods.generalSpiders.webProjectileGoal) {
            COBWEB  = EntityType.Builder
                    .create(CobwebProjectileEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .build();
            Registry.register(Registries.ENTITY_TYPE, id("cobweb"), COBWEB);
        }
    }
}
