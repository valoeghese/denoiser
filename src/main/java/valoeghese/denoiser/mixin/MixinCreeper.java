package valoeghese.denoiser.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.spongepowered.asm.mixin.Shadow;
import valoeghese.denoiser.entity.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.denoiser.entity.OwnerSetter;

import java.util.UUID;

@Mixin(Creeper.class)
public class MixinCreeper extends Monster implements OwnableEntity, OwnerSetter {
	protected MixinCreeper(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Unique
	private UUID owner;
	@Shadow
	private int explosionRadius;

	@Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
	private void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
		if (this.owner != null) {
			compoundTag.putUUID("owner", this.owner);
		}
	}

	@Inject(at = @At("RETURN"), method = "readAdditionalSaveData")
	private void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
		if (compoundTag.contains("owner")) {
			this.owner = compoundTag.getUUID("owner");
		}
	}

	/**
	 * @author me
	 * @reason for my cousin
	 */
	@Overwrite
	public void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new SwellGoal((Creeper) (Object) this));
		this.goalSelector.addGoal(3, new AvoidEntityGoal(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(3, new AvoidEntityGoal(this, Cat.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

		if (this.owner == null) {
			this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true));
			this.explosionRadius = 3;
		} else {
			this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
			this.explosionRadius = 7;
		}

		this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
	}

	@Nullable
	@Override
	public UUID getOwnerUUID() {
		return this.owner;
	}

	@Nullable
	@Override
	public Entity getOwner() {
		try {
			UUID uUID = this.getOwnerUUID();
			return uUID == null ? null : this.level.getPlayerByUUID(uUID);
		} catch (IllegalArgumentException var2) {
			return null;
		}
	}

	@Override
	public void setOwner(Player player) {
		this.owner = player.getUUID();
		this.goalSelector.removeAllGoals();
		this.targetSelector.removeAllGoals();
		this.registerGoals();
	}
}
