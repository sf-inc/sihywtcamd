package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$SwoopMovementGoal")
public abstract class PhantomSwoopGoalMixin {
    @Final @Shadow PhantomEntity field_7333;
    @Shadow private boolean catsNearby;

    @Inject(method = "shouldContinue()Z", at = @At("HEAD"), cancellable = true)
    private void fearLight(CallbackInfoReturnable<Boolean> cir) {
        // Reset to ensure further check validity
        this.catsNearby = false;

        if (ModConfig.get().undead.phantom.lightFear
                && this.field_7333.getWorld().getLightLevel(this.field_7333.getBlockPos()) > 10) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "shouldContinue()Z", at = @At("TAIL"), cancellable = true)
    private void removeCatFear(CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().general.mobsLessFear && this.catsNearby) {
            boolean shouldContinue = true;
            List<CatEntity> list = this.field_7333.getWorld().getEntitiesByClass(CatEntity.class, this.field_7333.getBoundingBox().expand(16.0), EntityPredicates.VALID_ENTITY);
            for (CatEntity catEntity : list) {
                if (catEntity.getHealth() > catEntity.getMaxHealth() / 2.0F) {
                    shouldContinue = false;
                    break;
                }
            }

            cir.setReturnValue(shouldContinue);
        }
    }
}
