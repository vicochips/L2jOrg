package org.l2j.gameserver.network.l2.s2c;

import org.l2j.gameserver.model.Creature;
import org.l2j.gameserver.utils.Location;

public class FlyToLocationPacket extends L2GameServerPacket
{
	private final int _chaObjId;
	private final FlyType _type;
	private final Location _loc;
	private final Location _destLoc;
	private final int _flySpeed;
	private final int _flyDelay;
	private final int _animationSpeed;

	public enum FlyType
	{
		THROW_UP,
		THROW_HORIZONTAL,
		DUMMY,
		CHARGE,
		PUSH_HORIZONTAL,
		JUMP_EFFECTED,
		NONE,
		PUSH_DOWN_HORIZONTAL,
		WARP_BACK,
		WARP_FORWARD;
	}

	public FlyToLocationPacket(Creature cha, Location destLoc, FlyType type, int flySpeed, int flyDelay, int animationSpeed)
	{
		_destLoc = destLoc;
		_type = type;
		_chaObjId = cha.getObjectId();
		_loc = cha.getLoc();
		_flySpeed = flySpeed;
		_flyDelay = flyDelay;
		_animationSpeed = animationSpeed;
	}

	@Override
	protected void writeImpl()
	{
		writeInt(_chaObjId);
		writeInt(_destLoc.x);
		writeInt(_destLoc.y);
		writeInt(_destLoc.z);
		writeInt(_loc.x);
		writeInt(_loc.y);
		writeInt(_loc.z);
		writeInt(_type.ordinal());
		writeInt(_flySpeed);
		writeInt(_flyDelay);
		writeInt(_animationSpeed);
	}
}