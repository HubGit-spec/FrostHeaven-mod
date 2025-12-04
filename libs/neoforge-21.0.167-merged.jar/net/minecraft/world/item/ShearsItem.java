package net.minecraft.world.item;

import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class ShearsItem extends Item {
    public ShearsItem(Item.Properties properties) {
        super(properties);
    }

    public static Tool createToolProperties() {
        return new Tool(
            List.of(
                Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F),
                Tool.Rule.overrideSpeed(BlockTags.LEAVES, 15.0F),
                Tool.Rule.overrideSpeed(BlockTags.WOOL, 5.0F),
                Tool.Rule.overrideSpeed(List.of(Blocks.VINE, Blocks.GLOW_LICHEN), 2.0F)
            ),
            1.0F,
            1
        );
    }

    /**
     * Called when a {@link net.minecraft.world.level.block.Block} is destroyed using this Item. Return {@code true} to trigger the "Use Item" statistic.
     */
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!level.isClientSide && !state.is(BlockTags.FIRE)) {
            stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);
        }

        return state.is(BlockTags.LEAVES)
            || state.is(Blocks.COBWEB)
            || state.is(Blocks.SHORT_GRASS)
            || state.is(Blocks.FERN)
            || state.is(Blocks.DEAD_BUSH)
            || state.is(Blocks.HANGING_ROOTS)
            || state.is(Blocks.VINE)
            || state.is(Blocks.TRIPWIRE)
            || state.is(BlockTags.WOOL);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, net.minecraft.world.InteractionHand hand) {
        if (entity instanceof net.neoforged.neoforge.common.IShearable target) {
            if (entity.level().isClientSide) return InteractionResult.CONSUME;
            BlockPos pos = entity.blockPosition();
            if (target.isShearable(player, stack, entity.level(), pos)) {
                target.onSheared(player, stack, entity.level(), pos)
                      .forEach(drop -> target.spawnShearedDrop(entity.level(), pos, drop));
                entity.gameEvent(GameEvent.SHEAR, player);
                stack.hurtAndBreak(1, player, hand == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return net.neoforged.neoforge.common.ItemAbilities.DEFAULT_SHEARS_ACTIONS.contains(itemAbility);
    }

    /**
     * Called when this item is used when targeting a Block
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        BlockState blockstate1 = blockstate.getToolModifiedState(context, net.neoforged.neoforge.common.ItemAbilities.SHEARS_TRIM, false);
        if (blockstate1 != null) {
            Player player = context.getPlayer();
            ItemStack itemstack = context.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
            }

            level.setBlockAndUpdate(blockpos, blockstate1);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(context.getPlayer(), blockstate1));
            if (player != null) {
                itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }
}
