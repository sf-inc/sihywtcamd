package com.github.galatynf.sihywtcamd.mixin.guardian;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.entity.mob.GuardianEntity$FireBeamGoal")
public class GuardianAttackGoalMixin {
    @Shadow @Final private GuardianEntity guardian;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V"))
    private void cancelSound(World instance, Entity entity, byte status) {
        if (!ModConfig.get().overworld.guardian.silentKill || this.guardian.getTarget() instanceof PlayerEntity) {
            instance.sendEntityStatus(this.guardian, EntityStatuses.PLAY_GUARDIAN_ATTACK_SOUND);
        }
    }
}
