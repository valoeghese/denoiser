package valoeghese.denoiser.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import valoeghese.denoiser.entity.OwnerSetter;

@Mixin(EntityType.class)
public class MixinSpawnEgg {
	@Inject(at = @At("RETURN"), method = "spawn(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/MobSpawnType;ZZ)Lnet/minecraft/world/entity/Entity;")
	private void spawn(ServerLevel serverLevel, ItemStack itemStack, Player player, BlockPos blockPos, MobSpawnType mobSpawnType, boolean bl, boolean bl2, CallbackInfoReturnable<Entity> cir) {
		if (mobSpawnType == MobSpawnType.SPAWN_EGG && cir.getReturnValue() instanceof OwnerSetter setter) {
			setter.setOwner(player);
		}
	}
}
