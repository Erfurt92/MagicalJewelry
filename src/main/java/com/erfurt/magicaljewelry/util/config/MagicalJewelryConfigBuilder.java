package com.erfurt.magicaljewelry.util.config;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import net.minecraftforge.common.ForgeConfigSpec;

public class MagicalJewelryConfigBuilder
{
	public static ForgeConfigSpec.IntValue JEWEL_UNCOMMON_EFFECT_AMOUNT;
	public static String UNCOMMON_AMOUNT = "uncommonAmount";
	public static ForgeConfigSpec.IntValue JEWEL_RARE_EFFECT_AMOUNT;
	public static String RARE_AMOUNT = "rareAmount";
	public static ForgeConfigSpec.IntValue JEWEL_EPIC_EFFECT_AMOUNT;
	public static String EPIC_AMOUNT = "epicAmount";
	public static ForgeConfigSpec.IntValue JEWEL_LEGENDARY_EFFECT_AMOUNT;
	public static String LEGENDARY_AMOUNT = "legendaryAmount";
	public static ForgeConfigSpec.IntValue JEWEL_MAX_EFFECT_LEVEL;
	public static String MAX_EFFECT_LEVEL = "maxEffectLevel";
	public static ForgeConfigSpec.BooleanValue JEWEL_ATTRIBUTES;
	public static String EPIC_LEGENDARY_ATTRIBUTES = "epicLegendaryAttributes";
	public static ForgeConfigSpec.BooleanValue JEWEL_LEGENDARY_EFFECTS;
	public static String LEGENDARY_EFFECTS = "legendaryEffects";
	public static ForgeConfigSpec.BooleanValue JEWEL_DURABILITY;
	public static String DURABILITY = "jewelDurability";
	public static ForgeConfigSpec.IntValue JEWEL_UNCOMMON_DURABILITY;
	public static String UNCOMMON_DURABILITY = "uncommonDurability";
	public static ForgeConfigSpec.IntValue JEWEL_RARE_DURABILITY;
	public static String RARE_DURABILITY = "rareDurability";
	public static ForgeConfigSpec.IntValue JEWEL_EPIC_DURABILITY;
	public static String EPIC_DURABILITY = "epicDurability";
	public static ForgeConfigSpec.BooleanValue JEWEL_RARITY_TOOLTIP;
	public static String RARITY_TOOLTIPS = "rarityTooltips";
	public static ForgeConfigSpec.BooleanValue JEWEL_RARITY_NAME;
	public static String RARITY_NAME = "rarityName";
	public static ForgeConfigSpec.BooleanValue JEWEL_EFFECT_ICON;
	public static String EFFECT_ICON_DISABLED = "effectIconDisabled";
	public static ForgeConfigSpec.IntValue JEWEL_RARE_DROP_RATE;
	public static String RARE_DROP = "rareDrop";
	public static ForgeConfigSpec.IntValue JEWEL_EPIC_DROP_RATE;
	public static String EPIC_DROP = "epicDrop";
	public static ForgeConfigSpec.IntValue JEWEL_LEGENDARY_DROP_RATE;
	public static String LEGENDARY_DROP = "legendaryDrop";
	public static ForgeConfigSpec.BooleanValue JEWEL_ONE_RARITY_DROP;
	public static String ONE_RARITY_DROP = "oneRarityDrop";
	public static ForgeConfigSpec.EnumValue<JewelRarity> JEWEL_RARITY_TO_DROP;
	public static String RARITY_TO_DROP = "jewelRarityToDrop";
	public static ForgeConfigSpec.BooleanValue JEWELS_IN_CHESTS;
	public static String DROP_IN_CHESTS = "jewelsInChest";

	public static String translatable(String name)
	{
		return "gui.config." + MagicalJewelry.MOD_ID + "." + name;
	}

