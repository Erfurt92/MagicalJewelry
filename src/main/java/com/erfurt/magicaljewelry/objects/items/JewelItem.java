package com.erfurt.magicaljewelry.objects.items;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.capability.JewelItemCapability;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import com.erfurt.magicaljewelry.util.interfaces.IJewel;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nullable;
import java.util.*;

public class JewelItem extends Item implements IJewel
{
	public static final String NBT_RARITY = "Rarity";
	public static final String NBT_EFFECTS = "Effects";
	public static final String NBT_LEGENDARY_EFFECT = "LegendaryEffect";
	public static final String NBT_ATTRIBUTES = "Attributes";
	public static final String NBT_UUID = "UUID";
	public static final String NBT_COLOR = "GemColor";

	public static List<Integer> jewelEffects = new ArrayList<>();
	public static Map<LivingEntity, Map<MobEffect, Integer>> totalJewelEffectsPlayer = new LinkedHashMap<>();

	public static Multimap<Attribute, AttributeModifier> jewelAttributesForRemoval = HashMultimap.create();

	public JewelItem()
	{
		super(new Item.Properties().stacksTo(1).tab(MagicalJewelry.GROUP));
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		int durability;

		String rarity = getJewelRarity(stack);
		if(rarity.equals(JewelRarity.UNCOMMON.getName())) durability = MagicalJewelryConfigBuilder.JEWEL_UNCOMMON_DURABILITY.get();
		else if(rarity.equals(JewelRarity.RARE.getName())) durability = MagicalJewelryConfigBuilder.JEWEL_RARE_DURABILITY.get();
		else if(rarity.equals(JewelRarity.EPIC.getName())) durability = MagicalJewelryConfigBuilder.JEWEL_EPIC_DURABILITY.get();
		else durability = 0;

		return durability;
	}

