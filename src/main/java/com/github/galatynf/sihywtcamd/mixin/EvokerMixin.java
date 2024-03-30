package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EvokerEntity.class)
public abstract class EvokerMixin extends MobEntityMixin {
    protected EvokerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                EntityData entityData, NbtCompound entityTag, CallbackInfoReturnable<EntityData> cir) {
        if (!ModConfig.get().illagers.evoker.increasedHealth) return;

        EntityAttributeInstance maxHealth = this.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(36.0D);
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return ModConfig.get().illagers.evoker.stopArrows
                ? damageSource.isIn(DamageTypeTags.IS_PROJECTILE) || super.isInvulnerableTo(damageSource)
                : super.isInvulnerableTo(damageSource);
    }
}
