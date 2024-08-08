package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.CobwebAttackGoal;
import com.github.galatynf.sihywtcamd.entity.CobwebProjectileEntity;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;
import static com.github.galatynf.sihywtcamd.init.BlockRegistry.MESSY_COBWEB;
import static com.github.galatynf.sihywtcamd.init.SoundRegistry.SPIDER_SPIT_EVENT;

@Mixin(SpiderEntity.class)
public abstract class SpiderMobMixin extends MobEntityMixin implements RangedAttackMob {
    @Unique
    private Goal sihywtcamd_cobwebAttackGoal;

    @Unique
    private static final Identifier BABY_MODIFIER_ID = id("baby_spawn_malus");
    @Unique
    private static final EntityAttributeModifier BABY_MODIFIER = new EntityAttributeModifier(
                    BABY_MODIFIER_ID, -0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

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
        MyComponents.BABY_COMPONENT.get(this).setBaby(baby);

        EntityAttributeInstance attackDamage = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (attackDamage != null) attackDamage.removeModifier(BABY_MODIFIER_ID);
        EntityAttributeInstance maxHealth = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealth != null) maxHealth.removeModifier(BABY_MODIFIER_ID);

        if (baby) {
            if (attackDamage != null) attackDamage.addTemporaryModifier(BABY_MODIFIER);
            if (maxHealth != null) maxHealth.addTemporaryModifier(BABY_MODIFIER);

            if (ModConfig.get().arthropods.generalSpiders.webProjectileGoal) {
                this.goalSelector.remove(this.sihywtcamd_cobwebAttackGoal);
            }
        }
    }

    @ModifyExpressionValue(method = "slowMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean slowMovement(boolean original, BlockState state, Vec3d multiplier) {
        return original || state.isOf(MESSY_COBWEB);
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        CobwebProjectileEntity cobwebProjectileEntity = CobwebProjectileEntity.create(this.getWorld(), this);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333D) - this.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        cobwebProjectileEntity.setVelocity(d, e + g * 0.20000000298023224D, f, 1.0F, (float) (14 - this.getWorld().getDifficulty().getId() * 2));
        this.playSound(SPIDER_SPIT_EVENT, 0.33F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(cobwebProjectileEntity);
    }
}
