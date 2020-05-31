package com.erfurt.magicaljewelry.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MagicalJewelryConfigBuilder
{
	public static ForgeConfigSpec.IntValue JEWEL_UNCOMMON_EFFECT_AMOUNT;
	public static ForgeConfigSpec.IntValue JEWEL_RARE_EFFECT_AMOUNT;
	public static ForgeConfigSpec.IntValue JEWEL_EPIC_EFFECT_AMOUNT;
	public static ForgeConfigSpec.IntValue JEWEL_LEGENDARY_EFFECT_AMOUNT;
	public static ForgeConfigSpec.IntValue JEWEL_MAX_EFFECT_LEVEL;
	public static ForgeConfigSpec.BooleanValue JEWEL_ATTRIBUTES;
	public static ForgeConfigSpec.BooleanValue JEWEL_LEGENDARY_EFFECTS;
	public static ForgeConfigSpec.BooleanValue JEWEL_DURABILITY;
	public static ForgeConfigSpec.IntValue JEWEL_UNCOMMON_DURABILITY;
	public static ForgeConfigSpec.IntValue JEWEL_RARE_DURABILITY;
	public static ForgeConfigSpec.IntValue JEWEL_EPIC_DURABILITY;
	public static ForgeConfigSpec.BooleanValue JEWEL_RARITY_TOOLTIP;
	public static ForgeConfigSpec.BooleanValue JEWEL_RARITY_NAME;
	public static ForgeConfigSpec.IntValue JEWEL_RARE_DROP_RATE;
	public static ForgeConfigSpec.IntValue JEWEL_EPIC_DROP_RATE;
	public static ForgeConfigSpec.IntValue JEWEL_LEGENDARY_DROP_RATE;
	
	public static void init(ForgeConfigSpec.Builder SERVER_BUILDER)
	{
		SERVER_BUILDER.comment("Misc Jewel Settings").push("effect_settings");
		JEWEL_UNCOMMON_EFFECT_AMOUNT = SERVER_BUILDER.comment("Amount of effects for Uncommon rarity [default: 1]").defineInRange("uncommonAmount", 1, 0, 3);
		JEWEL_RARE_EFFECT_AMOUNT = SERVER_BUILDER.comment("Amount of effects for Rare rarity [default: 2]").defineInRange("rareAmount", 2, 0, 4);
		JEWEL_EPIC_EFFECT_AMOUNT = SERVER_BUILDER.comment("Amount of effects for Epic rarity [default: 2]").defineInRange("epicAmount", 2, 0, 5);
		JEWEL_LEGENDARY_EFFECT_AMOUNT = SERVER_BUILDER.comment("Amount of effects for Legendary rarity, excluding Legendary Effect [default: 2]").defineInRange("legendaryAmount", 2, 0, 6);
		JEWEL_MAX_EFFECT_LEVEL = SERVER_BUILDER.comment("Max level for combined effects [default: 2]").defineInRange("maxEffectLevel", 2, 1, 3);
		JEWEL_ATTRIBUTES = SERVER_BUILDER.comment("Should Epic and Legendary rarity have attributes").define("epicLegendaryAttributes", true);
		JEWEL_LEGENDARY_EFFECTS = SERVER_BUILDER.comment("Should Legendary rarity have special effects").define("legendaryEffects", true);
		SERVER_BUILDER.pop();

		SERVER_BUILDER.comment("Misc Jewel Settings").push("jewel_durability");
		JEWEL_DURABILITY = SERVER_BUILDER.comment("Should Jewels have durability, legendary is unbreakable").define("jewelDurability", true);
		JEWEL_UNCOMMON_DURABILITY = SERVER_BUILDER.comment("Set the durability of Uncommon rarity [default: 7200]").defineInRange("uncommonDurability", 7200, 600, 21600);
		JEWEL_RARE_DURABILITY = SERVER_BUILDER.comment("Set the durability of Rare rarity [default: 14400]").defineInRange("rareDurability", 14400, 600, 21600);
		JEWEL_EPIC_DURABILITY = SERVER_BUILDER.comment("Set the durability of Epic rarity [default: 21600]").defineInRange("epicDurability", 21600, 600, 21600);
		SERVER_BUILDER.pop();

		SERVER_BUILDER.comment("Misc Jewel Settings").push("rarity_displayed");
		JEWEL_RARITY_TOOLTIP = SERVER_BUILDER.comment("Rarity is displayed in tooltips").define("rarityTooltips", true);
		JEWEL_RARITY_NAME = SERVER_BUILDER.comment("Rarity is displayed in the name").define("rarityName", true);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.comment("Rarity Drop Rate Settings", "Note that to change the drop rate for Uncommon rarity, you have to change the other values.", "If Rare, Epic and Legendary are all at max drop rate, Uncommon will not be able to drop.").push("rarity_drop_rate");
		JEWEL_RARE_DROP_RATE = SERVER_BUILDER.comment("Drop rate for Rare rarity [default: 30]").defineInRange("rareDrop", 30, 15, 60);
		JEWEL_EPIC_DROP_RATE = SERVER_BUILDER.comment("Drop rate for Epic rarity [default: 15]").defineInRange("epicDrop", 15, 5, 30);
		JEWEL_LEGENDARY_DROP_RATE = SERVER_BUILDER.comment("Drop rate for Legendary rarity [default: 5]").defineInRange("legendaryDrop", 5, 1, 10);
		SERVER_BUILDER.pop();
	}
}