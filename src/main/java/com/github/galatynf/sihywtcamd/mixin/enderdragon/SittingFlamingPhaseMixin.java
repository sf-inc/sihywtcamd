package com.github.galatynf.sihywtcamd.mixin.enderdragon;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.SittingFlamingPhase;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SittingFlamingPhase.class)
public abstract class SittingFlamingPhaseMixin extends AbstractPhase {
    @Shadow private int ticks;

    public SittingFlamingPhaseMixin(EnderDragonEntity dragon) {
        super(dragon);
    }

    @Inject(method = "serverTick", at = @At("TAIL"))
    private void makeEndermanTargetPlayer(CallbackInfo ci) {
        if (ModConfig.get().bosses.enderDragon.controlEndermanWhenLanding
                && this.ticks % 20 == 0) {
            Box box = this.dragon.getBoundingBox().expand(5.0,0.0,5.0).offset(0.0, -3.0, 0.0);
            List<EndermanEntity> endermen = this.dragon.getWorld().getEntitiesByType(
                    TypeFilter.instanceOf(EndermanEntity.class), box, EntityPredicates.VALID_ENTITY);
            for (EndermanEntity enderman : endermen) {
                if (enderman.getTarget() == null || !enderman.getTarget().isPlayer()) {
                    PlayerEntity player = this.dragon.getWorld().getClosestPlayer(enderman, 16.0);
                    enderman.setTarget(player);
                    break;
                }
            }
        }
    }
}
