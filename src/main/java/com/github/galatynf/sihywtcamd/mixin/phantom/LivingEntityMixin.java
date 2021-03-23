package com.github.galatynf.sihywtcamd.mixin.phantom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void cancelSuffocationDamagePhantom(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getType().equals(EntityType.PHANTOM)
                && (source.equals(DamageSource.IN_WALL) || source.equals(DamageSource.FLY_INTO_WALL))) {
            cir.setReturnValue(false);
        }
    }
}
