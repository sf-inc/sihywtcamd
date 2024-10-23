package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaCubeEntity.class)
public abstract class MagmaCubeMixin extends SlimeMixin {
    protected MagmaCubeMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onDamage(LivingEntity target, CallbackInfo ci) {
        if (ModConfig.get().nether.magmaCube.fireCollision
                && this.isAlive() && !target.isBlocking()
                && this.squaredDistanceTo(target) < 0.36 * this.getSize() * this.getSize()) {
            target.setOnFireFor(this.getSize());
        }
    }

    @Override
    protected void onTick(CallbackInfo ci) {
        if (!this.getWorld().isClient()
                && ModConfig.get().nether.magmaCube.slimeConversion
                && this.getWorld().getRegistryKey().equals(World.OVERWORLD)
                && this.getFluidHeight(FluidTags.WATER) > 0.0) {
            int size = this.getSize();
            SlimeEntity slime = this.convertTo(EntityType.SLIME, false);
            if (slime != null) {
                slime.setSize(size, true);
                this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            }
        }
    }
}
