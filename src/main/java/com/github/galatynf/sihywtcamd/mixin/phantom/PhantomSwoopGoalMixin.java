package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$SwoopMovementGoal")
public abstract class PhantomSwoopGoalMixin {
    @Shadow private PhantomEntity field_7333;

    @Shadow public abstract boolean canStart();

    @Inject(method = "Lnet/minecraft/entity/mob/PhantomEntity$SwoopMovementGoal;shouldContinue()Z", at = @At("HEAD"), cancellable = true)
    private void changePredicateContinue(CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().overworld.mobs.mobsLessFear || ModConfig.get().overworld.phantoms.phantomLightFear) {
            boolean shouldContinue = true;
            LivingEntity livingEntity = field_7333.getTarget();
            if (livingEntity == null) {
                shouldContinue = false;
            } else if (!livingEntity.isAlive()) {
                shouldContinue = false;
            } else if (livingEntity instanceof PlayerEntity && (livingEntity.isSpectator() || ((PlayerEntity) livingEntity).isCreative())) {
                shouldContinue = false;
            } else if (!this.canStart()) {
                shouldContinue = false;
            } else if (ModConfig.get().overworld.phantoms.phantomLightFear && field_7333.world.getLightLevel(field_7333.getBlockPos()) > 10) {
                shouldContinue = false;
            } else {
                if (field_7333.age % 20 == 0) {
                    List<CatEntity> list = field_7333.world.getEntitiesByClass(CatEntity.class, field_7333.getBoundingBox().expand(16.0D), EntityPredicates.VALID_ENTITY);
                    for (CatEntity catEntity : list) {
                        catEntity.hiss();
                        if (!(ModConfig.get().overworld.mobs.mobsLessFear && catEntity.getHealth() <= catEntity.getMaxHealth() / 2.0F)) {
                            shouldContinue = false;
                        }
                    }
                }
            }

            cir.setReturnValue(shouldContinue);
        }
    }
}
