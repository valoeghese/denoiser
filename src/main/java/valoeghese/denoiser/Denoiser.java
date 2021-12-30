package valoeghese.denoiser;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import valoeghese.denoiser.newbiome.TestBiomes;

import java.util.Random;
import java.util.Set;

public class Denoiser implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("denoiser");
	public static final Random RANDOM = new Random();
	public static final Set<ResourceKey<Biome>> NO_NOISE_BIOMES = Set.of(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU);

	// Options
	public static final boolean REMOVE_CARVERS = false;

	public static double denoised(int x, int z, double baseSample, double noCaveSample, BiomeSampler biomeInfo, int blendRadius) {
		int noCaveWeight = 0;
		int totalWeight = 0;
		final int sampleY = 310 >> 2;

		int qx = QuartPos.fromBlock(x);
		int qz = QuartPos.fromBlock(z);

		// it samples this on quarts so this is safe
		for (int xo = -blendRadius; xo <= blendRadius; ++xo) {
			int totalX = xo + qx;

			for (int zo = -blendRadius; zo <= blendRadius; ++zo) {
				if (xo * xo + zo * zo <= blendRadius * blendRadius) { // circular shape, reduce samples.
					ResourceKey<Biome> biome = biomeInfo.sampleBiome(totalX, sampleY, zo + qz);

					if (Denoiser.NO_NOISE_BIOMES.contains(biome)) {
						++noCaveWeight;
					}

					++totalWeight;
				}
			}
		}

		double trueNoCaveWeight = (double) noCaveWeight / (double) totalWeight;
		return noCaveSample * trueNoCaveWeight + baseSample * (1.0 - trueNoCaveWeight);
	}

	@Override
	public void onInitialize() {
		LOGGER.info(funnyMessage[RANDOM.nextInt(funnyMessage.length - 1)]);
		TestBiomes.registerBiome();
	}

	private static String[] funnyMessage = {
			"Why are you using this mod",
			"Why",
			"Valoeghese - doing the worldgen no one asked for since 2018",
			"\"Mod compatibility has gone from difficult to lmao\"",
			"Origin Valley 2 Electic Boogaloo",
			"Calculating the matrix vertices using the chonkamajig beardifier",
			"Writing funny startup messages instead of making the mod!",
			"No one asked for this",
			"Actually BOP kinda did but they decided it's not worth the effort",
			"If a datapacker saw this their eyes would explode",
			"If a datapacker saw this their ears would probably explode too",
			"Lambda, builder, lambda, FACTORY! Lambda, builder, lambda, FACTORY!",
			"Mixin :)",
			"Mixin :(",
			"Gosh damned datapackifying refactors >:(",
			"Stop reading the source code and experience these messages yourself you coward! ;)"
	};
}
