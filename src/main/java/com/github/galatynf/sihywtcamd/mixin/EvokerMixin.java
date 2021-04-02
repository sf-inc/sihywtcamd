package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EvokerEntity.class)
public abstract class EvokerMixin extends SpellcastingIllagerEntity {
    protected EvokerMixin(EntityType<? extends SpellcastingIllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createEvokerAttributes", at = @At("HEAD"), cancellable = true)
    private static void changeHealthE(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        if (ModConfig.get().evokerIncreasedHealth) {
            cir.setReturnValue(HostileEntity.createHostileAttributes()
                    .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D)
                    .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12.0D)
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 36.0D));
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return ModConfig.get().evokerStopArrows ? damageSource.isProjectile() || super.isInvulnerableTo(damageSource)
                : super.isInvulnerableTo(damageSource);
    }
}
