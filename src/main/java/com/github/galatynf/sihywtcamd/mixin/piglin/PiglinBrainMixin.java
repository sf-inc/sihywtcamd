package com.github.galatynf.sihywtcamd.mixin.piglin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    @Inject(method = "wearsGoldArmor", at = @At("HEAD"), cancellable = true)
    private static void needFullGoldenArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().piglinFullGold) {
            boolean isFullGold = true;

            for (ItemStack itemStack: entity.getArmorItems()) {
                if (!(itemStack.getItem() instanceof ArmorItem) || ((ArmorItem) itemStack.getItem()).getMaterial() != ArmorMaterials.GOLD) {
                    isFullGold = false;
                    break;
                }
            }

            cir.setReturnValue(isFullGold);
        }
    }
}
