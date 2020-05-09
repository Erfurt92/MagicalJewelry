package com.erfurt.magicaljewelry.util.interfaces;

import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface IJewelEffects
{
	Random rand = new Random();
	
	public static List<Effect> defaultEffectsList = new ArrayList<Effect>();
	
	public static void init()
	{
		Effects();
	}
	
	public static void Effects()
	{
		defaultEffectsList.add(Effects.SPEED);
		defaultEffectsList.add(Effects.HASTE);
		defaultEffectsList.add(Effects.STRENGTH);
		defaultEffectsList.add(Effects.JUMP_BOOST);
		defaultEffectsList.add(Effects.RESISTANCE);
		defaultEffectsList.add(Effects.LUCK);
		
		defaultEffectsList.add(Effects.FIRE_RESISTANCE);
		defaultEffectsList.add(Effects.WATER_BREATHING);
		defaultEffectsList.add(Effects.NIGHT_VISION);
	}
	
	public default List<Integer> getEffects(int rarity, List<Integer> effectsForJewel)
	{
		List<Effect> effectsForComparing = new ArrayList<Effect>();
		List<Integer> tempIntArray = new ArrayList<Integer>();
		int j = defaultEffectsList.size() - 3;

		int effectCount;
		
		switch(JewelRarity.byIndex(rarity))
		{
			case UNCOMMON: effectCount = MagicalJewelryConfigBuilder.JEWEL_UNCOMMON_EFFECT_AMOUNT.get(); break;
			case RARE: effectCount = MagicalJewelryConfigBuilder.JEWEL_RARE_EFFECT_AMOUNT.get(); break;
			case EPIC: effectCount = MagicalJewelryConfigBuilder.JEWEL_EPIC_EFFECT_AMOUNT.get(); break;
			case LEGENDARY: effectCount = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_EFFECT_AMOUNT.get(); break;
			default: effectCount = 0; break;
		}
		
		if(rarity == JewelRarity.LEGENDARY.getIndex() && MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_EFFECTS.get())
		{
			tempIntArray.add(rand.nextInt(3) + j);
		}
		
		for(int i = 1; i <= effectCount; i++)
		{
			int k = rand.nextInt(j);
			
			if(!effectsForComparing.contains(defaultEffectsList.get(k)))
			{
				effectsForComparing.add(defaultEffectsList.get(k));
				tempIntArray.add(k);
			}
			else
			{
				i = effectsForComparing.size();
			}
		}

		effectsForJewel = tempIntArray;
		
		return effectsForJewel;
	}
}