	@Override
	public boolean isDamageable(ItemStack stack)
	{
		return MagicalJewelryConfigBuilder.JEWEL_DURABILITY.get() && JewelRarity.containsRarity(getJewelRarity(stack)) && !getJewelRarity(stack).equals(JewelRarity.LEGENDARY.getName());
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		return JewelItemCapability.createProvider(new ICurio()
		{
			@Override
			public void curioTick(SlotContext slotContext)
			{
				LivingEntity livingEntity = slotContext.entity();
				if(!livingEntity.getCommandSenderWorld().isClientSide && stack.getItem() instanceof JewelItem)
				{
					if(livingEntity.tickCount % 19 == 0) stack.hurtAndBreak(1, livingEntity, (livingEntity1) -> { });

					if(livingEntity.tickCount % 199 == 0 && totalJewelEffectsPlayer.containsKey(livingEntity) && !totalJewelEffectsPlayer.get(livingEntity).isEmpty())
					{
						updateJewelEffects(stack, livingEntity, false);

						if(!MagicalJewelryConfigBuilder.JEWEL_ATTRIBUTES.get()) livingEntity.getAttributes().removeAttributeModifiers(jewelAttributesForRemoval);
					}
				}
			}

			@Override
			public ItemStack getStack()
			{
				return stack;
			}

			@Override
			public void onEquip(SlotContext slotContext, ItemStack prevStack)
			{
				LivingEntity livingEntity = slotContext.entity();
				if(stack.getItem() instanceof JewelItem)
				{
					getTotalJewelEffects(stack, livingEntity);
					updateJewelEffects(stack, livingEntity, false);
				}
			}

			@Override
			public void onUnequip(SlotContext slotContext, ItemStack newStack)
			{
				LivingEntity livingEntity = slotContext.entity();
				if(stack.getItem() instanceof JewelItem) updateJewelEffects(stack, livingEntity, true);
			}

			@Override
			public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid)
			{
				Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
				if(stack.getItem() instanceof JewelItem && stack.getTag().contains(NBT_ATTRIBUTES) && stack.getTag().contains(NBT_UUID)) updateJewelAttributes(stack, attributes);
				return attributes;
			}

			@Override
			public boolean canEquipFromUse(SlotContext slotContext)
			{
				return true;
			}
		});
	}

	/** This method is used to update the mapped effects for the player
	 *
	 * @param stack ItemStack in - Used to check what rarity and effects are stored in the NBT for the ItemStack
	 * @param player LivingEntity in - this should always a player
	 * @param removeEffects Boolean in - are the effects being removed or added - true if you want to remove the effects
	 */
	public void updateJewelEffects(ItemStack stack, LivingEntity player, boolean removeEffects)
	{
		if(removeEffects)
		{
			if(legendaryEffectsEnabled(stack))
			{
				int j = getJewelLegendaryEffect(stack);
				MobEffect effect = (MobEffect) legendaryEffectsList.toArray()[j];

				updateJewelEffects(player, effect);
			}

			for(int i = 0; i < effectsLength(stack); i++)
			{
				int j = getJewelEffects(stack)[i];
				MobEffect effect = (MobEffect) defaultEffectsList.toArray()[j];

				updateJewelEffects(player, effect);
			}
		}
		else
		{
			String rarity = getJewelRarity(stack);

			if(JewelRarity.containsRarity(rarity))
			{
				for(int i = 0; i < totalJewelEffectsPlayer.get(player).size(); i++)
				{
					MobEffect effect = (MobEffect) totalJewelEffectsPlayer.get(player).keySet().toArray()[i];
					int level = (int) totalJewelEffectsPlayer.get(player).values().toArray()[i] - 1;

					int maxLevel = MagicalJewelryConfigBuilder.JEWEL_MAX_EFFECT_LEVEL.get();

					switch (maxLevel)
					{
						case 1:
							level = 0;
							break;
						case 2:
							if (level > 1) level = 1;
							break;
						case 3:
							if (level > 2) level = 2;
							break;
					}

					boolean legendaryFlag = legendaryEffectsList.contains(effect);

					if(legendaryFlag) level = 0;

					player.addEffect(new MobEffectInstance(effect, Integer.MAX_VALUE, level, true, false, !MagicalJewelryConfigBuilder.JEWEL_EFFECT_ICON.get()));

					if(rarity.equals(JewelRarity.LEGENDARY.getName())) legendaryEffectRemoval(stack, player);
				}
			}
		}
	}

	/** This method is a sub-method for updateJewelEffects <br>
	 * Used to update the mapped jewel effects value or removal if the value get's to 0
	 *
	 * @param player LivingEntity in - should always be a player
	 * @param effect Effect in - the effect that are being updated
	 */
	private void updateJewelEffects(LivingEntity player, MobEffect effect)
	{
		int length = totalJewelEffectsPlayer.get(player).size();

		for(int k = 0; k < length; k++)
		{
			if(totalJewelEffectsPlayer.get(player).keySet().toArray()[k].equals(effect))
			{
				int oldValue = (int) totalJewelEffectsPlayer.get(player).values().toArray()[k];
				int newValue = (int) totalJewelEffectsPlayer.get(player).values().toArray()[k] - 1;
				totalJewelEffectsPlayer.get(player).replace(effect, oldValue, newValue);

				if(newValue < 1)
				{
					totalJewelEffectsPlayer.get(player).remove(effect);
					player.removeEffect(effect);
					break;
				}
				else
				{
					boolean legendaryFlag = legendaryEffectsList.contains(effect);
					if(!legendaryFlag)
					{
						if(newValue < MagicalJewelryConfigBuilder.JEWEL_MAX_EFFECT_LEVEL.get())
						{
							player.removeEffect(effect);
							player.addEffect(new MobEffectInstance(effect, Integer.MAX_VALUE, newValue - 1, true, false, !MagicalJewelryConfigBuilder.JEWEL_EFFECT_ICON.get()));
						}
					}
				}
			}
		}
	}

	/** This method is used for legendary effect removal
	 *
	 * @param stack ItemStack in - Used to check what rarity and effects are stored in the NBT for the ItemStack
	 * @param player LivingEntity in - this should always be a player
	 */
	private void legendaryEffectRemoval(ItemStack stack, LivingEntity player)
	{
		if(!legendaryEffectsEnabled(stack))
		{
			int j = getJewelLegendaryEffect(stack);
			MobEffect effect = (MobEffect) legendaryEffectsList.toArray()[j];

			boolean effectIsActive = player.getActiveEffectsMap().containsKey(effect);

			if(effectIsActive)
			{
				boolean effectDuration = player.getEffect(effect).getDuration() > 10000;

				if(effectDuration) player.removeEffect(effect);
			}
		}
	}

	/** This method is used to add a map for the player with effects and the amount of times it's present on the equipped ItemStacks
	 *
	 * @param stack ItemStack in - Used to check what rarity and effects are stored in the NBT for the ItemStack
	 * @param player LivingEntity in - should always be a player
	 */
	public void getTotalJewelEffects(ItemStack stack, LivingEntity player)
	{
		if(legendaryEffectsEnabled(stack))
		{
			int j = getJewelLegendaryEffect(stack);
			MobEffect effect = (MobEffect) legendaryEffectsList.toArray()[j];

			updateTotalJewelEffects(effect, player);
		}

		for(int i = 0; i < effectsLength(stack); i++)
		{
			int j = getJewelEffects(stack)[i];
			MobEffect effect = (MobEffect) defaultEffectsList.toArray()[j];

			updateTotalJewelEffects(effect, player);
		}
	}

	/** This method is a sub-method for getTotalJewelEffects <br>
	 * Used to update the total mapped jewel effects value for each player
	 *
	 * @param effect Effect in - the effect that are being updated
	 * @param player LivingEntity in - should always be a player
	 */
	private void updateTotalJewelEffects(MobEffect effect, LivingEntity player)
	{
		if(totalJewelEffectsPlayer.containsKey(player))
		{
			if(!totalJewelEffectsPlayer.get(player).isEmpty())
			{
				int length = totalJewelEffectsPlayer.get(player).size();

				for(int k = 0; k < length; k++)
				{
					if(totalJewelEffectsPlayer.get(player).keySet().toArray()[k].equals(effect))
					{
						int oldValue = (int) totalJewelEffectsPlayer.get(player).values().toArray()[k];
						int newValue = (int) totalJewelEffectsPlayer.get(player).values().toArray()[k] + 1;
						totalJewelEffectsPlayer.get(player).replace(effect, oldValue, newValue);

						break;
					}
					else if(k == (length - 1))
					{
						totalJewelEffectsPlayer.get(player).put(effect, 1);
					}
				}
			}
			else
			{
				totalJewelEffectsPlayer.get(player).put(effect, 1);
			}
		}
		else
		{
			totalJewelEffectsPlayer.put(player, new HashMap<>());
			totalJewelEffectsPlayer.get(player).put(effect, 1);
		}
	}

	public void updateJewelAttributes(ItemStack stack, Multimap<Attribute, AttributeModifier> jewelAttributes)
	{
		if(getJewelRarity(stack).equals(JewelRarity.LEGENDARY.getName()) || getJewelRarity(stack).equals(JewelRarity.EPIC.getName()))
		{
			double amount = 2.0D;
			int index = getJewelAttributes(stack);

			switch(index)
			{
				// ARMOR TOUGHNESS
				case 0: break;
				// ATTACK DAMAGE
				case 2: break;
				// ARMOR
				case 1: if(stack.getItem() instanceof JewelAmuletItem) amount = 4.0D; break;
				// MAX HEALTH
				case 3:
					if(stack.getItem() instanceof JewelAmuletItem) amount = 8.0D;
					else amount = 4.0D;
					break;
				// KNOCKBACK RESISTANCE
				case 4:
					amount = 1.0D;
					break;
			}

			String uuid = getJewelUUID(stack);
			AttributeModifier attributeModifier = new AttributeModifier(UUID.fromString(uuid), descriptionAttributesList.get(index), amount, AttributeModifier.Operation.ADDITION);

			jewelAttributesForRemoval.put(attributesList.get(index), attributeModifier);

			if(MagicalJewelryConfigBuilder.JEWEL_ATTRIBUTES.get()) jewelAttributes.put(attributesList.get(index), attributeModifier);
		}
	}

	public boolean legendaryEffectsEnabled(ItemStack stack)
	{
		return (MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_EFFECTS.get() && getJewelRarity(stack).equals(JewelRarity.LEGENDARY.getName()));
	}

	public int effectsLength(ItemStack stack)
	{
		int effectLength;

		String rarity = getJewelRarity(stack);

		if(rarity.equals(JewelRarity.UNCOMMON.getName())) effectLength = MagicalJewelryConfigBuilder.JEWEL_UNCOMMON_EFFECT_AMOUNT.get();
		else if(rarity.equals(JewelRarity.RARE.getName())) effectLength = MagicalJewelryConfigBuilder.JEWEL_RARE_EFFECT_AMOUNT.get();
		else if(rarity.equals(JewelRarity.EPIC.getName())) effectLength = MagicalJewelryConfigBuilder.JEWEL_EPIC_EFFECT_AMOUNT.get();
		else if(rarity.equals(JewelRarity.LEGENDARY.getName())) effectLength = MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_EFFECT_AMOUNT.get();
		else effectLength = 0;

		return effectLength;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		String rarity = getJewelRarity(stack);

		if(JewelRarity.containsRarity(rarity))
		{
			tooltip.set(0, tooltip.get(0).copy().withStyle(JewelRarity.byName(rarity).getFormat()));

			if(MagicalJewelryConfigBuilder.JEWEL_RARITY_NAME.get()) tooltip.set(0, tooltip.get(0).copy().append(" (" + JewelRarity.byName(rarity).getDisplayName() + ")"));

			if(MagicalJewelryConfigBuilder.JEWEL_RARITY_TOOLTIP.get()) tooltip.add(new TextComponent(JewelRarity.byName(rarity).getFormat() + JewelRarity.byName(rarity).getDisplayName()));

			if(legendaryEffectsEnabled(stack) && stack.getTag().contains(NBT_LEGENDARY_EFFECT))
			{
				int j = getJewelLegendaryEffect(stack);
				MobEffect effect = (MobEffect) legendaryEffectsList.toArray()[j];
				String effectName = effect.getDisplayName().getString();

				tooltip.add(new TextComponent(ChatFormatting.BLUE + effectName));
			}

			if(stack.getTag().contains(NBT_EFFECTS))
			{
				for (int i = 0; i < effectsLength(stack); i++)
				{
					int j = getJewelEffects(stack)[i];
					MobEffect effect = (MobEffect) defaultEffectsList.toArray()[j];
					String effectName = effect.getDisplayName().getString();

					tooltip.add(new TextComponent(ChatFormatting.BLUE + effectName));
				}
			}
		}
		else
		{
			String creativeJewelTooltip = new TranslatableComponent("item." + MagicalJewelry.MOD_ID + ".tooltip.creative").getString();
			for(String s : creativeJewelTooltip.split("(?<=\\G.{25,}\\s)"))
			{
				tooltip.add(new TextComponent(s).copy().withStyle(ChatFormatting.RED));
			}
		}
	}
	
	@Override
	public boolean isFoil(ItemStack stack)
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

	public static int getJewelLegendaryEffect(ItemStack stack)
	{
		return stack.getOrCreateTag().getInt(NBT_LEGENDARY_EFFECT);
	}

	public static ItemStack setJewelLegendaryEffect(ItemStack stack, int legendaryEffect)
	{
		stack.getOrCreateTag().putInt(NBT_LEGENDARY_EFFECT, legendaryEffect);
		return stack;
	}

	public static int getJewelAttributes(ItemStack stack)
	{
		return stack.getOrCreateTag().getInt(NBT_ATTRIBUTES);
	}

	public static ItemStack setJewelAttributes(ItemStack stack, int attributes)
	{
		stack.getOrCreateTag().putInt(NBT_ATTRIBUTES, attributes);
		return stack;
	}

	public static String getJewelUUID(ItemStack stack)
	{
		return stack.getOrCreateTag().getString(NBT_UUID);
	}

	public static ItemStack setJewelUUID(ItemStack stack, String uuid)
	{
		stack.getOrCreateTag().putString(NBT_UUID, uuid);
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

	private static int getColorValue(ItemStack stack)
	{
		float[] textureDiffuseColors = DyeColor.byName(getGemColor(stack), DyeColor.WHITE).getTextureDiffuseColors();
		int i = (int) (textureDiffuseColors[0] * 255.0F);
		int j = (int) (textureDiffuseColors[1] * 255.0F);
		int k = (int) (textureDiffuseColors[2] * 255.0F);
		int color = (i << 8) + j;
		color = (color << 8) + k;
		return color;
	}

	public static int getItemColor(ItemStack stack, int tintIndex)
	{
		if(tintIndex == 0) return getColorValue(stack);
		return 0xFFFFFF;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items)
	{
		if(allowdedIn(group))
		{
			for(DyeColor color : DyeColor.values())
			{
				ItemStack stack = new ItemStack(this);
				setGemColor(stack, color.getSerializedName());
				items.add(stack);
			}
		}
	}
}