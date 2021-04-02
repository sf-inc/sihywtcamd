package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EvokerEntity.class)
public abstract class EvokerMixin extends SpellcastingIllagerEntity {
    protected EvokerMixin(EntityType<? extends SpellcastingIllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        EntityAttributeInstance instance = this.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (instance != null && ModConfig.get().evokerIncreasedHealth) {
            instance.setBaseValue(36.0D);
            this.setHealth(this.getMaxHealth());
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return ModConfig.get().evokerStopArrows ? damageSource.isProjectile() || super.isInvulnerableTo(damageSource)
                : super.isInvulnerableTo(damageSource);
    }
}
