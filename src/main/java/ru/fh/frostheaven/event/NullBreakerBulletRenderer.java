package ru.fh.frostheaven.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import ru.fh.frostheaven.entity.NullbreakerBullet;

@OnlyIn(Dist.CLIENT)
public class NullBreakerBulletRenderer extends EntityRenderer<NullbreakerBullet> {
    // Цвет в формате RGB (R, G, B). Неоново-зелёный: очень яркий зелёный.
    private static final float RED = 0.0F;
    private static final float GREEN = 1.0F; // Максимальная зелень
    private static final float BLUE = 0.2F;  // Лёгкая синева для неонового оттенка
    private static final float ALPHA = 0.9F; // Небольшая прозрачность для эффекта

    // Радиус сферы. Подберите под размер вашего хитбокса (0.5F).
    private static final float SPHERE_RADIUS = 2F;
    // Количество сегментов для сглаживания сферы. Больше = плавнее, но тяжелее.
    private static final int SEGMENTS = 16;
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/white_concrete.png"); // <-- ИСПРАВЛЕНО: Новый метод создания

    public NullBreakerBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        System.out.println("[FH] Создан экземпляр NullBreakerBulletRenderer");
    }

    @Override
    public void render(NullbreakerBullet entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Смещаем сферу вверх на половину её высоты, чтобы она визуально центрировалась
        // на позиции сущности (которая обычно в ногах).
        poseStack.translate(0.0D, SPHERE_RADIUS, 0.0D);

        // Получаем VertexConsumer для рисования. Используем полупрозрачный тип рендера.
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));

        // Рисуем сферу
        this.renderSphere(poseStack, vertexConsumer, packedLight);

        poseStack.popPose();
    }

    private void renderSphere(PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        Matrix4f matrix = poseStack.last().pose();

        // Преобразуем стандартный уровень освещения в максимальную яркость для неонового эффекта
        // 15728880 - максимальное значение для блока, 15728640 - для неба.
        int brightLight = 15728880;

        // Генерация сферы по долготе и широте
        for (int lat = 0; lat <= SEGMENTS; lat++) {
            float v1 = (float) lat / SEGMENTS;
            float theta1 = v1 * (float) Math.PI;
            float sinTheta1 = Mth.sin(theta1);
            float cosTheta1 = Mth.cos(theta1);

            float v2 = (float) (lat + 1) / SEGMENTS;
            float theta2 = v2 * (float) Math.PI;
            float sinTheta2 = Mth.sin(theta2);
            float cosTheta2 = Mth.cos(theta2);

            for (int lon = 0; lon <= SEGMENTS; lon++) {
                float u1 = (float) lon / SEGMENTS;
                float phi1 = u1 * 2.0F * (float) Math.PI;
                float sinPhi1 = Mth.sin(phi1);
                float cosPhi1 = Mth.cos(phi1);

                float u2 = (float) (lon + 1) / SEGMENTS;
                float phi2 = u2 * 2.0F * (float) Math.PI;
                float sinPhi2 = Mth.sin(phi2);
                float cosPhi2 = Mth.cos(phi2);

                // Вершины для двух треугольников, образующих квад
                vertex(matrix, consumer, brightLight,
                        SPHERE_RADIUS * sinTheta1 * cosPhi1,
                        SPHERE_RADIUS * cosTheta1,
                        SPHERE_RADIUS * sinTheta1 * sinPhi1,
                        u1, v1);

                vertex(matrix, consumer, brightLight,
                        SPHERE_RADIUS * sinTheta2 * cosPhi1,
                        SPHERE_RADIUS * cosTheta2,
                        SPHERE_RADIUS * sinTheta2 * sinPhi1,
                        u1, v2);

                vertex(matrix, consumer, brightLight,
                        SPHERE_RADIUS * sinTheta2 * cosPhi2,
                        SPHERE_RADIUS * cosTheta2,
                        SPHERE_RADIUS * sinTheta2 * sinPhi2,
                        u2, v2);

                vertex(matrix, consumer, brightLight,
                        SPHERE_RADIUS * sinTheta1 * cosPhi2,
                        SPHERE_RADIUS * cosTheta1,
                        SPHERE_RADIUS * sinTheta1 * sinPhi2,
                        u2, v1);
            }
        }
    }

    private void vertex(Matrix4f matrix, VertexConsumer consumer, int packedLight,
                        float x, float y, float z, float u, float v) {
        consumer.addVertex(matrix, x, y, z)
                .setColor(RED, GREEN, BLUE, ALPHA)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight) // Используем максимальную яркость для свечения
                .setNormal(0.0F, 1.0F, 0.0F); // Нормали важны для освещения, но у нас своё свечение
    }

    @Override
    public ResourceLocation getTextureLocation(NullbreakerBullet entity) {
        return TEXTURE; // Возвращаем нашу текстуру
    }
}