package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract EntityType<?> getType();

    @Shadow
    private static Vec3d adjustMovementForCollisions(Vec3d movement, Box entityBoundingBox, List<VoxelShape> collisions) {
        throw new AssertionError();
    }

    @Inject(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
    private void noCollisionForPhantom(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
        if (this.getType().equals(EntityType.PHANTOM) && ModConfig.get().undead.phantom.throughBlocks) {
            Entity entity = (Entity) (Object) this;
            Box box = entity.getBoundingBox();
            WorldBorder worldBorder = entity.getEntityWorld().getWorldBorder();
            List<VoxelShape> collisions = worldBorder.canCollide(entity, box.stretch(movement)) ? List.of(worldBorder.asVoxelShape()) : List.of();
            cir.setReturnValue(adjustMovementForCollisions(movement, box, collisions));
        }
    }
}
