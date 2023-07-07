package com.erfurt.magicaljewelry.data;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.data.client.ModItemModelProvider;
import com.erfurt.magicaljewelry.data.client.ModLangProvider;
import com.erfurt.magicaljewelry.data.loot.lootmodifier.ModGlobalLootModifierProvider;
import com.erfurt.magicaljewelry.data.loot.loottable.ModLootTablesProvider;
import com.erfurt.magicaljewelry.data.recipes.ModRecipeProvider;
import com.erfurt.magicaljewelry.data.tags.ModBlockTagsProvider;
import com.erfurt.magicaljewelry.data.tags.ModItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = MagicalJewelry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        gen.addProvider(event.includeClient(), new ModLangProvider(packOutput));

        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        gen.addProvider(event.includeServer(), blockTags);
        gen.addProvider(event.includeServer(), new ModItemTagsProvider(packOutput, lookupProvider, existingFileHelper, blockTags));
        gen.addProvider(event.includeServer(), new ModLootTablesProvider(packOutput));
        gen.addProvider(event.includeServer(), new ModGlobalLootModifierProvider(packOutput));
        gen.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
    }
}