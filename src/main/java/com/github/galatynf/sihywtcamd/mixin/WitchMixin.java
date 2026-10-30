package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Witch.class)
public abstract class WitchMixin extends Raider {
    protected WitchMixin(EntityType<? extends Raider> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void fleePlayerW(CallbackInfo ci) {
        if (ModConfig.get().illagers.witch.fleeGoal) {
            this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 3, 1.2, 1.5,
                    (livingEntity) -> true));
        }
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void targetMerchantW(CallbackInfo ci) {
        if (ModConfig.get().general.merchantHostility) {
            this.targetSelector.addGoal(3, new NearestAttackableWitchTargetGoal<>(this, AbstractVillager.class, 10,
                    true, false, null));
        }
    }
}
