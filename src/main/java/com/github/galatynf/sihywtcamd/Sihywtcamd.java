package com.github.galatynf.sihywtcamd;

import com.github.galatynf.sihywtcamd.block.MessyCobweb;
import com.github.galatynf.sihywtcamd.entity.CobwebProjectileEntity;
import com.github.galatynf.sihywtcamd.statusEffect.TinnitusStatusEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Sihywtcamd implements ModInitializer {
    public static boolean areConfigsInit = false;

    public static final StatusEffect TINNITUS = new TinnitusStatusEffect();

    public static final EntityType<CobwebProjectileEntity> COBWEB  = Registry.register(Registry.ENTITY_TYPE, "cobweb", EntityType.Builder
            .create(CobwebProjectileEntity::new, SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F)
            .build("cobweb"));

    public static final Block MESSY_COBWEB = new MessyCobweb(FabricBlockSettings
            .of(Material.COBWEB).noCollision().requiresTool().strength(3.0F));

    @Override
    public void onInitialize() {
        Registry.register(Registry.STATUS_EFFECT, new Identifier("sihywtcamd", "tinnitus"), TINNITUS);

        Registry.register(Registry.BLOCK, new Identifier("sihywtcamd", "messy_cobweb"), MESSY_COBWEB);
    }
}
