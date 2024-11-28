package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalMixin extends Entity {
    public EndCrystalMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/EndCrystalEntity;crystalDestroyed(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;)V"))
    private void summonPhantoms(ServerWorld world, DamageSource source, float amount,
                                CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().bosses.enderDragon.crystalDestructionSpawnsPhantom
                && this.getWorld() instanceof ServerWorld serverWorld
                && serverWorld.getEnderDragonFight() != null
                && this.random.nextFloat() < 0.66F) {
            PhantomEntity phantom = EntityType.PHANTOM.spawn(serverWorld, this.getBlockPos(), SpawnReason.TRIGGERED);
            if (phantom != null && source.getAttacker() instanceof LivingEntity attacker) {
                phantom.setTarget(attacker);
            }
        }
    }
}
