package ru.fh.frostheaven.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import ru.fh.frostheaven.FrostHeaven;
import ru.fh.frostheaven.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FrostHeaven.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ICE_ORE.get())
                .add(ModBlocks.ICE_DEEPSLATE_ORE.get())
                .add(ModBlocks.MAGNETITE_ORE.get())
                .add(ModBlocks.VOIDITE_ORE.get())

                .add(ModBlocks.ICE_4.get())
                .add(ModBlocks.ICE_5.get())
                .add(ModBlocks.ICE_6.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.ICE_ORE.get())
                .add(ModBlocks.MAGNETITE_ORE.get())

                .add(ModBlocks.ICE_4.get())
                .add(ModBlocks.ICE_5.get())
                .add(ModBlocks.ICE_6.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.ICE_DEEPSLATE_ORE.get())
                .add(ModBlocks.VOIDITE_ORE.get());
    }
}
