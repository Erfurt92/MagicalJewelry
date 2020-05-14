package com.erfurt.magicaljewelry.util.enums;

import net.minecraft.util.text.TextFormatting;

public enum JewelRarity
{
	UNCOMMON("uncommon", TextFormatting.DARK_GREEN),
	RARE("rare", TextFormatting.DARK_AQUA),
	EPIC("epic", TextFormatting.DARK_PURPLE),
	LEGENDARY("legendary", TextFormatting.GOLD);

	private final String name;
	private final TextFormatting format;
	
	JewelRarity(String nameIn, TextFormatting formatIn)
	{
		this.name = nameIn;
		this.format = formatIn;
	}
	
	public String getName()
	{
		return this.name;
	}

	public String getDisplayName()
	{
		String displayName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
		return displayName;
	}
	
	public TextFormatting getFormat()
	{
		return this.format;
	}

	public static JewelRarity byName(String name)
	{
		for(JewelRarity rarity : values()) if(rarity.name.equals(name)) return rarity;

		return null;
	}
}