package com.erfurt.magicaljewelry.data.recipes;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static com.erfurt.magicaljewelry.init.ItemInit.*;
import static com.erfurt.magicaljewelry.util.enums.JewelRarity.*;

public class ModRecipeProvider extends RecipeProvider
{
    public ModRecipeProvider(PackOutput packOutput)
    {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer)
    {
        upgradeRecipe(consumer, GOLD_AMULET.get());
        upgradeRecipe(consumer, SILVER_AMULET.get());
        upgradeRecipe(consumer, GOLD_RING.get());
        upgradeRecipe(consumer, SILVER_RING.get());
        upgradeRecipe(consumer, GOLD_BRACELET.get());
        upgradeRecipe(consumer, SILVER_BRACELET.get());
        copySmithingTemplate(consumer, JEWEL_UPGRADE_SMITHING_TEMPLATE.get(), Items.NETHERRACK);
    }

    private void upgradeRecipe(Consumer<FinishedRecipe> consumer, Item upgradeableItem)
    {
        JewelUpgradeRecipeBuilder.jewelUpgradeRecipe(JEWEL_UPGRADE_SMITHING_TEMPLATE.get(), upgradeableItem, Items.DIAMOND_BLOCK, UNCOMMON.getName(), RARE.getName()).build(consumer, upgradeableItem.toString());
        JewelUpgradeRecipeBuilder.jewelUpgradeRecipe(JEWEL_UPGRADE_SMITHING_TEMPLATE.get(), upgradeableItem, Items.NETHERITE_INGOT, RARE.getName(), EPIC.getName()).build(consumer, upgradeableItem.toString());
        JewelUpgradeRecipeBuilder.jewelUpgradeRecipe(JEWEL_UPGRADE_SMITHING_TEMPLATE.get(), upgradeableItem, Items.NETHERITE_BLOCK, EPIC.getName(), LEGENDARY.getName()).build(consumer, upgradeableItem.toString());
    }
}