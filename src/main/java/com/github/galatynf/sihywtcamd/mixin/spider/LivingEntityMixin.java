package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!this.getWorld().isClient() && !this.isBaby()) {
            int nbBabies = 0;
            if (ModConfig.get().arthropods.caveSpider.baby
                    && livingEntity instanceof CaveSpiderEntity
                    && this.random.nextFloat() < 0.1F) {
                nbBabies = 1 + this.random.nextInt(3)
                        + Math.round(2 * this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty());
            } else if (ModConfig.get().arthropods.spider.baby
                    && livingEntity instanceof SpiderEntity
                    && this.random.nextFloat() < 0.15F) {
                nbBabies = 2 + this.random.nextInt(3)
                        + Math.round(3 * this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty());
            }
            ServerPlayerEntity serverPlayerEntity = source.getAttacker() != null && source.getAttacker().isPlayer()
                    ? (ServerPlayerEntity) source.getAttacker() : null;
            for (int i = 0; i < nbBabies; ++i) {
                MobEntity mob = (MobEntity) this.getType().spawn((ServerWorld) this.getWorld(), this.getBlockPos(), SpawnReason.NATURAL);
                if (mob != null) {
                    mob.setBaby(true);
                    if (serverPlayerEntity != null) {
                        Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, mob);
                        serverPlayerEntity = null;
                    }
                }
            }
        }
    }
}
