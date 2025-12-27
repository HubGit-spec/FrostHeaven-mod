package ru.fh.frostheaven.datagen;

import net.neoforged.neoforge.client.model.generators.ModelFile;
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

        ModelFile magneticFurnaceModel = models().cube("magnetic_furnace",
                modLoc("block/magnetic_furnace_bottom"),  // down
                modLoc("block/magnetic_furnace_top"),     // up
                modLoc("block/magnetic_furnace_front"),   // north
                modLoc("block/magnetic_furnace_side"),    // south
                modLoc("block/magnetic_furnace_side"),    // east
                modLoc("block/magnetic_furnace_side")     // west
        );

        // 2. Регистрируем блок с горизонтальной ориентацией
        horizontalBlock(ModBlocks.MAGNETIC_FURNACE.get(), magneticFurnaceModel);

        // 3. Создаем модель для предмета (используем ту же модель, что и для блока)
        simpleBlockItem(ModBlocks.MAGNETIC_FURNACE.get(), magneticFurnaceModel);
    }


    private void  blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
