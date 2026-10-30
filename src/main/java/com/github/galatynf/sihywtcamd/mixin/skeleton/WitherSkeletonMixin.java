package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WitherSkeleton.class)
public abstract class WitherSkeletonMixin extends AbstractSkeleton {
    protected WitherSkeletonMixin(EntityType<? extends AbstractSkeleton> entityType, Level world) {
        super(entityType, world);
    }

    @WrapOperation(method = "populateDefaultEquipmentSlots", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/skeleton/WitherSkeleton;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V"))
    private void useBetterEquipment(WitherSkeleton instance, EquipmentSlot equipmentSlot, ItemStack stack,
                                    Operation<Void> original) {
        if (ModConfig.get().skeletons.witherSkeleton.bow && !this.isBaby()
                && this.random.nextFloat() < 0.25F) {
            original.call(instance, equipmentSlot, new ItemStack(Items.BOW));
        } else if (ModConfig.get().skeletons.witherSkeleton.ironSword) {
            original.call(instance, equipmentSlot, new ItemStack(Items.IRON_SWORD));
            this.setDropChance(EquipmentSlot.MAINHAND, 0.05f);
        } else {
            original.call(instance, equipmentSlot, stack);
        }
    }
}
