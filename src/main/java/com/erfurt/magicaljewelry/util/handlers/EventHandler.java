package com.erfurt.magicaljewelry.util.handlers;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.data.loot.loottable.ModChestLootTablesBuilder;
import com.erfurt.magicaljewelry.data.loot.loottable.ModFishingLootTablesBuilder;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
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
		if(MagicalJewelryConfigBuilder.JEWELS_IN_CHESTS.get()) if(resourceName.getPath().startsWith("chests/")) event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(MagicalJewelry.getId("inject/chests/" + ModChestLootTablesBuilder.chestLootTable))).name("magicaljewelry_jewels_chests").build());
		if(name.equals("minecraft:gameplay/fishing/treasure")) event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(MagicalJewelry.getId("inject/gameplay/fishing/" + ModFishingLootTablesBuilder.fishingLootTable))).name("magicaljewelry_jewels_fishing_treasure").build());
	}
}