package valoeghese.denoiser.mixin;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.denoiser.BiomeInfoAttacher;

import java.util.function.Supplier;

@Mixin(NoiseBasedChunkGenerator.class)
public class MixinNoiseBasedChunkGenerator implements BiomeInfoAttacher {
	@Shadow
	@Final
	private NoiseSampler sampler;

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/biome/BiomeSource;Lnet/minecraft/world/level/biome/BiomeSource;JLjava/util/function/Supplier;)V")
	private void attachBiomeSource(Registry<NormalNoise.NoiseParameters> registry, BiomeSource biomeSource, BiomeSource biomeSource2, long l, Supplier<NoiseGeneratorSettings> supplier, CallbackInfo ci) {
		((BiomeInfoAttacher) this.sampler).attachBiomeSource(biomeSource);
	}

	@Override
	public void attachBiomeSource(BiomeSource source) {
		throw new UnsupportedOperationException("Cannot attach a biome source to a chunk generator using BiomeInfoAttacher!");
	}

	@Override
	public void attachRegistry(Registry<Biome> biomeRegistry) {
		((BiomeInfoAttacher) this.sampler).attachRegistry(biomeRegistry);
	}
}
