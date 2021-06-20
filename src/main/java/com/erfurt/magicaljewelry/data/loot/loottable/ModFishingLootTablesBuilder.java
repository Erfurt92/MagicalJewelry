package com.erfurt.magicaljewelry.data.loot.loottable;

import com.erfurt.magicaljewelry.MagicalJewelry;
import net.minecraft.data.loot.FishingLootTables;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

public class ModFishingLootTablesBuilder extends FishingLootTables implements IModLootTablesHelper
{
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> builder)
    {
        builder.accept(MagicalJewelry.getId("inject/gameplay/fishing/fishing_jewel_drops"), LootTable.builder()
                .addLootPool(jewelWaterLootTable("fishing_treasure")
                        .acceptCondition(RandomChance.builder(0.001F))));
    }
}