package com.github.galatynf.sihywtcamd;

import com.github.galatynf.sihywtcamd.entity.CobwebProjectileEntity;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class Sihywtcamd implements ModInitializer {
    public static boolean areConfigsInit = false;

    public static final EntityType<CobwebProjectileEntity> COBWEB  = Registry.register(Registry.ENTITY_TYPE, "cobweb", EntityType.Builder
            .create(CobwebProjectileEntity::new, SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F)
            .build("cobweb"));

    @Override
    public void onInitialize() {
    }
}
