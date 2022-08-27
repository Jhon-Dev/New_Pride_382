package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.enums.items.CrystalType;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

public class SoulShots implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player))
			return;

		 Player player = (Player) playable;
		 ItemInstance weaponInst = player.getActiveWeaponInstance();
		 Weapon weaponItem = player.getActiveWeaponItem();
		int itemId = item.getItemId();
		
		// Check if soulshot can be used
		if (weaponInst == null || weaponItem.getSoulShotCount() == 0)
		{
			if (!player.getAutoSoulShot().contains(item.getItemId()))
				player.sendPacket(SystemMessageId.CANNOT_USE_SOULSHOTS);
			
			return;
		}
		final CrystalType weaponGrade = weaponItem.getCrystalType();
		boolean gradeCheck = true;

		switch (weaponGrade)
		{
			case NONE:
				if (itemId != 5789 && itemId != 1835)
					gradeCheck = false;
				break;
			case D:
				if (itemId != 1463)
					gradeCheck = false;
				break;
			case C:
				if (itemId != 1464)
					gradeCheck = false;
				break;
			case B:
				if (itemId != 1465)
					gradeCheck = false;
				break;
			case A:
				if (itemId != 1466)
					gradeCheck = false;
				break;
			case S:
				if (itemId != 1467)
					gradeCheck = false;
				break;
		}


		if (!gradeCheck)
		{
			if (!player.getAutoSoulShot().contains(item.getItemId()))
				player.sendPacket(SystemMessageId.SOULSHOTS_GRADE_MISMATCH);
			
			return;
		}

		player.soulShotLock.lock();

		try {


			// Check if Soulshot are already active.
			if (weaponInst.getChargedSoulshot() != ItemInstance.CHARGED_NONE)
				return;

			// Consume Soulshots if player has enough of them.
			int saSSCount = (int) player.getStat().calcStat(Stats.SOULSHOT_COUNT, 0, null, null);
			int SSCount = saSSCount == 0 ? weaponItem.getSoulShotCount() : saSSCount;

			if (!player.destroyItemWithoutTrace("Consume", item.getObjectId(), SSCount, null, false)) {
				if (!player.disableAutoShot(itemId))
					player.sendPacket(SystemMessageId.NOT_ENOUGH_SOULSHOTS);

				return;
			}

			// Charge soulshot
			weaponInst.setChargedSoulshot(ItemInstance.CHARGED_SOULSHOT);

		}
		finally
		{
			player.soulShotLock.unlock();
		}

		int skillId = 0;
		switch (itemId)
		{
			case 1835:
			case 5789:
				skillId = 2039;
				break;
			case 1463:
				skillId = 2150;
				break;
			case 1464:
				skillId = 2151;
				break;
			case 1465:
				skillId = 2152;
				break;
			case 1466:
				skillId = 2153;
				break;
			case 1467:
				skillId = 2154;
				break;
		}

		// Send message to client
		player.sendPacket(SystemMessageId.ENABLED_SOULSHOT);
		player.broadcastPacketInRadius(new MagicSkillUse(player, player, skillId, 1, 0, 0), 600);
	}
}