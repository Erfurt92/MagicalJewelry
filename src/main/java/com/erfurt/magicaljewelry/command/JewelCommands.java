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
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static com.erfurt.magicaljewelry.util.LootTableUtil.getChance;
import static com.erfurt.magicaljewelry.util.LootTableUtil.getLootingMultiplier;
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
    private static final String allLootTables = "all_jewel_drops";
    private static final String chestHostileDrop = "chests_hostile_jewel_drops";

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
            chestHostileDrop,
            allLootTables
    };

    private static final SuggestionProvider<CommandSourceStack> itemIdSuggestions =
            (context, builder) -> SharedSuggestionProvider.suggestResource(ForgeRegistries.ITEMS.getValues().stream()
                    .filter(item -> item instanceof JewelItem).map(ForgeRegistryEntry::getRegistryName), builder);
    private static final SuggestionProvider<CommandSourceStack> raritySuggestions =
            (context, builder) -> SharedSuggestionProvider.suggest(rarities, builder);
    private static final SuggestionProvider<CommandSourceStack> testLootSuggestions =
            (context, builder) -> SharedSuggestionProvider.suggest(testLootTables, builder);

    private JewelCommands() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> builderJewelGive = Commands.literal(jewelGive)
                .requires(source -> source.hasPermission(2));

        LiteralArgumentBuilder<CommandSourceStack> builderTestLoot = Commands.literal(jewelTestLoot);

        builderJewelGive.then(Commands.argument(targets, EntityArgument.players())
                .then(Commands.argument(itemId, ResourceLocationArgument.id())
                        .suggests(itemIdSuggestions)
                                .executes(context -> giveJewel(
                                        context.getSource(),
                                        ResourceLocationArgument.getId(context, itemId),
                                        EntityArgument.getPlayers(context, targets),
                                        randomRarity)
                                )
                        .then(Commands.argument(rarityId, StringArgumentType.string())
                                .suggests(raritySuggestions)
                                .executes(context -> giveJewel(
                                        context.getSource(),
                                        ResourceLocationArgument.getId(context, itemId),
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

    private static int giveJewel(CommandSourceStack source, ResourceLocation itemId, Collection<ServerPlayer> targets, String rarity)
    {
        Item item = ForgeRegistries.ITEMS.getValue(itemId);

        if(item == null)
        {
            source.sendFailure(TranslatableComponent(jewelGive, "failure.exist", itemId.toString()));
            return 0;
        }

        if(!(item instanceof JewelItem))
        {
            source.sendFailure(TranslatableComponent(jewelGive, "failure.item", itemId.toString()));
            return 0;
        }

        ItemStack stack = new ItemStack(item, 1);

        for(ServerPlayer player : targets)
        {
            if(rarity.equals(randomRarity)) rarity = IJewelRarity.getRarityCommand();
            else if(!containsRarity(rarity))
            {
                source.sendFailure(TranslatableComponent(jewelGive, "failure.rarity", rarity));
                return 0;
            }

            IJewelNBTHandler.setJewelNBTData(stack, rarity);

            boolean addedToInventory = player.getInventory().add(stack);
            if(addedToInventory && stack.isEmpty())
            {
                stack.setCount(1);
                ItemEntity entityItem = player.drop(stack, false);
                if(entityItem != null)
                {
                    entityItem.makeFakeItem();
                }

                player.level.playSound(
                        null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
                        0.2F,
                        ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.containerMenu.broadcastChanges();
            }
            else
            {
                ItemEntity entityItem = player.drop(stack, false);
                if(entityItem != null)
                {
                    entityItem.setNoPickUpDelay();
                    entityItem.setOwner(player.getUUID());
                }
            }
        }

        MutableComponent itemText = stack.getDisplayName().copy().withStyle(JewelRarity.byName(rarity).getFormat());

        if(targets.size() == 1)
        {
            source.sendSuccess(new TranslatableComponent("commands.give.success.single", 1, itemText, targets.iterator().next().getDisplayName()), true);
        }
        else
        {
            source.sendSuccess(new TranslatableComponent("commands.give.success.single", 1, itemText, targets.size()), true);
        }

        return targets.size();
    }

    private static int testLoot(CommandSourceStack source, String lootTable)
    {
        MinecraftServer server = source.getServer();

        String[] settings = { jewelTestLoot, "settings"};
        String[] droprate = { jewelTestLoot, "droprate" };
        String[] dropratewith = { jewelTestLoot, "dropratewith" };
        String[] config1 = { jewelTestLoot, "config1" };
        String[] config2 = { jewelTestLoot, "config2" };

        String looting = Enchantments.MOB_LOOTING.getFullname(3).getString();

        MutableComponent typeHostile = TranslatableComponent(jewelTestLoot, "type.hostile");
        MutableComponent typeBoss = TranslatableComponent(jewelTestLoot, "type.boss");
        MutableComponent typeChest = TranslatableComponent(jewelTestLoot, "type.chest");

        MutableComponent hostile = TranslatableComponent(settings, typeHostile.getString());
        MutableComponent chest = TranslatableComponent(settings, typeChest.getString());
        MutableComponent boss = TranslatableComponent(settings, typeBoss.getString());
        MutableComponent chestHostile = TranslatableComponent(settings, typeChest.getString() + " & " + typeHostile.getString());

        float hostileDropRate = getChance((MagicalJewelry.getId("entities/" + hostileLootDrop)), server);
        float hostileLootingMultiplier = getLootingMultiplier((MagicalJewelry.getId("entities/" + hostileLootDrop)), server);
        float bossDropRate = getChance((MagicalJewelry.getId("entities/" + bossLootDrop)), server);
        float bossLootingMultiplier = getLootingMultiplier((MagicalJewelry.getId("entities/" + bossLootDrop)), server);
        float chestDropRate = getChance((MagicalJewelry.getId("inject/chests/" + chestLootDrop)), server);

        MutableComponent hostileDropRatePercent = null;
        MutableComponent hostileDropRateLooting = null;

        MutableComponent bossDropRatePercent = null;
        MutableComponent bossDropRateLooting = null;

        MutableComponent chestDropRatePercent = null;
        
        if(hostileLootingMultiplier >= 0F && hostileDropRate >= 0F)
        {
            hostileDropRatePercent = new TextComponent(typeHostile.getString() + " - ").append(TranslatableComponent(droprate, floatToDeci(hostileDropRate * 100))).withStyle(ChatFormatting.GREEN);
            hostileDropRateLooting = new TextComponent(typeHostile.getString() + " - ").append(TranslatableComponent(dropratewith, looting, dropRateWithLootingPercent(hostileDropRate, hostileLootingMultiplier))).withStyle(ChatFormatting.GREEN);
        }
        else if(hostileDropRate >= 0F)
        {
            hostileDropRatePercent = new TextComponent(typeHostile.getString() + " - ").append(TranslatableComponent(droprate, floatToDeci(hostileDropRate * 100))).withStyle(ChatFormatting.GREEN);
        }

        if(bossLootingMultiplier >= 0F && bossDropRate >= 0F)
        {
            bossDropRatePercent = new TextComponent(typeBoss.getString() + " - ").append(TranslatableComponent(droprate, floatToDeci(bossDropRate * 100))).withStyle(ChatFormatting.GREEN);
            bossDropRateLooting = new TextComponent(typeBoss.getString() + " - ").append(TranslatableComponent(dropratewith, looting, dropRateWithLootingPercent(bossDropRate, bossLootingMultiplier))).withStyle(ChatFormatting.GREEN);
        }
        else if(bossDropRate >= 0F)
        {
            bossDropRatePercent = new TextComponent(typeBoss.getString() + " - ").append(TranslatableComponent(droprate, floatToDeci(bossDropRate * 100))).withStyle(ChatFormatting.GREEN);
        }

        if(chestDropRate >= 0F)
        {
            chestDropRatePercent = new TextComponent(typeChest.getString() + " - ").append(TranslatableComponent(droprate, floatToDeci(chestDropRate * 100))).withStyle(ChatFormatting.GREEN);
        }
        
        MutableComponent oneRarityDrop = TranslatableComponent(config1, "'oneRarityDrop'", "" + true).withStyle(ChatFormatting.YELLOW);
        MutableComponent legendaryUpgradeOnly = TranslatableComponent(config1, "'legendaryUpgradeOnly'", "" + true).withStyle(ChatFormatting.YELLOW);
        MutableComponent bothConfigOptions = TranslatableComponent(config2, "'legendaryUpgradeOnly'", "'oneRarityDrop'", "" + false).withStyle(ChatFormatting.YELLOW);
        MutableComponent chestDisable = TranslatableComponent(config1, "'jewelsInChest'", "" + false).withStyle(ChatFormatting.YELLOW);

        float legendaryRate = (float) MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_DROP_RATE.get();
        float epicRate = (float) MagicalJewelryConfigBuilder.JEWEL_EPIC_DROP_RATE.get();
        float rareRate = (float) MagicalJewelryConfigBuilder.JEWEL_RARE_DROP_RATE.get();
        float uncommonRate = 100 - legendaryRate - epicRate - rareRate;

        if(!Arrays.asList(testLootTables).contains(lootTable))
        {
            source.sendFailure(TranslatableComponent(jewelTestLoot, "failure", lootTable));
        }
        if(lootTable.equals(hostileLootDrop) || lootTable.equals(chestLootDrop) || lootTable.equals(chestHostileDrop) || lootTable.equals(allLootTables))
        {
            if(lootTable.equals(hostileLootDrop))
            {
                source.sendSuccess(hostile, true);
                chestDropRatePercent = null;
            }
            else if(lootTable.equals(chestLootDrop))
            {
                source.sendSuccess(chest, true);
                hostileDropRatePercent = null;
                hostileDropRateLooting = null;
            }
            else
            {
                source.sendSuccess(chestHostile, true);
                if(!MagicalJewelryConfigBuilder.JEWELS_IN_CHESTS.get())
                {
                    chestDropRatePercent = chestDisable;
                }
            }

            if(lootTable.equals(chestLootDrop) && !MagicalJewelryConfigBuilder.JEWELS_IN_CHESTS.get())
            {
                source.sendSuccess(chestDisable, false);
            }
            else if(MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get())
            {
                oneRarityDrop(source, hostileDropRatePercent, hostileDropRateLooting, chestDropRatePercent, oneRarityDrop);
            }
            else if(MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_UPGRADE_ONLY.get())
            {
                legendaryUpgradeRarityDrop(source, hostileDropRatePercent, hostileDropRateLooting, chestDropRatePercent, legendaryUpgradeOnly, legendaryRate, epicRate, rareRate);
            }
            else
            {
                defaultRarityDrop(source, hostileDropRatePercent, hostileDropRateLooting, chestDropRatePercent, bothConfigOptions, legendaryRate, epicRate, rareRate, uncommonRate);
            }
        }
        if(lootTable.equals(bossLootDrop) || lootTable.equals(allLootTables))
        {
            source.sendSuccess(boss, true);

            if(MagicalJewelryConfigBuilder.JEWEL_ONE_RARITY_DROP.get())
            {
                oneRarityDrop(source, bossDropRatePercent, bossDropRateLooting, null, oneRarityDrop);
            }
            else if(MagicalJewelryConfigBuilder.JEWEL_LEGENDARY_UPGRADE_ONLY.get())
            {
                source.sendSuccess(legendaryUpgradeOnly, false);
                if(bossDropRatePercent != null) source.sendSuccess(bossDropRatePercent, false);
                if(bossDropRateLooting != null) source.sendSuccess(bossDropRateLooting, false);
                source.sendSuccess(oneRarity(EPIC), false);
            }
            else
            {
                source.sendSuccess(bothConfigOptions, false);
                if(bossDropRatePercent != null) source.sendSuccess(bossDropRatePercent, false);
                if(bossDropRateLooting != null) source.sendSuccess(bossDropRateLooting, false);

                float totalDropRate = legendaryRate + epicRate;
                float legendaryDropRate = legendaryRate / totalDropRate * 100;
                float epicDropRate = epicRate / totalDropRate * 100;

                source.sendSuccess(new TextComponent(LEGENDARY.getDisplayName() + ": " + floatToDeci(legendaryDropRate)).withStyle(LEGENDARY.getFormat()), false);
                source.sendSuccess(new TextComponent(EPIC.getDisplayName() + ": " + floatToDeci(epicDropRate)).withStyle(EPIC.getFormat()), false);
            }
        }

        return 0;
    }

    private static void oneRarityDrop(CommandSourceStack source, @Nullable MutableComponent dropRatePercent, @Nullable MutableComponent dropRateLooting, @Nullable MutableComponent chestDropRatePercent, MutableComponent oneRarityDrop)
    {
        source.sendSuccess(oneRarityDrop, false);
        if(chestDropRatePercent != null) source.sendSuccess(chestDropRatePercent, false);
        if(dropRatePercent != null) source.sendSuccess(dropRatePercent, false);
        if(dropRateLooting != null) source.sendSuccess(dropRateLooting, false);
        JewelRarity rarity = MagicalJewelryConfigBuilder.JEWEL_RARITY_TO_DROP.get();
        source.sendSuccess(oneRarity(rarity), false);
    }

    private static void legendaryUpgradeRarityDrop(CommandSourceStack source, @Nullable MutableComponent dropRatePercent, @Nullable MutableComponent dropRateLooting, @Nullable MutableComponent chestDropRatePercent, MutableComponent legendaryUpgradeOnly, float legendaryRate, float epicRate, float rareRate)
    {
        float totalDropRate = 100 - legendaryRate;
        float epicDropRate = epicRate / totalDropRate * 100;
        float rareDropRate = rareRate / totalDropRate * 100;
        float uncommonDropRate = 100 - epicDropRate - rareDropRate;

        source.sendSuccess(legendaryUpgradeOnly, false);
        if(chestDropRatePercent != null) source.sendSuccess(chestDropRatePercent, false);
        if(dropRatePercent != null) source.sendSuccess(dropRatePercent, false);
        if(dropRateLooting != null) source.sendSuccess(dropRateLooting, false);
        source.sendSuccess(new TextComponent(EPIC.getDisplayName() + ": " + floatToDeci(epicDropRate)).withStyle(EPIC.getFormat()), false);
        source.sendSuccess(new TextComponent(RARE.getDisplayName() + ": " + floatToDeci(rareDropRate)).withStyle(RARE.getFormat()), false);
        source.sendSuccess(new TextComponent(UNCOMMON.getDisplayName() + ": " + floatToDeci(uncommonDropRate)).withStyle(UNCOMMON.getFormat()), false);
    }

    private static void defaultRarityDrop(CommandSourceStack source, @Nullable MutableComponent dropRatePercent, @Nullable MutableComponent dropRateLooting, @Nullable MutableComponent chestDropRatePercent, MutableComponent bothConfigOptions, float legendaryRate, float epicRate, float rareRate, float uncommonDropRate)
    {
        source.sendSuccess(bothConfigOptions, false);
        if(chestDropRatePercent != null) source.sendSuccess(chestDropRatePercent, false);
        if(dropRatePercent != null) source.sendSuccess(dropRatePercent, false);
        if(dropRateLooting != null) source.sendSuccess(dropRateLooting, false);
        source.sendSuccess(new TextComponent(LEGENDARY.getDisplayName() + ": " + floatToDeci(legendaryRate)).withStyle(LEGENDARY.getFormat()), false);
        source.sendSuccess(new TextComponent(EPIC.getDisplayName() + ": " + floatToDeci(epicRate)).withStyle(EPIC.getFormat()), false);
        source.sendSuccess(new TextComponent(RARE.getDisplayName() + ": " + floatToDeci(rareRate)).withStyle(RARE.getFormat()), false);
        source.sendSuccess(new TextComponent(UNCOMMON.getDisplayName() + ": " + floatToDeci(uncommonDropRate)).withStyle(UNCOMMON.getFormat()), false);
    }

    private static String dropRateWithLootingPercent(float dropRate, float lootingMultiplier)
    {
        if(dropRate <= 0.0F && lootingMultiplier <= 0.0F) return "0,0F";
        else if(dropRate < 0.0001F && dropRate != 0.0F && lootingMultiplier < 0.0001F && lootingMultiplier != 0.0F) return "<0,01%";
        else if(dropRate < 0.0001F && dropRate != 0.0F) return "~" + String.format("%.2f", (3 * lootingMultiplier) * 100) + "%";
        else if(lootingMultiplier < 0.0001F && lootingMultiplier != 0.0F) return "~" + String.format("%.2f", dropRate * 100) + "%";
        else
        {
            if(((dropRate + (3 * lootingMultiplier)) * 100) >= 100) return "100,00%";
            else return String.format("%.2f", (dropRate + (3 * lootingMultiplier)) * 100) + "%";
        }
    }

    private static String floatToDeci(float dropRate)
    {
        if(dropRate <= 0.0F) return "0,00%";
        else if(dropRate < 0.01F) return "<0,01%";
        else if(dropRate > 100F) return "100,00%";
        else return String.format("%.2f", dropRate) + "%";
    }

    private static MutableComponent oneRarity(JewelRarity rarityIn)
    {
        return TranslatableComponent(jewelTestLoot, "onerarity", rarityIn.getDisplayName()).withStyle(rarityIn.getFormat());
    }

    private static TranslatableComponent TranslatableComponent(String[] strings, String stringIn)
    {
        return TranslatableComponent(strings, stringIn, null);
    }

    private static TranslatableComponent TranslatableComponent(String[] strings, String string1In, String string2In)
    {
        return TranslatableComponent(strings, string1In, string2In, null);
    }

    private static TranslatableComponent TranslatableComponent(String[] strings, String string1In, String string2In, String string3In)
    {
        return TranslatableComponent(strings[0], strings[1], string1In, string2In, string3In);
    }

    private static TranslatableComponent TranslatableComponent(String type, String nameIn)
    {
        return TranslatableComponent(type, nameIn, null);
    }

    private static TranslatableComponent TranslatableComponent(String type, String nameIn, String stringIn)
    {
        return TranslatableComponent(type, nameIn, stringIn, null, null);
    }

    private static TranslatableComponent TranslatableComponent(String type, String nameIn, @Nullable String string1In, @Nullable String string2In, @Nullable String string3In)
    {
        return new TranslatableComponent("commands." + MagicalJewelry.MOD_ID + "." + type + "." + nameIn, string1In, string2In, string3In);
    }
}