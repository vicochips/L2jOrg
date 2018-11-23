package org.l2j.gameserver.network.l2.s2c;

public class ExRegistPartySubstitute extends L2GameServerPacket
{
	private final int _object;

	public ExRegistPartySubstitute(int obj)
	{
		_object = obj;
	}

	@Override
	protected void writeImpl()
	{
		writeInt(_object);
		writeInt(0x01);
	}
}