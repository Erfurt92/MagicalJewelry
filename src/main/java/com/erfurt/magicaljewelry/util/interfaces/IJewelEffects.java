package com.erfurt.magicaljewelry.util.interfaces;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface IJewelEffects
{
	Random rand = new Random();
	
	List<MobEffect> defaultEffectsList = new ArrayList<>();
	List<MobEffect> legendaryEffectsList = new ArrayList<>();
	
	static void init()
	{
		Effects();
	}
	
	static void Effects()
	{
		defaultEffectsList.add(0, MobEffects.MOVEMENT_SPEED);
		defaultEffectsList.add(1, MobEffects.DIG_SPEED);
		defaultEffectsList.add(2, MobEffects.DAMAGE_BOOST);
		defaultEffectsList.add(3, MobEffects.JUMP);
		defaultEffectsList.add(4, MobEffects.DAMAGE_RESISTANCE);
		defaultEffectsList.add(5, MobEffects.LUCK);

		legendaryEffectsList.add(0, MobEffects.REGENERATION);
		legendaryEffectsList.add(1, MobEffects.FIRE_RESISTANCE);
		legendaryEffectsList.add(2, MobEffects.WATER_BREATHING);
		legendaryEffectsList.add(3, MobEffects.NIGHT_VISION);
	}
	
	static List<Integer> getEffects(String rarity, List<Integer> effectsForJewel)
	{
		List<MobEffect> effectsForComparing = new ArrayList<>();
		List<Integer> tempIntArray = new ArrayList<>();

		for(int i = 1; i <= defaultEffectsList.size(); i++)
		{
			int k = rand.nextInt(defaultEffectsList.size());
			
			if(!effectsForComparing.contains(defaultEffectsList.get(k)))
			{
				effectsForComparing.add(defaultEffectsList.get(k));
				tempIntArray.add(k);
			}
			else i = effectsForComparing.size();
		}

		effectsForJewel = tempIntArray;
		
		return effectsForJewel;
	}
}