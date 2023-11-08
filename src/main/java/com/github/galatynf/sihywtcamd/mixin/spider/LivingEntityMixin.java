package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
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
        if (!this.getWorld().isClient() && !this.isBaby()) {
            int nbBabies = 0;
            if (ModConfig.get().arthropod.spider.baby
                    && this.getType().equals(EntityType.SPIDER)
                    && this.random.nextFloat() < 0.15F) {
                nbBabies = 2 + this.random.nextInt(3)
                        + Math.round(3 * this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty());
            } else if (ModConfig.get().arthropod.spider.babyCaveSpider
                    && this.getType().equals(EntityType.CAVE_SPIDER)
                    && this.random.nextFloat() < 0.1F) {
                nbBabies = 1 + this.random.nextInt(3)
                        + Math.round(2 * this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty());
            }
            for (int i = 0; i < nbBabies; ++i) {
                MobEntity mob = (MobEntity) this.getType().spawn((ServerWorld) this.getWorld(), this.getBlockPos(), SpawnReason.NATURAL);
                if (mob != null) {
                    mob.setBaby(true);
                }
            }
        }
    }
}
