package com.erfurt.magicaljewelry.util.interfaces;

import java.util.Random;

import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;

public interface IJewelRarity
{
	Random rand = new Random();

	int legendaryDropRate = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
	int epicDropRate = MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();
	int rareDropRate = MagicalJewelryConfigBuilder.JEWEL_RARE_DROP_RATE.get();

	int totalEpicDropRate = epicDropRate + legendaryDropRate;
	int totalRareDropRate = rareDropRate + totalEpicDropRate;

	default String getRarity()
	{
		int chance = rand.nextInt(100) + 1;

		String rarity;
		
		if(chance <= legendaryDropRate) rarity = JewelRarity.LEGENDARY.getName();
		else if(chance <= totalEpicDropRate) rarity = JewelRarity.EPIC.getName();
		else if(chance <= totalRareDropRate) rarity = JewelRarity.RARE.getName();
		else rarity = JewelRarity.UNCOMMON.getName();
		
		return rarity;
	}
	
	default String getRarityBoss()
	{
		int chance = rand.nextInt(totalEpicDropRate) + 1;
		
		String rarity;
		
		if(chance <= legendaryDropRate) rarity = JewelRarity.LEGENDARY.getName();
		else rarity = JewelRarity.EPIC.getName();
		
		return rarity;
	}
}