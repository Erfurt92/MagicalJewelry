package com.erfurt.magicaljewelry.data.client;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static com.erfurt.magicaljewelry.init.ItemInit.*;
import static com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder.*;
import static com.erfurt.magicaljewelry.util.enums.JewelRarity.*;

public class ModLangProvider extends LanguageProvider
{
    public ModLangProvider(DataGenerator gen)
    {
        super(gen, MagicalJewelry.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        addItem(GOLD_AMULET, "Gold Amulet");
        addItem(SILVER_AMULET, "Silver Amulet");
        addItem(GOLD_RING, "Gold Ring");
        addItem(SILVER_RING, "Silver Ring");
        addItem(GOLD_BRACELET, "Gold Bracelet");
        addItem(SILVER_BRACELET, "Silver Bracelet");
        addItem(THE_ONE_RING, "The One Ring");

        add("item.magicaljewelry.tooltip.creative", "This item have no effects or attributes attached to it, and therefore does nothing! This item can't be upgraded!");

        add(rarity(UNCOMMON), "Uncommon");
        add(rarity(RARE), "Rare");
        add(rarity(EPIC), "Epic");
        add(rarity(LEGENDARY), "Legendary");

        add(jewelGiveFailureCommand("exist"), "Item '%s' does not exist?");
        add(jewelGiveFailureCommand("item"), "Item '%s' is not valid!");
        add(jewelGiveFailureCommand("rarity"), "Rarity '%s' is not valid!");

        add(jewelTestLootCommand("settings"), "Rarity Drop Setting for %s");
        add(jewelTestLootCommand("type.hostile"), "Hostile mobs");
        add(jewelTestLootCommand("type.boss"), "Boss mobs");
        add(jewelTestLootCommand("type.chest"), "Chests");
        add(jewelTestLootCommand("droprate"), "Default drop rate: %s");
        add(jewelTestLootCommand("dropratewith"), "Default drop rate with %s: %s");
        add(jewelTestLootCommand("onerarity"), "The only rarity that can drop is: %s");
        add(jewelTestLootCommand("config1"), "Config option %s is set to %s");
        add(jewelTestLootCommand("config2"), "Config option %s and %s are both set to %s");
        add(jewelTestLootCommand("failure"), "Loot Table '%s' is not valid for testing!");

        add(translatable(UNCOMMON_AMOUNT), "Uncommon effect amount");
        add(translatable(RARE_AMOUNT), "Rare effect amount");
        add(translatable(EPIC_AMOUNT), "Epic effect amount");
        add(translatable(LEGENDARY_AMOUNT), "Legendary effect amount");
        add(translatable(MAX_EFFECT_LEVEL), "Max stacked effect level");
        add(translatable(EPIC_LEGENDARY_ATTRIBUTES), "Epic and Legendary have attributes");
        add(translatable(LEGENDARY_EFFECTS), "Legendary have special effects");
        add(translatable(REGENERATION_COMBINABLE), "Regeneration is combinable");
        add(translatable(DURABILITY), "Jewels have durability");
        add(translatable(UNCOMMON_DURABILITY), "Uncommon durability amount");
        add(translatable(RARE_DURABILITY), "Rare durability amount");
        add(translatable(EPIC_DURABILITY), "Epic durability amount");
        add(translatable(RARITY_TOOLTIPS), "Rarity displayed in tooltip");
        add(translatable(RARITY_NAME), "Rarity displayed in name");
        add(translatable(EFFECT_ICON_DISABLED), "Effect icons are disabled");
        add(translatable(RARE_DROP), "Rare rarity drop rate");
        add(translatable(EPIC_DROP), "Epic rarity drop rate");
        add(translatable(LEGENDARY_DROP), "Legendary rarity drop rate");
        add(translatable(ONE_RARITY_DROP), "Only one rarity drops");
        add(translatable(RARITY_TO_DROP), "What rarity should drop");
        add(translatable(LEGENDARY_UPGRADE_ONLY), "Legendary rarity is upgrade only");
        add(translatable(UPGRADE_DISABLE), "Upgrading is disabled");
        add(translatable(DROP_IN_CHESTS), "Jewels in chests");

        add("itemGroup.magicaljewelryGroup", "Magical Jewelry");
    }

    private String rarity(JewelRarity rarity)
    {
        return "rarity." + MagicalJewelry.MOD_ID + "." + rarity.getName();
    }

    private String jewelGiveFailureCommand(String type)
    {
        return "commands." + MagicalJewelry.MOD_ID + ".jewelGive.failure." + type;
    }

    private String jewelTestLootCommand(String type)
    {
        return "commands." + MagicalJewelry.MOD_ID + ".jewelTestLoot." + type;
    }
}