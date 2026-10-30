package com.github.galatynf.sihywtcamd.cardinal;

import com.github.galatynf.sihywtcamd.cardinal.api.*;
import com.github.galatynf.sihywtcamd.cardinal.impl.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
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
    public static final ComponentKey<ZombifiedPiglinComponentAPI> ZOMBIFIED_PIGLIN_COMPONENT =
            ComponentRegistry.getOrCreate(id("zombified_piglin_component"), ZombifiedPiglinComponentAPI.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(AbstractArrow.class, ARROW_COMPONENT, entity -> new ArrowEntityComponent());
        registry.registerFor(Pillager.class, PILLAGER_COMPONENT, PillagerEntityComponent::new);
        registry.registerFor(Skeleton.class, SKELETON_COMPONENT, SkeletonEntityComponent::new);
        registry.registerFor(Slime.class, SLIME_COMPONENT, entity -> new SlimeEntityComponent());
        registry.registerFor(EnderDragon.class, ENDER_DRAGON_COMPONENT, entity -> new EnderDragonEntityComponent());
        registry.registerFor(WitherBoss.class, WITHER_COMPONENT, entity -> new WitherEntityComponent());
        registry.registerFor(ZombifiedPiglin.class, ZOMBIFIED_PIGLIN_COMPONENT, entity -> new ZombifiedPiglinComponent());
    }
}
