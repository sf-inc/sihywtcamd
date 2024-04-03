package com.github.galatynf.sihywtcamd.cardinal;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.api.*;
import com.github.galatynf.sihywtcamd.cardinal.impl.*;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;

public final class MyComponents implements EntityComponentInitializer {
    public static final ComponentKey<ArrowEntityComponentAPI> ARROW_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(Sihywtcamd.MOD_ID, "arrow_component"), ArrowEntityComponentAPI.class);
    public static final ComponentKey<BabyComponentAPI> BABY_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(Sihywtcamd.MOD_ID, "baby_component"), BabyComponentAPI.class);
    public static final ComponentKey<EnderDragonEntityComponentAPI> ENDER_DRAGON_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(Sihywtcamd.MOD_ID, "ender_dragon_component"), EnderDragonEntityComponentAPI.class);
    public static final ComponentKey<SkeletonEntityComponentAPI> SKELETON_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(Sihywtcamd.MOD_ID, "skeleton_component"), SkeletonEntityComponentAPI.class);
    public static final ComponentKey<SlimeEntityComponentAPI> SLIME_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(Sihywtcamd.MOD_ID, "slime_component"), SlimeEntityComponentAPI.class);
    public static final ComponentKey<WitherEntityComponentAPI> WITHER_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(Sihywtcamd.MOD_ID, "wither_component"), WitherEntityComponentAPI.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PersistentProjectileEntity.class, ARROW_COMPONENT, entity -> new ArrowEntityComponent());
        registry.registerFor(AbstractSkeletonEntity.class, BABY_COMPONENT, BabyComponent::new);
        registry.registerFor(SpiderEntity.class, BABY_COMPONENT, BabyComponent::new);
        registry.registerFor(SkeletonEntity.class, SKELETON_COMPONENT, entity -> new SkeletonEntityComponent());
        registry.registerFor(SlimeEntity.class, SLIME_COMPONENT, entity -> new SlimeEntityComponent());
        registry.registerFor(EnderDragonEntity.class, ENDER_DRAGON_COMPONENT, entity -> new EnderDragonEntityComponent());
        registry.registerFor(WitherEntity.class, WITHER_COMPONENT, entity -> new WitherEntityComponent());
    }
}
