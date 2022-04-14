package com.erfurt.magicaljewelry.util.interfaces;

import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;

import java.util.Random;

public interface IJewelRarity
{
	Random rand = new Random();

	int legendaryLuckIncrease = 3;
	int epicLuckIncrease = 2;
	int rareLuckIncrease = 1;

	static String getRarity(Entity entity)
	{
		String rarity;

		LivingEntity livingEntity = null;
		if(entity instanceof LivingEntity) livingEntity = (LivingEntity) entity;

		if(!MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get())
		{
			int legendaryDRConfig = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
			int epicDRConfig = MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();
			int rareDRConfig = MagicalJewelryConfigBuilder.JEWEL_RARE_DROP_RATE.get();

			int legendaryDropRate;
			int epicDropRate;
			int rareDropRate;

			if(!(livingEntity == null) && livingEntity.getActivePotionMap().containsKey(Effects.LUCK))
			{
				int luckLevel = livingEntity.getActivePotionEffect(Effects.LUCK).getAmplifier() + 1;

				legendaryDropRate = legendaryDRConfig + (legendaryLuckIncrease * luckLevel);
				epicDropRate = epicDRConfig + (epicLuckIncrease * luckLevel);
				rareDropRate = rareDRConfig + (rareLuckIncrease * luckLevel);
			}
			else
			{
				legendaryDropRate = legendaryDRConfig;
				epicDropRate = epicDRConfig;
				rareDropRate = rareDRConfig;
			}

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

	static String getRarityBoss(Entity entity)
	{
		String rarity;

		LivingEntity livingEntity = null;
		if(entity instanceof LivingEntity) livingEntity = (LivingEntity) entity;

		if(MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get()) rarity = MagicalJewelryConfigBuilder.JEWEL_RARITY_TO_DROP.get().getName();
		else
		{
			int legendaryDRConfig = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
			int epicDRConfig = MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();

			int legendaryDropRate;
			int epicDropRate;

			if(!(livingEntity == null) && livingEntity.getActivePotionMap().containsKey(Effects.LUCK))
			{
				int luckLevel = livingEntity.getActivePotionEffect(Effects.LUCK).getAmplifier() + 1;

				legendaryDropRate = legendaryDRConfig + (legendaryLuckIncrease * luckLevel);
				epicDropRate = epicDRConfig + (epicLuckIncrease * luckLevel);
			}
			else
			{
				legendaryDropRate = legendaryDRConfig;
				epicDropRate = epicDRConfig;
			}

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