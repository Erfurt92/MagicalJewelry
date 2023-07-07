package com.erfurt.magicaljewelry.data.loot.loottable;

import com.erfurt.magicaljewelry.MagicalJewelry;
import net.minecraft.data.loot.packs.VanillaFishingLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.function.BiConsumer;

public class ModFishingLootTablesBuilder extends VanillaFishingLoot implements IModLootTablesHelper
{

    public static String fishingLootTable = "fishing_jewel_drops";
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> builder)
    {
        builder.accept(MagicalJewelry.getId("inject/gameplay/fishing/" + fishingLootTable), LootTable.lootTable()
                .withPool(jewelWaterLootTable("fishing_treasure")
                        .when(LootItemRandomChanceCondition.randomChance(0.001F))));
    }
}