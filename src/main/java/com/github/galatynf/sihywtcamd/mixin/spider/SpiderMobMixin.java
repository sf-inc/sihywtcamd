package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Spider.class)
public abstract class SpiderMobMixin extends MobEntityMixin {
    protected SpiderMobMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void addSpiderGoals(CallbackInfo ci) {
        if (ModConfig.get().general.merchantHostility) {
            this.targetSelector.addGoal(3, new Spider.SpiderTargetGoal<>((Spider) (Object) this, AbstractVillager.class));
        }
    }
}
