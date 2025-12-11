package ru.fh.frostheaven.item;

import ru.fh.frostheaven.FrostHeaven;
import ru.fh.frostheaven.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FrostHeaven.MODID);

    public static final Supplier<CreativeModeTab> FROSTHEAVEN_TAB = CREATIVE_MODE_TAB.register("item_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.ICE_SHARD.get()))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .title(Component.translatable("creativetab.fh.item_tab"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.ICE_SHARD);
                        output.accept(ModItems.MAGNETITE);
                        output.accept(ModItems.RAW_MAGNETITE);
                        output.accept(ModItems.VOIDITE);

                        output.accept(ModBlocks.ICE_ORE);
                        output.accept(ModBlocks.ICE_DEEPSLATE_ORE);
                        output.accept(ModBlocks.MAGNETITE_ORE);
                        output.accept(ModBlocks.VOIDITE_ORE);
                    }))
                    .build());

    public static void register(IEventBus eventBus){
            CREATIVE_MODE_TAB.register(eventBus);
    }
}
