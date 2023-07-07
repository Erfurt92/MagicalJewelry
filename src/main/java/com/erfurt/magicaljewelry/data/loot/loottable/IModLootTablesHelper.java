package com.erfurt.magicaljewelry.data.loot.loottable;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.init.ItemInit;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public interface IModLootTablesHelper
{
    String poolName = MagicalJewelry.MOD_ID + "_jewel_";
    default LootPool.Builder jewelDefaultLootTable(String type, LootItemFunction.Builder function)
    {
        return LootPool.lootPool()
                .name(poolName + type)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(ItemInit.GOLD_AMULET.get())
                        .setWeight(1))
                .add(LootItem.lootTableItem(ItemInit.SILVER_AMULET.get())
                        .setWeight(1))
                .add(LootItem.lootTableItem(ItemInit.GOLD_RING.get())
                        .setWeight(2))
                .add(LootItem.lootTableItem(ItemInit.SILVER_RING.get())
                        .setWeight(2))
                .add(LootItem.lootTableItem(ItemInit.GOLD_BRACELET.get())
                        .setWeight(2))
                .add(LootItem.lootTableItem(ItemInit.SILVER_BRACELET.get())
                        .setWeight(2))
                .apply(function);
    }

    default LootPool.Builder jewelChestLootTable(String type)
    {
        return LootPool.lootPool()
                .name(poolName + type)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(ItemInit.JEWEL_UPGRADE_SMITHING_TEMPLATE.get())
                        .setWeight(1));
    }

    default LootPool.Builder jewelWaterLootTable(String type)
    {
        return LootPool.lootPool()
                .name(poolName + type)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(ItemInit.THE_ONE_RING.get())
                        .setWeight(1));
    }
}