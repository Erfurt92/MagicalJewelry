package com.erfurt.magicaljewelry.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MagicalJewelryConfigBuilder
{
	public static ForgeConfigSpec.IntValue JEWEL_UNCOMMON_EFFECT_AMOUNT, JEWEL_RARE_EFFECT_AMOUNT, JEWEL_EPIC_EFFECT_AMOUNT, JEWEL_LEGENDARY_EFFECT_AMOUNT;
	
	public static ForgeConfigSpec.BooleanValue JEWEL_RARTIY_TOOLTIP, JEWEL_RARTIY_NAME;
	
	public static ForgeConfigSpec.IntValue JEWEL_RARE_DROP_RATE, JEWEL_EPIC_DROP_RATE, JEWEL_LEGENDARY_DROP_RATE;
	
	public static void init(ForgeConfigSpec.Builder SERVER_BUILDER)
	{
		SERVER_BUILDER.comment("Misc Jewel Settings").push("effect_amount");
		JEWEL_UNCOMMON_EFFECT_AMOUNT = SERVER_BUILDER.comment("Amount of effects for Uncommon rarity [default: 1]").defineInRange("uncommenAmount", 1, 0, 4);
		JEWEL_RARE_EFFECT_AMOUNT = SERVER_BUILDER.comment("Amount of effects for Rare rarity [default: 2]").defineInRange("rareAmount", 2, 0, 5);
		JEWEL_EPIC_EFFECT_AMOUNT = SERVER_BUILDER.comment("Amount of effects for Epic rarity [default: 3]").defineInRange("epicAmount", 3, 0, 6);
		JEWEL_LEGENDARY_EFFECT_AMOUNT = SERVER_BUILDER.comment("Amount of effects for Legendary rarity [default: 3]").defineInRange("legendaryAmount", 3, 0, 6);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.push("rarity_displayed");
		JEWEL_RARTIY_TOOLTIP = SERVER_BUILDER.comment("Rarity is displayed in tooltips").define("rarityTooltips", true);
		JEWEL_RARTIY_NAME = SERVER_BUILDER.comment("Rarity is displayed in the name").define("rarityName", true);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.comment("Rarity Drop Rate Settings", "Note that to change the drop rate for Uncommon rarity, you have to change the other values.", "If Rare, Epic and Legendary are all at max drop rate, Uncommon will not be able to drop.").push("rarity_drop_rate");
		JEWEL_RARE_DROP_RATE = SERVER_BUILDER.comment("Drop rate for Rare rarity [default: 30]").defineInRange("rareDrop", 30, 15, 60);
		JEWEL_RARE_DROP_RATE = SERVER_BUILDER.comment("Drop rate for Epic rarity [default: 15]").defineInRange("epicDrop", 15, 5, 30);
		JEWEL_RARE_DROP_RATE = SERVER_BUILDER.comment("Drop rate for Legendary rarity [default: 5]").defineInRange("legendaryDrop", 5, 1, 10);
		
	}
}