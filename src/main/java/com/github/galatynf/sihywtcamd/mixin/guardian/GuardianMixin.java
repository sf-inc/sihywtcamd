package com.github.galatynf.sihywtcamd.mixin.guardian;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Guardian.class)
public abstract class GuardianMixin extends Monster {
    protected GuardianMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "travelInWater", at = @At("HEAD"))
    private void usePassengerDirection(Vec3 movementInput, double gravity, boolean falling, double y, CallbackInfo ci) {
        if (this.isVehicle() && this.getFirstPassenger() instanceof LivingEntity livingEntity) {
            this.setXRot(livingEntity.getXRot() * 0.5f);
        }
    }
}
