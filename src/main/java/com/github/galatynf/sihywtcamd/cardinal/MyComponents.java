package com.github.galatynf.sihywtcamd.cardinal;

import com.github.galatynf.sihywtcamd.cardinal.api.*;
import com.github.galatynf.sihywtcamd.cardinal.impl.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

public final class MyComponents implements EntityComponentInitializer {
    public static final ComponentKey<ArrowEntityComponentAPI> ARROW_COMPONENT =
            ComponentRegistry.getOrCreate(id("arrow_component"), ArrowEntityComponentAPI.class);
    public static final ComponentKey<EnderDragonEntityComponentAPI> ENDER_DRAGON_COMPONENT =
            ComponentRegistry.getOrCreate(id("ender_dragon_component"), EnderDragonEntityComponentAPI.class);
    public static final ComponentKey<PillagerEntityComponentAPI> PILLAGER_COMPONENT =
            ComponentRegistry.getOrCreate(id("pillager_component"), PillagerEntityComponentAPI.class);
    public static final ComponentKey<SkeletonEntityComponentAPI> SKELETON_COMPONENT =
            ComponentRegistry.getOrCreate(id("skeleton_component"), SkeletonEntityComponentAPI.class);
    public static final ComponentKey<SlimeEntityComponentAPI> SLIME_COMPONENT =
            ComponentRegistry.getOrCreate(id("slime_component"), SlimeEntityComponentAPI.class);
    public static final ComponentKey<WitherEntityComponentAPI> WITHER_COMPONENT =
            ComponentRegistry.getOrCreate(id("wither_component"), WitherEntityComponentAPI.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PersistentProjectileEntity.class, ARROW_COMPONENT, entity -> new ArrowEntityComponent());
        registry.registerFor(PillagerEntity.class, PILLAGER_COMPONENT, PillagerEntityComponent::new);
        registry.registerFor(SkeletonEntity.class, SKELETON_COMPONENT, SkeletonEntityComponent::new);
        registry.registerFor(SlimeEntity.class, SLIME_COMPONENT, entity -> new SlimeEntityComponent());
        registry.registerFor(EnderDragonEntity.class, ENDER_DRAGON_COMPONENT, entity -> new EnderDragonEntityComponent());
        registry.registerFor(WitherEntity.class, WITHER_COMPONENT, entity -> new WitherEntityComponent());
    }
}
