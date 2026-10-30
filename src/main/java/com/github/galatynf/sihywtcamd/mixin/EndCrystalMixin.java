package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystal.class)
public abstract class EndCrystalMixin extends Entity {
    public EndCrystalMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/EndCrystal;onDestroyedBy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V"))
    private void summonPhantoms(ServerLevel world, DamageSource source, float amount,
                                CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().bosses.enderDragon.crystalDestructionSpawnsPhantom
                && this.isPillarCrystalDuringFight(world)
                && this.random.nextFloat() < 0.66F) {
            Phantom phantom = EntityType.PHANTOM.spawn(world, this.blockPosition(), EntitySpawnReason.TRIGGERED);
            if (phantom != null && source.getEntity() instanceof LivingEntity attacker) {
                phantom.setTarget(attacker);
            }
        }
    }

    @Unique
    private boolean isPillarCrystalDuringFight(ServerLevel world) {
        EndDragonFight fight = world.getDragonFight();
        if (fight == null || fight.getDragonUUID() == null
                || !(world.getEntity(fight.getDragonUUID()) instanceof EnderDragon dragon)
                || !dragon.isAlive()) {
            return false;
        }

        for (SpikeFeature.EndSpike spike : SpikeFeature.getSpikesForLevel(world)) {
            if (spike.getTopBoundingBox().intersects(this.getBoundingBox())) {
                return true;
            }
        }
        return false;
    }
}
