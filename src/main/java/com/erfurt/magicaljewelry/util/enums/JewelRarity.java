package com.erfurt.magicaljewelry.util.enums;

import com.erfurt.magicaljewelry.MagicalJewelry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public enum JewelRarity
{
	UNCOMMON("uncommon", TextFormatting.DARK_GREEN),
	RARE("rare", TextFormatting.DARK_AQUA),
	EPIC("epic", TextFormatting.DARK_PURPLE),
	LEGENDARY("legendary", TextFormatting.GOLD);

	private final String name;
	private final TextFormatting format;
	private final ITextComponent displayName;
	
	JewelRarity(String nameIn, TextFormatting formatIn)
	{
		this.name = nameIn;
		this.format = formatIn;
		this.displayName = new TranslationTextComponent("rarity." + MagicalJewelry.MOD_ID + "." + nameIn);
	}
	
	public String getName()
	{
		return this.name;
	}

	public String getDisplayName()
	{
		return displayName.getString();
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

	public static boolean containsRarity(String name)
	{
		for(JewelRarity rarity : values()) if(rarity.name.equals(name)) return true;

		return false;
	}
}