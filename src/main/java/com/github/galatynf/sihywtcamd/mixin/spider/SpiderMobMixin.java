package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.CobwebAttackGoal;
import com.github.galatynf.sihywtcamd.entity.CobwebProjectileEntity;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.MESSY_COBWEB;
import static com.github.galatynf.sihywtcamd.Sihywtcamd.SPIDER_BABY;

@Mixin(SpiderEntity.class)
public abstract class SpiderMobMixin extends MobEntityMixin implements RangedAttackMob {
    @Unique
    private Goal sihywtcamd_cobwebAttackGoal;

    protected SpiderMobMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void addSpiderGoals(CallbackInfo ci) {
        if (ModConfig.get().general.merchantHostility) {
            this.targetSelector.add(3, new SpiderEntity.TargetGoal<>((SpiderEntity) (Object) this, MerchantEntity.class));
        }
        if (ModConfig.get().arthropods.generalSpiders.webProjectileGoal) {
            this.sihywtcamd_cobwebAttackGoal = new CobwebAttackGoal<>((HostileEntity & RangedAttackMob) (Object) this, 1.0D, 40, 15.0F);
            this.goalSelector.add(4, this.sihywtcamd_cobwebAttackGoal);
        }
    }

    @Override
    protected void onSetBaby(boolean baby, CallbackInfo ci) {
        this.getDataTracker().set(SPIDER_BABY, baby);
        if (baby) {
            EntityAttributeInstance attackDamage = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            EntityAttributeInstance maxHealth = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

            Objects.requireNonNull(attackDamage).addPersistentModifier(new EntityAttributeModifier(
                    "Baby spawn malus", -0.5F * attackDamage.getValue(), EntityAttributeModifier.Operation.ADDITION));
            Objects.requireNonNull(maxHealth).addPersistentModifier(new EntityAttributeModifier(
                    "Baby spawn malus", -0.5F * maxHealth.getValue(), EntityAttributeModifier.Operation.ADDITION));

            if (ModConfig.get().arthropods.generalSpiders.webProjectileGoal) {
                this.goalSelector.remove(this.sihywtcamd_cobwebAttackGoal);
            }
        }
    }

    @Override
    protected void readModDataFromNbt(NbtCompound tag, CallbackInfo ci) {
        this.setBaby(tag.getBoolean("IsBaby"));
    }

    @Override
    protected void writeModDataToNbt(NbtCompound tag, CallbackInfo ci) {
        tag.putBoolean("IsBaby", this.isBaby());
    }

    @ModifyReturnValue(method = "getActiveEyeHeight", at = @At("RETURN"))
    private float updateSpiderEyeHeight(float original) {
        if (ModConfig.get().arthropods.spider.baby) {
            return original * this.getScaleFactor();
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "slowMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean slowMovement(boolean original, BlockState state, Vec3d multiplier) {
        return original || state.isOf(MESSY_COBWEB);
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        CobwebProjectileEntity cobwebProjectileEntity = CobwebProjectileEntity.create(this.getWorld(), this);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333D) - this.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        cobwebProjectileEntity.setVelocity(d, e + g * 0.20000000298023224D, f, 1.0F, (float) (14 - this.getWorld().getDifficulty().getId() * 2));
        this.playSound(Sihywtcamd.SPIDER_SPIT_EVENT, 0.33F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(cobwebProjectileEntity);
    }
}
