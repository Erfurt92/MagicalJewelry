package com.erfurt.magicaljewelry.util.enums;

import net.minecraft.util.text.TextFormatting;

public enum JewelRarity
{
	UNCOMMON(0, "uncommon", TextFormatting.DARK_GREEN),
	RARE(1, "rare", TextFormatting.DARK_AQUA),
	EPIC(2, "epic", TextFormatting.DARK_PURPLE),
	LEGENDARY(3, "legendary", TextFormatting.GOLD);
	
	private static final JewelRarity[] INDEX_LOOKUP = new JewelRarity[values().length];

	private final int index;
	private final String name;
	private final TextFormatting format;
	
	private JewelRarity(int indexIn, String nameIn, TextFormatting formatIn)
	{
		this.index = indexIn;
		this.name = nameIn;
		this.format = formatIn;
	}
	
	public int getIndex()
	{
		return this.index;
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
	
	public static JewelRarity byIndex(int index)
	{
		if(index < 0 || index >= INDEX_LOOKUP.length)
		{
			index = -1;
		}
		
		return INDEX_LOOKUP[index];
	}
	static
	{
		for(JewelRarity types : values())
		{
			INDEX_LOOKUP[types.getIndex()] = types;
		}
	}
}