package ru.fh.frostheaven.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import ru.fh.frostheaven.FrostHeaven;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> ICE_ORE_KEY = registerKey("ice_ore_placed");
    public static final ResourceKey<PlacedFeature> MAGNETITE_ORE_KEY = registerKey("magnetite_ore_placed");
    public static final ResourceKey<PlacedFeature> VOIDITE_ORE_KEY = registerKey("voidite_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, ICE_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ICE_ORE_KEY),
                ModOrePlacement.commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(80))));

        register(context, MAGNETITE_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.MAGNETITE_ORE_KEY),
                ModOrePlacement.commonOrePlacement(192, HeightRangePlacement.uniform(VerticalAnchor.absolute(3), VerticalAnchor.absolute(125))));

        register(context, VOIDITE_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.VOIDITE_ORE_KEY),
                ModOrePlacement.rareOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(20))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FrostHeaven.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
