package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.imixin.SpectralSkeletonIMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonMixin extends AbstractSkeletonMobMixin implements SpectralSkeletonIMixin {
    protected SkeletonMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected PersistentProjectileEntity updateArrowProjectile(LivingEntity entity, ItemStack stack, float damageModifier,
                                                               Operation<PersistentProjectileEntity> original) {
        return MyComponents.SKELETON_COMPONENT.get(this).isSpectral()
                ? original.call(entity, new ItemStack(Items.SPECTRAL_ARROW), damageModifier)
                : original.call(entity, stack, damageModifier);
    }

    @Override
    protected void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().skeletons.skeleton.spectralArrow
                && world.getRandom().nextFloat() < 0.05f) {
            this.sihywtcamd$setSpectral();
        }
    }

    @Override
    public void sihywtcamd$setSpectral() {
        MyComponents.SKELETON_COMPONENT.get(this).setSpectral();

        if (Sihywtcamd.DEBUG) {
            this.setCustomName(Text.of("Spectral"));
            this.setCustomNameVisible(true);
        }
    }
}
