package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.advancement.AdvancementRegistry;
import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.LivingEntityMixin;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombifiedPiglin.class)
public abstract class ZombifiedPiglinMixin extends LivingEntityMixin {
    @Shadow public abstract void setTarget(@Nullable LivingEntity target);

    public ZombifiedPiglinMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @WrapOperation(method = "populateDefaultEquipmentSlots", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/ZombifiedPiglin;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V"))
    private void replaceWithBruteEquipment(ZombifiedPiglin instance, EquipmentSlot equipmentSlot, ItemStack stack,
                                           Operation<Void> original) {
        if (ModConfig.get().zombies.zombifiedPiglin.bruteVariant
                && this.random.nextFloat() < 0.09f) {
            MyComponents.ZOMBIFIED_PIGLIN_COMPONENT.get(this).setBrute(true);
            original.call(instance, equipmentSlot, new ItemStack(Items.GOLDEN_AXE));
        } else {
            original.call(instance, equipmentSlot, stack);
        }
    }

    @WrapWithCondition(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/ZombifiedPiglin;maybeAlertOthers()V"))
    private boolean dontAngerWhenCollisionOnly(ZombifiedPiglin instance) {
        return !MyComponents.ZOMBIFIED_PIGLIN_COMPONENT.get(this).isBrute()
                || this.getLastHurtByMob() != null;
    }

    @Override
    protected void onPushAwayFrom(Entity entity, CallbackInfo ci) {
        Mob thisMob = (Mob) (Object) this;
        if (MyComponents.ZOMBIFIED_PIGLIN_COMPONENT.get(this).isBrute()
                && entity instanceof ServerPlayer player
                && thisMob.getTarget() == null
                && thisMob.canAttack(player)) {
            this.setTarget(player);
            AdvancementRegistry.ZOMBIFIED_PIGLIN_BRUTE_COLLISION.trigger(player);
        }
    }
}
