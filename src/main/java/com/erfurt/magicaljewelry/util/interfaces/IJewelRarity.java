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

	default int getRarity()
	{
		int chance = rand.nextInt(100) + 1;

		int rarity;
		
		if(chance <= legendaryDropRate) rarity = JewelRarity.LEGENDARY.getIndex();
		else if(chance <= totalEpicDropRate && chance > legendaryDropRate) rarity = JewelRarity.EPIC.getIndex();
		else if(chance <= totalRareDropRate && chance > totalEpicDropRate) rarity = JewelRarity.RARE.getIndex();
		else rarity = JewelRarity.UNCOMMON.getIndex();
		
		return rarity;
	}
	
	default int getRarityBoss()
	{
		int chance =  rand.nextInt(totalEpicDropRate) + 1;
		
		int rarity;
		
		if(chance <= legendaryDropRate) rarity = JewelRarity.LEGENDARY.getIndex();
		else rarity = JewelRarity.EPIC.getIndex();
		
		return rarity;
	}
}