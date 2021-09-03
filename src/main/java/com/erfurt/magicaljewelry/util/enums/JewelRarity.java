package com.erfurt.magicaljewelry.util.enums;

import com.erfurt.magicaljewelry.MagicalJewelry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public enum JewelRarity
{
	UNCOMMON("uncommon", ChatFormatting.DARK_GREEN),
	RARE("rare", ChatFormatting.DARK_AQUA),
	EPIC("epic", ChatFormatting.DARK_PURPLE),
	LEGENDARY("legendary", ChatFormatting.GOLD);

	private final String name;
	private final ChatFormatting format;
	private final Component displayName;
	
	JewelRarity(String nameIn, ChatFormatting formatIn)
	{
		this.name = nameIn;
		this.format = formatIn;
		this.displayName = new TranslatableComponent("rarity." + MagicalJewelry.MOD_ID + "." + nameIn);
	}
	
	public String getName()
	{
		return this.name;
	}

	public String getDisplayName()
	{
		return displayName.getString();
	}
	
	public ChatFormatting getFormat()
	{
		return this.format;
	}

	public static JewelRarity byName(String name)
	{
		for(JewelRarity rarity : values()) if(rarity.name.equals(name)) return rarity;

		return null;
	}

	public static boolean containsRarity(String name)
	{
		for(JewelRarity rarity : values()) if(rarity.name.equals(name)) return true;

		return false;
	}
}