package valoeghese.denoiser.mixin;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.TerrainInfo;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import valoeghese.denoiser.BiomeSampler;
import valoeghese.denoiser.ChunkAwareNoiseFiller;
import valoeghese.denoiser.Denoiser;

@Mixin(NoiseSampler.class)
public abstract class FuckNoiseCavesInParticular implements BiomeSampler {
	@Shadow
	@Final
	private BlendedNoise blendedNoise;
	@Shadow
	@Final
	private boolean isNoiseCavesEnabled;

	@Unique
	private BiomeSource denoiser_biomesource;
	@Unique
	private Registry<Biome> denoiser_registry; // just in case multithreading madness

	@Inject(at = @At("RETURN"), cancellable = true, method = "calculateBaseNoise(IIILnet/minecraft/world/level/levelgen/TerrainInfo;Lnet/minecraft/world/level/levelgen/blending/Blender;)D")
	private void modifyBaseNoise(int x, int y, int z, TerrainInfo terrainInfo, Blender blender, CallbackInfoReturnable<Double> info) {
		if (this.isNoiseCavesEnabled) {
			double baseSample = info.getReturnValue();
			double noCaveSample = this.calculateBaseNoise(
					x,
					y,
					z,
					terrainInfo,
					this.blendedNoise.calculateNoise(x, y, z),
					true,
					true,
					blender);

			if (baseSample != noCaveSample) { // only modify if we have to
				info.setReturnValue(Denoiser.denoised(x, z, baseSample, noCaveSample, this)); // transition
			}
		}
	}

	@Shadow
	abstract protected double calculateBaseNoise(int x, int y, int z, TerrainInfo terrainInfo, double d, boolean ignoreNoiseCaves, boolean bl2, Blender blender);

	/**
	 * @reason inject here so we can sample on the interpolated points for far less lag
	 */
	@Inject(
			method = "yLimitedInterpolatableNoise",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void removeSomeNoodleCaves(NormalNoise normalNoise, int i, int j, int defaultValue, double frequency, CallbackInfoReturnable<NoiseChunk.InterpolatableNoise> info) {
		if (defaultValue == -1) { // noodle toggle
			ChunkAwareNoiseFiller noodleFilterableFiller = new ChunkAwareNoiseFiller((x, y, z, biomeSampler) -> {
				if (y <= j && y >= i) {
					double result = normalNoise.getValue((double) x * frequency, (double) y * frequency, (double) z * frequency);

					if (result >= 0.0) { // noodle cave
						result = Denoiser.denoised(x, z, result, -0.2, biomeSampler);
					}

					return result;
				} else {
					return defaultValue;
				}
			});

			info.setReturnValue(chunk -> {
				AccessorNoiseChunk chunk_ = ((AccessorNoiseChunk) chunk);
				return chunk_.callCreateNoiseInterpolator(noodleFilterableFiller.withBiomeSampler((BiomeSampler) chunk_.getSampler()));
			});
		}
	}

	@Override
	public void attachBiomeSource(BiomeSource source) {
		this.denoiser_biomesource = source;
	}

	@Override
	public void attachRegistry(Registry<Biome> biomeRegistry) {
		if (this.denoiser_registry == null) this.denoiser_registry = biomeRegistry;
	}

	@Override
	public ResourceKey<Biome> sampleBiome(int qx, int qy, int qz) {
		return this.denoiser_registry.getResourceKey(this.denoiser_biomesource.getNoiseBiome(qx, qy, qz, (Climate.Sampler) this)).get();
	}
}
