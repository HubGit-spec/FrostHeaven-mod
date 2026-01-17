package ru.fh.frostheaven.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import ru.fh.frostheaven.ModSounds;
import ru.fh.frostheaven.entity.NullbreakerBullet;

public class Nullbreaker extends Item {
    public Nullbreaker(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Воспроизводим звук
        if (!level.isClientSide) {
            NullbreakerBullet bullet = new NullbreakerBullet(level, player);

            // 2. Запускаем его в мир
            level.addFreshEntity(bullet);

            // Этот метод запускает звук на стороне всех игроков
            level.playSound(
                null, // Если передать null, звук услышат все игроки
                player.getX(), player.getY(), player.getZ(), // Позиция звука
                ModSounds.NULLBREAKER.get(), // Ваш звук
                SoundSource.PLAYERS, // Категория звука (PLAYERS, BLOCKS и т.д.)
                2.0F, // Громкость (от 0.0 до 1.0)
                1.0F  // Тон (от 0.5 до 2.0)
            );
        }

        player.getCooldowns().addCooldown(this, 9);
        // Дополнительно можно запустить анимацию "замаха" предмета
        player.swing(hand, true);

        return InteractionResultHolder.success(stack);
    }
}