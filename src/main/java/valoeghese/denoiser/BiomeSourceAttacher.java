package valoeghese.denoiser;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

public interface BiomeSourceAttacher {
	void attachBiomeSource(BiomeSource source);
	void attachRegistry(Registry<Biome> levelRegistry);
}
