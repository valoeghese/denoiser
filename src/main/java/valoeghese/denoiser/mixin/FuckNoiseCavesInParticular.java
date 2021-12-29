package valoeghese.denoiser.mixin;

import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.TerrainInfo;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseSampler.class)
public abstract class FuckNoiseCavesInParticular {
	@Shadow
	@Final
	private boolean isNoiseCavesEnabled;

	@Inject(at = @At("RETURN"), cancellable = true, method = "calculateBaseNoise(IIILnet/minecraft/world/level/levelgen/TerrainInfo;Lnet/minecraft/world/level/levelgen/blending/Blender;)D")
	private void modifyBaseNoise(int x, int y, int z, TerrainInfo terrainInfo, Blender blender, CallbackInfoReturnable<Double> info) {
		// this better be in block coordinates

	}

	@Shadow
	abstract protected double calculateBaseNoise(int i, int j, int k, TerrainInfo terrainInfo, double d, boolean bl, boolean bl2, Blender blender);
}
