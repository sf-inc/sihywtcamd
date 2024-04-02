package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.mixin.EntityMixin;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.SpiderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.SPIDER_BABY;

@Mixin(SpiderEntity.class)
public abstract class SpiderEntityMixin extends EntityMixin {
    @Override
    protected void onTrackedDataSet(TrackedData<?> data, CallbackInfo ci) {
        if (SPIDER_BABY.equals(data)) {
            this.calculateDimensions();
        }
    }
}
