package com.erfurt.magicaljewelry.util.interfaces;

import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;

import java.util.Random;

public interface IJewelRarity
{
	Random rand = new Random();

	default String getRarity()
	{
		String rarity;

		if(!MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get())
		{
			int legendaryDropRate = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
			int epicDropRate = MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();
			int rareDropRate = MagicalJewelryConfigBuilder.JEWEL_RARE_DROP_RATE.get();

			int totalEpicDropRate = epicDropRate + legendaryDropRate;
			int totalRareDropRate = rareDropRate + totalEpicDropRate;

			int chance = rand.nextInt(100) + 1;

			if(MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_UPGRADE_ONLY.get() && chance <= legendaryDropRate) chance = chance + legendaryDropRate;

			if(chance <= legendaryDropRate) rarity = JewelRarity.LEGENDARY.getName();
			else if(chance <= totalEpicDropRate) rarity = JewelRarity.EPIC.getName();
			else if(chance <= totalRareDropRate) rarity = JewelRarity.RARE.getName();
			else rarity = JewelRarity.UNCOMMON.getName();
		}
		else rarity = MagicalJewelryConfigBuilder.JEWEL_RARITY_TO_DROP.get().getName();

		return rarity;
	}
	
	default String getRarityBoss()
	{
		String rarity;

		if(MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_UPGRADE_ONLY.get() && !MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get()) rarity = JewelRarity.EPIC.getName();
		else if(MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get()) rarity = MagicalJewelryConfigBuilder.JEWEL_RARITY_TO_DROP.get().getName();
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
}