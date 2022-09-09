package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.*;

import java.util.Collection;

public final class RequestEnchantItem extends AbstractEnchantPacket
{
	private int _objectId = 0;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getPlayer();
		if (activeChar == null || _objectId == 0)
			return;
		
		if (!activeChar.isOnline() || getClient().isDetached())
		{
			activeChar.setActiveEnchantItem(null);
			return;
		}
		
		if (activeChar.isProcessingTransaction() || activeChar.isInStoreMode())
		{
			activeChar.sendPacket(SystemMessageId.CANNOT_ENCHANT_WHILE_STORE);
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(EnchantResult.CANCELLED);
			return;
		}
		
		ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		ItemInstance scroll = activeChar.getActiveEnchantItem();
		
		if (item == null || scroll == null)
		{
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(SystemMessageId.ENCHANT_SCROLL_CANCELLED);
			activeChar.sendPacket(EnchantResult.CANCELLED);
			return;
		}
		
		// template for scroll
		EnchantScroll scrollTemplate = getEnchantScroll(scroll);
		if (scrollTemplate == null)
			return;
		
		// first validation check
		if (!scrollTemplate.isValid(item) || !isEnchantable(item))
		{
			activeChar.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(EnchantResult.CANCELLED);
			return;
		}
		
		// attempting to destroy scroll
		scroll = activeChar.getInventory().destroyItem("Enchant", scroll.getObjectId(), 1, activeChar, item);
		if (scroll == null)
		{
			activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
			//Util.handleIllegalPlayerAction(activeChar, activeChar.getName() + " tried to enchant without scroll.", Config.DEFAULT_PUNISH);
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(EnchantResult.CANCELLED);
			return;
		}
		
		if (activeChar.getActiveTradeList() != null)
		{
			activeChar.cancelActiveTrade();
			activeChar.sendPacket(SystemMessageId.TRADE_ATTEMPT_FAILED);
			return;
		}
		
		synchronized (item)
		{
			double chance = scrollTemplate.getChance(item);
			
			// last validation check
			if (item.getOwnerId() != activeChar.getObjectId() || !isEnchantable(item) || chance < 0)
			{
				activeChar.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
				activeChar.setActiveEnchantItem(null);
				activeChar.sendPacket(EnchantResult.CANCELLED);
				return;
			}
			
			// success
			if (Rnd.get(100) < chance)
			{
				// announce the success
				SystemMessage sm;
				
				if (item.getEnchantLevel() == 0)
				{
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1_SUCCESSFULLY_ENCHANTED);
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
				else
				{
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1_S2_SUCCESSFULLY_ENCHANTED);
					sm.addNumber(item.getEnchantLevel());
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}

				// announce the success
				if ((item.isItemList1() && item.getEnchantLevel() >= 18) || (item.isItemList2() && item.getEnchantLevel() >= 15) || (item.isItemList3() && item.getEnchantLevel() >= 12))
				{
					final Collection<Player> pls = World.getInstance().getPlayers();
					for (Player onlinePlayer : pls)
					{
						if (onlinePlayer == null)
							continue;

						onlinePlayer.sendPacket(SystemMessage.sendString(activeChar.getName() + " has successfuly enchanted a +" + item.getEnchantLevel() + " " + item.getName() + " with " + scroll.getName() + "."));
						L2Skill skill = SkillTable.FrequentSkill.LARGE_FIREWORK.getSkill();
						activeChar.broadcastPacket(new MagicSkillUse(activeChar, skill.getId(), skill.getLevel(), skill.getHitTime(), skill.getReuseDelay()));
						activeChar.broadcastPacket(new SocialAction(activeChar, 3));
					}
				}

				item.setEnchantLevel(item.getEnchantLevel() + 1);
				item.updateDatabase();
				activeChar.sendPacket(EnchantResult.SUCCESS);
			}
			else
			{
				// unequip item on enchant failure to avoid item skills stack
				if (item.isEquipped())
				{
					if (item.getEnchantLevel() > 0)
					{
						SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED);
						sm.addNumber(item.getEnchantLevel());
						sm.addItemName(item.getItemId());
						activeChar.sendPacket(sm);
					}
					else
					{
						SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DISARMED);
						sm.addItemName(item.getItemId());
						activeChar.sendPacket(sm);
					}

					ItemInstance[] unequiped = activeChar.getInventory().unEquipItemInSlotAndRecord(item.getLocationSlot());
					InventoryUpdate iu = new InventoryUpdate();
					for (ItemInstance itm : unequiped)
						iu.addModifiedItem(itm);

