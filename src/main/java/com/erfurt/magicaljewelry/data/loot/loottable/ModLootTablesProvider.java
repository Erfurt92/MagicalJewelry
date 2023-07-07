package com.erfurt.magicaljewelry.data.loot.loottable;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;

public class ModLootTablesProvider extends LootTableProvider
{
    private static final List<LootTableProvider.SubProviderEntry> subProviders = List.of(new LootTableProvider.SubProviderEntry(ModFishingLootTablesBuilder::new, LootContextParamSets.FISHING), new LootTableProvider.SubProviderEntry(ModChestLootTablesBuilder::new, LootContextParamSets.CHEST), new LootTableProvider.SubProviderEntry(ModEntityLootTablesBuilder::new, LootContextParamSets.ENTITY));

    public ModLootTablesProvider(PackOutput packOutput)
    {
        super(packOutput, BuiltInLootTables.all(), subProviders);
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker)
    {
        map.forEach((id, table) -> table.validate(validationtracker.setParams(table.getParamSet())));
    }
}