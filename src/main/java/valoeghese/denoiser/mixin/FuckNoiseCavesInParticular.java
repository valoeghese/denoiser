package valoeghese.denoiser.mixin;

import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import valoeghese.denoiser.BiomeInfoAttacher;
import valoeghese.denoiser.Denoiser;

@Mixin(NoiseSampler.class)
public abstract class FuckNoiseCavesInParticular implements BiomeInfoAttacher {
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
				int noCaveWeight = 0;
				int totalWeight = 0;
				final int sampleY = 310 >> 2;

				final int searchRad = 3;
				int qx = QuartPos.fromBlock(x);
				int qz = QuartPos.fromBlock(z);

				for (int xo = -searchRad; xo <= searchRad; ++xo) {
					int totalX = xo + qx;

					for (int zo = -searchRad; zo <= searchRad; ++zo) {
						if (Denoiser.NO_NOISE_BIOMES.contains(
								this.denoiser_registry.getKey(this.denoiser_biomesource.getNoiseBiome(totalX, sampleY, zo + qz, (Climate.Sampler) this))
						)) {
							++noCaveWeight;
						}

						++totalWeight;
					}
				}

				double trueNoCaveWeight = (double) noCaveWeight / (double) totalWeight;
				info.setReturnValue(noCaveSample * trueNoCaveWeight + baseSample * (1.0 - trueNoCaveWeight)); // transition
			}
		}
	}

	@Shadow
	abstract protected double calculateBaseNoise(int x, int y, int z, TerrainInfo terrainInfo, double d, boolean ignoreNoiseCaves, boolean bl2, Blender blender);

	@Override
	public void attachBiomeSource(BiomeSource source) {
		this.denoiser_biomesource = source;
	}

	@Override
	public void attachRegistry(Registry<Biome> biomeRegistry) {
		if (this.denoiser_registry == null) this.denoiser_registry = biomeRegistry;
	}
}
