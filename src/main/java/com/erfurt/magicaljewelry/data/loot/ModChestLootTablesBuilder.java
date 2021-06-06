package com.erfurt.magicaljewelry.data.loot;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTFunction;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.RandomChance;

import java.util.function.BiConsumer;

public class ModChestLootTablesBuilder extends ChestLootTables implements IModLootTablesHelper
{
    public static float chestDropRate = 0.01F;

    public static String chestLootTable = "jewel_drops_chests";

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> builder)
    {
        builder.accept(MagicalJewelry.getId("inject/chests/" + chestLootTable), LootTable.builder()
                .addLootPool(jewelDefaultLootTable("chests", SetJewelNBTFunction.builder())
                        .acceptCondition(RandomChance.builder(chestDropRate))));
    }
}