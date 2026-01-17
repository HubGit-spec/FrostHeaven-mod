package ru.fh.frostheaven.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import ru.fh.frostheaven.item.ModItems;

import java.util.List;

public class NullbreakerBullet extends ThrowableItemProjectile {
    final private int maxLifetimeTicks = 20;
    final private float speedPerTick = 3F;
    private long lastProcessedTick = 0;

    public NullbreakerBullet(EntityType<? extends NullbreakerBullet> type, Level level) {
        super(type, level);
    }


    public NullbreakerBullet(Level level, LivingEntity shooter) {
        super(ModEntities.NULLBREAKER_BULLET.get(), shooter, level);
        this.setNoGravity(true);
        Vec3 look = shooter.getLookAngle();
        Vec3 eyePos = shooter.getEyePosition();
        Vec3 spawnPos = eyePos.add(look.scale(2)).add(new Vec3(0, -2, 0));
        //this.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

        this.shoot(look.x, look.y, look.z, speedPerTick, 0.0F);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        // Всегда возвращаем явные размеры
        EntityDimensions dims = EntityDimensions.scalable(4F, 4F);
        return dims;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity hitEntity = hitResult.getEntity();

        if (!this.level().isClientSide) {
            if (hitEntity instanceof LivingEntity livingTarget) {
                DamageSource damageSource = this.damageSources().thrown(this, this.getOwner());
                boolean wasHurt = livingTarget.hurt(damageSource, 7.5F);


            }
        }
        this.discard();
    }
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        this.discard();
    }
    @Override
    public void tick() {
        long currentGameTick = this.level().getGameTime();
        if (currentGameTick == lastProcessedTick) return;
        lastProcessedTick = currentGameTick;

        Vec3 currentPos = this.position();
        Vec3 motion = this.getDeltaMovement();
        Vec3 newPos = currentPos.add(motion);
        AABB futureBox = this.getBoundingBox().move(motion);

        // ОТЛАДКА: позиция и размер
        System.out.println("[FH] Позиция: " + currentPos + ", Будущий AABB: " + futureBox);

        // ПРОВЕРКА СТОЛКНОВЕНИЙ
        HitResult hit = this.checkCollisions(futureBox);

        if (hit != null) {
            this.setPos(newPos.x, newPos.y, newPos.z);

            if (hit.getType() == HitResult.Type.BLOCK) {
                System.out.println("[FH] Попадание в блок!");
                this.onHitBlock((BlockHitResult) hit);
            } else if (hit.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) hit).getEntity();
                System.out.println("[FH] Попадание в сущность: " + target.getName().getString());
                this.onHitEntity((EntityHitResult) hit);
            }
            return;
        }

        this.setPos(newPos.x, newPos.y, newPos.z);
        this.setBoundingBox(this.makeBoundingBox());

        if (this.tickCount++ > maxLifetimeTicks) {
            this.discard();
        }
    }

    // ================== ОБЩАЯ ПРОВЕРКА ==================
    private HitResult checkCollisions(AABB bulletBox) {
        // Сначала блоки
        BlockHitResult blockHit = this.checkBlockCollision(bulletBox);
        if (blockHit != null) return blockHit;

        // Потом сущности
        return this.checkEntityCollision(bulletBox);
    }

    // ================== ПРОВЕРКА БЛОКОВ ==================
    private BlockHitResult checkBlockCollision(AABB bulletBox) {
        // Проверяем блоки по 8 углам AABB + центру
        double[] xPoints = {bulletBox.minX, bulletBox.maxX, bulletBox.getCenter().x};
        double[] yPoints = {bulletBox.minY, bulletBox.maxY, bulletBox.getCenter().y};
        double[] zPoints = {bulletBox.minZ, bulletBox.maxZ, bulletBox.getCenter().z};

        for (double x : xPoints) {
            for (double y : yPoints) {
                for (double z : zPoints) {
                    BlockPos pos = BlockPos.containing(x, y, z);
                    if (this.isSolidBlock(pos)) {
                        Vec3 hitPoint = new Vec3(x, y, z);
                        Direction dir = this.calculateHitDirection(bulletBox, pos);
                        return new BlockHitResult(hitPoint, dir, pos, false);
                    }
                }
            }
        }
        return null;
    }

    private boolean isSolidBlock(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        // Правильная проверка: блок не воздух И имеет коллизионную форму
        return !state.isAir() && !state.getCollisionShape(this.level(), pos).isEmpty();
    }

    private Direction calculateHitDirection(AABB bulletBox, BlockPos blockPos) {
        Vec3 bulletCenter = bulletBox.getCenter();
        Vec3 blockCenter = new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
        Vec3 diff = bulletCenter.subtract(blockCenter);

        double absX = Math.abs(diff.x);
        double absY = Math.abs(diff.y);
        double absZ = Math.abs(diff.z);

        if (absX > absY && absX > absZ) {
            return diff.x > 0 ? Direction.EAST : Direction.WEST;
        } else if (absY > absZ) {
            return diff.y > 0 ? Direction.UP : Direction.DOWN;
        } else {
            return diff.z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    // ================== ПРОВЕРКА СУЩНОСТЕЙ ==================
    private EntityHitResult checkEntityCollision(AABB bulletBox) {
        // Получаем все сущности, чьи AABB пересекаются с нашим
        List<Entity> entities = this.level().getEntities(
                this,
                bulletBox,
                entity -> {
                    // Фильтр: не владелец, можно поразить, жива
                    return entity != this.getOwner()
                            && entity.isPickable()
                            && entity.isAlive();
                }
        );

        if (entities.isEmpty()) return null;

        // Ищем ближайшую сущность к центру нашего AABB
        Entity closest = null;
        double closestDist = Double.MAX_VALUE;
        Vec3 center = bulletBox.getCenter();

        for (Entity entity : entities) {
            // Дополнительная проверка пересечения AABB
            if (bulletBox.intersects(entity.getBoundingBox())) {
                double dist = center.distanceToSqr(entity.position());
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = entity;
                }
            }
        }

        return closest != null ? new EntityHitResult(closest) : null;
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.NULLBREAKER.get();
    }
}