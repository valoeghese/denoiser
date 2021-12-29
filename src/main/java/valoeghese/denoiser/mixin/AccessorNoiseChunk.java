package valoeghese.denoiser.mixin;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseChunk.class)
public interface AccessorNoiseChunk {
	@Accessor("sampler")
	NoiseSampler getSampler();

	@Invoker
	NoiseChunk.NoiseInterpolator callCreateNoiseInterpolator(NoiseChunk.NoiseFiller noiseFiller);
}
