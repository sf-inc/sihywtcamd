package com.github.galatynf.sihywtcamd.entity;

import com.github.galatynf.sihywtcamd.init.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CobwebProjectileEntity extends ProjectileEntity {
    private boolean inGround = false;
    private int inGroundTime = 100;

    public CobwebProjectileEntity(EntityType<CobwebProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static CobwebProjectileEntity create(World world, LivingEntity owner) {
        CobwebProjectileEntity cobwebProjectileEntity = new CobwebProjectileEntity(EntityRegistry.COBWEB, world);
        cobwebProjectileEntity.setPosition(owner.getPos());
        cobwebProjectileEntity.setOwner(owner);
        return cobwebProjectileEntity;
    }

    public void tick() {
        super.tick();
        Vec3d velocity = this.getVelocity();
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            double d = velocity.horizontalLength();
            this.setYaw((float)(MathHelper.atan2(velocity.x, velocity.z) * 57.2957763671875D));
            this.setPitch((float)(MathHelper.atan2(velocity.y, d) * 57.2957763671875D));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }

        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = this.getWorld().getBlockState(blockPos);
        Vec3d pos2;
        if (!blockState.isAir()) {
            VoxelShape voxelShape = blockState.getCollisionShape(this.getWorld(), blockPos);
            if (!voxelShape.isEmpty()) {
                pos2 = this.getPos();

                for (Box box : voxelShape.getBoundingBoxes()) {
                    if (box.offset(blockPos).contains(pos2)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }

        if (this.isOnFire() || this.isTouchingWater()) {
            this.discard();
        }

        if (this.inGround) {
            if (--this.inGroundTime < 0) {
                this.discard();
            }
        } else {
            Vec3d pos = this.getPos();
            pos2 = pos.add(velocity);
            HitResult hitResult = this.getWorld().raycast(new RaycastContext(pos, pos2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
            if (hitResult.getType() != HitResult.Type.MISS) {
                pos2 = hitResult.getPos();
            }

            while (!this.isRemoved()) {
                EntityHitResult entityHitResult = this.getEntityCollision(pos, pos2);
                if (entityHitResult != null) {
                    hitResult = entityHitResult;
                }

                if (hitResult != null) {
                    this.onCollision(hitResult);
                    this.velocityDirty = true;
                }

                if (entityHitResult == null) {
                    break;
                }

                hitResult = null;
            }

            velocity = this.getVelocity();
            double vX = velocity.x;
            double vY = velocity.y;
            double vZ = velocity.z;

            double x2 = this.getX() + vX;
            double y2 = this.getY() + vY;
            double z2 = this.getZ() + vZ;
            double l = velocity.horizontalLength();

            this.setYaw((float)(MathHelper.atan2(vX, vZ) * 57.2957763671875D));
            this.setPitch((float)(MathHelper.atan2(vY, l) * 57.2957763671875D));
            this.setPitch(updateRotation(this.prevPitch, this.getPitch()));
            this.setYaw(updateRotation(this.prevYaw, this.getYaw()));
            this.setVelocity(velocity.multiply(0.99F));

            if (!this.hasNoGravity()) {
                velocity = this.getVelocity();
                this.setVelocity(velocity.x, velocity.y - 0.05000000074505806D, velocity.z);
            }

            this.setPosition(x2, y2, z2);
            this.checkBlockCollision();
        }
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.getWorld(), this, currentPosition, nextPosition,
                this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!entityHitResult.getEntity().isTouchingWater()) {
            BlockPos blockPos = entityHitResult.getEntity().getBlockPos().withY(this.getBlockY());
            if (getWorld().getBlockState(blockPos).isAir()) {
                this.getWorld().setBlockState(blockPos, BlockRegistry.MESSY_COBWEB.getDefaultState());
            }
        }

        this.discard();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.build();
    }
}
