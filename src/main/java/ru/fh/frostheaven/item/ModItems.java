package ru.fh.frostheaven.item;

import ru.fh.frostheaven.FrostHeaven;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FrostHeaven.MODID);

    public static final DeferredItem<Item> MAGNETITE = ITEMS.register("magnetite",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_MAGNETITE = ITEMS.register("raw_magnetite",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ICE_SHARD = ITEMS.register("ice_shard",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> VOIDITE = ITEMS.register("voidite",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> NULLBREAKER = ITEMS.register("nullbreaker",
            () -> new Nullbreaker(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
