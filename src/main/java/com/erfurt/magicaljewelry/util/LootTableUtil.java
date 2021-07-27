package com.erfurt.magicaljewelry.util;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;

public class LootTableUtil
{
    private static List<LootPool> getPools(LootTable table)
    {
        // public net.minecraft.loot.LootTable field_186466_c # pools
        return ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "field_186466_c");
    }

    private static List<ILootCondition> getLootConditions(LootPool pool)
    {
        // public net.minecraft.loot.LootPool field_186454_b # conditions
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186454_b");
    }

    private static float getRandomChanceValue(RandomChance condition)
    {
        // public net.minecraft.loot.conditions.RandomChance field_186630_a # chance
        return ObfuscationReflectionHelper.getPrivateValue(RandomChance.class, condition, "field_186630_a");
    }

    private static float getRandomChanceWithLootingValue(RandomChanceWithLooting condition)
    {
        // public net.minecraft.loot.conditions.RandomChanceWithLooting field_186627_a # chance
        return ObfuscationReflectionHelper.getPrivateValue(RandomChanceWithLooting.class, condition, "field_186627_a");
    }

    private static float getLootingMultiplierValue(RandomChanceWithLooting condition)
    {
        // public net.minecraft.loot.conditions.RandomChanceWithLooting field_186628_b # lootingMultiplier
        return ObfuscationReflectionHelper.getPrivateValue(RandomChanceWithLooting.class, condition, "field_186628_b");
    }

    public static float getChance(ResourceLocation resource, MinecraftServer server)
    {
        final float[] chance = {-0.1F};

        LootTableManager manager = server.getLootTableManager();
        LootTable table = manager.getLootTableFromLocation(resource);
        System.out.println(table.getLootTableId());

        getPools(table).forEach(
                pool -> {
                    System.out.println(pool.getName());
                    final List<ILootCondition> poolConditions = getLootConditions(pool);
                    poolConditions.forEach(condition -> {
                        System.out.println(condition);
                            if(condition instanceof RandomChance)
                            {
                                chance[0] = getRandomChanceValue((RandomChance) condition);
                            }
                            else if(condition instanceof RandomChanceWithLooting)
                            {
                                chance[0] = getRandomChanceWithLootingValue((RandomChanceWithLooting) condition);
                            }
                        }
                    );
                }
        );

        System.out.println("Value is: " + chance[0]);
        return chance[0];
    }

    public static float getLootingMultiplier(ResourceLocation resource, MinecraftServer server)
    {
        final float[] lootingMultiplier = {-0.1F};

        LootTableManager manager = server.getLootTableManager();
        LootTable table = manager.getLootTableFromLocation(resource);
        System.out.println(table.getLootTableId());

        getPools(table).forEach(
                pool -> {
                    System.out.println(pool.getName());
                    final List<ILootCondition> poolConditions = getLootConditions(pool);
                    poolConditions.forEach(condition -> {
                        System.out.println(condition);
                            if(condition instanceof RandomChanceWithLooting)
                            {
                                lootingMultiplier[0] = getLootingMultiplierValue((RandomChanceWithLooting) condition);
                            }
                        }
                    );
                }
        );

        System.out.println("Value is: " + lootingMultiplier[0]);
        return lootingMultiplier[0];
    }
}