package ru.fh.frostheaven.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import ru.fh.frostheaven.FrostHeaven;
import ru.fh.frostheaven.entity.ModEntities;

@EventBusSubscriber(modid = FrostHeaven.MODID, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        System.out.println("[FH] DEBUG: Событие registerRenderers вызвано!");
        System.out.println("[FH] DEBUG: NULLBREAKER_BULLET = " + ModEntities.NULLBREAKER_BULLET.get());

        event.registerEntityRenderer(
                ModEntities.NULLBREAKER_BULLET.get(),
                NullBreakerBulletRenderer::new
        );
        System.out.println("[FH] DEBUG: Рендерер зарегистрирован");
    }
}