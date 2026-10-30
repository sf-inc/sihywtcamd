package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void cancelSuffocationDamagePhantom(ServerLevel world, DamageSource source, float amount,
                                                CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().undead.phantom.throughBlocks && this.getType().equals(EntityType.PHANTOM)
                && (source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.FLY_INTO_WALL))) {
            cir.setReturnValue(false);
        }
    }
}
