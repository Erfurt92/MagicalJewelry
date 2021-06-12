package com.erfurt.magicaljewelry.data.loot.loottable;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;

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
        map.forEach((id, table) -> LootTableManager.func_227508_a_(validationtracker, id, table));
    }
}