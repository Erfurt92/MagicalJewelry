package com.erfurt.magicaljewelry.data.client;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static com.erfurt.magicaljewelry.init.ItemInit.*;
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

        add(rarity(UNCOMMON), "Uncommon");;
        add(rarity(RARE), "Rare");
        add(rarity(EPIC), "Epic");
        add(rarity(LEGENDARY), "Legendary");

        add(jewelGiveFailureCommand("exist"), "Item '%s' does not exist?");
        add(jewelGiveFailureCommand("item"), "Item '%s' is not valid!");
        add(jewelGiveFailureCommand("rarity"), "Rarity '%s' is not valid!");

        add(jewelTestLootCommand("hostile"), "Rarity Drop Setting for Hostiles mobs");
        add(jewelTestLootCommand("boss"), "Rarity Drop Setting for Boss mobs");
        add(jewelTestLootCommand("chest"), "Rarity Drop Setting for Chests");
        add(jewelTestLootCommand("default"), "Default drop rate: %s");
        add(jewelTestLootCommand("looting"), "Default drop rate with Looting III: %s");
        add(jewelTestLootCommand("onerarity"), "The only rarity that can drop is: %s");
        add(jewelTestLootCommand("config1"), "Config option %s is set to %s");
        add(jewelTestLootCommand("config2"), "Config option %s and %s are both set to %s");
        add(jewelTestLootCommand("failure"), "Loot Table '%s' is not valid for testing!");

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