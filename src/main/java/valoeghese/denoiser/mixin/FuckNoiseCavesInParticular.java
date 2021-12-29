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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import valoeghese.denoiser.BiomeSampler;
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

	@Redirect(
			method = "lambda$makeBaseNoiseFiller$10",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseChunk$Sampler;sample()D", ordinal = 1)
	)
	private static double removeSomeNoodleCaves(NoiseChunk.Sampler instance, NoiseChunk.Sampler _1, NoiseChunk.Sampler _2, NoiseChunk.Sampler _3, NoiseChunk.Sampler _4, NoiseChunk.Sampler _5, NoiseChunk.NoiseFiller filler, NoiseChunk chunk, int x, int y, int z) {
		double result = instance.sample();

		if (result >= 0.0) { // noodle cave
			BiomeSampler sampler = (BiomeSampler) ((AccessorNoiseChunk) chunk).getSampler();

			return Denoiser.denoised(x, z, result, -0.1, sampler);
		} else {
			return result;
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
