package com.github.galatynf.sihywtcamd.mixin.spider;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract boolean isBaby();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void spawnBabies(DamageSource source, CallbackInfo ci) {
        if (!this.world.isClient
                && this.getType().equals(EntityType.SPIDER)
                && !this.isBaby()
                && this.random.nextFloat() < 0.1F) {
            int numberBabies = 3 + this.random.nextInt(3)
                    + Math.round(3 * this.world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty());
            for (int i=0; i < numberBabies; i++) {
                MobEntity mob = (MobEntity) this.getType().spawn((ServerWorld) world, null, null, null, this.getBlockPos(), SpawnReason.NATURAL, false, false);
                if (mob != null) mob.setBaby(true);
            }
        }
    }
}
