package com.bioxx.tfc.Entities.AI;

import java.util.Iterator;
import java.util.List;

import com.bioxx.tfc.api.Entities.IAnimal;
import com.bioxx.tfc.api.Entities.IAnimal.GenderEnum;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class EntityAIMateTFC extends EntityAIBase
{
	private IAnimal theAnimal;
	World theWorld;
	private IAnimal targetMate;
	int matingCounter;
	float field_48262_c;

	public EntityAIMateTFC (IAnimal par1EntityAnimal, World world, float par2)
	{
		matingCounter = 0;
		theAnimal = par1EntityAnimal;
		theWorld = world;
		field_48262_c = par2;
		setMutexBits (3);
	}


	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute ()
	{
		if (!theAnimal.getInLove())
		{
			return false;
		}
		else
		{
			targetMate = getLocalMate();
			if (targetMate != null)
			{
				if (targetMate.getGender() == theAnimal.getGender() || 
						theAnimal.isPregnant() || targetMate.isPregnant())
				{
					return false;
				}
			}
			return targetMate != null;
		}
	}


	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting ()
	{
		return targetMate.getEntity().isEntityAlive() && targetMate.getInLove() && matingCounter < 60 && theAnimal.getInLove() && 
				((targetMate.getGender() == GenderEnum.FEMALE && theAnimal.getGender() == GenderEnum.MALE) || 
						(targetMate.getGender() == GenderEnum.MALE && theAnimal.getGender() == GenderEnum.FEMALE));
	}


	/**
	 * Resets the task
	 */
	@Override
	public void resetTask ()
	{
		targetMate = null;
		matingCounter = 0;
	}


	/**
	 * Updates the task
	 */
	@Override
	public void updateTask ()
	{
		theAnimal.getEntity().getLookHelper().setLookPositionWithEntity(targetMate.getEntity(), 10F, theAnimal.getEntity().getVerticalFaceSpeed());
		theAnimal.getEntity().getNavigator().tryMoveToEntityLiving(targetMate.getEntity(), field_48262_c);
		matingCounter++;

		if (matingCounter >= 60 && theAnimal.getEntity().getDistanceSqToEntity(this.targetMate.getEntity()) < 9.0D)
			theAnimal.mate(targetMate);
	}


	private IAnimal getLocalMate()
	{
		float f = 8F;
		List list = theWorld.getEntitiesWithinAABB (theAnimal.getClass (), theAnimal.getEntity().boundingBox.expand (f, f, f));

		for (Iterator iterator = list.iterator () ; iterator.hasNext () ;)
		{
			Entity entity = (Entity) iterator.next();
			if(entity instanceof IAnimal)
			{
				IAnimal entityanimal = (IAnimal) entity;
				if (theAnimal.canMateWith(entityanimal))
					return entityanimal;
			}
		}

		return null;
	}
}
