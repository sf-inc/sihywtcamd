package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.PillagerEntityComponentAPI;
import com.github.galatynf.sihywtcamd.imixin.PillatrooperIMixin;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class PillagerEntityComponent implements PillagerEntityComponentAPI {
    private final Object provider;
    private boolean isPillatrooper = false;

    public PillagerEntityComponent(Object provider) {
        this.provider = provider;
    }

    @Override
    public void setPillatrooper(boolean isPillatrooper) {
        this.isPillatrooper = isPillatrooper;
        if (this.provider instanceof PillatrooperIMixin pillatrooper) {
            pillatrooper.sihywtcamd$setPillatrooper(isPillatrooper);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.setPillatrooper(tag.getBoolean("IsPillatrooper"));
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean("IsPillatrooper", this.isPillatrooper);
    }
}
