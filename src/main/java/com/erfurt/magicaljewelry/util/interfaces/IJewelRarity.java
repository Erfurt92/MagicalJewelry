package com.erfurt.magicaljewelry.util.interfaces;

import java.util.Random;

import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;

public interface IJewelRarity
{
	Random rand = new Random();

	static int legendaryDropRate = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
	static int epicDropRate = MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();
	static int rareDropRate = MagicalJewelryConfigBuilder.JEWEL_RARE_DROP_RATE.get();

	public static int totalEpicDropRate = epicDropRate + legendaryDropRate;
	public static int totalRareDropRate = rareDropRate + totalEpicDropRate;

	public default int getRarity()
	{
		int chance = rand.nextInt(100) + 1;

		int rarity;
		
		if(chance <= legendaryDropRate) rarity = JewelRarity.LEGENDARY.getIndex();
		else if(chance <= totalEpicDropRate && chance > legendaryDropRate) rarity = JewelRarity.EPIC.getIndex();
		else if(chance <= totalRareDropRate && chance > totalEpicDropRate) rarity = JewelRarity.RARE.getIndex();
		else rarity = JewelRarity.UNCOMMON.getIndex();
		
		return rarity;
	}
	
	public default int getRarityBoss()
	{
		int chance =  rand.nextInt(totalEpicDropRate) + 1;
		
		int rarity;
		
		if(chance <= legendaryDropRate) rarity = JewelRarity.LEGENDARY.getIndex();
		else rarity = JewelRarity.EPIC.getIndex();
		
		return rarity;
	}
}