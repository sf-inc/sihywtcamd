package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HuskEntity.class)
public class HuskMixin extends ZombieEntity {
    public HuskMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return damageSource.equals(DamageSource.ON_FIRE) || super.isInvulnerableTo(damageSource);
    }
}
