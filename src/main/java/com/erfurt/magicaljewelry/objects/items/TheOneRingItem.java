package com.erfurt.magicaljewelry.objects.items;

import com.erfurt.magicaljewelry.capability.JewelItemCapability;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TheOneRingItem extends Item
{
    private static final UUID ARMOR_TOUGHNESS_UUID = UUID.fromString("c9d470e7-bd2a-4c30-a13c-c381b99655da");
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("ba527873-7d67-48a2-90a3-7e2cf0a7aa97");
    private static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("a1efe6cf-83e1-45a1-a45c-227381251caa");

    public TheOneRingItem()
    {
        super(new Item.Properties().stacksTo(1));
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
                if(!livingEntity.getCommandSenderWorld().isClientSide && livingEntity.tickCount % 199 == 0) livingEntity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false, !MagicalJewelryConfigBuilder.JEWEL_EFFECT_ICON.get()));
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
                livingEntity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false, !MagicalJewelryConfigBuilder.JEWEL_EFFECT_ICON.get()));
            }

            @Override
            public void onUnequip(SlotContext slotContext, ItemStack newStack)
            {
                LivingEntity livingEntity = slotContext.entity();
                livingEntity.removeEffect(MobEffects.INVISIBILITY);
            }

            @Override
            public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid)
            {
                Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
                attributes.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ARMOR_TOUGHNESS_UUID, "Armor Toughness bonus", 2.0D, AttributeModifier.Operation.ADDITION));
                attributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_UUID, "Attack Damage bonus", 2.0D, AttributeModifier.Operation.ADDITION));
                attributes.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(KNOCKBACK_RESISTANCE_UUID, "Knockback Resistance bonus", 0.1D, AttributeModifier.Operation.ADDITION));
                return attributes;
            }

            @Override
            public boolean canEquipFromUse(SlotContext slotContext)
            {
                return true;
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        tooltip.set(0, tooltip.get(0).copy().withStyle(ChatFormatting.GOLD));

        tooltip.add(new TextComponent(ChatFormatting.ITALIC + "Ash nazg durbatul\u00FBk.").copy().withStyle(ChatFormatting.DARK_RED));
        tooltip.add(new TextComponent(ChatFormatting.ITALIC + "Ash nazg gimbatul.").copy().withStyle(ChatFormatting.DARK_RED));
        tooltip.add(new TextComponent(ChatFormatting.ITALIC + "Ash nazg thrakatul\u00FBk").copy().withStyle(ChatFormatting.DARK_RED));
        tooltip.add(new TextComponent(ChatFormatting.ITALIC + "agh burzum-ishi krimpatul.").copy().withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return true;
    }
}
