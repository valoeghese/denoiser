package valoeghese.denoiser.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;

import java.util.EnumSet;

public class OwnerHurtTargetGoal<T extends Monster & OwnableEntity> extends TargetGoal {
	private final T monster;
	private LivingEntity ownerLastHurt;
	private int timestamp;

	public OwnerHurtTargetGoal(T tamableAnimal) {
		super(tamableAnimal, false);
		this.monster = tamableAnimal;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	public boolean canUse() {
		LivingEntity livingEntity = (LivingEntity) this.monster.getOwner();

		if (livingEntity == null) {
			return false;
		} else {
			this.ownerLastHurt = livingEntity.getLastHurtMob();
			int i = livingEntity.getLastHurtMobTimestamp();
			return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT);
		}
	}

	public void start() {
		this.mob.setTarget(this.ownerLastHurt);
		LivingEntity livingEntity = (LivingEntity) this.monster.getOwner();
		if (livingEntity != null) {
			this.timestamp = livingEntity.getLastHurtMobTimestamp();
		}

		super.start();
	}
}