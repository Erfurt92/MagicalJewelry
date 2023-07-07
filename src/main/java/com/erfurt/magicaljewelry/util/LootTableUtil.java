package com.erfurt.magicaljewelry.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.List;

public class LootTableUtil
{
    private static List<LootPool> getPools(LootTable table)
    {
        // public net.minecraft.world.level.storage.loot.LootTable f_79109_ # pools
        return ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "f_79109_");
    }

    private static LootItemCondition[] getLootConditions(LootPool pool)
    {
        // public net.minecraft.world.level.storage.loot.LootPool f_79024_ # conditions
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "f_79024_");
    }

    private static float getLootItemRandomChanceConditionValue(LootItemRandomChanceCondition condition)
    {
        // public net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition f_81921_ # probability
        return ObfuscationReflectionHelper.getPrivateValue(LootItemRandomChanceCondition.class, condition, "f_81921_");
    }

    private static float getLootItemRandomChanceWithLootingConditionValue(LootItemRandomChanceWithLootingCondition condition)
    {
        // public net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition f_81953_ # percent
        return ObfuscationReflectionHelper.getPrivateValue(LootItemRandomChanceWithLootingCondition.class, condition, "f_81953_");
    }

    private static float getLootingMultiplierValue(LootItemRandomChanceWithLootingCondition condition)
    {
        // public net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition f_81954_ # lootingMultiplier
        return ObfuscationReflectionHelper.getPrivateValue(LootItemRandomChanceWithLootingCondition.class, condition, "f_81954_");
    }

    public static float getChance(ResourceLocation resource, MinecraftServer server)
    {
        final float[] chance = {-0.1F};

        LootDataManager manager = server.getLootData();
        LootTable table = manager.getLootTable(resource);

        for(LootPool pool : getPools(table))
        {
            for(LootItemCondition condition : getLootConditions(pool))
            {
                if(condition instanceof LootItemRandomChanceCondition)
                {
                    chance[0] = getLootItemRandomChanceConditionValue((LootItemRandomChanceCondition) condition);
                }
                else if(condition instanceof LootItemRandomChanceWithLootingCondition)
                {
                    chance[0] = getLootItemRandomChanceWithLootingConditionValue((LootItemRandomChanceWithLootingCondition) condition);
                }
            }
        }

        return chance[0];
    }

    public static float getLootingMultiplier(ResourceLocation resource, MinecraftServer server)
    {
        final float[] lootingMultiplier = {-0.1F};

        LootDataManager manager = server.getLootData();
        LootTable table = manager.getLootTable(resource);

        for(LootPool pool : getPools(table))
        {
            for(LootItemCondition condition : getLootConditions(pool))
            {
                if(condition instanceof LootItemRandomChanceWithLootingCondition)
                {
                    lootingMultiplier[0] = getLootingMultiplierValue((LootItemRandomChanceWithLootingCondition) condition);
                }
            }
        }

        return lootingMultiplier[0];
    }
}