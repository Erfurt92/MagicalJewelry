package com.erfurt.magicaljewelry.command;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.data.loot.loottable.ModChestLootTablesBuilder;
import com.erfurt.magicaljewelry.data.loot.loottable.ModEntityLootTablesBuilder;
import com.erfurt.magicaljewelry.objects.items.JewelItem;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import com.erfurt.magicaljewelry.util.interfaces.IJewelNBTHandler;
import com.erfurt.magicaljewelry.util.interfaces.IJewelRarity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static com.erfurt.magicaljewelry.util.enums.JewelRarity.*;

public final class JewelCommands implements IJewelNBTHandler
{
    private static final String jewelTestLoot = "jewelTestLoot";
    private static final String jewelGive = "jewelGive";
    private static final String targets = "targets";
    private static final String itemId = "itemId";
    private static final String rarityId = "rarity";
    private static final String randomRarity = "random";
    private static final String lootTableSettings = "lootTable";
    private static final String hostileLootDrop = ModEntityLootTablesBuilder.hostileLootTable;
    private static final String bossLootDrop = ModEntityLootTablesBuilder.bossLootTable;
    private static final String chestLootDrop = ModChestLootTablesBuilder.chestLootTable;
    private static final String allLootTables = "allLootTable";

    private static final String[] rarities = {
            randomRarity,
            UNCOMMON.getName(),
            RARE.getName(),
            EPIC.getName(),
            LEGENDARY.getName()
    };

    private static final String[] testLootTables = {
            hostileLootDrop,
            bossLootDrop,
            chestLootDrop,
            allLootTables
    };

    private static final SuggestionProvider<CommandSource> itemIdSuggestions =
            (context, builder) -> ISuggestionProvider.func_212476_a(ForgeRegistries.ITEMS.getValues().stream()
                    .filter(item -> item instanceof JewelItem).map(ForgeRegistryEntry::getRegistryName), builder);
    private static final SuggestionProvider<CommandSource> raritySuggestions =
            (context, builder) -> ISuggestionProvider.suggest(rarities, builder);
    private static final SuggestionProvider<CommandSource> testLootSuggestions =
            (context, builder) -> ISuggestionProvider.suggest(testLootTables, builder);

    private JewelCommands() {}

    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        LiteralArgumentBuilder<CommandSource> builderJewelGive = Commands.literal(jewelGive)
                .requires(source -> source.hasPermissionLevel(2));

        LiteralArgumentBuilder<CommandSource> builderTestLoot = Commands.literal(jewelTestLoot);

        builderJewelGive.then(Commands.argument(targets, EntityArgument.players())
                .then(Commands.argument(itemId, ResourceLocationArgument.resourceLocation())
                        .suggests(itemIdSuggestions)
                        .executes(context -> giveJewel(
                                context.getSource(),
                                ResourceLocationArgument.getResourceLocation(context, itemId),
                                EntityArgument.getPlayers(context, targets),
                                randomRarity)
                        )
                        .then(Commands.argument(rarityId, StringArgumentType.string())
                                .suggests(raritySuggestions)
                                .executes(context -> giveJewel(
                                        context.getSource(),
                                        ResourceLocationArgument.getResourceLocation(context, itemId),
                                        EntityArgument.getPlayers(context, targets),
                                        StringArgumentType.getString(context, rarityId).toLowerCase(Locale.ROOT))
                                )
                        )
                )
        );

        builderTestLoot.executes(context -> testLoot(
                context.getSource(),
                allLootTables))
                .then(Commands.argument(lootTableSettings, StringArgumentType.string())
                        .suggests(testLootSuggestions)
                        .executes(context -> testLoot(
                                context.getSource(),
                                StringArgumentType.getString(context, lootTableSettings))
                        )
                );

