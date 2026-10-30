package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class EndermanMixin extends MobEntityMixin {
    protected EndermanMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void onTryAttackSuccess(ServerLevel world, Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().end.enderman.blindnessAttack
                && this.level() instanceof ServerLevel serverWorld
                && target instanceof Player playerEntity
                && this.random.nextBoolean()) {
            int duration = 30;
            duration += (int) (30 * serverWorld.getCurrentDifficultyAt(this.blockPosition()).getSpecialMultiplier());
            duration += (int) (40 * this.random.nextFloat());
            playerEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration));
        }
    }
}
