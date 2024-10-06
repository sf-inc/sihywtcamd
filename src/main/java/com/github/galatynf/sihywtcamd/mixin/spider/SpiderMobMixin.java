package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpiderEntity.class)
public abstract class SpiderMobMixin extends MobEntityMixin {
    protected SpiderMobMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void addSpiderGoals(CallbackInfo ci) {
        if (ModConfig.get().general.merchantHostility) {
            this.targetSelector.add(3, new SpiderEntity.TargetGoal<>((SpiderEntity) (Object) this, MerchantEntity.class));
        }
    }
}
