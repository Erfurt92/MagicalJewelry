package com.erfurt.magicaljewelry.data.loot.lootmodifier;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.data.loot.loottable.ModEntityLootTablesBuilder;
import com.erfurt.magicaljewelry.loot.JewelModifier;
import com.erfurt.magicaljewelry.loot.conditions.BossEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.HostileEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.WaterEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.WaterHostileEntityCondition;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider
{
    public ModGlobalLootModifierProvider(PackOutput packOutput)
    {
        super(packOutput, MagicalJewelry.MOD_ID);
    }

    @Override
    protected void start()
    {
        add("hostile_jewel_modifier", new JewelModifier(
                new LootItemCondition[] {
                        new HostileEntityCondition()
                },
                MagicalJewelry.getId("entities/" + ModEntityLootTablesBuilder.hostileLootTable).toString()
        ));
        add("boss_jewel_modifier", new JewelModifier(
                new LootItemCondition[] {
                        new BossEntityCondition()
                },
                MagicalJewelry.getId("entities/" + ModEntityLootTablesBuilder.bossLootTable).toString()
        ));
       add("water_jewel_modifier", new JewelModifier(
               new LootItemCondition[] {
                       new WaterEntityCondition()
               },
               MagicalJewelry.getId("entities/" + ModEntityLootTablesBuilder.waterLootTable).toString()
       ));
       add("water_hostile_jewel_modifier", new JewelModifier(
               new LootItemCondition[] {
                       new WaterHostileEntityCondition()
               },
               MagicalJewelry.getId("entities/" + ModEntityLootTablesBuilder.waterHostileLootTable).toString()
       ));
    }
}