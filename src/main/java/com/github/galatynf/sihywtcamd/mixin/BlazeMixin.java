package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlazeEntity.class)
public abstract class BlazeMixin extends MobEntityMixin {
    @Shadow protected abstract boolean isFireActive();

    protected BlazeMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "createBlazeAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder reduceFollowRange(DefaultAttributeContainer.Builder original) {
        if (ModConfig.get().nether.blaze.reducedFollowRange) {
            original.add(EntityAttributes.FOLLOW_RANGE, 32.0);
        }
        return original;
    }

    @Override
    protected void onTryAttackSuccess(ServerWorld world, Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().nether.blaze.fireAttack && this.isFireActive()) {
            target.setOnFireFor(5);
        }
    }
}
