package com.github.galatynf.sihywtcamd.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

public class EntityRegistry {
    public static final EntityType<CobwebProjectileEntity> COBWEB  = EntityType.Builder
            .create(CobwebProjectileEntity::new, SpawnGroup.MISC)
            .dimensions(0.5F, 0.5F)
            .build();

    public static void init() {
        Registry.register(Registries.ENTITY_TYPE, id("cobweb"), COBWEB);
    }

    public static void initClient() {
        EntityRendererRegistry.register(COBWEB, CobwebEntityRenderer::new);
    }
}
