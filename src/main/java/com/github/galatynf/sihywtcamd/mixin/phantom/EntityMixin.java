package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract EntityType<?> getType();

    @Shadow
    private static Vec3 collideWithShapes(Vec3 movement, AABB entityBoundingBox, List<VoxelShape> collisions) {
        throw new AssertionError();
    }

    @Inject(method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
    private void noCollisionForPhantom(Vec3 movement, CallbackInfoReturnable<Vec3> cir) {
        if (this.getType().equals(EntityType.PHANTOM) && ModConfig.get().undead.phantom.throughBlocks) {
            Entity entity = (Entity) (Object) this;
            AABB box = entity.getBoundingBox();
            WorldBorder worldBorder = entity.level().getWorldBorder();
            List<VoxelShape> collisions = worldBorder.isInsideCloseToBorder(entity, box.expandTowards(movement)) ? List.of(worldBorder.getCollisionShape()) : List.of();
            cir.setReturnValue(collideWithShapes(movement, box, collisions));
        }
    }
}
