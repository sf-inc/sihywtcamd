package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.cardinal.api.BabyComponentAPI;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class BabyComponent implements BabyComponentAPI {
    private final Object provider;
    private boolean isBaby = false;

    public BabyComponent(Object provider) {
        this.provider = provider;
    }

    @Override
    public boolean isBaby() {
        return this.isBaby;
    }

    @Override
    public void setBaby(boolean baby) {
        this.isBaby = baby;
        if (this.provider instanceof Entity entity) {
            entity.calculateDimensions();
        }
        MyComponents.BABY_COMPONENT.sync(this.provider);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.setBaby(tag.getBoolean("IsBaby"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IsBaby", this.isBaby);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.setBaby(buf.readBoolean());
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        buf.writeBoolean(this.isBaby);
    }
}
