package com.github.galatynf.sihywtcamd.mixin.pillager;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.cardinal.api.PillagerEntityComponentAPI;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
    @ModifyReturnValue(method = "createArrowEntity", at = @At("RETURN"))
    private ProjectileEntity createFireworkRocketEntity(ProjectileEntity original, World world, LivingEntity shooter,
                                                        ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        Optional<PillagerEntityComponentAPI> pillagerComponent = MyComponents.PILLAGER_COMPONENT.maybeGet(shooter);
        if (pillagerComponent.isEmpty() || !pillagerComponent.get().hasFireworkRocket()) return original;

        if (world.random.nextBoolean()) {
            pillagerComponent.get().setFireworkRocket(false);
        }

        FireworkExplosionComponent explosion = new FireworkExplosionComponent(
                FireworkExplosionComponent.Type.SMALL_BALL,
                IntList.of(DyeColor.BLACK.getFireworkColor(), DyeColor.GRAY.getFireworkColor(),
                        DyeColor.LIGHT_GRAY.getFireworkColor(), DyeColor.CYAN.getFireworkColor(),
                        DyeColor.WHITE.getFireworkColor()),
                IntList.of(),
                false,
                false);

        ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
        firework.set(
                DataComponentTypes.FIREWORKS,
                new FireworksComponent(2, List.of(explosion)));

        return new FireworkRocketEntity(world, firework, shooter, shooter.getX(), shooter.getY(), shooter.getZ(), true);
    }
}
