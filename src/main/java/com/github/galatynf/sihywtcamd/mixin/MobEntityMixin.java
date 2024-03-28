package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
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
        MobEntity mob = (MobEntity) (Object) this;
        if (ModConfig.get().end.enderman.blindnessAttack
                && mob instanceof EndermanEntity
                && target instanceof PlayerEntity playerEntity
                && this.random.nextBoolean()) {
            int duration = 30;
            duration += (int) (30 * this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty());
            duration += (int) (40 * this.random.nextFloat());
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, duration));
        }
    }
}
