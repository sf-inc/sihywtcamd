package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MagmaCubeEntity.class)
public class MagmaCubeMixin extends SlimeEntity {
    public MagmaCubeMixin(EntityType<? extends SlimeEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void damage(LivingEntity target) {
        if (ModConfig.get().nether.magmaCube.fireCollision
                && this.isAlive() && !target.isBlocking()
                && this.squaredDistanceTo(target) < 0.36 * this.getSize() * this.getSize()) {
            target.setOnFireFor(this.getSize());
        }
        super.damage(target);
    }
}
