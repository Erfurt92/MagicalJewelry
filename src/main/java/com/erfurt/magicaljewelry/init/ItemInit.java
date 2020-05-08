package com.erfurt.magicaljewelry.init;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.objects.items.JewelItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit
{
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MagicalJewelry.MOD_ID);

	public static final RegistryObject<Item> GOLD_AMULET = ITEMS.register("gold_amulet", JewelItem::new);
	public static final RegistryObject<Item> SILVER_AMULET = ITEMS.register("silver_amulet", JewelItem::new);
	public static final RegistryObject<Item> GOLD_RING = ITEMS.register("gold_ring", JewelItem::new);
	public static final RegistryObject<Item> SILVER_RING = ITEMS.register("silver_ring", JewelItem::new);
}