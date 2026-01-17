package ru.fh.frostheaven.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import ru.fh.frostheaven.ModSounds;

public class ModSoundProvider extends SoundDefinitionsProvider {

    public ModSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, "fh", helper);
    }

    @Override
    public void registerSounds() {
        // Регистрация вашего звука nullbreaker_sfx
        this.add(ModSounds.NULLBREAKER, // Ваш SoundEvent
            definition()
                .subtitle("subtitles.fh.nullbreaker_sfx") // Субтитры (если нужны)
                .with(
                    sound(ResourceLocation.fromNamespaceAndPath("fh", "nullbreaker_sfx")) // Указывает на файл nullbreaker_sfx.ogg в папке sounds/
                )
        );
        // Здесь вы можете зарегистрировать другие звуки вашего мода
    }
}