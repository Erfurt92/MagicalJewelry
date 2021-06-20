package com.erfurt.magicaljewelry.data.loot.loottable;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTBossFunction;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTFunction;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

public class ModEntityLootTablesBuilder extends EntityLootTables implements IModLootTablesHelper
{
    public static float hostileDropRate = 0.01F;
    public static float bossDropRate = 0.25F;
    public static float lootingMultiplier = 0.01F;

    public static String hostileLootTable = "hostile_jewel_drops";
    public static String bossLootTable = "boss_jewel_drops";
    public static String waterLootTable = "water_jewel_drops";
    public static String waterHostileLootTable = "water_hostile_jewel_drops";

    float waterDropRate = 0.001F;
    float waterLootingMultiplier = 0.001F;

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> builder)
    {
        builder.accept(MagicalJewelry.getId("entities/" + hostileLootTable),  LootTable.builder().addLootPool(jewelDefaultLootTable("hostile", SetJewelNBTFunction.builder())
                .acceptCondition(KilledByPlayer.builder())
                .acceptCondition(RandomChanceWithLooting.builder(hostileDropRate, lootingMultiplier))));
        builder.accept(MagicalJewelry.getId("entities/" + bossLootTable), LootTable.builder().addLootPool(jewelDefaultLootTable("boss", SetJewelNBTBossFunction.builder())
                .acceptCondition(KilledByPlayer.builder())
                .acceptCondition(RandomChanceWithLooting.builder(bossDropRate, lootingMultiplier))));
        builder.accept(MagicalJewelry.getId("entities/" + waterLootTable), LootTable.builder().addLootPool(jewelWaterLootTable("water")
                .acceptCondition(KilledByPlayer.builder())
                .acceptCondition(RandomChanceWithLooting.builder(waterDropRate, waterLootingMultiplier))));
        builder.accept(MagicalJewelry.getId("entities/" + waterHostileLootTable), jewelWaterHostileLootTable(SetJewelNBTFunction.builder(), waterDropRate,hostileDropRate));
    }

    private LootTable.Builder jewelWaterHostileLootTable(ILootFunction.IBuilder function, float chanceWater, float chanceHostile)
    {
        return LootTable.builder()
                .addLootPool(jewelWaterLootTable("water_hostile1")
                        .acceptCondition(KilledByPlayer.builder())
                        .acceptCondition(RandomChanceWithLooting.builder(chanceWater, waterLootingMultiplier)))
                .addLootPool(jewelDefaultLootTable("water_hostile2", function)
                        .acceptCondition(KilledByPlayer.builder())
                        .acceptCondition(RandomChanceWithLooting.builder(chanceHostile, lootingMultiplier)));
    }
}