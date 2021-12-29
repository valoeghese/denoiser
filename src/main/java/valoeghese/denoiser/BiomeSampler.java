package valoeghese.denoiser;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

public interface BiomeSampler {
	void attachBiomeSource(BiomeSource source);
	void attachRegistry(Registry<Biome> biomeRegistry);
	ResourceKey<Biome> sampleBiome(int qx, int qy, int qz);
}
