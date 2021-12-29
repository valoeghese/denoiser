package valoeghese.denoiser;

import net.minecraft.world.level.levelgen.NoiseChunk;

public class ChunkAwareNoiseFiller implements NoiseChunk.NoiseFiller {
	public ChunkAwareNoiseFiller(Sampler sampler) {
		this.sampler = sampler;
	}

	private Sampler sampler;
	private NoiseChunk chunk;

	public ChunkAwareNoiseFiller withChunk(NoiseChunk chunk) {
		this.chunk = chunk;
		return this;
	}

	@Override
	public double calculateNoise(int x, int y, int z) {
		return this.sampler.sample(x, y, z, this.chunk);
	}

	@FunctionalInterface
	public interface Sampler {
		double sample(int x, int y, int z, NoiseChunk chunk);
	}
}
