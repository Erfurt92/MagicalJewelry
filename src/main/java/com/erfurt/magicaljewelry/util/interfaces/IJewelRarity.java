package com.erfurt.magicaljewelry.util.interfaces;

import java.util.Random;

import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;

public interface IJewelRarity
{
	Random rand = new Random();
	
	public static int rareDropRate = MagicalJewelryConfigBuilder.JEWEL_RARE_DROP_RATE.get();
	public static int epicDropRate = MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();
	public static int legendaryDropRate = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
	
	public default int getRarity()
	{
		int chance = rand.nextInt(100);
		
		int rarity;
		
		if(chance <= legendaryDropRate) rarity = JewelRarity.LEGENDARY.getIndex();
		else if(chance <= epicDropRate && chance > legendaryDropRate) rarity = JewelRarity.EPIC.getIndex();
		else if(chance <= rareDropRate && chance > epicDropRate) rarity = JewelRarity.RARE.getIndex();
		else rarity = JewelRarity.UNCOMMEN.getIndex();
		
		return rarity;
	}
	
	public default int getRarityBoss()
	{
		int chance =  rand.nextInt(epicDropRate + legendaryDropRate);
		
		int rarity;
		
		if(chance <= legendaryDropRate) rarity = JewelRarity.LEGENDARY.getIndex();
		else rarity = JewelRarity.EPIC.getIndex();
		
		return rarity;
	}
}