package com.erfurt.magicaljewelry.util.handlers;

import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.objects.items.JewelItem;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;

@OnlyIn(Dist.CLIENT)
public class ModColorHandler
{
	public static void registerItemColor(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register(JewelItem::getItemColor, ItemInit.GOLD_AMULET.get().getItem());
		event.getItemColors().register(JewelItem::getItemColor, ItemInit.SILVER_AMULET.get().getItem());
		
		event.getItemColors().register(JewelItem::getItemColor, ItemInit.GOLD_RING.get().getItem());
		event.getItemColors().register(JewelItem::getItemColor, ItemInit.SILVER_RING.get().getItem());
	}
}