package com.erfurt.magicaljewelry.data.loot;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTFunction;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

public class ModChestLootTablesBuilder extends ChestLootTables implements IModLootTablesHelper
{
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> builder)
    {
        builder.accept(MagicalJewelry.getId("inject/chests/jewel_drops_chests"), LootTable.builder()
                .addLootPool(jewelHostileLootTable("chests", SetJewelNBTFunction.builder())
                        .acceptCondition(RandomChance.builder(0.01F))));
    }
}