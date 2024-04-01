package com.github.galatynf.sihywtcamd.mixin.guardian;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.entity.mob.GuardianEntity$FireBeamGoal")
public class GuardianAttackGoalMixin {
    @Shadow @Final private GuardianEntity guardian;

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V"))
    private boolean playSoundCondition(World instance, Entity entity, byte status) {
        return !ModConfig.get().overworld.guardian.silentKill
                || this.guardian.getBeamTarget() instanceof PlayerEntity;
    }
}
