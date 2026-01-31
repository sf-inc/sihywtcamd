package com.github.galatynf.sihywtcamd.mixin.guardian;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuardianEntity.class)
public abstract class GuardianMixin extends HostileEntity {
    protected GuardianMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "travelInWater", at = @At("HEAD"))
    private void usePassengerDirection(Vec3d movementInput, double gravity, boolean falling, double y, CallbackInfo ci) {
        if (this.hasPassengers() && this.getFirstPassenger() instanceof LivingEntity livingEntity) {
            this.setPitch(livingEntity.getPitch() * 0.5f);
        }
    }
}
