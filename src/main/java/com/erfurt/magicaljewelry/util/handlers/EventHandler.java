package com.erfurt.magicaljewelry.util.handlers;

import com.erfurt.magicaljewelry.MagicalJewelry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = MagicalJewelry.MOD_ID)
public final class EventHandler
{
	private EventHandler() {}

	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event)
	{
		if(event.getName().getPath().contains("zombie") && !event.getName().getPath().contains("horse"))
		{
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(MagicalJewelry.MOD_ID, "entities/jewel_drops"))).build());
		}
		else if(event.getName().getPath().contains("skeleton") && !event.getName().getPath().contains("horse"))
		{
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(MagicalJewelry.MOD_ID, "entities/jewel_drops"))).build());
		}
	}
}