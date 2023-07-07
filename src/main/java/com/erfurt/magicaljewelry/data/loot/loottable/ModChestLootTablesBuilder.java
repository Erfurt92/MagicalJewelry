package com.erfurt.magicaljewelry.data.loot.loottable;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTFunction;
import net.minecraft.data.loot.packs.VanillaChestLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.function.BiConsumer;

public class ModChestLootTablesBuilder extends VanillaChestLoot implements IModLootTablesHelper
{
    public static float chestDropRate = 0.01F;

    public static String chestLootTable = "chests_jewel_drops";

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> builder)
    {
        builder.accept(MagicalJewelry.getId("inject/chests/" + chestLootTable), LootTable.lootTable()
                .withPool(jewelDefaultLootTable("chests", SetJewelNBTFunction.builder())
                        .when(LootItemRandomChanceCondition.randomChance(chestDropRate))));
    }
}