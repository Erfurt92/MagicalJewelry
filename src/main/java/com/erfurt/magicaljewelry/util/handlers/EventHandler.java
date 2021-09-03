package com.erfurt.magicaljewelry.util.handlers;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.data.loot.loottable.ModChestLootTablesBuilder;
import com.erfurt.magicaljewelry.data.loot.loottable.ModFishingLootTablesBuilder;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = MagicalJewelry.MOD_ID)
public final class EventHandler
{
	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event)
	{
		ResourceLocation resourceName = event.getName();
		String name = event.getName().toString();
		if(MagicalJewelryConfigBuilder.JEWELS_IN_CHESTS.get()) if(resourceName.getPath().startsWith("chests/")) event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(MagicalJewelry.getId("inject/chests/" + ModChestLootTablesBuilder.chestLootTable))).name("magicaljewelry_jewels_chests").build());
		if(name.equals("minecraft:gameplay/fishing/treasure")) event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(MagicalJewelry.getId("inject/gameplay/fishing/" + ModFishingLootTablesBuilder.fishingLootTable))).name("magicaljewelry_jewels_fishing_treasure").build());
	}
}