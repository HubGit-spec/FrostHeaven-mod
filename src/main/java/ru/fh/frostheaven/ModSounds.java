package ru.fh.frostheaven;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "fh");

    public static final DeferredHolder<SoundEvent, SoundEvent> NULLBREAKER = SOUNDS.register("nullbreaker_sfx",
        () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("fh", "nullbreaker_sfx"))
    );
}