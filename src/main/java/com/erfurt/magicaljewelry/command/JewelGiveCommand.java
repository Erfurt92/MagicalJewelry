package com.erfurt.magicaljewelry.command;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.data.loot.ModEntityLootTablesBuilder;
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
import java.util.Collection;
import java.util.Locale;

import static com.erfurt.magicaljewelry.util.enums.JewelRarity.*;

public final class JewelGiveCommand implements IJewelNBTHandler
{
    private static final String jewelTestLoot = "jewelTestLoot";
    private static final String jewelGive = "jewelGive";
    private static final String targets = "targets";
    private static final String itemId = "itemId";
    private static final String rarityId = "rarity";
    private static final String randomRarity = "random";
    private static final String lootTableSettings = "lootTable";
    private static final String hostileLootDrop = "hostileLootTable";
    private static final String bossLootDrop = "bossLootTable";
    private static final String bothLootDrop = "bothLootTable";

    private static final String[] rarities = {
            UNCOMMON.getName(),
            RARE.getName(),
            EPIC.getName(),
            LEGENDARY.getName()
    };

    private static final String[] testLootTables = {
            hostileLootDrop,
            bossLootDrop,
            bothLootDrop
    };

    private static final SuggestionProvider<CommandSource> itemIdSuggestions =
            (context, builder) -> ISuggestionProvider.func_212476_a(ForgeRegistries.ITEMS.getValues().stream()
                    .filter(item -> item instanceof JewelItem).map(ForgeRegistryEntry::getRegistryName), builder);
    private static final SuggestionProvider<CommandSource> raritySuggestions =
            (context, builder) -> ISuggestionProvider.suggest(rarities, builder);
    private static final SuggestionProvider<CommandSource> testLootSuggestions =
            (context, builder) -> ISuggestionProvider.suggest(testLootTables, builder);

