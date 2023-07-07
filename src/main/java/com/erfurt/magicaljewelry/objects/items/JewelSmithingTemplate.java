package com.erfurt.magicaljewelry.objects.items;

import com.erfurt.magicaljewelry.MagicalJewelry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class JewelSmithingTemplate extends SmithingTemplateItem
{
    private static final Component JEWEL_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", MagicalJewelry.getId("jewel_upgrade"))).withStyle(ChatFormatting.GRAY);
    private static final Component JEWEL_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", MagicalJewelry.getId("smithing_template.jewel_upgrade.applies_to"))).withStyle(ChatFormatting.BLUE);
    private static final Component JEWEL_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", MagicalJewelry.getId("smithing_template.jewel_upgrade.ingredients"))).withStyle(ChatFormatting.BLUE);
    private static final Component JEWEL_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", MagicalJewelry.getId("smithing_template.jewel_upgrade.base_slot_description")));
    private static final Component JEWEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", MagicalJewelry.getId("smithing_template.jewel_upgrade.additions_slot_description")));
    private static final ResourceLocation EMPTY_SLOT_AMULET = new ResourceLocation(CuriosApi.MODID, "slot/empty_necklace_slot");
    private static final ResourceLocation EMPTY_SLOT_BRACELET = new ResourceLocation(CuriosApi.MODID, "slot/empty_bracelet_slot");
    private static final ResourceLocation EMPTY_SLOT_RING = new ResourceLocation(CuriosApi.MODID, "slot/empty_ring_slot");
    private static final ResourceLocation EMPTY_SLOT_INGOT = new ResourceLocation("item/empty_slot_ingot");
    private static final ResourceLocation EMPTY_SLOT_BLOCK = MagicalJewelry.getId("item/empty_slot_block");

    public JewelSmithingTemplate(Component appliesTo, Component ingredients, Component upgradeDescription, Component baseSlotDescription, Component additionsSlotDescription, List<ResourceLocation> baseSlotEmptyIcons, List<ResourceLocation> additionalSlotEmptyIcons)
    {
        super(appliesTo, ingredients, upgradeDescription, baseSlotDescription, additionsSlotDescription, baseSlotEmptyIcons, additionalSlotEmptyIcons);
    }

    public static JewelSmithingTemplate createJewelUpgradeTemplate()
    {
        return new JewelSmithingTemplate(JEWEL_UPGRADE_APPLIES_TO, JEWEL_UPGRADE_INGREDIENTS, JEWEL_UPGRADE, JEWEL_UPGRADE_BASE_SLOT_DESCRIPTION, JEWEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createJewelUpgradeIconList(), createJewelUpgradeMaterialList());
    }

    private static List<ResourceLocation> createJewelUpgradeIconList()
    {
        return List.of(EMPTY_SLOT_AMULET, EMPTY_SLOT_BRACELET, EMPTY_SLOT_RING);
    }

    private static List<ResourceLocation> createJewelUpgradeMaterialList()
    {
        return List.of(EMPTY_SLOT_INGOT, EMPTY_SLOT_BLOCK);
    }
}