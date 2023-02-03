package com.github.galatynf.sihywtcamd;

import com.github.galatynf.sihywtcamd.block.MessyCobweb;
import com.github.galatynf.sihywtcamd.entity.CobwebProjectileEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Sihywtcamd implements ModInitializer {
    public static boolean areConfigsInit = false;
    public static final String MOD_ID = "sihywtcamd";

    public static final EntityType<CobwebProjectileEntity> COBWEB  = Registry.register(Registries.ENTITY_TYPE, "cobweb", EntityType.Builder
            .create(CobwebProjectileEntity::new, SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F)
            .build("cobweb"));

    public static final Block MESSY_COBWEB = new MessyCobweb(FabricBlockSettings
            .of(Material.COBWEB).noCollision().requiresTool().strength(3.0F));

    public static final Identifier SPIDER_SPIT_ID = new Identifier(MOD_ID, "spider_spit");
    public static SoundEvent SPIDER_SPIT_EVENT = SoundEvent.of(SPIDER_SPIT_ID);

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "messy_cobweb"), MESSY_COBWEB);
        Registry.register(Registries.SOUND_EVENT, SPIDER_SPIT_ID, SPIDER_SPIT_EVENT);
    }
}
