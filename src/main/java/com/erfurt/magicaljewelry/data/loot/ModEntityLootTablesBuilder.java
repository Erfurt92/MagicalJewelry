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
    public static float hostileDropRate = 0.01F;
    public static float bossDropRate = 0.25F;
    public static float lootingMultiplier = 0.01F;

    public static String hostileLootTable = "jewel_drops_hostile";
    public static String bossLootTable = "jewel_drops_boss";
    public static String waterLootTable = "jewel_drops_water";
    public static String waterHostileLootTable = "jewel_drops_water_hostile";

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