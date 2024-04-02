package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpiderEntity.class)
public class SpiderHostileMixin extends HostileMixin {
    protected SpiderHostileMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected boolean updateDropLoot(boolean original) {
        if (ModConfig.get().arthropods.spider.baby || ModConfig.get().arthropods.caveSpider.baby) {
            return !this.isBaby();
        } else  {
            return original;
        }
    }
}
