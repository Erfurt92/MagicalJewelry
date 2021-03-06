package com.erfurt.magicaljewelry.objects.items;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.render.model.JewelAmuletModel;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import com.erfurt.magicaljewelry.util.interfaces.IJewel;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.capability.CurioItemCapability;

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
	public static Map<LivingEntity, Map<Effect, Integer>> totalJewelEffectsPlayer = new LinkedHashMap<>();

	public static Multimap<Attribute, AttributeModifier> jewelAttributesForRemoval = HashMultimap.create();

	public static final ResourceLocation GOLD_AMULET_TEXTURE = MagicalJewelry.getId( "textures/models/amulet/gold_amulet.png");
	public static final ResourceLocation SILVER_AMULET_TEXTURE = MagicalJewelry.getId( "textures/models/amulet/silver_amulet.png");
	public static final ResourceLocation GEM_AMULET_TEXTURE = MagicalJewelry.getId( "textures/models/amulet/gem_amulet.png");

	public JewelItem()
	{
		super(new Item.Properties().maxStackSize(1).group(MagicalJewelry.GROUP));
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

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt)
	{
		return CurioItemCapability.createProvider(new ICurio()
		{
			private Object amuletModel;

			@Override
			public boolean canRender(String identifier, int index, LivingEntity livingEntity)
			{
				return stack.getItem() instanceof JewelAmuletItem;
			}

			@Override
			public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
			{
				RenderHelper.translateIfSneaking(matrixStack, livingEntity);
				RenderHelper.rotateIfSneaking(matrixStack, livingEntity);

				if(!(this.amuletModel instanceof JewelAmuletModel)) this.amuletModel = new JewelAmuletModel();

				JewelAmuletModel<?> jewelAmuletModel = (JewelAmuletModel)this.amuletModel;
				IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(renderTypeBuffer, jewelAmuletModel.getRenderType(getRenderType(stack)), false, stack.hasEffect());
				jewelAmuletModel.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

				float[] i = DyeColor.byTranslationKey(getGemColor(stack), DyeColor.WHITE).getColorComponentValues();
				float red = i[0];
				float green = i[1];
				float blue = i[2];

				IVertexBuilder vertexBuilderGem = ItemRenderer.getBuffer(renderTypeBuffer, jewelAmuletModel.getRenderType(GEM_AMULET_TEXTURE), false, stack.hasEffect());
				jewelAmuletModel.render(matrixStack, vertexBuilderGem, light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
			}

			@Override
			public void curioTick(String identifier, int index, LivingEntity livingEntity)
			{
				if(!livingEntity.getEntityWorld().isRemote && stack.getItem() instanceof JewelItem)
				{
					if(livingEntity.ticksExisted % 19 == 0) stack.damageItem(1, livingEntity, (livingEntity1) -> { });

					if(livingEntity.ticksExisted % 199 == 0 && totalJewelEffectsPlayer.containsKey(livingEntity) && !totalJewelEffectsPlayer.get(livingEntity).isEmpty())
					{
						updateJewelEffects(stack, livingEntity, false);

						if(!MagicalJewelryConfigBuilder.JEWEL_ATTRIBUTES.get()) livingEntity.getAttributeManager().removeModifiers(jewelAttributesForRemoval);
					}
				}
			}

			@Override
			public void onEquip(SlotContext slotContext, ItemStack prevStack)
			{
				LivingEntity livingEntity = slotContext.getWearer();
				if(stack.getItem() instanceof JewelItem)
				{
					getTotalJewelEffects(stack, livingEntity);
					updateJewelEffects(stack, livingEntity, false);
				}
			}

			@Override
			public void onUnequip(SlotContext slotContext, ItemStack newStack)
			{
				LivingEntity livingEntity = slotContext.getWearer();
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

	private static ResourceLocation getRenderType(ItemStack stack)
	{
		if(stack.getItem() == ItemInit.GOLD_AMULET.get().getItem()) return GOLD_AMULET_TEXTURE;
		return SILVER_AMULET_TEXTURE;
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
				Effect effect = (Effect) legendaryEffectsList.toArray()[j];

				updateJewelEffects(player, effect);
			}

			for(int i = 0; i < effectsLength(stack); i++)
			{
				int j = getJewelEffects(stack)[i];
				Effect effect = (Effect) defaultEffectsList.toArray()[j];

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
					Effect effect = (Effect) totalJewelEffectsPlayer.get(player).keySet().toArray()[i];
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

					player.addPotionEffect(new EffectInstance(effect, Integer.MAX_VALUE, level, true, false, !MagicalJewelryConfigBuilder.JEWEL_EFFECT_ICON.get()));

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
	private void updateJewelEffects(LivingEntity player, Effect effect)
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
					player.removePotionEffect(effect);
					break;
				}
				else
				{
					boolean legendaryFlag = legendaryEffectsList.contains(effect);
					if(!legendaryFlag)
					{
						if(newValue < MagicalJewelryConfigBuilder.JEWEL_MAX_EFFECT_LEVEL.get())
						{
							player.removePotionEffect(effect);
							player.addPotionEffect(new EffectInstance(effect, Integer.MAX_VALUE, newValue - 1, true, false, !MagicalJewelryConfigBuilder.JEWEL_EFFECT_ICON.get()));
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
			Effect effect = (Effect) legendaryEffectsList.toArray()[j];

			boolean effectIsActive = player.getActivePotionMap().containsKey(effect);

			if(effectIsActive)
			{
				boolean effectDuration = player.getActivePotionEffect(effect).getDuration() > 10000;

				if(effectDuration) player.removePotionEffect(effect);
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
			Effect effect = (Effect) legendaryEffectsList.toArray()[j];

			updateTotalJewelEffects(effect, player);
		}

		for(int i = 0; i < effectsLength(stack); i++)
		{
			int j = getJewelEffects(stack)[i];
			Effect effect = (Effect) defaultEffectsList.toArray()[j];

			updateTotalJewelEffects(effect, player);
		}
	}

	/** This method is a sub-method for getTotalJewelEffects <br>
	 * Used to update the total mapped jewel effects value for each player
	 *
	 * @param effect Effect in - the effect that are being updated
	 * @param player LivingEntity in - should always be a player
	 */
	private void updateTotalJewelEffects(Effect effect, LivingEntity player)
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
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		String rarity = getJewelRarity(stack);

		if(JewelRarity.containsRarity(rarity))
		{
			tooltip.set(0, tooltip.get(0).deepCopy().mergeStyle(JewelRarity.byName(rarity).getFormat()));

			if(MagicalJewelryConfigBuilder.JEWEL_RARITY_NAME.get()) tooltip.set(0, tooltip.get(0).deepCopy().appendString(" (" + JewelRarity.byName(rarity).getDisplayName() + ")"));

			if(MagicalJewelryConfigBuilder.JEWEL_RARITY_TOOLTIP.get()) tooltip.add(new StringTextComponent(JewelRarity.byName(rarity).getFormat() + JewelRarity.byName(rarity).getDisplayName()));

			if(legendaryEffectsEnabled(stack) && stack.getTag().contains(NBT_LEGENDARY_EFFECT))
			{
				int j = getJewelLegendaryEffect(stack);
				Effect effect = (Effect) legendaryEffectsList.toArray()[j];
				String effectName = effect.getDisplayName().getString();

				tooltip.add(new StringTextComponent(TextFormatting.BLUE + effectName));
			}

			if(stack.getTag().contains(NBT_EFFECTS))
			{
				for (int i = 0; i < effectsLength(stack); i++)
				{
					int j = getJewelEffects(stack)[i];
					Effect effect = (Effect) defaultEffectsList.toArray()[j];
					String effectName = effect.getDisplayName().getString();

					tooltip.add(new StringTextComponent(TextFormatting.BLUE + effectName));
				}
			}
		}
		else
		{
			String creativeJewelTooltip = new TranslationTextComponent("item." + MagicalJewelry.MOD_ID + ".tooltip.creative").getString();
			for(String s : creativeJewelTooltip.split("(?<=\\G.{25,}\\s)"))
			{
				tooltip.add(new StringTextComponent(s).deepCopy().mergeStyle(TextFormatting.RED));
			}
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
				setGemColor(stack, color.getString());
				items.add(stack);
			}
		}
	}
}