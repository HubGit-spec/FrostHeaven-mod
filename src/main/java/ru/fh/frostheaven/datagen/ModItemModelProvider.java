package ru.fh.frostheaven.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import ru.fh.frostheaven.FrostHeaven;
import ru.fh.frostheaven.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FrostHeaven.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.ICE_SHARD.get());
        basicItem(ModItems.RAW_MAGNETITE.get());
        basicItem(ModItems.MAGNETITE.get());
        basicItem(ModItems.VOIDITE.get());
    }
}
