package com.erfurt.magicaljewelry.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
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

        getPools(table).forEach(
                pool -> {
                    System.out.println(pool.getName());
                    final List<ILootCondition> poolConditions = getLootConditions(pool);
                    poolConditions.forEach(condition -> {
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

        return chance[0];
    }

    public static float getLootingMultiplier(ResourceLocation resource, MinecraftServer server)
    {
        final float[] lootingMultiplier = {-0.1F};

        LootTableManager manager = server.getLootTableManager();
        LootTable table = manager.getLootTableFromLocation(resource);

        getPools(table).forEach(
                pool -> {
                    final List<ILootCondition> poolConditions = getLootConditions(pool);
                    poolConditions.forEach(condition -> {
                            if(condition instanceof RandomChanceWithLooting)
                            {
                                lootingMultiplier[0] = getLootingMultiplierValue((RandomChanceWithLooting) condition);
                            }
                        }
                    );
                }
        );

        return lootingMultiplier[0];
    }
}