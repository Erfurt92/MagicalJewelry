package com.erfurt.magicaljewelry.data.loot.lootmodifier;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.data.loot.loottable.ModEntityLootTablesBuilder;
import com.erfurt.magicaljewelry.init.LootInit;
import com.erfurt.magicaljewelry.loot.JewelModifier;
import com.erfurt.magicaljewelry.loot.conditions.BossEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.HostileEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.WaterEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.WaterHostileEntityCondition;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider
{
    public ModGlobalLootModifierProvider(DataGenerator generator)
    {
        super(generator, MagicalJewelry.MOD_ID);
    }

    @Override
    protected void start()
    {
        add("hostile_jewel_modifier", LootInit.JEWEL_MODIFIER.get(), new JewelModifier(
                new ILootCondition[] {
                        new HostileEntityCondition()
                },
                MagicalJewelry.getId("entities/" + ModEntityLootTablesBuilder.hostileLootTable)
        ));
        add("boss_jewel_modifier", LootInit.JEWEL_MODIFIER.get(), new JewelModifier(
                new ILootCondition[] {
                        new BossEntityCondition()
                },
                MagicalJewelry.getId("entities/" + ModEntityLootTablesBuilder.bossLootTable)
        ));
       add("water_jewel_modifier", LootInit.JEWEL_MODIFIER.get(), new JewelModifier(
               new ILootCondition[] {
                       new WaterEntityCondition()
               },
               MagicalJewelry.getId("entities/" + ModEntityLootTablesBuilder.waterLootTable)
       ));
       add("water_hostile_jewel_modifier", LootInit.JEWEL_MODIFIER.get(), new JewelModifier(
               new ILootCondition[] {
                       new WaterHostileEntityCondition()
               },
               MagicalJewelry.getId("entities/" + ModEntityLootTablesBuilder.waterHostileLootTable)
       ));
    }
}