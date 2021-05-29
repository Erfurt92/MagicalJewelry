package com.erfurt.magicaljewelry.data.loot;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTBossFunction;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTFunction;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
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

    float waterDropRate = 0.001F;
    float waterLootingMultiplier = 0.001F;

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