	static class CommonConfig
	{
		CommonConfig(ForgeConfigSpec.Builder config_builder)
		{
			config_builder.comment("Misc Jewel Settings").push("effect_settings");
			JEWEL_UNCOMMON_EFFECT_AMOUNT = config_builder.comment("Amount of effects for Uncommon rarity [default: 1]").translation(translatable(UNCOMMON_AMOUNT)).defineInRange(UNCOMMON_AMOUNT, 1, 0, 3);
			JEWEL_RARE_EFFECT_AMOUNT = config_builder.comment("Amount of effects for Rare rarity [default: 2]").translation(translatable(RARE_AMOUNT)).defineInRange(RARE_AMOUNT, 2, 0, 4);
			JEWEL_EPIC_EFFECT_AMOUNT = config_builder.comment("Amount of effects for Epic rarity [default: 2]").translation(translatable(EPIC_AMOUNT)).defineInRange(EPIC_AMOUNT, 2, 0, 5);
			JEWEL_LEGENDARY_EFFECT_AMOUNT = config_builder.comment("Amount of effects for Legendary rarity, excluding Legendary Effect [default: 2]").translation(translatable(LEGENDARY_AMOUNT)).defineInRange(LEGENDARY_AMOUNT, 2, 0, 6);
			JEWEL_MAX_EFFECT_LEVEL = config_builder.comment("Max level for combined effects [default: 2]").translation(translatable(MAX_EFFECT_LEVEL)).defineInRange(MAX_EFFECT_LEVEL, 2, 1, 3);
			JEWEL_ATTRIBUTES = config_builder.comment("Should Epic and Legendary rarity have attributes").translation(translatable(EPIC_LEGENDARY_ATTRIBUTES)).define(EPIC_LEGENDARY_ATTRIBUTES, true);
			JEWEL_LEGENDARY_EFFECTS = config_builder.comment("Should Legendary rarity have special effects").translation(translatable(LEGENDARY_EFFECTS)).define(LEGENDARY_EFFECTS, true);
			config_builder.pop();

			config_builder.comment("Misc Jewel Settings").push("effect_icon_disabled");
			JEWEL_EFFECT_ICON = config_builder.comment("Disable Effect Icons in the top right corner").translation(translatable(EFFECT_ICON_DISABLED)).define(EFFECT_ICON_DISABLED, false);
			config_builder.pop();

			config_builder.comment("Misc Jewel Settings").push("jewel_durability");
			JEWEL_DURABILITY = config_builder.comment("Should Jewels have durability, Legendary rarity is always unbreakable").translation(translatable(DURABILITY)).define(DURABILITY, true);
			JEWEL_UNCOMMON_DURABILITY = config_builder.comment("Set the durability of Uncommon rarity [default: 7200]").translation(translatable(UNCOMMON_DURABILITY)).defineInRange(UNCOMMON_DURABILITY, 7200, 600, 21600);
			JEWEL_RARE_DURABILITY = config_builder.comment("Set the durability of Rare rarity [default: 14400]").translation(translatable(RARE_DURABILITY)).defineInRange(RARE_DURABILITY, 14400, 600, 21600);
			JEWEL_EPIC_DURABILITY = config_builder.comment("Set the durability of Epic rarity [default: 21600]").translation(translatable(EPIC_DURABILITY)).defineInRange(EPIC_DURABILITY, 21600, 600, 21600);
			config_builder.pop();

			config_builder.comment("Rarity Drop Settings").push("rarity_drop_settings");
			JEWEL_RARE_DROP_RATE = config_builder.comment("Note that to change the drop rate for Uncommon rarity, you have to change the other values.", "If Rare, Epic and Legendary are all at max drop rate, Uncommon will not be able to drop.", "Drop rate for Rare rarity [default: 30]").translation(translatable(RARE_DROP)).defineInRange(RARE_DROP, 30, 15, 60);
			JEWEL_EPIC_DROP_RATE = config_builder.comment("Note that to change the drop rate for Uncommon rarity, you have to change the other values.", "If Rare, Epic and Legendary are all at max drop rate, Uncommon will not be able to drop.", "Drop rate for Epic rarity [default: 15]").translation(translatable(EPIC_DROP)).defineInRange(EPIC_DROP, 15, 5, 30);
			JEWEL_LEGENDARY_DROP_RATE = config_builder.comment("Note that to change the drop rate for Uncommon rarity, you have to change the other values.", "If Rare, Epic and Legendary are all at max drop rate, Uncommon will not be able to drop.", "Drop rate for Legendary rarity [default: 5]").translation(translatable(LEGENDARY_DROP)).defineInRange(LEGENDARY_DROP, 5, 1, 10);
			JEWEL_ONE_RARITY_DROP = config_builder.comment("Should there only be one rarity that can drop").translation(translatable(ONE_RARITY_DROP)).define(ONE_RARITY_DROP, false);
			JEWEL_RARITY_TO_DROP = config_builder.comment("Pick what rarity to drop, if oneRarityDrop is enabled").translation(translatable(RARITY_TO_DROP)).defineEnum(RARITY_TO_DROP, JewelRarity.UNCOMMON);
			config_builder.pop();

			config_builder.comment("Jewels in chests").push("jewels_in_chests");
			JEWELS_IN_CHESTS = config_builder.comment("Jewels can be found in chests").translation(translatable(DROP_IN_CHESTS)).define(DROP_IN_CHESTS, true);
			config_builder.pop();
		}
	}

	static class ClientConfig
	{
		ClientConfig(ForgeConfigSpec.Builder config_builder)
		{
			config_builder.comment("Misc Jewel Settings").push("rarity_displayed");
			JEWEL_RARITY_TOOLTIP = config_builder.comment("Rarity is displayed in tooltips").translation(translatable(RARITY_TOOLTIPS)).define(RARITY_TOOLTIPS, true);
			JEWEL_RARITY_NAME = config_builder.comment("Rarity is displayed in the name").translation(translatable(RARITY_NAME)).define(RARITY_NAME, true);
			config_builder.pop();
		}
	}
}