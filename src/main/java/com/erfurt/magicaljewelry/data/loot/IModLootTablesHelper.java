package com.erfurt.magicaljewelry.data.loot;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.init.ItemInit;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.functions.ILootFunction;

public interface IModLootTablesHelper
{
    default LootPool.Builder jewelHostileLootTable(String type, ILootFunction.IBuilder function)
    {
        return LootPool.builder()
                .name(MagicalJewelry.MOD_ID + "_jewel_" + type)
                .rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ItemInit.GOLD_AMULET.get())
                        .weight(1)
                        .acceptFunction(function))
                .addEntry(ItemLootEntry.builder(ItemInit.SILVER_AMULET.get())
                        .weight(1)
                        .acceptFunction(function))
                .addEntry(ItemLootEntry.builder(ItemInit.GOLD_RING.get())
                        .weight(2)
                        .acceptFunction(function))
                .addEntry(ItemLootEntry.builder(ItemInit.SILVER_RING.get())
                        .weight(2)
                        .acceptFunction(function))
                .addEntry(ItemLootEntry.builder(ItemInit.GOLD_BRACELET.get())
                        .weight(2)
                        .acceptFunction(function))
                .addEntry(ItemLootEntry.builder(ItemInit.SILVER_BRACELET.get())
                        .weight(2)
                        .acceptFunction(function));
    }

    default LootPool.Builder jewelWaterLootTable(String type)
    {
        return LootPool.builder()
                .name(MagicalJewelry.MOD_ID + "_jewels_" + type)
                .rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ItemInit.THE_ONE_RING.get())
                        .weight(1));
    }
}