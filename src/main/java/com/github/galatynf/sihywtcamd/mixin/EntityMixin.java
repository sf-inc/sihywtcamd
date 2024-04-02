package com.github.galatynf.sihywtcamd.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract void calculateDimensions();

    @Inject(method = "onTrackedDataSet", at = @At("TAIL"))
    protected void onTrackedDataSet(TrackedData<?> data, CallbackInfo ci) {

    }

    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    protected boolean updateInvulnerableTo(boolean original, DamageSource damageSource) {
        return original;
    }
}
