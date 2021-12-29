package valoeghese.denoiser;

import net.minecraft.world.level.levelgen.NoiseChunk;

public class ChunkAwareNoiseFiller implements NoiseChunk.NoiseFiller {
	public ChunkAwareNoiseFiller(NoiseSampler sampler) {
		this.noiseSampler = sampler;
	}

	private NoiseSampler noiseSampler;
	private BiomeSampler biomeSampler;

	public ChunkAwareNoiseFiller withBiomeSampler(BiomeSampler sampler) {
		this.biomeSampler = sampler;
		return this;
	}

	@Override
	public double calculateNoise(int x, int y, int z) {
		return this.noiseSampler.sample(x, y, z, this.biomeSampler);
	}

	@FunctionalInterface
	public interface NoiseSampler {
		double sample(int x, int y, int z, BiomeSampler chunk);
	}
}
