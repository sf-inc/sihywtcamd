package com.github.galatynf.sihywtcamd.mixin.pillager;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.CrossbowAirAttackGoal;
import com.github.galatynf.sihywtcamd.imixin.PillatrooperIMixin;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PillagerEntity.class)
public abstract class PillagerMixin extends IllagerEntity implements PillatrooperIMixin {
    @Unique
    private int airAttackDelay = 0;
    @Unique
    private final Goal crossbowAirAttackGoal = new CrossbowAirAttackGoal<>(
            (HostileEntity & CrossbowUser & PillatrooperIMixin) this, 1.0, 16.f);

    protected PillagerMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("HEAD"))
    private void addSpeedBonusP(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().illagers.pillager.pillatrooper
                && !this.hasVehicle()
                && world.getRandom().nextFloat() < 0.2f) {
            this.sihywtcamd$resetDelay();
            MyComponents.PILLAGER_COMPONENT.get(this).setPillatrooper(true);

            if (Sihywtcamd.DEBUG) {
                this.setCustomName(Text.of("Pillatrooper"));
                this.setCustomNameVisible(true);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getTarget() != null && this.airAttackDelay > 0) {
            this.airAttackDelay--;
        }
    }

    @ModifyArg(method = "enchantMainHandItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private int changeProbabilityMoreEnchant(int range) {
        return ModConfig.get().illagers.pillager.moreEnchants ? range / 3 : range;
    }

    @Override
    public boolean sihywtcamd$canUseAirAttack() {
        return this.airAttackDelay <= 0;
    }

    @Override
    public void sihywtcamd$resetDelay() {
        this.airAttackDelay = this.random.nextBetween(100, 200);
    }

    @Override
    public void sihywtcamd$setPillatrooper(boolean isPillatrooper) {
        if (isPillatrooper) {
            this.goalSelector.add(2, this.crossbowAirAttackGoal);
        } else {
            this.goalSelector.remove(this.crossbowAirAttackGoal);
        }
    }
}
