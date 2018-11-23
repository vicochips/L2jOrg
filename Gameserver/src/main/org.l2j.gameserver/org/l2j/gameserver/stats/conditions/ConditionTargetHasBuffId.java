package org.l2j.gameserver.stats.conditions;

import org.l2j.gameserver.model.Creature;
import org.l2j.gameserver.model.actor.instances.creature.Abnormal;
import org.l2j.gameserver.stats.Env;

public final class ConditionTargetHasBuffId extends Condition
{
	private final int _id;
	private final int _level;

	public ConditionTargetHasBuffId(int id, int level)
	{
		_id = id;
		_level = level;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		Creature target = env.target;
		if(target == null)
			return false;

		for(Abnormal effect : target.getAbnormalList())
		{
			if(effect.getSkill().getId() != _id)
				continue;

			if(_level == -1)
				return true;

			if(effect.getSkill().getLevel() >= _level)
				return true;
		}

		return false;
	}
}