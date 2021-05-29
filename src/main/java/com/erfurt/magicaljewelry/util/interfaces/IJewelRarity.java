package com.erfurt.magicaljewelry.util.interfaces;

import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;

import java.util.Random;

public interface IJewelRarity
{
	Random rand = new Random();

	static String getRarity()
	{
		String rarity;

		if(!MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get())
		{
			int legendaryDropRate = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
			int epicDropRate = MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();
			int rareDropRate = MagicalJewelryConfigBuilder.JEWEL_RARE_DROP_RATE.get();

			int totalEpicDropRate = epicDropRate + legendaryDropRate;
			int totalRareDropRate = rareDropRate + totalEpicDropRate;

			int totalChance = 100;

			int chance = rand.nextInt(totalChance) + 1;

			if(chance > 100 - legendaryDropRate) rarity = JewelRarity.LEGENDARY.getName();
			else if(chance > totalChance - totalEpicDropRate) rarity = JewelRarity.EPIC.getName();
			else if(chance > totalChance - totalRareDropRate) rarity = JewelRarity.RARE.getName();
			else rarity = JewelRarity.UNCOMMON.getName();
		}
		else rarity = MagicalJewelryConfigBuilder.JEWEL_RARITY_TO_DROP.get().getName();

		return rarity;
	}

	static String getRarityBoss()
	{
		String rarity;

		if(MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get()) rarity = MagicalJewelryConfigBuilder.JEWEL_RARITY_TO_DROP.get().getName();
		else
		{
			int legendaryDropRate = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
			int epicDropRate = MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();

			int totalEpicDropRate = epicDropRate + legendaryDropRate;

			int chance = rand.nextInt(totalEpicDropRate) + 1;

			if(chance <= legendaryDropRate) rarity = JewelRarity.LEGENDARY.getName();
			else rarity = JewelRarity.EPIC.getName();
		}

		return rarity;
	}

	static String getRarityCommand()
	{
		String rarity = JewelRarity.UNCOMMON.getName();
		int random = rand.nextInt(JewelRarity.values().length);
		if(random == 0) rarity = JewelRarity.LEGENDARY.getName();
		else if(random == 1) rarity = JewelRarity.EPIC.getName();
		else if(random == 2) rarity = JewelRarity.RARE.getName();
		return rarity;
	}
}