    private JewelGiveCommand() {}

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
                bothLootDrop))
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

    private static int testLoot(CommandSource source, String testLootTables)
    {
        float hostileDropRate = ModEntityLootTablesBuilder.hostileDropRate;
        float bossDropRate = ModEntityLootTablesBuilder.bossDropRate;
        float lootingMultiplier = ModEntityLootTablesBuilder.lootingMultiplier;

        IFormattableTextComponent hostile = translationTextComponent(jewelTestLoot, "hostile");
        IFormattableTextComponent boss = translationTextComponent(jewelTestLoot, "boss");

        IFormattableTextComponent hostileDropRatePercent = translationTextComponent(jewelTestLoot, "default", floatToDeci(hostileDropRate * 100)).mergeStyle(TextFormatting.GREEN);
        IFormattableTextComponent hostileDropRateLooting = translationTextComponent(jewelTestLoot, "looting", floatToDeci(dropRateWithLootingPercent(hostileDropRate, lootingMultiplier))).mergeStyle(TextFormatting.GREEN);

        IFormattableTextComponent bossDropRatePercent = translationTextComponent(jewelTestLoot, "default", floatToDeci(bossDropRate * 100)).mergeStyle(TextFormatting.GREEN);
        IFormattableTextComponent bossDropRateLooting = translationTextComponent(jewelTestLoot, "looting", floatToDeci(dropRateWithLootingPercent(bossDropRate, lootingMultiplier))).mergeStyle(TextFormatting.GREEN);

        IFormattableTextComponent oneRarityDrop = translationTextComponent(jewelTestLoot, "config1", "'oneRarityDrop'", "" + true).mergeStyle(TextFormatting.YELLOW);
        IFormattableTextComponent legendaryUpgradeOnly = translationTextComponent(jewelTestLoot, "config1", "'legendaryUpgradeOnly'", "" + true).mergeStyle(TextFormatting.YELLOW);
        IFormattableTextComponent bothConfigOptions = translationTextComponent(jewelTestLoot, "config2", "'legendaryUpgradeOnly'", "'oneRarityDrop'", "" + false).mergeStyle(TextFormatting.YELLOW);

        float legendaryRate = (float) MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
        float epicRate = (float) MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();
        float rareRate = (float) MagicalJewelryConfigBuilder.JEWEL_RARE_DROP_RATE.get();

        if(!testLootTables.equals(hostileLootDrop) && !testLootTables.equals(bossLootDrop) && !testLootTables.equals(bothLootDrop))
        {
            source.sendErrorMessage(translationTextComponent(jewelTestLoot, "failure", testLootTables));
        }
        if(testLootTables.equals(hostileLootDrop) || testLootTables.equals(bothLootDrop))
        {
            source.sendFeedback(hostile, true);

            if(MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get())
            {
                source.sendFeedback(oneRarityDrop, true);
                source.sendFeedback(hostileDropRatePercent, true);
                source.sendFeedback(hostileDropRateLooting, true);
                JewelRarity rarity = MagicalJewelryConfigBuilder.JEWEL_RARITY_TO_DROP.get();
                source.sendFeedback(oneRarity(rarity), true);
            }
            else if(MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_UPGRADE_ONLY.get())
            {
                float totalDropRate = 100 - legendaryRate;
                float epicDropRate = epicRate / totalDropRate * 100;
                float rareDropRate = rareRate / totalDropRate * 100;
                float uncommonDropRate = 100 - epicDropRate - rareDropRate;

                source.sendFeedback(legendaryUpgradeOnly, true);
                source.sendFeedback(hostileDropRatePercent, true);
                source.sendFeedback(hostileDropRateLooting, true);
                source.sendFeedback(new StringTextComponent(EPIC.getDisplayName() + ": " + floatToDeci(epicDropRate)).mergeStyle(EPIC.getFormat()), true);
                source.sendFeedback(new StringTextComponent(RARE.getDisplayName() + ": " + floatToDeci(rareDropRate)).mergeStyle(RARE.getFormat()), true);
                source.sendFeedback(new StringTextComponent(UNCOMMON.getDisplayName() + ": " + floatToDeci(uncommonDropRate)).mergeStyle(UNCOMMON.getFormat()), true);
            }
            else
            {
                float uncommonDropRate = 100 - legendaryRate - epicRate - rareRate;

                source.sendFeedback(bothConfigOptions, true);
                source.sendFeedback(hostileDropRatePercent, true);
                source.sendFeedback(hostileDropRateLooting, true);
                source.sendFeedback(new StringTextComponent(LEGENDARY.getDisplayName() + ": " + floatToDeci(legendaryRate)).mergeStyle(LEGENDARY.getFormat()), true);
                source.sendFeedback(new StringTextComponent(EPIC.getDisplayName() + ": " + floatToDeci(epicRate)).mergeStyle(EPIC.getFormat()), true);
                source.sendFeedback(new StringTextComponent(RARE.getDisplayName() + ": " + floatToDeci(rareRate)).mergeStyle(RARE.getFormat()), true);
                source.sendFeedback(new StringTextComponent(UNCOMMON.getDisplayName() + ": " + floatToDeci(uncommonDropRate)).mergeStyle(UNCOMMON.getFormat()), true);
            }
        }
        if(testLootTables.equals(bossLootDrop) || testLootTables.equals(bothLootDrop))
        {
            source.sendFeedback(boss, true);

            if(MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get())
            {
                source.sendFeedback(oneRarityDrop, true);
                source.sendFeedback(bossDropRatePercent, true);
                source.sendFeedback(bossDropRateLooting, true);
                JewelRarity rarity = MagicalJewelryConfigBuilder.JEWEL_RARITY_TO_DROP.get();
                source.sendFeedback(oneRarity(rarity), true);
            }
            else if(MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_UPGRADE_ONLY.get())
            {
                source.sendFeedback(legendaryUpgradeOnly, true);
                source.sendFeedback(bossDropRatePercent, true);
                source.sendFeedback(bossDropRateLooting, true);
                source.sendFeedback(oneRarity(EPIC), true);
            }
            else
            {
                source.sendFeedback(bothConfigOptions, true);
                source.sendFeedback(bossDropRatePercent, true);
                source.sendFeedback(bossDropRateLooting, true);

                float totalDropRate = legendaryRate + epicRate;
                float legendaryDropRate = legendaryRate / totalDropRate * 100;
                float epicDropRate = epicRate / totalDropRate * 100;

                source.sendFeedback(new StringTextComponent(LEGENDARY.getDisplayName() + ": " + floatToDeci(legendaryDropRate)).mergeStyle(LEGENDARY.getFormat()), true);
                source.sendFeedback(new StringTextComponent(EPIC.getDisplayName() + ": " + floatToDeci(epicDropRate)).mergeStyle(EPIC.getFormat()), true);
            }
        }

        return 0;
    }

    private static float dropRateWithLootingPercent(float dropRate, float lootingMultiplier)
    {
        return (dropRate + (3 * lootingMultiplier)) * 100;
    }

    private static String floatToDeci(float floatIn)
    {
        return String.format("%.2f", floatIn) + "%";
    }

    private static IFormattableTextComponent oneRarity(JewelRarity rarityIn)
    {
        return translationTextComponent(jewelTestLoot, "onerarity", rarityIn.getDisplayName()).mergeStyle(rarityIn.getFormat());
    }

    private static TranslationTextComponent translationTextComponent(String type, String nameIn)
    {
        return translationTextComponent(type, nameIn, null);
    }

    private static TranslationTextComponent translationTextComponent(String type, String nameIn, String stringIn)
    {
        return translationTextComponent(type, nameIn, stringIn, null);
    }

    private static TranslationTextComponent translationTextComponent(String type, String nameIn, String string1In, String string2In)
    {
        return translationTextComponent(type, nameIn, string1In, string2In, null);
    }

    private static TranslationTextComponent translationTextComponent(String type, String nameIn, @Nullable String string1In, @Nullable String string2In, @Nullable String string3In)
    {
        return new TranslationTextComponent("commands." + MagicalJewelry.MOD_ID + "." + type.toLowerCase(Locale.ROOT) + "." + nameIn, string1In, string2In, string3In);
    }
}