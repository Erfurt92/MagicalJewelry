package com.erfurt.magicaljewelry.objects.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.capability.CurioItemCapability;

import java.util.List;
import java.util.UUID;

public class TheOneRingItem extends Item
{
    private static final UUID ARMOR_TOUGHNESS_UUID = UUID.fromString("c9d470e7-bd2a-4c30-a13c-c381b99655da");
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("ba527873-7d67-48a2-90a3-7e2cf0a7aa97");
    private static final UUID ARMOR_UUID = UUID.fromString("a1efe6cf-83e1-45a1-a45c-227381251caa");
    private static final UUID MAX_HEALTH_UUID = UUID.fromString("4715d2d3-0b7f-4ac7-9875-f18ef0fc7321");

    public TheOneRingItem()
    {
        super(new Item.Properties().maxStackSize(1).defaultMaxDamage(0));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt)
    {
        return CurioItemCapability.createProvider(new ICurio()
        {
            @Override
            public void curioTick(String identifier, int index, LivingEntity livingEntity)
            {
                if(!livingEntity.getEntityWorld().isRemote && livingEntity.ticksExisted % 199 == 0) livingEntity.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false, true));
            }

            @Override
            public void onEquip(String identifier, int index, LivingEntity livingEntity)
            {
                livingEntity.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false, true));
            }

            @Override
            public void onUnequip(String identifier, int index, LivingEntity livingEntity)
            {
                livingEntity.removePotionEffect(Effects.INVISIBILITY);
            }

            @Override
            public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier)
            {
                Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
                attributes.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ARMOR_TOUGHNESS_UUID, "Armor Toughness bonus", 2.0D, AttributeModifier.Operation.ADDITION));
                attributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_UUID, "Attack Damage bonus", 2.0D, AttributeModifier.Operation.ADDITION));
                attributes.put(Attributes.ARMOR, new AttributeModifier(ARMOR_UUID, "Armor bonus", 2.0D, AttributeModifier.Operation.ADDITION));
                attributes.put(Attributes.MAX_HEALTH, new AttributeModifier(MAX_HEALTH_UUID, "Health Boost bonus", 4.0D, AttributeModifier.Operation.ADDITION));
                return attributes;
            }

            @Override
            public boolean canRightClickEquip()
            {
                return true;
            }
        });
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.set(0, tooltip.get(0).deepCopy().mergeStyle(TextFormatting.GOLD));

        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "Ash nazg durbatul\u00FBk.").deepCopy().mergeStyle(TextFormatting.DARK_RED));
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "Ash nazg gimbatul.").deepCopy().mergeStyle(TextFormatting.DARK_RED));
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "Ash nazg thrakatul\u00FBk").deepCopy().mergeStyle(TextFormatting.DARK_RED));
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "agh burzum-ishi krimpatul.").deepCopy().mergeStyle(TextFormatting.DARK_RED));
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}
