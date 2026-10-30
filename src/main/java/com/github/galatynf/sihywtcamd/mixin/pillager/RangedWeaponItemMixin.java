package com.github.galatynf.sihywtcamd.mixin.pillager;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.cardinal.api.PillagerEntityComponentAPI;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import it.unimi.dsi.fastutil.ints.IntList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;

@Mixin(ProjectileWeaponItem.class)
public class RangedWeaponItemMixin {
    @ModifyReturnValue(method = "createProjectile", at = @At("RETURN"))
    private Projectile createFireworkRocketEntity(Projectile original, Level world, LivingEntity shooter,
                                                        ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        Optional<PillagerEntityComponentAPI> pillagerComponent = MyComponents.PILLAGER_COMPONENT.maybeGet(shooter);
        if (pillagerComponent.isEmpty() || !pillagerComponent.get().hasFireworkRocket()) return original;

        if (world.random.nextBoolean()) {
            pillagerComponent.get().setFireworkRocket(false);
        }

        FireworkExplosion explosion = new FireworkExplosion(
                FireworkExplosion.Shape.SMALL_BALL,
                IntList.of(DyeColor.BLACK.getFireworkColor(), DyeColor.GRAY.getFireworkColor(),
                        DyeColor.LIGHT_GRAY.getFireworkColor(), DyeColor.CYAN.getFireworkColor(),
                        DyeColor.WHITE.getFireworkColor()),
                IntList.of(),
                false,
                false);

        ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
        firework.set(
                DataComponents.FIREWORKS,
                new Fireworks(2, List.of(explosion)));

        return new FireworkRocketEntity(world, firework, shooter, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(), true);
    }
}
