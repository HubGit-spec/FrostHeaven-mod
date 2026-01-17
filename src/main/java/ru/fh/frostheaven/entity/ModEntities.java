package ru.fh.frostheaven.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "fh");

    public static final Supplier<EntityType<NullbreakerBullet>> NULLBREAKER_BULLET =
            ENTITIES.register("nullbreaker_bullet",
                    () -> EntityType.Builder.<NullbreakerBullet>of(NullbreakerBullet::new, MobCategory.MISC)
                            .sized(4F, 4F) // Хитбокс шара (ширина, высота)
                            .clientTrackingRange(4) // На каком расстоянии игрок получает обновления о сущности
                            .updateInterval(20)
                            .build("fh:nullbreaker_bullet")
            );
}