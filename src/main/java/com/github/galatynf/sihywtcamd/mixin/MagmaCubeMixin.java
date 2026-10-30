package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.advancement.AdvancementRegistry;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaCube.class)
public abstract class MagmaCubeMixin extends SlimeMixin {
    protected MagmaCubeMixin(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void onDamage(LivingEntity target, CallbackInfo ci) {
        if (ModConfig.get().nether.magmaCube.fireCollision
                && this.isAlive() && !target.isBlocking()
                && this.distanceToSqr(target) < 0.36 * this.getSize() * this.getSize()) {
            target.igniteForSeconds(this.getSize());
        }
    }

    @Override
    protected void onTick(CallbackInfo ci) {
        if (!this.level().isClientSide()
                && this.isAlive()
                && ModConfig.get().nether.magmaCube.slimeConversion
                && this.level().dimension().equals(Level.OVERWORLD)
                && this.getFluidHeight(FluidTags.WATER) > 0.0) {
            int size = this.getSize();
            Slime slime = this.convertTo(
                    EntityType.SLIME,
                    ConversionParams.single(this, false, true),
                    magmaCube -> {});
            if (slime != null) {
                slime.setSize(size, true);
                this.playSound(SoundEvents.GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                this.level().getEntitiesOfClass(ServerPlayer.class, slime.getBoundingBox().inflate(16.0),
                                player -> !player.isSpectator() && player.hasLineOfSight(slime))
                        .forEach(AdvancementRegistry.MAGMA_TO_SLIME_CONVERSION::trigger);
            }
        }
    }
}
