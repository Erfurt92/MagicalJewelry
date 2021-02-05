package com.erfurt.magicaljewelry.objects.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.common.capability.CapCurioItem;

import java.util.List;
import java.util.UUID;

public class TheOneRingItem extends Item
{
    private static final UUID ARMOR_TOUGHNESS_UUID = UUID.fromString("c9d470e7-bd2a-4c30-a13c-c381b99655da");
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("ba527873-7d67-48a2-90a3-7e2cf0a7aa97");
    private static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("a1efe6cf-83e1-45a1-a45c-227381251caa");

    public TheOneRingItem()
    {
        super(new Item.Properties().maxStackSize(1).defaultMaxDamage(0));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt)
    {
        return CapCurioItem.createProvider(new ICurio()
        {
            @Override
            public void onCurioTick(String identifier, int index, LivingEntity livingEntity)
            {
                if(!livingEntity.getEntityWorld().isRemote && livingEntity.ticksExisted % 199 == 0) livingEntity.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false, true));
            }

            @Override
            public void onEquipped(String identifier, LivingEntity livingEntity)
            {
                livingEntity.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false, true));
            }

            @Override
            public void onUnequipped(String identifier, LivingEntity livingEntity)
            {
                livingEntity.removePotionEffect(Effects.INVISIBILITY);
            }

            @Override
            public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier)
            {
                Multimap<String, AttributeModifier> attributes = HashMultimap.create();
                attributes.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_TOUGHNESS_UUID, "Armor Toughness bonus", 2.0D, AttributeModifier.Operation.ADDITION));
                attributes.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_UUID, "Attack Damage bonus", 2.0D, AttributeModifier.Operation.ADDITION));
                attributes.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(KNOCKBACK_RESISTANCE_UUID, "Knockback Resistance bonus", 1.0D, AttributeModifier.Operation.ADDITION));
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
        tooltip.set(0, tooltip.get(0).applyTextStyle(TextFormatting.GOLD));

        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "Ash nazg durbatul\u00FBk.").applyTextStyle(TextFormatting.DARK_RED));
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "Ash nazg gimbatul.").applyTextStyle(TextFormatting.DARK_RED));
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "Ash nazg thrakatul\u00FBk").applyTextStyle(TextFormatting.DARK_RED));
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "agh burzum-ishi krimpatul.").applyTextStyle(TextFormatting.DARK_RED));
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}
