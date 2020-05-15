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
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JewelItem extends Item implements IJewelEffects, IJewelRarity
{
	private static final String NBT_RARITY = "Rarity";
	
	private static final String NBT_COLOR = "GemColor";
	
	private static final String NBT_EFFECTS = "Effects";
	
	public static List<Integer> jewelEffects = new ArrayList<>();
	
	public static Map<Effect, Integer> totalJewelEffects = new LinkedHashMap<>();
	
	public JewelItem()
	{
		super(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS).defaultMaxDamage(0));
	}
	
	public void updateJewelEffects(ItemStack stack, LivingEntity livingEntity, boolean removeEffects)
	{
		if(removeEffects)
		{
			if(legendaryEffectsEnabled() && getJewelRarity(stack).equals(JewelRarity.LEGENDARY.getName()))
			{
				int j = getJewelEffects(stack)[6];

				Effect effect = (Effect) defaultEffectsList.toArray()[j];
				updateJewelEffects(livingEntity, effect);
			}

			for(int i = 0; i < effectsLength(stack); i++)
			{
				int j = getJewelEffects(stack)[i];
				
				Effect effect = (Effect) defaultEffectsList.toArray()[j];
				updateJewelEffects(livingEntity, effect);
			}
		}
		else
		{
			for(int i = 0; i < totalJewelEffects.size(); i++)
			{
				Effect effect = (Effect) totalJewelEffects.keySet().toArray()[i];
				int level = (int) totalJewelEffects.values().toArray()[i];

				int maxLevel = MagicalJewelryConfigBuilder.JEWEL_MAX_EFFECT_LEVEL.get();
				switch(maxLevel)
				{
					case 1: level = 0; break;
					case 2: if(level > 1) level = 1; break;
					case 3: break;
				}

				boolean flag1 = (effect == Effects.FIRE_RESISTANCE);
				boolean flag2 = (effect == Effects.WATER_BREATHING);
				boolean flag3 = (effect == Effects.NIGHT_VISION);
				boolean finalFlag = flag1 || flag2 || flag3;

				if(finalFlag) level = 0;

				livingEntity.addPotionEffect(new EffectInstance(effect, Integer.MAX_VALUE, level, true, false, true));
			}

			legendaryEffectRemoval(stack, livingEntity);
		}
	}

	private void updateJewelEffects(LivingEntity livingEntity, Effect effect)
	{
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

	private void legendaryEffectRemoval(ItemStack stack, LivingEntity livingEntity)
	{
		if(!legendaryEffectsEnabled() && getJewelRarity(stack).equals(JewelRarity.LEGENDARY.getName()))
		{
			int j = getJewelEffects(stack)[6];
			Effect effect = (Effect) defaultEffectsList.toArray()[j];

			boolean effectIsActive = livingEntity.getActivePotionMap().containsKey(effect);

			if(effectIsActive)
			{
				boolean effectDuration = livingEntity.getActivePotionEffect(effect).getDuration() > 10000;

				if(effectDuration) livingEntity.removePotionEffect(effect);
			}
		}
	}

	public void getTotalJewelEffects(ItemStack stack)
	{
		if(legendaryEffectsEnabled() && getJewelRarity(stack).equals(JewelRarity.LEGENDARY.getName()))
		{
			int j = getJewelEffects(stack)[6];

			Effect effect = (Effect) defaultEffectsList.toArray()[j];

			updateTotalJewelEffects(effect);
		}
		for(int i = 0; i < effectsLength(stack); i++)
		{
			int j = getJewelEffects(stack)[i];

			Effect effect = (Effect) defaultEffectsList.toArray()[j];

			updateTotalJewelEffects(effect);
		}
	}

	private void updateTotalJewelEffects(Effect effect)
	{
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
				if(k == (length - 1)) totalJewelEffects.put(effect, 0);
			}
		}
		else totalJewelEffects.put(effect, 0);
	}

	public boolean legendaryEffectsEnabled()
	{
		return MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_EFFECTS.get();
	}

	public int effectsLength(ItemStack stack)
	{
		int effectLength = 0;

		if(getJewelRarity(stack).equals(JewelRarity.UNCOMMON.getName())) effectLength = MagicalJewelryConfigBuilder.JEWEL_UNCOMMON_EFFECT_AMOUNT.get();
		else if(getJewelRarity(stack).equals(JewelRarity.RARE.getName())) effectLength = MagicalJewelryConfigBuilder.JEWEL_RARE_EFFECT_AMOUNT.get();
		else if(getJewelRarity(stack).equals(JewelRarity.EPIC.getName())) effectLength = MagicalJewelryConfigBuilder.JEWEL_EPIC_EFFECT_AMOUNT.get();
		else if(getJewelRarity(stack).equals(JewelRarity.LEGENDARY.getName())) effectLength = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_EFFECT_AMOUNT.get();

		return effectLength;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		String rarity = getJewelRarity(stack);
		if(!getJewelRarity(stack).equals("common"))
		{
			tooltip.set(0, tooltip.get(0).applyTextStyle(JewelRarity.byName(rarity).getFormat()));

			if(MagicalJewelryConfigBuilder.JEWEL_RARITY_NAME.get()) tooltip.set(0, tooltip.get(0).appendText(" (" + JewelRarity.byName(rarity).getDisplayName() + ")"));

			if(MagicalJewelryConfigBuilder.JEWEL_RARITY_TOOLTIP.get()) tooltip.add(new StringTextComponent(JewelRarity.byName(rarity).getFormat() + JewelRarity.byName(rarity).getDisplayName()));
		}

		if(legendaryEffectsEnabled() && rarity.equals(JewelRarity.LEGENDARY.getName()))
		{
			int j = getJewelEffects(stack)[6];

			Effect effect = (Effect) defaultEffectsList.toArray()[j];
			String effectName = effect.getDisplayName().getString();

			tooltip.add(new StringTextComponent(TextFormatting.BLUE + effectName));
		}

		for(int i = 0; i < effectsLength(stack); i++)
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
		return getJewelRarity(stack).equals(JewelRarity.LEGENDARY.getName());
	}
	
	public static String getJewelRarity(ItemStack stack)
	{
		return stack.getOrCreateTag().getString(NBT_RARITY);
	}
	
	public static ItemStack setJewelRarity(ItemStack stack, String rarity)
	{
		stack.getOrCreateTag().putString(NBT_RARITY, rarity);
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
	
	public static String getGemColor(ItemStack stack)
	{
		return stack.getOrCreateTag().getString(NBT_COLOR);
	}
	
	public static ItemStack setGemColor(ItemStack stack, String color)
	{
		stack.getOrCreateTag().putString(NBT_COLOR, color);
		return stack;
	}

	public static int getItemColor(ItemStack stack, int tintIndex)
	{
		if(tintIndex == 0) return DyeColor.byTranslationKey(getGemColor(stack), DyeColor.WHITE).getColorValue();
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
				setGemColor(stack, color.getName());
				setJewelRarity(stack, "common");
				items.add(stack);
			}
		}
	}
}