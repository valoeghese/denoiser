package valoeghese.denoiser.mixin;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.denoiser.BiomeSampler;
import valoeghese.denoiser.Denoiser;

import java.util.function.Supplier;

@Mixin(NoiseBasedChunkGenerator.class)
public class MixinNoiseBasedChunkGenerator implements BiomeSampler {
	@Shadow
	@Final
	private NoiseSampler sampler;

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/biome/BiomeSource;Lnet/minecraft/world/level/biome/BiomeSource;JLjava/util/function/Supplier;)V")
	private void attachBiomeSource(Registry<NormalNoise.NoiseParameters> registry, BiomeSource biomeSource, BiomeSource biomeSource2, long l, Supplier<NoiseGeneratorSettings> supplier, CallbackInfo ci) {
		((BiomeSampler) this.sampler).attachBiomeSource(biomeSource);
	}

	@Override
	public void attachBiomeSource(BiomeSource source) {
		throw new UnsupportedOperationException("Cannot attach a biome source to a chunk generator using BiomeSampler!");
	}

	@Override
	public void attachRegistry(Registry<Biome> biomeRegistry) {
		((BiomeSampler) this.sampler).attachRegistry(biomeRegistry);
	}

	@Override
	public ResourceKey<Biome> sampleBiome(int qx, int qy, int qz) {
		return ((BiomeSampler) this.sampler).sampleBiome(qx, qy, qz);
	}

	/**
	 * @author Valoeghese
	 * @reason Debug noise caves.
	 */
	@Inject(at = @At("HEAD"), method = "applyCarvers", cancellable = true)
	public void disableCarversWhenDebugging(WorldGenRegion worldGenRegion, long l, BiomeManager biomeManager, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving, CallbackInfo info) {
		if (Denoiser.REMOVE_CARVERS) {
			info.cancel();
		}
	}
}