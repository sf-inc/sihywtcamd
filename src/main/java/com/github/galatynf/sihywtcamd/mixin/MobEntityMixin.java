package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;onAttacking(Lnet/minecraft/entity/Entity;)V"))
    private void endermanBlindnessAttack(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().end.enderman.blindnessAttack
                && this.getType().equals(EntityType.ENDERMAN)
                && target instanceof PlayerEntity playerEntity) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100));
        }
    }
}
