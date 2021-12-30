package valoeghese.denoiser.newbiome;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

public class TestBiomes extends OverworldBiomes {
	public static final ResourceKey<Biome> TEST_1_18_BIOME = register("test_biome");

	public static final Biome BIOME = new Biome.BiomeBuilder()
			.biomeCategory(Biome.BiomeCategory.DESERT)
			.temperature(0.5f)
			.downfall(0.5f)
			.precipitation(Biome.Precipitation.NONE)
			.generationSettings(new BiomeGenerationSettings.Builder()
					.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE)
					.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND)
					.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON)
					.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.FOREST_ROCK)
					.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.ICEBERG_BLUE)
					.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TreePlacements.JUNGLE_BUSH)
					.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TreePlacements.FANCY_OAK_BEES)
					.build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
					.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, 10, 5, 7)) // weight min max
					.build())
			.specialEffects(new BiomeSpecialEffects.Builder()
					.fogColor(0xFF0000)
					.waterColor(0x44BFEF)
					.waterFogColor(0x00A4A4)
					.skyColor(calculateSkyColor(0.5f))
					.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
					.backgroundMusic(null)
					.build())
			.build();

	public static void registerBiome() {
		Registry.register(BuiltinRegistries.BIOME, TEST_1_18_BIOME, BIOME);
	}

	private static ResourceKey<Biome> register(String string) {
		return ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("denoiser", string));
	}
}
