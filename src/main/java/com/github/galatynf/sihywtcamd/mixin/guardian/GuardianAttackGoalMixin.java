package com.github.galatynf.sihywtcamd.mixin.guardian;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.monster.Guardian$GuardianAttackGoal")
public class GuardianAttackGoalMixin {
    @Shadow @Final private Guardian guardian;

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
    private boolean playSoundCondition(Level instance, Entity entity, byte status) {
        return !ModConfig.get().overworld.guardian.silentKill
                || this.guardian.getActiveAttackTarget() instanceof Player;
    }
}
