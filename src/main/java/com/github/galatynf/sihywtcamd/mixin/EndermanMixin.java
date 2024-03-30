package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanMixin extends MobEntityMixin {
    protected EndermanMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onTryAttackSuccess(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().end.enderman.blindnessAttack
                && target instanceof PlayerEntity playerEntity
                && this.random.nextBoolean()) {
            int duration = 30;
            duration += (int) (30 * this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty());
            duration += (int) (40 * this.random.nextFloat());
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, duration));
        }
    }
}
