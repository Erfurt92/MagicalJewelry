package com.erfurt.magicaljewelry.util.handlers;

import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.objects.items.JewelItem;
import net.minecraftforge.client.event.ColorHandlerEvent;

public class ModColorHandler
{
	public static void registerItemColor(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register(JewelItem::getItemColor, ItemInit.GOLD_AMULET.get());
		event.getItemColors().register(JewelItem::getItemColor, ItemInit.SILVER_AMULET.get());
		
		event.getItemColors().register(JewelItem::getItemColor, ItemInit.GOLD_RING.get());
		event.getItemColors().register(JewelItem::getItemColor, ItemInit.SILVER_RING.get());
	}
}