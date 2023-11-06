package com.github.galatynf.sihywtcamd.cardinal;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.api.WitherEntityComponentAPI;
import com.github.galatynf.sihywtcamd.cardinal.implem.WitherEntityComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.util.Identifier;

public final class MyComponents implements EntityComponentInitializer {
    public static final ComponentKey<WitherEntityComponentAPI> WITHER_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(Sihywtcamd.MOD_ID, "wither_component"), WitherEntityComponentAPI.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(WitherEntity.class, WITHER_COMPONENT, entity -> new WitherEntityComponent());
    }
}
