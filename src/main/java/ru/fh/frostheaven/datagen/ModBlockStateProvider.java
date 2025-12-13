package ru.fh.frostheaven.datagen;

import net.neoforged.neoforge.registries.DeferredBlock;
import ru.fh.frostheaven.FrostHeaven;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import ru.fh.frostheaven.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FrostHeaven.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.ICE_ORE);
        blockWithItem(ModBlocks.ICE_DEEPSLATE_ORE);
        blockWithItem(ModBlocks.MAGNETITE_ORE);
        blockWithItem(ModBlocks.VOIDITE_ORE);

        blockWithItem(ModBlocks.ICE_4);
        blockWithItem(ModBlocks.ICE_5);
        blockWithItem(ModBlocks.ICE_6);
    }

    private void  blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
