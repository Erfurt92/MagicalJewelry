package com.erfurt.magicaljewelry.util.enums;

import com.erfurt.magicaljewelry.util.interfaces.IJewelRarity;

import net.minecraft.util.text.TextFormatting;

public enum JewelRarity implements IJewelRarity
{
	UNCOMMEN(0, "Uncommon", TextFormatting.DARK_GREEN),
	RARE(1, "Rare", TextFormatting.DARK_AQUA),
	EPIC(2, "Epic", TextFormatting.DARK_PURPLE),
	LEGENDARY(3, "Legendary", TextFormatting.GOLD);
	
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
	
	public TextFormatting getFormat()
	{
		return this.format;
	}
	
	public static JewelRarity byIndex(int index)
	{
		if(index < 0 || index >= INDEX_LOOKUP.length)
		{
			index = 0;
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