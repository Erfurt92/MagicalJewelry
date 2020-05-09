package com.erfurt.magicaljewelry.objects.items;

import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import com.erfurt.magicaljewelry.util.interfaces.IJewelEffects;
import com.erfurt.magicaljewelry.util.interfaces.IJewelRarity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.common.capability.CapCurioItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JewelItem extends Item implements IJewelEffects, IJewelRarity
{
	private static final String NBT_RARITY = "Rarity";
	
	private static final String NBT_COLOR = "GemColor";
	
	private static final String NBT_EFFECTS = "Effects";
	
	public static List<Integer> jewelEffects = new ArrayList<Integer>();
	
	public static Map<Effect, Integer> totalJewelEffects = new LinkedHashMap<Effect, Integer>();
	
	public JewelItem()
	{
		super(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS).defaultMaxDamage(0));
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt)
	{
		return CapCurioItem.createProvider(new ICurio()
		{
			@Override
			public void onCurioTick(String identifier, int index, LivingEntity livingEntity)
			{
				if(!livingEntity.getEntityWorld().isRemote && livingEntity.ticksExisted % 199 == 0 && !totalJewelEffects.isEmpty())
				{
					updateJewelEffects(stack, livingEntity, false);
				}
			}
			
			@Override
			public void onEquipped(String identifier, LivingEntity livingEntity)
			{
				getTotalJewelEffects(stack);
				updateJewelEffects(stack, livingEntity, false);
			}
			
			@Override
			public void onUnequipped(String identifier, LivingEntity livingEntity)
			{
				updateJewelEffects(stack, livingEntity, true);
			}
			
			@Override
			public boolean canRightClickEquip()
			{	
				if(!(getJewelEffects(stack).length == 0))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		});
	}
	
	public void updateJewelEffects(ItemStack stack, LivingEntity livingEntity, boolean removeEffects)
	{
		if(removeEffects)
		{
			for(int i = 0; i < getJewelEffects(stack).length; i++)
			{
				int j = getJewelEffects(stack)[i];
				
				Effect effect = (Effect) defaultEffectsList.toArray()[j];
				int length = totalJewelEffects.size();
				
				for(int k = 0; k < length; k++)
				{
					if(totalJewelEffects.keySet().toArray()[k].equals(effect))
					{
						int oldValue = (int) totalJewelEffects.values().toArray()[k];
						
						int newValue = (int) totalJewelEffects.values().toArray()[k] - 1;
						totalJewelEffects.replace(effect, oldValue, newValue);
						
						livingEntity.removePotionEffect(effect);
						livingEntity.addPotionEffect(new EffectInstance(effect, Integer.MAX_VALUE, newValue, true, false, true));
						
						if(oldValue <= 0)
						{
							totalJewelEffects.remove(effect);
							livingEntity.removePotionEffect(effect);
							
							break;
						}
					}
				}
			}
		}
		else
		{
			for(int i = 0; i < totalJewelEffects.size(); i++)
			{
				Effect effect = (Effect) totalJewelEffects.keySet().toArray()[i];
				int level = (int) totalJewelEffects.values().toArray()[i];
				if(level > 1)
				{
					level = 1;
				}

				boolean flag1 = (effect == (Effect) Effects.FIRE_RESISTANCE);
				boolean flag2 = (effect == (Effect) Effects.WATER_BREATHING);
				boolean flag3 = (effect == (Effect) Effects.NIGHT_VISION);
				boolean finalFlag = flag1 || flag2 || flag3;

				if(finalFlag)
				{
					level = 0;
				}

				livingEntity.addPotionEffect(new EffectInstance(effect, Integer.MAX_VALUE, level, true, false, true));
			}
		}
	}
	
	public void getTotalJewelEffects(ItemStack stack)
	{
		for(int i = 0; i < getJewelEffects(stack).length; i++)
		{
			int j = getJewelEffects(stack)[i];

			Effect effect = (Effect) defaultEffectsList.toArray()[j];
			
			int length = totalJewelEffects.size();

			if(!totalJewelEffects.isEmpty())
			{
				for(int k = 0; k < length; k++)
				{
					if(totalJewelEffects.keySet().toArray()[k].equals(effect))
					{
						int oldValue = (int) totalJewelEffects.values().toArray()[k];
						int newValue = (int) totalJewelEffects.values().toArray()[k] + 1;
						
						totalJewelEffects.replace(effect, oldValue, newValue);
						
						break;
					}
					if(k == (length - 1))
					{
						totalJewelEffects.put(effect, 0);
					}
				}
			}
			else
			{
				totalJewelEffects.put(effect, 0);
			}
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		int rarity = getJewelRarity(stack);
		if(rarity >= JewelRarity.UNCOMMON.getIndex())
		{
			tooltip.set(0, tooltip.get(0).applyTextStyle(JewelRarity.byIndex(rarity).getFormat()));

			if(MagicalJewelryConfigBuilder.JEWEL_RARITY_NAME.get())
			{
				tooltip.set(0, tooltip.get(0).appendText(" (" + JewelRarity.byIndex(rarity).getDisplayName() + ")"));
			}

			if(MagicalJewelryConfigBuilder.JEWEL_RARITY_TOOLTIP.get())
			{
				tooltip.add(new StringTextComponent(JewelRarity.byIndex(rarity).getFormat() + JewelRarity.byIndex(rarity).getDisplayName()));
			}
		}
		
		for(int i = 0; i < getJewelEffects(stack).length; i++)
		{
			int j = getJewelEffects(stack)[i];
			
			Effect effect = (Effect) defaultEffectsList.toArray()[j];
			String effectName = effect.getDisplayName().getString();
			
			tooltip.add(new StringTextComponent(TextFormatting.BLUE + effectName));
		}
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		if(getJewelRarity(stack) == JewelRarity.LEGENDARY.getIndex())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static int getJewelRarity(ItemStack stack)
	{
		return stack.getOrCreateTag().getInt(NBT_RARITY);
	}
	
	public static ItemStack setJewelRarity(ItemStack stack, int rarity)
	{
		stack.getOrCreateTag().putInt(NBT_RARITY, rarity);
		return stack;
    }
	
	public static int[] getJewelEffects(ItemStack stack)
	{
		return stack.getOrCreateTag().getIntArray(NBT_EFFECTS);
	}
	
	public static ItemStack setJewelEffects(ItemStack stack, List<Integer> effects)
	{
		stack.getOrCreateTag().putIntArray(NBT_EFFECTS, effects);
		return stack;
	}
	
	public static int getGemColor(ItemStack stack)
	{
		return stack.getOrCreateTag().getInt(NBT_COLOR);
	}
	
	public static ItemStack setGemColor(ItemStack stack, int color)
	{
		stack.getOrCreateTag().putInt(NBT_COLOR, color);
		return stack;
	}

	public static int getItemColor(ItemStack stack, int tintIndex)
	{
		if(tintIndex == 0)
		{
			return getGemColor(stack);
		}
		return 0xFFFFFF;
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(isInGroup(group))
		{
			for(DyeColor color : DyeColor.values())
			{
				ItemStack stack = new ItemStack(this);
				setGemColor(stack, color.getColorValue());
				setJewelRarity(stack, -1);
				items.add(stack);
			}
		}
	}
}
