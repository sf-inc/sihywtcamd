package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.LivingEntityMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.SPIDER_BABY;

@Mixin(SpiderEntity.class)
public abstract class SpiderLivingMixin extends LivingEntityMixin {
    public SpiderLivingMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void addBabyData(CallbackInfo ci) {
        this.getDataTracker().startTracking(SPIDER_BABY, false);
    }

    @Override
    public boolean updateBaby(boolean original) {
        return ModConfig.get().arthropods.spider.baby
                ? this.getDataTracker().get(SPIDER_BABY)
                : original;
    }

    @Override
    protected float updateScaleFactor(float original) {
        if (ModConfig.get().arthropods.spider.baby && this.isBaby()) {
            return 0.33F;
        } else {
            return original;
        }
    }
}
