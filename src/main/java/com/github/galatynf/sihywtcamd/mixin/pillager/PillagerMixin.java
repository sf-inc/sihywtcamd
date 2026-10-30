package com.github.galatynf.sihywtcamd.mixin.pillager;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.CrossbowAirAttackGoal;
import com.github.galatynf.sihywtcamd.imixin.PillatrooperIMixin;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import net.minecraft.network.chat.Component;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pillager.class)
public abstract class PillagerMixin extends MobEntityMixin implements PillatrooperIMixin {
    @Unique
    private int airAttackDelay = 0;
    @Unique
    private final Goal crossbowAirAttackGoal = new CrossbowAirAttackGoal<>(
            (Monster & CrossbowAttackMob & PillatrooperIMixin) (Object) this, 1.0, 16.f);

    protected PillagerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "finalizeSpawn", at = @At("HEAD"))
    private void makePillatrooper(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                                SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (ModConfig.get().illagers.pillager.pillatrooper
                && !this.isPassenger()
                && world.getRandom().nextFloat() < 0.2f) {
            this.sihywtcamd$resetDelay();
            MyComponents.PILLAGER_COMPONENT.get(this).setPillatrooper(true);

            if (ModConfig.get().general.enableDebugInfo) {
                this.setCustomName(Component.nullToEmpty("Pillatrooper"));
                this.setCustomNameVisible(true);
            }
        }
    }

    @Override
    protected void onTickMovement(CallbackInfo ci) {
        if (this.getTarget() != null && this.airAttackDelay > 0) {
            this.airAttackDelay--;
        }
        if (!this.level().isClientSide()) {
            CrossbowAirAttackGoal.removeAirEffectsOnGround(this);
        }
    }

    @Override
    protected void readModData(ValueInput view, CallbackInfo ci) {
        this.airAttackDelay = view.getIntOr("AirAttackDelay", 0);
    }

    @Override
    protected void writeModData(ValueOutput view, CallbackInfo ci) {
        view.putInt("AirAttackDelay", this.airAttackDelay);
    }

    @ModifyArg(method = "enchantSpawnedWeapon", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int changeProbabilityMoreEnchant(int range) {
        return ModConfig.get().illagers.pillager.moreEnchants ? range / 6 : range;
    }

    @Override
    public boolean sihywtcamd$canUseAirAttack() {
        return this.airAttackDelay <= 0;
    }

    @Override
    public void sihywtcamd$resetDelay() {
        this.airAttackDelay = this.random.nextIntBetweenInclusive(100, 200);
    }

    @Override
    public void sihywtcamd$setPillatrooper(boolean isPillatrooper) {
        if (isPillatrooper) {
            this.goalSelector.addGoal(2, this.crossbowAirAttackGoal);
        } else {
            this.goalSelector.removeGoal(this.crossbowAirAttackGoal);
        }
    }
}
