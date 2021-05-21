package com.erfurt.magicaljewelry.data.loot;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTablesProvider extends LootTableProvider
{
    public ModLootTablesProvider(DataGenerator dataGeneratorIn)
    {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
    {
        return ImmutableList.of(
                Pair.of(ModFishingLootTablesBuilder::new, LootParameterSets.FISHING),
                Pair.of(ModChestLootTablesBuilder::new, LootParameterSets.CHEST),
                Pair.of(ModEntityLootTablesBuilder::new, LootParameterSets.ENTITY)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker)
    {
        map.forEach((id, table) -> LootTableManager.validateLootTable(validationtracker, id, table));
    }
}