					activeChar.sendPacket(iu);
					activeChar.broadcastUserInfo();
				}

				// announce the fail
				if ((item.isItemList1() && item.getEnchantLevel() > 18) || (item.isItemList2() && item.getEnchantLevel() > 15) || (item.isItemList3() && item.getEnchantLevel() > 12))
				{
					final Collection<Player> pls = World.getInstance().getPlayers();
					for (Player onlinePlayer : pls)
					{
						if (onlinePlayer == null)
							continue;

						onlinePlayer.sendPacket(SystemMessage.sendString(activeChar.getName() + " has failed enchanted a +" + item.getEnchantLevel() + " " + item.getName() + " with " + scroll.getName() + "."));
						activeChar.broadcastPacket(new SocialAction(activeChar, 13));
					}
				}
				
				if (scrollTemplate.isBlessed())
				{
					// blessed enchant - clear enchant value
					// activeChar.sendPacket(SystemMessageId.BLESSED_ENCHANT_FAILED);

					if (item.getEnchantLevel() > Config.ENCHANT_SAFE_MAX)
					{
						activeChar.sendMessage("Failed in Blessed Enchant. The enchant value of the item reduced 1.");
						item.setEnchantLevel(item.getEnchantLevel() - 1);
					}
					else if (item.getEnchantLevel() == Config.ENCHANT_SAFE_MAX)
					{
						activeChar.sendMessage("Failed in Blessed Enchant. The enchant value of the item became " + Config.ENCHANT_SAFE_MAX + ".");
						item.setEnchantLevel(Config.ENCHANT_SAFE_MAX);
					}

					item.updateDatabase();
					activeChar.sendPacket(EnchantResult.UNSUCCESS);
				}
				else if (scrollTemplate.isCrystal())
				{
					// crystal enchant - clear enchant value
					activeChar.sendMessage("Failed in Crystal Enchant. The enchant value of the item became " + Config.ENCHANT_SAFE_MAX + ".");;

					item.setEnchantLevel(Config.ENCHANT_SAFE_MAX);
					item.updateDatabase();
					activeChar.sendPacket(EnchantResult.UNSUCCESS);
				}
				else
				{
					if (!item.isItemList1() && !item.isItemList2() && !item.isItemList3())
					{
						// enchant - clear enchant value
						activeChar.sendMessage("Failed in Enchant. The enchant value of the item became " + Config.ENCHANT_SAFE_MAX + ".");

						item.setEnchantLevel(Config.ENCHANT_SAFE_MAX);
						item.updateDatabase();
						activeChar.sendPacket(EnchantResult.UNSUCCESS);
					}
					else
					{
						// enchant failed, destroy item
						int crystalId = item.getItem().getCrystalItemId();
						int count = item.getCrystalCount() - (item.getItem().getCrystalCount() + 1) / 2;
						if (count < 1)
							count = 1;

						ItemInstance destroyItem = activeChar.getInventory().destroyItem("Enchant", item, activeChar, null);
						if (destroyItem == null)
						{
							// unable to destroy item, cheater ?
							//Util.handleIllegalPlayerAction(activeChar, "Unable to delete item on enchant failure from player " + activeChar.getName() + ", possible cheater !", Config.DEFAULT_PUNISH);
							activeChar.setActiveEnchantItem(null);
							activeChar.sendPacket(EnchantResult.CANCELLED);
							return;
						}

						ItemInstance crystals = null;
						if (crystalId != 0)
						{
							crystals = activeChar.getInventory().addItem("Enchant", crystalId, count, activeChar, destroyItem);

							SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
							sm.addItemName(crystals.getItemId());
							sm.addItemNumber(count);
							activeChar.sendPacket(sm);
						}

						if (!Config.FORCE_INVENTORY_UPDATE)
						{
							InventoryUpdate iu = new InventoryUpdate();
							if (destroyItem.getCount() == 0)
								iu.addRemovedItem(destroyItem);
							else
								iu.addModifiedItem(destroyItem);

							if (crystals != null)
								iu.addItem(crystals);

							activeChar.sendPacket(iu);
						}
						else
							activeChar.sendPacket(new ItemList(activeChar, true));

						World world = World.getInstance();
						world.removeObject(destroyItem);
						if (crystalId == 0)
							activeChar.sendPacket(EnchantResult.UNK_RESULT_4);
						else
							activeChar.sendPacket(EnchantResult.UNK_RESULT_1);
					}
				}
			}

			StatusUpdate su = new StatusUpdate(activeChar);
			su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
			activeChar.sendPacket(su);

			activeChar.sendPacket(new ItemList(activeChar, false));
			activeChar.broadcastUserInfo();
			activeChar.setActiveEnchantItem(null);
		}
	}
}