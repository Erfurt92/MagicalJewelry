package com.erfurt.magicaljewelry.init;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.objects.items.JewelAmuletItem;
import com.erfurt.magicaljewelry.objects.items.JewelBraceletItem;
import com.erfurt.magicaljewelry.objects.items.JewelRingItem;
import com.erfurt.magicaljewelry.objects.items.TheOneRingItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public class ItemInit
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MagicalJewelry.MOD_ID);

	public static final RegistryObject<Item> GOLD_AMULET = ITEMS.register("gold_amulet", JewelAmuletItem::new);
	public static final RegistryObject<Item> SILVER_AMULET = ITEMS.register("silver_amulet", JewelAmuletItem::new);
	public static final RegistryObject<Item> GOLD_RING = ITEMS.register("gold_ring", JewelRingItem::new);
	public static final RegistryObject<Item> SILVER_RING = ITEMS.register("silver_ring", JewelRingItem::new);
	public static final RegistryObject<Item> GOLD_BRACELET = ITEMS.register("gold_bracelet", JewelBraceletItem::new);
	public static final RegistryObject<Item> SILVER_BRACELET = ITEMS.register("silver_bracelet", JewelBraceletItem::new);

	public static final RegistryObject<Item> THE_ONE_RING = ITEMS.register("the_one_ring", TheOneRingItem::new);
}