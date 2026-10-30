package com.github.galatynf.sihywtcamd.mixin.enderdragon;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonSittingFlamingPhase;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

@Mixin(DragonSittingFlamingPhase.class)
public abstract class SittingFlamingPhaseMixin extends AbstractDragonPhaseInstance {
    @Shadow private int flameTicks;

    public SittingFlamingPhaseMixin(EnderDragon dragon) {
        super(dragon);
    }

    @Inject(method = "doServerTick", at = @At("TAIL"))
    private void makeEndermanTargetPlayer(CallbackInfo ci) {
        if (ModConfig.get().bosses.enderDragon.controlEndermanWhenLanding
                && this.flameTicks % 20 == 0) {
            AABB box = this.dragon.getBoundingBox().inflate(5.0,0.0,5.0).move(0.0, -3.0, 0.0);
            List<EnderMan> endermen = this.dragon.level().getEntities(
                    EntityTypeTest.forClass(EnderMan.class), box, EntitySelector.ENTITY_STILL_ALIVE);
            for (EnderMan enderman : endermen) {
                if (enderman.getTarget() == null || !enderman.getTarget().isAlwaysTicking()) {
                    Player player = this.dragon.level().getNearestPlayer(enderman.getX(), enderman.getY(), enderman.getZ(), 16.0, true);
                    if (player != null) {
                        enderman.setTarget(player);
                        break;
                    }
                }
            }
        }
    }
}
