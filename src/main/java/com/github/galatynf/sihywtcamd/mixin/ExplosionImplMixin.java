package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExplosionImpl.class)
public abstract class ExplosionImplMixin {
    @Shadow @Final private ExplosionBehavior behavior;

    @WrapOperation(method = "damageEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isImmuneToExplosion(Lnet/minecraft/world/explosion/Explosion;)Z"))
    private boolean chainCreeperExplosion(Entity entity, Explosion explosion, Operation<Boolean> original) {
        if (original.call(entity, explosion)) return true;
        if (!(entity instanceof CreeperEntity creeper)
                || !ModConfig.get().overworld.creeper.chainExplosions
                || (explosion.getCausingEntity() instanceof CreeperEntity source && source.isCharged())
                || !this.behavior.shouldDamage(explosion, entity)
                || Math.sqrt(entity.squaredDistanceTo(explosion.getPosition())) > explosion.getPower() * 2.0f) {
            return false;
        }

        creeper.ignite();
        return true;
    }
}
