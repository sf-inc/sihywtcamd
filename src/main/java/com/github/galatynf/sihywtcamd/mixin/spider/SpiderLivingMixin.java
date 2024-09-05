package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.LivingEntityMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.ComponentProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpiderEntity.class)
public abstract class SpiderLivingMixin extends LivingEntityMixin {
    public SpiderLivingMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean updateBaby(boolean original) {
        return ModConfig.get().arthropods.spider.babyOnDeath
                && ((ComponentProvider) this).getComponentContainer() != null
                ? MyComponents.BABY_COMPONENT.get(this).isBaby()
                : original;
    }

    @Override
    protected float updateScaleFactor(float original) {
        if (ModConfig.get().arthropods.spider.babyOnDeath && this.isBaby()) {
            return 0.33F;
        } else {
            return original;
        }
    }
}
