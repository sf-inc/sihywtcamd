package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.advancement.AdvancementRegistry;
import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.LivingEntityMixin;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombifiedPiglinEntity.class)
public abstract class ZombifiedPiglinMixin extends LivingEntityMixin {
    @Shadow public abstract void setTarget(@Nullable LivingEntity target);

    public ZombifiedPiglinMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ZombifiedPiglinEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V"))
    private void replaceWithBruteEquipment(ZombifiedPiglinEntity instance, EquipmentSlot equipmentSlot, ItemStack stack,
                                           Operation<Void> original) {
        if (ModConfig.get().zombies.zombifiedPiglin.bruteVariant
                && this.random.nextFloat() < 0.09f) {
            MyComponents.ZOMBIFIED_PIGLIN_COMPONENT.get(this).setBrute(true);
            original.call(instance, equipmentSlot, new ItemStack(Items.GOLDEN_AXE));
        } else {
            original.call(instance, equipmentSlot, stack);
        }
    }

    @WrapWithCondition(method = "mobTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ZombifiedPiglinEntity;tickAngerPassing()V"))
    private boolean dontAngerWhenCollisionOnly(ZombifiedPiglinEntity instance) {
        return !MyComponents.ZOMBIFIED_PIGLIN_COMPONENT.get(this).isBrute()
                || this.getAttacker() != null;
    }

    @Override
    protected void onPushAwayFrom(Entity entity, CallbackInfo ci) {
        MobEntity thisMob = (MobEntity) (Object) this;
        if (MyComponents.ZOMBIFIED_PIGLIN_COMPONENT.get(this).isBrute()
                && entity instanceof ServerPlayerEntity player
                && thisMob.getTarget() == null) {
            this.setTarget(player);
            AdvancementRegistry.ZOMBIFIED_PIGLIN_BRUTE_COLLISION.trigger(player);
        }
    }
}
