package com.github.galatynf.sihywtcamd.mixin.piglin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    @Inject(method = "isWearingPiglinSafeArmor", at = @At("HEAD"), cancellable = true)
    private static void needFullGoldenArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().nether.piglin.goldenArmor > 1) {
            int goldenArmorPiece = 0;

            for (ItemStack itemStack: entity.getAllArmorItems()) {
                if (itemStack.isIn(ItemTags.PIGLIN_SAFE_ARMOR)) {
                    goldenArmorPiece++;
                }
            }

            cir.setReturnValue(goldenArmorPiece >= ModConfig.get().nether.piglin.goldenArmor);
        }
    }
}
