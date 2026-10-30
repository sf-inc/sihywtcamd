package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Blaze.class)
public abstract class BlazeMixin extends MobEntityMixin {
    @Shadow protected abstract boolean isCharged();

    protected BlazeMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder reduceFollowRange(AttributeSupplier.Builder original) {
        if (ModConfig.get().nether.blaze.reducedFollowRange) {
            original.add(Attributes.FOLLOW_RANGE, 32.0);
        }
        return original;
    }

    @Override
    protected void onTryAttackSuccess(ServerLevel world, Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().nether.blaze.fireAttack && this.isCharged()) {
            target.igniteForSeconds(5);
        }
    }
}
