package com.erfurt.magicaljewelry.data.loot;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTBossFunction;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTFunction;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.ILootFunction;

import java.util.function.BiConsumer;

public class ModEntityLootTablesBuilder extends EntityLootTables implements IModLootTablesHelper
{
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> builder)
    {
        builder.accept(MagicalJewelry.getId("entities/jewel_drops_hostile"),  LootTable.builder().addLootPool(jewelHostileLootTable("hostile", SetJewelNBTFunction.builder())
                .acceptCondition(KilledByPlayer.builder())
                .acceptCondition(RandomChanceWithLooting.builder(hostileDropRate, lootingMultiplier))));
        builder.accept(MagicalJewelry.getId("entities/jewel_drops_boss"), LootTable.builder().addLootPool(jewelHostileLootTable("boss", SetJewelNBTBossFunction.builder())
                .acceptCondition(KilledByPlayer.builder())
                .acceptCondition(RandomChanceWithLooting.builder(bossDropRate, lootingMultiplier))));
        builder.accept(MagicalJewelry.getId("entities/jewel_drops_water"), LootTable.builder().addLootPool(jewelWaterLootTable("water")
                .acceptCondition(KilledByPlayer.builder())
                .acceptCondition(RandomChanceWithLooting.builder(waterDropRate, waterLootingMultiplier))));
        builder.accept(MagicalJewelry.getId("entities/jewel_drops_water_hostile"), jewelWaterHostileLootTable("water_hostile", SetJewelNBTFunction.builder(), waterDropRate,hostileDropRate));
    }

    private LootTable.Builder jewelWaterHostileLootTable(String type, ILootFunction.IBuilder function, float chanceWater, float chanceHostile)
    {
        return LootTable.builder()
                .addLootPool(jewelWaterLootTable(type + "1")
                        .acceptCondition(KilledByPlayer.builder())
                        .acceptCondition(RandomChanceWithLooting.builder(chanceWater, waterLootingMultiplier)))
                .addLootPool(jewelHostileLootTable(type + "2", function)
                        .acceptCondition(KilledByPlayer.builder())
                        .acceptCondition(RandomChanceWithLooting.builder(chanceHostile, lootingMultiplier)));
    }
}