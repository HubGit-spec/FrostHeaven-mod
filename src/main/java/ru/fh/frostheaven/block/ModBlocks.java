package ru.fh.frostheaven.block;

import ru.fh.frostheaven.FrostHeaven;
import ru.fh.frostheaven.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(FrostHeaven.MODID);


    public static final DeferredBlock<Block> ICE_ORE = registerBlock("ice_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.STONE).lightLevel((state) -> 1).noOcclusion()));

    public static final DeferredBlock<Block> ICE_DEEPSLATE_ORE = registerBlock("ice_deepslate_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE).lightLevel((state) -> 1).noOcclusion()));

    public static final DeferredBlock<Block> VOIDITE_ORE = registerBlock("voidite_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5.5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MAGNETITE_ORE = registerBlock("magnetite_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5f).requiresCorrectToolForDrops().sound(SoundType.NETHERRACK)));


    private static <T extends Block> DeferredBlock<T> registerBlock (String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
