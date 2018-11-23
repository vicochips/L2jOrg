package org.l2j.gameserver.skills.effects;

import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;

import org.l2j.commons.lang.reference.HardReference;
import org.l2j.gameserver.listener.actor.OnCurrentHpDamageListener;
import org.l2j.gameserver.model.Creature;
import org.l2j.gameserver.model.Skill;
import org.l2j.gameserver.model.actor.instances.creature.Abnormal;
import org.l2j.gameserver.network.l2.components.SystemMsg;
import org.l2j.gameserver.network.l2.s2c.SystemMessagePacket;
import org.l2j.gameserver.stats.Env;
import org.l2j.gameserver.templates.skill.EffectTemplate;

public final class EffectCurseOfLifeFlow extends Effect
{
	private CurseOfLifeFlowListener _listener;

	private TObjectIntHashMap<HardReference<? extends Creature>> _damageList = new TObjectIntHashMap<HardReference<? extends Creature>>();

	public EffectCurseOfLifeFlow(Abnormal abnormal, Env env, EffectTemplate template)
	{
		super(abnormal, env, template);
	}

	@Override
	public void onStart()
	{
		_listener = new CurseOfLifeFlowListener();
		getEffected().addListener(_listener);
	}

	@Override
	public void onExit()
	{
		getEffected().removeListener(_listener);
		_listener = null;
	}

	@Override
	public boolean onActionTime()
	{
		if(getEffected().isDead())
			return false;

		for(TObjectIntIterator<HardReference<? extends Creature>> iterator = _damageList.iterator(); iterator.hasNext();)
		{
			iterator.advance();
			Creature damager = iterator.key().get();
			if(damager == null || damager.isDead() || damager.isCurrentHpFull())
				continue;

			int damage = iterator.value();
			if(damage <= 0)
				continue;

			double max_heal = getValue();
			double heal = Math.min(damage, max_heal);
			double newHp = Math.min(damager.getCurrentHp() + heal, damager.getMaxHp());

			if(damager != getEffector())
				damager.sendPacket(new SystemMessagePacket(SystemMsg.S2_HP_HAS_BEEN_RESTORED_BY_C1).addName(getEffector()).addLong((long) (newHp - damager.getCurrentHp())));
			else
				damager.sendPacket(new SystemMessagePacket(SystemMsg.S1_HP_HAS_BEEN_RESTORED).addLong((long) (newHp - damager.getCurrentHp())));
			damager.setCurrentHp(newHp, false);
		}

		_damageList.clear();

		return true;
	}

	private class CurseOfLifeFlowListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if(attacker == actor || attacker == getEffected())
				return;
			int old_damage = _damageList.get(attacker.getRef());
			_damageList.put(attacker.getRef(), old_damage == 0 ? (int) damage : old_damage + (int) damage);
		}
	}
}