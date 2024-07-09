package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlimeEntity.class)
public abstract class SlimeMixin extends MobEntity {
    @Shadow public abstract int getSize();

    @Shadow public abstract void setSize(int size, boolean heal);

    @Shadow public abstract EntityType<? extends SlimeEntity> getType();

    @Shadow protected abstract ParticleEffect getParticles();

    protected SlimeMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;"))
    private void spawnBigger(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                             EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        SlimeEntity slime = (SlimeEntity) (Object) this;
        if (ModConfig.get().overworld.slime.biggerSize
                && !(slime instanceof MagmaCubeEntity)
                && this.random.nextFloat() < 0.05f + 0.05f * difficulty.getClampedLocalDifficulty()) {
            this.setSize(8, true);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tryToMerge(CallbackInfo ci) {
        SlimeEntity slime = (SlimeEntity) (Object) this;
        if (!this.getWorld().isClient()
                && !(slime instanceof MagmaCubeEntity)
                && ModConfig.get().overworld.slime.canMerge
                && !MyComponents.SLIME_COMPONENT.get(this).hasMerged()
                && this.isAlive()
                && this.getSize() < 4
                && (this.getWorld().getTime() % 5) == 0) {
            SlimeEntity slimeEntity = this.getWorld().getClosestEntity(SlimeEntity.class, TargetPredicate.DEFAULT, this,
                    this.getX(), this.getY(), this.getZ(), this.getBoundingBox());
            if (slimeEntity != null
                    && this.getSize() == slimeEntity.getSize()) {
                MyComponents.SLIME_COMPONENT.get(this).setMerged(true);
                slimeEntity.remove(RemovalReason.DISCARDED);
                this.setSize(this.getSize() * 2, true);
                this.getWorld().addParticle(this.getParticles(), this.getX(), this.getY(), this.getZ(),
                        0.0, 0.0, 0.0);
                this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0f,
                        (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);

                if (Sihywtcamd.DEBUG) {
                    this.setCustomName(Text.of("Merged"));
                    this.setCustomNameVisible(true);
                }
            }
        }
    }

    @ModifyVariable(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SlimeEntity;setInvulnerable(Z)V"))
    private SlimeEntity transferMergedGene(SlimeEntity slime, RemovalReason value) {
        boolean hasMerged = MyComponents.SLIME_COMPONENT.get(this).hasMerged();
        MyComponents.SLIME_COMPONENT.get(slime).setMerged(hasMerged);

        if (Sihywtcamd.DEBUG && hasMerged) {
            this.setCustomName(Text.of("Merged Child"));
            this.setCustomNameVisible(true);
        }

        return slime;
    }

    @Inject(method = "damage", at = @At("HEAD"))
    protected void onDamage(LivingEntity target, CallbackInfo ci) {

    }
}
