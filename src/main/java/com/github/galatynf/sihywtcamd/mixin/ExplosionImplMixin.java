package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerExplosion.class)
public abstract class ExplosionImplMixin {
    @Shadow @Final private ExplosionDamageCalculator damageCalculator;

    @WrapOperation(method = "hurtEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;ignoreExplosion(Lnet/minecraft/world/level/Explosion;)Z"))
    private boolean chainCreeperExplosion(Entity entity, Explosion explosion, Operation<Boolean> original) {
        if (original.call(entity, explosion)) return true;
        if (!(entity instanceof Creeper creeper)
                || !ModConfig.get().overworld.creeper.chainExplosions
                || (explosion.getIndirectSourceEntity() instanceof Creeper source && source.isPowered())
                || !this.damageCalculator.shouldDamageEntity(explosion, entity)
                || Math.sqrt(entity.distanceToSqr(explosion.center())) > explosion.radius() * 2.0f) {
            return false;
        }

        creeper.ignite();
        return true;
    }
}
