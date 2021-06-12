package com.erfurt.magicaljewelry.data.loot.lootmodifier;

import com.erfurt.magicaljewelry.MagicalJewelry;
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
        add("jewel_modifier_hostile", LootInit.JEWEL_MODIFIER.get(), new JewelModifier(
                new ILootCondition[] {
                        new HostileEntityCondition()
                },
                MagicalJewelry.getId("entities/jewel_drops_hostile")
        ));
        add("jewel_modifier_boss", LootInit.JEWEL_MODIFIER.get(), new JewelModifier(
                new ILootCondition[] {
                        new BossEntityCondition()
                },
                MagicalJewelry.getId("entities/jewel_drops_boss")
        ));
       add("jewel_modifier_water", LootInit.JEWEL_MODIFIER.get(), new JewelModifier(
               new ILootCondition[] {
                       new WaterEntityCondition()
               },
               MagicalJewelry.getId("entities/jewel_drops_water")
       ));
       add("jewel_modifier_water_hostile", LootInit.JEWEL_MODIFIER.get(), new JewelModifier(
               new ILootCondition[] {
                       new WaterHostileEntityCondition()
               },
               MagicalJewelry.getId("entities/jewel_drops_water_hostile")
       ));
    }
}