package com.github.galatynf.sihywtcamd.mixin.husk;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerFireHuskMixin {
    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V", shift = At.Shift.AFTER))
    private void noFireHusk(Entity target, CallbackInfo ci) {
        if (target instanceof HuskEntity) {
            target.extinguish();
        }
    }
}
