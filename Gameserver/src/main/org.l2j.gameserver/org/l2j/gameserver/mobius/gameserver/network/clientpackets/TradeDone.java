package org.l2j.gameserver.mobius.gameserver.network.clientpackets;

import org.l2j.gameserver.mobius.gameserver.model.L2World;
import org.l2j.gameserver.mobius.gameserver.model.TradeList;
import org.l2j.gameserver.mobius.gameserver.model.actor.instance.L2PcInstance;
import org.l2j.gameserver.mobius.gameserver.network.SystemMessageId;

import java.nio.ByteBuffer;

/**
 * This packet manages the trade response.
 */
public final class TradeDone extends IClientIncomingPacket {
    private int _response;

    @Override
    public void readImpl(ByteBuffer packet) {
        _response = packet.getInt();
    }

    @Override
    public void runImpl() {
        final L2PcInstance player = client.getActiveChar();
        if (player == null) {
            return;
        }

        if (!client.getFloodProtectors().getTransaction().tryPerformAction("trade")) {
            player.sendMessage("You are trading too fast.");
            return;
        }

        final TradeList trade = player.getActiveTradeList();
        if (trade == null) {
            return;
        }

        if (trade.isLocked()) {
            return;
        }

        if (_response == 1) {
            if ((trade.getPartner() == null) || (L2World.getInstance().getPlayer(trade.getPartner().getObjectId()) == null)) {
                // Trade partner not found, cancel trade
                player.cancelActiveTrade();
                player.sendPacket(SystemMessageId.THAT_PLAYER_IS_NOT_ONLINE);
                return;
            }

            if ((trade.getOwner().hasItemRequest()) || (trade.getPartner().hasItemRequest())) {
                return;
            }

            if (!player.getAccessLevel().allowTransaction()) {
                player.cancelActiveTrade();
                player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
                return;
            }

            if (player.getInstanceWorld() != trade.getPartner().getInstanceWorld()) {
                player.cancelActiveTrade();
                return;
            }

            if (player.calculateDistance3D(trade.getPartner()) > 150) {
                player.cancelActiveTrade();
                return;
            }
            trade.confirm();
        } else {
            player.cancelActiveTrade();
        }
    }
}