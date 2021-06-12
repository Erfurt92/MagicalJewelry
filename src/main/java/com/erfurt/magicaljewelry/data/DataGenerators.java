package com.erfurt.magicaljewelry.data;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.data.client.ModItemModelProvider;
import com.erfurt.magicaljewelry.data.client.ModLangProvider;
import com.erfurt.magicaljewelry.data.loot.loottable.ModLootTablesProvider;
import com.erfurt.magicaljewelry.data.tags.ModItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = MagicalJewelry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if(event.includeClient())
        {
            gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));
            gen.addProvider(new ModLangProvider(gen));
        }
        if(event.includeServer())
        {
            gen.addProvider(new ModItemTagsProvider(gen));
            gen.addProvider(new ModLootTablesProvider(gen));
        }
    }
}