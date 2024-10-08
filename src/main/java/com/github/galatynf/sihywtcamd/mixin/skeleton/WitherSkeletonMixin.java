package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WitherSkeletonEntity.class)
public abstract class WitherSkeletonMixin extends AbstractSkeletonEntity {
    protected WitherSkeletonMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @WrapOperation(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/WitherSkeletonEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V"))
    private void useBetterEquipment(WitherSkeletonEntity instance, EquipmentSlot equipmentSlot, ItemStack stack,
                                    Operation<Void> original) {
        if (ModConfig.get().skeletons.witherSkeleton.bow && !this.isBaby()
                && this.random.nextFloat() < 0.25F) {
            original.call(instance, equipmentSlot, new ItemStack(Items.BOW));
        } else if (ModConfig.get().skeletons.witherSkeleton.ironSword) {
            original.call(instance, equipmentSlot, new ItemStack(Items.IRON_SWORD));
            this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.05f);
        } else {
            original.call(instance, equipmentSlot, stack);
        }
    }
}
