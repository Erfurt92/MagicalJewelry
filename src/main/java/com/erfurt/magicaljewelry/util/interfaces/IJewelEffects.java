package com.erfurt.magicaljewelry.util.interfaces;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface IJewelEffects
{
	Random rand = new Random();
	
	List<Effect> defaultEffectsList = new ArrayList<>();
	List<Effect> legendaryEffectsList = new ArrayList<>();
	
	static void init()
	{
		Effects();
	}
	
	static void Effects()
	{
		defaultEffectsList.add(0, Effects.SPEED);
		defaultEffectsList.add(1, Effects.HASTE);
		defaultEffectsList.add(2, Effects.STRENGTH);
		defaultEffectsList.add(3, Effects.JUMP_BOOST);
		defaultEffectsList.add(4, Effects.RESISTANCE);
		defaultEffectsList.add(5, Effects.LUCK);

		legendaryEffectsList.add(0, null);
		legendaryEffectsList.add(1, Effects.FIRE_RESISTANCE);
		legendaryEffectsList.add(2, Effects.WATER_BREATHING);
		legendaryEffectsList.add(3, Effects.NIGHT_VISION);
		// legendaryEffectsList.add(, Effects.REGENERATION); // Maybe add, still on the fence about it
	}
	
	default List<Integer> getEffects(String rarity, List<Integer> effectsForJewel)
	{
		List<Effect> effectsForComparing = new ArrayList<>();
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