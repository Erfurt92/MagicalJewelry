package com.erfurt.magicaljewelry.util.interfaces;

import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface IJewelEffects
{
	Random rand = new Random();
	
	List<Effect> defaultEffectsList = new ArrayList<>();
	
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
		
		defaultEffectsList.add(6, Effects.FIRE_RESISTANCE);
		defaultEffectsList.add(7, Effects.WATER_BREATHING);
		defaultEffectsList.add(8, Effects.NIGHT_VISION);
	}
	
	default List<Integer> getEffects(String rarity, List<Integer> effectsForJewel)
	{
		List<Effect> effectsForComparing = new ArrayList<Effect>();
		List<Integer> tempIntArray = new ArrayList<Integer>();
		int j = defaultEffectsList.size() - 3;

		for(int i = 1; i <= j; i++)
		{
			int k = rand.nextInt(j);
			
			if(!effectsForComparing.contains(defaultEffectsList.get(k)))
			{
				effectsForComparing.add(defaultEffectsList.get(k));
				tempIntArray.add(k);
			}
			else i = effectsForComparing.size();
		}

		if(rarity == JewelRarity.LEGENDARY.getName()) tempIntArray.add(rand.nextInt(3) + j);

		effectsForJewel = tempIntArray;
		
		return effectsForJewel;
	}
}