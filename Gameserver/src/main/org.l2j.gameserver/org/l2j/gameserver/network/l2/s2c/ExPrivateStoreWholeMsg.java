package org.l2j.gameserver.network.l2.s2c;

import org.l2j.gameserver.model.Player;

import org.apache.commons.lang3.StringUtils;

public class ExPrivateStoreWholeMsg extends L2GameServerPacket
{
	private final int _objId;
	private final String _name;

	public ExPrivateStoreWholeMsg(Player player, boolean showName)
	{
		_objId = player.getObjectId();
		_name = showName ? StringUtils.defaultString(player.getPackageSellStoreName()) : "";
	}

	@Override
	protected final void writeImpl()
	{
		writeInt(_objId);
		writeString(_name);
	}
}