        dispatcher.register(builderJewelGive);
        dispatcher.register(builderTestLoot);
    }

    private static int giveJewel(CommandSource source, ResourceLocation itemId, Collection<ServerPlayerEntity> targets, String rarity)
    {
        Item item = ForgeRegistries.ITEMS.getValue(itemId);

        if(item == null)
        {
            source.sendErrorMessage(translationTextComponent(jewelGive, "failure.exist", itemId.toString()));
            return 0;
        }

        if(!(item instanceof JewelItem))
        {
            source.sendErrorMessage(translationTextComponent(jewelGive, "failure.item", itemId.toString()));
            return 0;
        }

        ItemStack stack = new ItemStack(item, 1);

        for(ServerPlayerEntity player : targets)
        {
            if(rarity.equals(randomRarity)) rarity = IJewelRarity.getRarityCommand();
            else if(!containsRarity(rarity))
            {
                source.sendErrorMessage(translationTextComponent(jewelGive, "failure.rarity", rarity));
                return 0;
            }

            IJewelNBTHandler.setJewelNBTData(stack, rarity);

            boolean addedToInventory = player.inventory.addItemStackToInventory(stack);
            if(addedToInventory && stack.isEmpty())
            {
                stack.setCount(1);
                ItemEntity entityItem = player.dropItem(stack, false);
                if(entityItem != null)
                {
                    entityItem.makeFakeItem();
                }

                player.world.playSound(
                        null, player.getPosX(), player.getPosY(), player.getPosZ(),
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                        0.2F,
                        ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.container.detectAndSendChanges();
            }
            else
            {
                ItemEntity entityItem = player.dropItem(stack, false);
                if(entityItem != null)
                {
                    entityItem.setNoPickupDelay();
                    entityItem.setOwnerId(player.getUniqueID());
                }
            }
        }

        ITextComponent itemText = stack.getTextComponent();
        if(targets.size() == 1)
        {
            source.sendFeedback(new TranslationTextComponent("commands.give.success.single", 1, itemText, targets.iterator().next().getDisplayName()), true);
        }
        else
        {
            source.sendFeedback(new TranslationTextComponent("commands.give.success.single", 1, itemText, targets.size()), true);
        }

        return targets.size();
    }

    private static int testLoot(CommandSource source, String lootTable)
    {
        String[] def = { jewelTestLoot, "default" };
        String[] looting = { jewelTestLoot, "looting" };
        String[] config1 = { jewelTestLoot, "config1" };

        ITextComponent hostile = translationTextComponent(jewelTestLoot, "hostile");
        ITextComponent boss = translationTextComponent(jewelTestLoot, "boss");
        ITextComponent chest = translationTextComponent(jewelTestLoot, "chest");

        // float hostileDropRate = ModEntityLootTablesBuilder.hostileDropRate;
        // float bossDropRate = ModEntityLootTablesBuilder.bossDropRate;
        // float hostileLootingMultiplier = ModEntityLootTablesBuilder.lootingMultiplier;
        // float bossLootingMultiplier = ModEntityLootTablesBuilder.lootingMultiplier;
        // float chestDropRate = ModChestLootTablesBuilder.chestDropRate;

        // ITextComponent hostileDropRatePercent = translationTextComponent(def, floatToDeci(hostileDropRate * 100)).applyTextStyle(TextFormatting.GREEN);
        // ITextComponent hostileDropRateLooting = translationTextComponent(looting, dropRateWithLootingPercent(hostileDropRate, hostileLootingMultiplier)).applyTextStyle(TextFormatting.GREEN);

        // ITextComponent bossDropRatePercent = translationTextComponent(def, floatToDeci(bossDropRate * 100)).applyTextStyle(TextFormatting.GREEN);
        // ITextComponent bossDropRateLooting = translationTextComponent(looting, dropRateWithLootingPercent(bossDropRate, bossLootingMultiplier)).applyTextStyle(TextFormatting.GREEN);

        // ITextComponent chestDropRatePercent = translationTextComponent(def, floatToDeci(chestDropRate * 100)).applyTextStyle(TextFormatting.GREEN);

        ITextComponent hostileDropRatePercent = null;
        ITextComponent hostileDropRateLooting = null;

        ITextComponent bossDropRatePercent = null;
        ITextComponent bossDropRateLooting = null;

        ITextComponent chestDropRatePercent = null;

        ITextComponent oneRarityDropTrue = translationTextComponent(config1, "'oneRarityDrop'", "" + true).applyTextStyle(TextFormatting.YELLOW);
        ITextComponent oneRarityDropFalse = translationTextComponent(config1, "'oneRarityDrop'", "" + false).applyTextStyle(TextFormatting.YELLOW);
        ITextComponent chestDisable = translationTextComponent(config1, "'jewelsInChest'", "" + false).applyTextStyle(TextFormatting.YELLOW);

        float legendaryRate = (float) MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
        float epicRate = (float) MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();
        float rareRate = (float) MagicalJewelryConfigBuilder.JEWEL_RARE_DROP_RATE.get();
        float uncommonRate = 100 - legendaryRate - epicRate - rareRate;

        if(!Arrays.asList(testLootTables).contains(lootTable))
        {
            source.sendErrorMessage(translationTextComponent(jewelTestLoot, "failure", lootTable));
        }
        if(lootTable.equals(hostileLootDrop) || lootTable.equals(allLootTables))
        {
            source.sendFeedback(hostile, true);

            if(MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get())
            {
                oneRarityDrop(source, hostileDropRatePercent, hostileDropRateLooting, oneRarityDropTrue);
            }
            else
            {
                defaultRarityDrop(source, hostileDropRatePercent, hostileDropRateLooting, oneRarityDropFalse, legendaryRate, epicRate, rareRate, uncommonRate);
            }
        }
        if(lootTable.equals(bossLootDrop) || lootTable.equals(allLootTables))
        {
            source.sendFeedback(boss, true);

            if(MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get())
            {
                oneRarityDrop(source, bossDropRatePercent, bossDropRateLooting, oneRarityDropTrue);
            }
            else
            {
                source.sendFeedback(oneRarityDropFalse, true);
                if(bossDropRatePercent != null) source.sendFeedback(bossDropRatePercent, true);
                if(bossDropRateLooting != null) source.sendFeedback(bossDropRateLooting, true);

                float totalDropRate = legendaryRate + epicRate;
                float legendaryDropRate = legendaryRate / totalDropRate * 100;
                float epicDropRate = epicRate / totalDropRate * 100;

                source.sendFeedback(new StringTextComponent(LEGENDARY.getDisplayName() + ": " + floatToDeci(legendaryDropRate)).applyTextStyle(LEGENDARY.getFormat()), true);
                source.sendFeedback(new StringTextComponent(EPIC.getDisplayName() + ": " + floatToDeci(epicDropRate)).applyTextStyle(EPIC.getFormat()), true);
            }
        }
        if(lootTable.equals(chestLootDrop) || lootTable.equals(allLootTables))
        {
            source.sendFeedback(chest, true);

            if(!MagicalJewelryConfigBuilder.JEWELS_IN_CHESTS.get())
            {
                source.sendFeedback(chestDisable, true);
            }
            else if(MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get())
            {
                oneRarityDrop(source, chestDropRatePercent, null, oneRarityDropTrue);
            }
            else
            {
                defaultRarityDrop(source, chestDropRatePercent, null, oneRarityDropFalse, legendaryRate, epicRate, rareRate, uncommonRate);
            }
        }

        return 0;
    }

    private static void oneRarityDrop(CommandSource source, @Nullable ITextComponent dropRatePercent, @Nullable ITextComponent dropRateLooting, ITextComponent oneRarityDropTrue)
    {
        source.sendFeedback(oneRarityDropTrue, true);
        if(dropRatePercent != null) source.sendFeedback(dropRatePercent, true);
        if(dropRateLooting != null) source.sendFeedback(dropRateLooting, true);
        JewelRarity rarity = MagicalJewelryConfigBuilder.JEWEL_RARITY_TO_DROP.get();
        source.sendFeedback(oneRarity(rarity), true);
    }

    private static void defaultRarityDrop(CommandSource source, @Nullable ITextComponent dropRatePercent, @Nullable ITextComponent dropRateLooting, ITextComponent oneRarityDropFalse, float legendaryRate, float epicRate, float rareRate, float uncommonDropRate)
    {
        source.sendFeedback(oneRarityDropFalse, true);
        if(dropRatePercent != null) source.sendFeedback(dropRatePercent, true);
        if(dropRateLooting != null) source.sendFeedback(dropRateLooting, true);
        source.sendFeedback(new StringTextComponent(LEGENDARY.getDisplayName() + ": " + floatToDeci(legendaryRate)).applyTextStyle(LEGENDARY.getFormat()), true);
        source.sendFeedback(new StringTextComponent(EPIC.getDisplayName() + ": " + floatToDeci(epicRate)).applyTextStyle(EPIC.getFormat()), true);
        source.sendFeedback(new StringTextComponent(RARE.getDisplayName() + ": " + floatToDeci(rareRate)).applyTextStyle(RARE.getFormat()), true);
        source.sendFeedback(new StringTextComponent(UNCOMMON.getDisplayName() + ": " + floatToDeci(uncommonDropRate)).applyTextStyle(UNCOMMON.getFormat()), true);
    }

    private static String dropRateWithLootingPercent(float dropRate, float lootingMultiplier)
    {
        if(dropRate < 0.0001F && lootingMultiplier < 0.0001F) return "<0,01%";
        else if(dropRate < 0.0001F) return "~" + String.format("%.2f", (3 * lootingMultiplier) * 100) + "%";
        else if(lootingMultiplier < 0.0001F) return "~" + String.format("%.2f", dropRate * 100) + "%";
        else return String.format("%.2f", (dropRate + (3 * lootingMultiplier)) * 100) + "%";
    }

    private static String floatToDeci(float dropRate)
    {
        if(dropRate < 0.01F) return "<0,01%";
        else return String.format("%.2f", dropRate) + "%";
    }

    private static ITextComponent oneRarity(JewelRarity rarityIn)
    {
        return translationTextComponent(jewelTestLoot, "onerarity", rarityIn.getDisplayName()).applyTextStyle(rarityIn.getFormat());
    }

    private static TranslationTextComponent translationTextComponent(String[] strings, String stringIn)
    {
        return translationTextComponent(strings, stringIn, null);
    }

    private static TranslationTextComponent translationTextComponent(String[] strings, String string1In, String string2In)
    {
        return translationTextComponent(strings, string1In, string2In, null);
    }

    private static TranslationTextComponent translationTextComponent(String[] strings, String string1In, String string2In, String string3In)
    {
        return translationTextComponent(strings[0], strings[1], string1In, string2In, string3In);
    }

    private static TranslationTextComponent translationTextComponent(String type, String nameIn)
    {
        return translationTextComponent(type, nameIn, null);
    }

    private static TranslationTextComponent translationTextComponent(String type, String nameIn, String stringIn)
    {
        return translationTextComponent(type, nameIn, stringIn, null, null);
    }

    private static TranslationTextComponent translationTextComponent(String type, String nameIn, @Nullable String string1In, @Nullable String string2In, @Nullable String string3In)
    {
        return new TranslationTextComponent("commands." + MagicalJewelry.MOD_ID + "." + type + "." + nameIn, string1In, string2In, string3In);
    }
}