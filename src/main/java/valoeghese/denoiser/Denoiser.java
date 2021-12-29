package valoeghese.denoiser;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.Set;

public class Denoiser implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("denoiser");
	public static final Random RANDOM = new Random();
	public static final Set<ResourceKey<Biome>> NO_NOISE_BIOMES = Set.of(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU);

	public static double denoised(int x, int z, double baseSample, double noCaveSample, Registry<Biome> registry, BiomeSource biomeSource, Climate.Sampler sampler) {
//		int noCaveWeight = 0;
//		int totalWeight = 0;
//		final int sampleY = 310 >> 2;
//
//		final int searchRad = 3;
//		int qx = QuartPos.fromBlock(x);
//		int qz = QuartPos.fromBlock(z);
//
//		for (int xo = -searchRad; xo <= searchRad; ++xo) {
//			int totalX = xo + qx;
//
//			for (int zo = -searchRad; zo <= searchRad; ++zo) {
//				ResourceKey<Biome> biome = registry.getResourceKey(biomeSource.getNoiseBiome(totalX, sampleY, zo + qz, sampler)).get();
//
//				if (Denoiser.NO_NOISE_BIOMES.contains(biome)) {
////					System.out.println("nocave biome");
//					++noCaveWeight;
//				}
//
//				++totalWeight;
//			}
//		}
//
//		double trueNoCaveWeight = (double) noCaveWeight / (double) totalWeight;
		//if (trueNoCaveWeight > 0.99999) System.out.println(noCaveSample + " " + baseSample);
		return noCaveSample;//noCaveSample * trueNoCaveWeight + baseSample * (1.0 - trueNoCaveWeight);
	}

	@Override
	public void onInitialize() {
		LOGGER.info(funnyMessage[RANDOM.nextInt(funnyMessage.length - 1)]);
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
