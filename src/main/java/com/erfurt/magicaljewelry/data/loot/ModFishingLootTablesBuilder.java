package com.erfurt.magicaljewelry.data.loot;

import com.erfurt.magicaljewelry.MagicalJewelry;
import net.minecraft.data.loot.FishingLootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.RandomChance;

import java.util.function.BiConsumer;

public class ModFishingLootTablesBuilder extends FishingLootTables implements IModLootTablesHelper
{
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> builder)
    {
        builder.accept(MagicalJewelry.getId("inject/gameplay/fishing/jewel_drops_fishing"), LootTable.builder()
                .addLootPool(jewelWaterLootTable("fishing_treasure")
                        .acceptCondition(RandomChance.builder(0.001F))));
    }
}