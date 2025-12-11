package ru.fh.frostheaven.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import ru.fh.frostheaven.FrostHeaven;
import ru.fh.frostheaven.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_ORE_KEY = registerKey("ice_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAGNETITE_ORE_KEY = registerKey("magnetite_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VOIDITE_ORE_KEY = registerKey("voidite_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        RuleTest netherrackReplaceables = new TagMatchTest(BlockTags.BASE_STONE_NETHER);

        RuleTest airReplaceables = new BlockMatchTest(Blocks.AIR);

        List<OreConfiguration.TargetBlockState> iceOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.ICE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.ICE_DEEPSLATE_ORE.get().defaultBlockState())
        );

        register(context, ICE_ORE_KEY, Feature.ORE, new OreConfiguration(iceOres, 9));
        register(context, MAGNETITE_ORE_KEY, Feature.ORE, new OreConfiguration(netherrackReplaceables,
                ModBlocks.MAGNETITE_ORE.get().defaultBlockState(), 2));
        register(context, VOIDITE_ORE_KEY, Feature.ORE, new OreConfiguration(airReplaceables,
                ModBlocks.VOIDITE_ORE.get().defaultBlockState(), 9));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FrostHeaven.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
