package valoeghese.denoiser.mixin;

import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.TerrainInfo;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseSampler.class)
public abstract class FuckNoiseCavesInParticular {
	@Inject(at = @At("RETURN"), method = "calculateBaseNoise(IIILnet/minecraft/world/level/levelgen/TerrainInfo;Lnet/minecraft/world/level/levelgen/blending/Blender;)D")
	private void init(int i, int j, int k, TerrainInfo terrainInfo, Blender blender, CallbackInfoReturnable<Double> info) {

	}

	abstract protected double calculateBaseNoise(int i, int j, int k, TerrainInfo terrainInfo, double d, boolean bl, boolean bl2, Blender blender);
}
