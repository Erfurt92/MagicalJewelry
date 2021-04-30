package com.erfurt.magicaljewelry.util.config;

import com.erfurt.magicaljewelry.util.enums.JewelRarity;
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
	public static ForgeConfigSpec.BooleanValue JEWEL_EFFECT_ICON;
	public static ForgeConfigSpec.IntValue JEWEL_RARE_DROP_RATE;
	public static ForgeConfigSpec.IntValue JEWEL_EPIC_DROP_RATE;
	public static ForgeConfigSpec.IntValue JEWEL_LEGENDARY_DROP_RATE;
	public static ForgeConfigSpec.BooleanValue JEWEL_ONE_RARITY_DROP;
	public static ForgeConfigSpec.EnumValue<JewelRarity> JEWEL_RARITY_TO_DROP;
	public static ForgeConfigSpec.BooleanValue JEWELS_IN_CHESTS;

	static class CommonConfig
	{
		CommonConfig(ForgeConfigSpec.Builder config_builder)
		{
			config_builder.comment("Misc Jewel Settings").push("effect_settings");
			JEWEL_UNCOMMON_EFFECT_AMOUNT = config_builder.comment("Amount of effects for Uncommon rarity [default: 1]").defineInRange("uncommonAmount", 1, 0, 3);
			JEWEL_RARE_EFFECT_AMOUNT = config_builder.comment("Amount of effects for Rare rarity [default: 2]").defineInRange("rareAmount", 2, 0, 4);
			JEWEL_EPIC_EFFECT_AMOUNT = config_builder.comment("Amount of effects for Epic rarity [default: 2]").defineInRange("epicAmount", 2, 0, 5);
			JEWEL_LEGENDARY_EFFECT_AMOUNT = config_builder.comment("Amount of effects for Legendary rarity, excluding Legendary Effect [default: 2]").defineInRange("legendaryAmount", 2, 0, 6);
			JEWEL_MAX_EFFECT_LEVEL = config_builder.comment("Max level for combined effects [default: 2]").defineInRange("maxEffectLevel", 2, 1, 3);
			JEWEL_ATTRIBUTES = config_builder.comment("Should Epic and Legendary rarity have attributes").define("epicLegendaryAttributes", true);
			JEWEL_LEGENDARY_EFFECTS = config_builder.comment("Should Legendary rarity have special effects").define("legendaryEffects", true);
			config_builder.pop();

			config_builder.comment("Misc Jewel Settings").push("effect_icon_disabled");
			JEWEL_EFFECT_ICON = config_builder.define("effectIconDisabled", false);
			config_builder.pop();

			config_builder.comment("Misc Jewel Settings").push("jewel_durability");
			JEWEL_DURABILITY = config_builder.comment("Should Jewels have durability, Legendary rarity is always unbreakable").define("jewelDurability", true);
			JEWEL_UNCOMMON_DURABILITY = config_builder.comment("Set the durability of Uncommon rarity [default: 7200]").defineInRange("uncommonDurability", 7200, 600, 21600);
			JEWEL_RARE_DURABILITY = config_builder.comment("Set the durability of Rare rarity [default: 14400]").defineInRange("rareDurability", 14400, 600, 21600);
			JEWEL_EPIC_DURABILITY = config_builder.comment("Set the durability of Epic rarity [default: 21600]").defineInRange("epicDurability", 21600, 600, 21600);
			config_builder.pop();

			config_builder.comment("Rarity Drop Settings").push("rarity_drop_settings");
			JEWEL_RARE_DROP_RATE = config_builder.comment("Note that to change the drop rate for Uncommon rarity, you have to change the other values.", "If Rare, Epic and Legendary are all at max drop rate, Uncommon will not be able to drop.", "Drop rate for Rare rarity [default: 30]").defineInRange("rareDrop", 30, 15, 60);
			JEWEL_EPIC_DROP_RATE = config_builder.comment("Drop rate for Epic rarity [default: 15]").defineInRange("epicDrop", 15, 5, 30);
			JEWEL_LEGENDARY_DROP_RATE = config_builder.comment("Drop rate for Legendary rarity [default: 5]").defineInRange("legendaryDrop", 5, 1, 10);
			JEWEL_ONE_RARITY_DROP = config_builder.comment("Should there only be one rarity that can drop").define("oneRarityDrop", false);
			JEWEL_RARITY_TO_DROP = config_builder.comment("Pick what rarity to drop, if oneRarityDrop is enabled").defineEnum("jewelRarityToDrop", JewelRarity.UNCOMMON);
			config_builder.pop();

			config_builder.comment("Jewels in chests").push("jewels_in_chests");
			JEWELS_IN_CHESTS = config_builder.comment("Jewels can be found in chests").define("jewelsInChest", true);
			config_builder.pop();
		}
	}

	static class ClientConfig
	{
		ClientConfig(ForgeConfigSpec.Builder config_builder)
		{
			config_builder.comment("Misc Jewel Settings").push("rarity_displayed");
			JEWEL_RARITY_TOOLTIP = config_builder.comment("Rarity is displayed in tooltips").define("rarityTooltips", true);
			JEWEL_RARITY_NAME = config_builder.comment("Rarity is displayed in the name").define("rarityName", true);
			config_builder.pop();
		}
	}
}