package com.erfurt.magicaljewelry.data.loot.loottable;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTBossFunction;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTFunction;
import net.minecraft.data.loot.packs.VanillaEntityLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;

import java.util.function.BiConsumer;

public class ModEntityLootTablesBuilder extends VanillaEntityLoot implements IModLootTablesHelper
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
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> builder)
    {
        builder.accept(MagicalJewelry.getId("entities/" + hostileLootTable), LootTable.lootTable().withPool(jewelDefaultLootTable("hostile", SetJewelNBTFunction.builder())
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(hostileDropRate, lootingMultiplier))));
        builder.accept(MagicalJewelry.getId("entities/" + bossLootTable), LootTable.lootTable().withPool(jewelDefaultLootTable("boss", SetJewelNBTBossFunction.builder())
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(bossDropRate, lootingMultiplier))));
        builder.accept(MagicalJewelry.getId("entities/" + waterLootTable), LootTable.lootTable().withPool(jewelWaterLootTable("water")
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(waterDropRate, waterLootingMultiplier))));
        builder.accept(MagicalJewelry.getId("entities/" + waterHostileLootTable), jewelWaterHostileLootTable(SetJewelNBTFunction.builder(), waterDropRate, hostileDropRate));
    }

    private LootTable.Builder jewelWaterHostileLootTable(LootItemFunction.Builder function, float chanceWater, float chanceHostile)
    {
        return LootTable.lootTable()
                .withPool(jewelWaterLootTable("water_hostile1")
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(chanceWater, waterLootingMultiplier)))
                .withPool(jewelDefaultLootTable("water_hostile2", function)
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(chanceHostile, lootingMultiplier)));
    }
}