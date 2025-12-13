package ru.fh.frostheaven.datagen;

import ru.fh.frostheaven.block.ModBlocks;
import ru.fh.frostheaven.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.ICE_4.get());
        dropSelf(ModBlocks.ICE_5.get());
        dropSelf(ModBlocks.ICE_6.get());

        add(ModBlocks.ICE_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.ICE_ORE.get(), ModItems.ICE_SHARD.get(), 1, 3));
        add(ModBlocks.ICE_DEEPSLATE_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.ICE_DEEPSLATE_ORE.get(), ModItems.ICE_SHARD.get(), 1, 4));
        add(ModBlocks.MAGNETITE_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.MAGNETITE_ORE.get(), ModItems.RAW_MAGNETITE.get(), 1, 2));
        add(ModBlocks.VOIDITE_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.VOIDITE_ORE.get(), ModItems.VOIDITE.get(), 2, 4));
    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        float adjustedMin = minDrops * 3f;
        float adjustedMax = maxDrops * 3f;

        return this.createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(adjustedMin, adjustedMax)))
                        .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE), 1))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
