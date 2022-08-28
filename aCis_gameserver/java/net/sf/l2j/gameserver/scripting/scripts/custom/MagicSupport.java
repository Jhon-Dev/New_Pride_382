package net.sf.l2j.gameserver.scripting.scripts.custom;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class MagicSupport extends Quest
{
    private static final String qn = "MagicSupport";

    private static final int NPC = 50000;

    // Reuse between buffs
    private static final int _seconds = 30;

    public MagicSupport()
    {
        super(-1, "custom");

        addStartNpc(NPC);
        addFirstTalkId(NPC);
        addTalkId(NPC);
    }

    @Override
    public String onAdvEvent(String event, Npc npc, Player player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(getName());
        L2Skill skill;

        if (event.equalsIgnoreCase("restoreCp"))
        {
            player.setCurrentCp(player.getMaxCp());
            return "50000.htm";
        }
        else if (event.equalsIgnoreCase("restoreHp"))
        {
            player.setCurrentHp(player.getMaxHp());
            return "50000.htm";
        }
        else if (event.equalsIgnoreCase("restoreMp"))
        {
            player.setCurrentMp(player.getMaxMp());
            return "50000.htm";
        }
        else if (event.equalsIgnoreCase("restoreCpHpMp"))
        {
            if (player.getPvpFlag() != 0 || player.getKarma() != 0 || player.isInCombat())
            {
                player.sendMessage("You cannot be healed while in combat mode.");
                return "50000-error.htm";
            }

            long _reuse = 0;
            String _streuse = st.get("reuse");
            if (_streuse != null)
            {
                _reuse = Long.parseLong(_streuse);
            }
            if (_reuse > System.currentTimeMillis())
            {
                long remainingTime = (_reuse - System.currentTimeMillis()) / 1000;
                int seconds = (int) ((remainingTime % 3600));
                player.sendMessage("Available to use after " + seconds + " second(s).");
            }
            else
            {
                player.setCurrentCp(player.getMaxCp());
                player.setCurrentHp(player.getMaxHp());
                player.setCurrentMp(player.getMaxMp());
                st.set("reuse", String.valueOf(System.currentTimeMillis() + (_seconds * 3600)));
                htmltext = "50000.htm";
            }
        }
        else if (event.equalsIgnoreCase("cancelEffects"))
        {
            player.stopAllEffectsExceptThoseThatLastThroughDeath();
            return "50000.htm";
        }
        else if (event.equalsIgnoreCase("warrior_w_zerk"))
        {
            // Shield
            skill = SkillTable.getInstance().getInfo(1040, 3);
            skill.getEffects(player, player);
            // Might
            skill = SkillTable.getInstance().getInfo(1068, 3);
            skill.getEffects(player, player);
            // Focus
            skill = SkillTable.getInstance().getInfo(1077, 3);
            skill.getEffects(player, player);
            // Wind Walk
            skill = SkillTable.getInstance().getInfo(1204, 2);
            skill.getEffects(player, player);
            // Magic Barrier
            skill = SkillTable.getInstance().getInfo(1036, 2);
            skill.getEffects(player, player);
            // Blessed Body
            skill = SkillTable.getInstance().getInfo(1045, 6);
            skill.getEffects(player, player);
            // Blessed Soul
            skill = SkillTable.getInstance().getInfo(1048, 6);
            skill.getEffects(player, player);
            // Haste
            skill = SkillTable.getInstance().getInfo(1086, 2);
            skill.getEffects(player, player);
            // Guidance
            skill = SkillTable.getInstance().getInfo(1240, 3);
            skill.getEffects(player, player);
            // Berserker Spirit
            skill = SkillTable.getInstance().getInfo(1062, 2);
            skill.getEffects(player, player);
            // Death Whisper
            skill = SkillTable.getInstance().getInfo(1242, 3);
            skill.getEffects(player, player);
            // Greater Might
            skill = SkillTable.getInstance().getInfo(1388, 3);
            skill.getEffects(player, player);
            // Mental Shield
            skill = SkillTable.getInstance().getInfo(1035, 4);
            skill.getEffects(player, player);
            // Blessing of Queen
            skill = SkillTable.getInstance().getInfo(4699, 13);
            skill.getEffects(player, player);
            // Victory Chant
            skill = SkillTable.getInstance().getInfo(1363, 1);
            skill.getEffects(player, player);
            // Dance of the Warrior
            skill = SkillTable.getInstance().getInfo(271, 1);
            skill.getEffects(player, player);
            // Dance of Inspiration
            skill = SkillTable.getInstance().getInfo(272, 1);
            skill.getEffects(player, player);
            // Dance of Fire
            skill = SkillTable.getInstance().getInfo(274, 1);
            skill.getEffects(player, player);
            // Dance of Fury
            skill = SkillTable.getInstance().getInfo(275, 1);
            skill.getEffects(player, player);
            // Song of Earth
            skill = SkillTable.getInstance().getInfo(264, 1);
            skill.getEffects(player, player);
            // Song of Warding
            skill = SkillTable.getInstance().getInfo(267, 1);
            skill.getEffects(player, player);
            // Song of Wind
            skill = SkillTable.getInstance().getInfo(268, 1);
            skill.getEffects(player, player);
            // Song of Hunter
            skill = SkillTable.getInstance().getInfo(269, 1);
            skill.getEffects(player, player);
            // Song of Vitality
            skill = SkillTable.getInstance().getInfo(304, 1);
            skill.getEffects(player, player);
            // Song of Renewal
            skill = SkillTable.getInstance().getInfo(349, 1);
            skill.getEffects(player, player);
            // Song of Champion
            skill = SkillTable.getInstance().getInfo(364, 1);
            skill.getEffects(player, player);
            return "50000.htm";
        }
        else if (event.equalsIgnoreCase("warrior_w/o_zerk"))
        {
            // Shield
            skill = SkillTable.getInstance().getInfo(1040, 3);
            skill.getEffects(player, player);
            // Might
            skill = SkillTable.getInstance().getInfo(1068, 3);
            skill.getEffects(player, player);
            // Focus
            skill = SkillTable.getInstance().getInfo(1077, 3);
            skill.getEffects(player, player);
            // Wind Walk
            skill = SkillTable.getInstance().getInfo(1204, 2);
            skill.getEffects(player, player);
            // Magic Barrier
            skill = SkillTable.getInstance().getInfo(1036, 2);
            skill.getEffects(player, player);
            // Blessed Body
            skill = SkillTable.getInstance().getInfo(1045, 6);
            skill.getEffects(player, player);
            // Blessed Soul
            skill = SkillTable.getInstance().getInfo(1048, 6);
            skill.getEffects(player, player);
            // Haste
            skill = SkillTable.getInstance().getInfo(1086, 2);
            skill.getEffects(player, player);
            // Guidance
            skill = SkillTable.getInstance().getInfo(1240, 3);
            skill.getEffects(player, player);
            // Death Whisper
            skill = SkillTable.getInstance().getInfo(1242, 3);
            skill.getEffects(player, player);
            // Greater Might
            skill = SkillTable.getInstance().getInfo(1388, 3);
            skill.getEffects(player, player);
            // Mental Shield
            skill = SkillTable.getInstance().getInfo(1035, 4);
            skill.getEffects(player, player);
            // Blessing of Queen
            skill = SkillTable.getInstance().getInfo(4699, 13);
            skill.getEffects(player, player);
            // Victory Chant
            skill = SkillTable.getInstance().getInfo(1363, 1);
            skill.getEffects(player, player);
            // Dance of the Warrior
            skill = SkillTable.getInstance().getInfo(271, 1);
            skill.getEffects(player, player);
            // Dance of Inspiration
            skill = SkillTable.getInstance().getInfo(272, 1);
            skill.getEffects(player, player);
            // Dance of Fire
            skill = SkillTable.getInstance().getInfo(274, 1);
            skill.getEffects(player, player);
            // Dance of Fury
            skill = SkillTable.getInstance().getInfo(275, 1);
            skill.getEffects(player, player);
            // Song of Earth
            skill = SkillTable.getInstance().getInfo(264, 1);
            skill.getEffects(player, player);
            // Song of Warding
            skill = SkillTable.getInstance().getInfo(267, 1);
            skill.getEffects(player, player);
            // Song of Wind
            skill = SkillTable.getInstance().getInfo(268, 1);
            skill.getEffects(player, player);
            // Song of Hunter
            skill = SkillTable.getInstance().getInfo(269, 1);
            skill.getEffects(player, player);
            // Song of Vitality
            skill = SkillTable.getInstance().getInfo(304, 1);
            skill.getEffects(player, player);
            // Song of Renewal
            skill = SkillTable.getInstance().getInfo(349, 1);
            skill.getEffects(player, player);
            // Song of Champion
            skill = SkillTable.getInstance().getInfo(364, 1);
            skill.getEffects(player, player);
            return "50000.htm";
        }
        else if (event.equalsIgnoreCase("mage_w_zerk"))
        {
            // Shield
            skill = SkillTable.getInstance().getInfo(1040, 3);
            skill.getEffects(player, player);
            // Concentration
            skill = SkillTable.getInstance().getInfo(1078, 6);
            skill.getEffects(player, player);
            // Acumen
            skill = SkillTable.getInstance().getInfo(1085, 3);
            skill.getEffects(player, player);
            // Wind Walk
            skill = SkillTable.getInstance().getInfo(1204, 2);
            skill.getEffects(player, player);
            // Magic Barrier
            skill = SkillTable.getInstance().getInfo(1036, 2);
            skill.getEffects(player, player);
            // Blessed Body
            skill = SkillTable.getInstance().getInfo(1045, 6);
            skill.getEffects(player, player);
            // Blessed Soul
            skill = SkillTable.getInstance().getInfo(1048, 6);
            skill.getEffects(player, player);
            // Berserker Spirit
            skill = SkillTable.getInstance().getInfo(1062, 2);
            skill.getEffects(player, player);
            // Empower
            skill = SkillTable.getInstance().getInfo(1059, 3);
            skill.getEffects(player, player);
            // Wild Magic
            skill = SkillTable.getInstance().getInfo(1303, 2);
            skill.getEffects(player, player);
            // Greater Shield
            skill = SkillTable.getInstance().getInfo(1389, 3);
            skill.getEffects(player, player);
            // Mental Shield
            skill = SkillTable.getInstance().getInfo(1035, 4);
            skill.getEffects(player, player);
            // Gift of Seraphim
            skill = SkillTable.getInstance().getInfo(4703, 13);
            skill.getEffects(player, player);
            // Victory Chant
            skill = SkillTable.getInstance().getInfo(1363, 1);
            skill.getEffects(player, player);
            // Dance of the Mystic
            skill = SkillTable.getInstance().getInfo(273, 1);
            skill.getEffects(player, player);
            // Dance of Concentration
            skill = SkillTable.getInstance().getInfo(276, 1);
            skill.getEffects(player, player);
            // Siren's Dance
            skill = SkillTable.getInstance().getInfo(365, 1);
            skill.getEffects(player, player);
            // Song of Earth
            skill = SkillTable.getInstance().getInfo(264, 1);
            skill.getEffects(player, player);
            // Song of Warding
            skill = SkillTable.getInstance().getInfo(267, 1);
            skill.getEffects(player, player);
            // Song of Wind
            skill = SkillTable.getInstance().getInfo(268, 1);
            skill.getEffects(player, player);
            // Song of Vitality
            skill = SkillTable.getInstance().getInfo(304, 1);
            skill.getEffects(player, player);
            // Song of Renewal
            skill = SkillTable.getInstance().getInfo(349, 1);
            skill.getEffects(player, player);
            return "50000.htm";
        }
        else if (event.equalsIgnoreCase("mage_w/o_zerk"))
        {
            // Shield
            skill = SkillTable.getInstance().getInfo(1040, 3);
            skill.getEffects(player, player);
            // Concentration
            skill = SkillTable.getInstance().getInfo(1078, 6);
            skill.getEffects(player, player);
            // Acumen
            skill = SkillTable.getInstance().getInfo(1085, 3);
            skill.getEffects(player, player);
            // Wind Walk
            skill = SkillTable.getInstance().getInfo(1204, 2);
            skill.getEffects(player, player);
            // Magic Barrier
            skill = SkillTable.getInstance().getInfo(1036, 2);
            skill.getEffects(player, player);
            // Blessed Body
            skill = SkillTable.getInstance().getInfo(1045, 6);
            skill.getEffects(player, player);
            // Blessed Soul
            skill = SkillTable.getInstance().getInfo(1048, 6);
            skill.getEffects(player, player);
            // Empower
            skill = SkillTable.getInstance().getInfo(1059, 3);
            skill.getEffects(player, player);
            // Wild Magic
            skill = SkillTable.getInstance().getInfo(1303, 2);
            skill.getEffects(player, player);
            // Greater Shield
            skill = SkillTable.getInstance().getInfo(1389, 3);
            skill.getEffects(player, player);
            // Mental Shield
            skill = SkillTable.getInstance().getInfo(1035, 4);
            skill.getEffects(player, player);
            // Gift of Seraphim
            skill = SkillTable.getInstance().getInfo(4703, 13);
            skill.getEffects(player, player);
            // Victory Chant
            skill = SkillTable.getInstance().getInfo(1363, 1);
            skill.getEffects(player, player);
            // Dance of the Mystic
            skill = SkillTable.getInstance().getInfo(273, 1);
            skill.getEffects(player, player);
            // Dance of Concentration
            skill = SkillTable.getInstance().getInfo(276, 1);
            skill.getEffects(player, player);
            // Siren's Dance
            skill = SkillTable.getInstance().getInfo(365, 1);
            skill.getEffects(player, player);
            // Song of Earth
            skill = SkillTable.getInstance().getInfo(264, 1);
            skill.getEffects(player, player);
            // Song of Warding
            skill = SkillTable.getInstance().getInfo(267, 1);
            skill.getEffects(player, player);
            // Song of Wind
            skill = SkillTable.getInstance().getInfo(268, 1);
            skill.getEffects(player, player);
            // Song of Vitality
            skill = SkillTable.getInstance().getInfo(304, 1);
            skill.getEffects(player, player);
            // Song of Renewal
            skill = SkillTable.getInstance().getInfo(349, 1);
            skill.getEffects(player, player);
            return "50000.htm";
        }
        else if (event.equalsIgnoreCase("1040"))
        {
            // Shield
            skill = SkillTable.getInstance().getInfo(1040, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1068"))
        {
            // Might
            skill = SkillTable.getInstance().getInfo(1068, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1043"))
        {
            // Holy Weapon
            skill = SkillTable.getInstance().getInfo(1043, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1044"))
        {
            // Regeneration
            skill = SkillTable.getInstance().getInfo(1044, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1073"))
        {
            // Kiss of Eva
            skill = SkillTable.getInstance().getInfo(1073, 2);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1077"))
        {
            // Focus
            skill = SkillTable.getInstance().getInfo(1077, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1078"))
        {
            // Concentration
            skill = SkillTable.getInstance().getInfo(1078, 6);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1085"))
        {
            // Acumen
            skill = SkillTable.getInstance().getInfo(1085, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1204"))
        {
            // Wind Walk
            skill = SkillTable.getInstance().getInfo(1204, 2);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1036"))
        {
            // Magic Barrier
            skill = SkillTable.getInstance().getInfo(1036, 2);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1045"))
        {
            // Blessed Body
            skill = SkillTable.getInstance().getInfo(1045, 6);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1048"))
        {
            // Blessed Soul
            skill = SkillTable.getInstance().getInfo(1048, 6);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1086"))
        {
            // Haste
            skill = SkillTable.getInstance().getInfo(1086, 2);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1240"))
        {
            // Guidance
            skill = SkillTable.getInstance().getInfo(1240, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1257"))
        {
            // Decrease Weight
            skill = SkillTable.getInstance().getInfo(1257, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1062"))
        {
            // Berserker Spirit
            skill = SkillTable.getInstance().getInfo(1062, 2);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1242"))
        {
            // Death Whisper
            skill = SkillTable.getInstance().getInfo(1242, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1243"))
        {
            // Bless Shield
            skill = SkillTable.getInstance().getInfo(1243, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1059"))
        {
            // Empower
            skill = SkillTable.getInstance().getInfo(1059, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1268"))
        {
            // Vampiric Rage
            skill = SkillTable.getInstance().getInfo(1268, 4);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1303"))
        {
            // Wild Magic
            skill = SkillTable.getInstance().getInfo(1303, 2);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1087"))
        {
            // Agility
            skill = SkillTable.getInstance().getInfo(1087, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1304"))
        {
            // Advanced Block
            skill = SkillTable.getInstance().getInfo(1304, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1397"))
        {
            // Clarity
            skill = SkillTable.getInstance().getInfo(1397, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1388"))
        {
            // Greater Might
            skill = SkillTable.getInstance().getInfo(1388, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1389"))
        {
            // Greater Shield
            skill = SkillTable.getInstance().getInfo(1389, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1356"))
        {
            // Pro. of Fire
            skill = SkillTable.getInstance().getInfo(1356, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1357"))
        {
            // Pro. of Wind
            skill = SkillTable.getInstance().getInfo(1357, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1355"))
        {
            // Pro. of Water
            skill = SkillTable.getInstance().getInfo(1355, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1035"))
        {
            // Mental Shield
            skill = SkillTable.getInstance().getInfo(1035, 4);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1032"))
        {
            // Invigor
            skill = SkillTable.getInstance().getInfo(1032, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1191"))
        {
            // Resist Fire
            skill = SkillTable.getInstance().getInfo(1191, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1033"))
        {
            // Resist Poison
            skill = SkillTable.getInstance().getInfo(1033, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1182"))
        {
            // Resist Aqua
            skill = SkillTable.getInstance().getInfo(1182, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1189"))
        {
            // Resist Wind
            skill = SkillTable.getInstance().getInfo(1189, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1392"))
        {
            // Holy Resistance
            skill = SkillTable.getInstance().getInfo(1392, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1393"))
        {
            // Unholy Resistance
            skill = SkillTable.getInstance().getInfo(1393, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1352"))
        {
            // Elemental Protection
            skill = SkillTable.getInstance().getInfo(1352, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1354"))
        {
            // Arcane Protection
            skill = SkillTable.getInstance().getInfo(1354, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1353"))
        {
            // Divine Protection
            skill = SkillTable.getInstance().getInfo(1353, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("4699"))
        {
            // Blessing of Queen
            skill = SkillTable.getInstance().getInfo(4699, 13);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("4700"))
        {
            // Gift of Queen
            skill = SkillTable.getInstance().getInfo(4700, 13);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("4702"))
        {
            // Blessing of Seraphim
            skill = SkillTable.getInstance().getInfo(4702, 13);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("4703"))
        {
            // Gift of Seraphim
            skill = SkillTable.getInstance().getInfo(4703, 13);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1007"))
        {
            // Chant of Battle
            skill = SkillTable.getInstance().getInfo(1007, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1009"))
        {
            // Chant of Shielding
            skill = SkillTable.getInstance().getInfo(1009, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1002"))
        {
            // Flame Chant
            skill = SkillTable.getInstance().getInfo(1002, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1006"))
        {
            // Chant of Fire
            skill = SkillTable.getInstance().getInfo(1006, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1251"))
        {
            // Chant of Fury
            skill = SkillTable.getInstance().getInfo(1251, 2);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1252"))
        {
            // Chant of Evasion
            skill = SkillTable.getInstance().getInfo(1252, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1253"))
        {
            // Chant of Rage
            skill = SkillTable.getInstance().getInfo(1253, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1284"))
        {
            // Chant of Revange
            skill = SkillTable.getInstance().getInfo(1284, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1308"))
        {
            // Chant of Predator
            skill = SkillTable.getInstance().getInfo(1308, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1309"))
        {
            // Chant of Eagle
            skill = SkillTable.getInstance().getInfo(1309, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1310"))
        {
            // Chant of Vampire
            skill = SkillTable.getInstance().getInfo(1310, 4);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1362"))
        {
            // Chant of Spirit
            skill = SkillTable.getInstance().getInfo(1362, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1390"))
        {
            // War Chant
            skill = SkillTable.getInstance().getInfo(1390, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1391"))
        {
            // Earth Chant
            skill = SkillTable.getInstance().getInfo(1391, 3);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1363"))
        {
            // Victory Chant
            skill = SkillTable.getInstance().getInfo(1363, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("1413"))
        {
            // Magnus' Chant
            skill = SkillTable.getInstance().getInfo(1413, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("271"))
        {
            // Dance of the Warrior
            skill = SkillTable.getInstance().getInfo(271, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("272"))
        {
            // Dance of Inspiration
            skill = SkillTable.getInstance().getInfo(272, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("273"))
        {
            // Dance of the Mystic
            skill = SkillTable.getInstance().getInfo(273, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("274"))
        {
            // Dance of Fire
            skill = SkillTable.getInstance().getInfo(274, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("275"))
        {
            // Dance of Fury
            skill = SkillTable.getInstance().getInfo(275, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("276"))
        {
            // Dance of Concentration
            skill = SkillTable.getInstance().getInfo(276, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("277"))
        {
            // Dance of Light
            skill = SkillTable.getInstance().getInfo(277, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("307"))
        {
            // Dance of Aqua Guard
            skill = SkillTable.getInstance().getInfo(307, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("309"))
        {
            // Dance of Earth Guard
            skill = SkillTable.getInstance().getInfo(309, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("310"))
        {
            // Dance of the Vampire
            skill = SkillTable.getInstance().getInfo(310, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("311"))
        {
            // Dance of Protection
            skill = SkillTable.getInstance().getInfo(311, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("365"))
        {
            // Siren's Dance
            skill = SkillTable.getInstance().getInfo(365, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("264"))
        {
            // Song of Earth
            skill = SkillTable.getInstance().getInfo(264, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("265"))
        {
            // Song of Life
            skill = SkillTable.getInstance().getInfo(265, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("266"))
        {
            // Song of Water
            skill = SkillTable.getInstance().getInfo(266, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("267"))
        {
            // Song of Warding
            skill = SkillTable.getInstance().getInfo(267, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("268"))
        {
            // Song of Wind
            skill = SkillTable.getInstance().getInfo(268, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("269"))
        {
            // Song of Hunter
            skill = SkillTable.getInstance().getInfo(269, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("270"))
        {
            // Song of Invocation
            skill = SkillTable.getInstance().getInfo(270, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("304"))
        {
            // Song of Vitality
            skill = SkillTable.getInstance().getInfo(304, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("305"))
        {
            // Song of Vengeance
            skill = SkillTable.getInstance().getInfo(305, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("306"))
        {
            // Song of Flame Guard
            skill = SkillTable.getInstance().getInfo(306, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("308"))
        {
            // Song of Storm Guard
            skill = SkillTable.getInstance().getInfo(308, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("349"))
        {
            // Song of Renewal
            skill = SkillTable.getInstance().getInfo(349, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("363"))
        {
            // Song of Meditation
            skill = SkillTable.getInstance().getInfo(363, 1);
            skill.getEffects(player, player);
        }
        else if (event.equalsIgnoreCase("364"))
        {
            // Song of Champion
            skill = SkillTable.getInstance().getInfo(364, 1);
            skill.getEffects(player, player);
        }
        return htmltext;
    }
    @Override
    public String onFirstTalk(Npc npc, Player player)
    {
        QuestState st = player.getQuestState(qn);
        if (st == null)
        {
            st = newQuestState(player);
        }
        return npc.getNpcId() + ".htm";
    }

}
