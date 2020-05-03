package com.erfurt.magicaljewelry.init;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.objects.items.JewelItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit
{
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MagicalJewelry.MOD_ID);
	
	public static final RegistryObject<Item> GOLD_AMULET = ITEMS.register("gold_amulet", () -> new JewelItem(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS).defaultMaxDamage(0)));
	public static final RegistryObject<Item> SILVER_AMULET = ITEMS.register("silver_amulet", () -> new JewelItem(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS).defaultMaxDamage(0)));
	public static final RegistryObject<Item> GOLD_RING = ITEMS.register("gold_ring", () -> new JewelItem(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS).defaultMaxDamage(0)));
	public static final RegistryObject<Item> SILVER_RING = ITEMS.register("silver_ring", () -> new JewelItem(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS).defaultMaxDamage(0)));
}