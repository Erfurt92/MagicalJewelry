package com.erfurt.magicaljewelry;

import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.init.LootInit;
import com.erfurt.magicaljewelry.loot.JewelModifier;
import com.erfurt.magicaljewelry.recipes.JewelUpgradeRecipe;
import com.erfurt.magicaljewelry.recipes.UpgradeNBTIngredient;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfig;
import com.erfurt.magicaljewelry.util.handlers.ModColorHandler;
import com.erfurt.magicaljewelry.util.interfaces.IJewelAttributes;
import com.erfurt.magicaljewelry.util.interfaces.IJewelEffects;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

public class SideProxy
{
    SideProxy()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        modLoadingContext.registerConfig(ModConfig.Type.COMMON, MagicalJewelryConfig.COMMON_CONFIG);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, MagicalJewelryConfig.CLIENT_CONFIG);
        IJewelEffects.init();
        IJewelAttributes.init();
        LootInit.init();

        ItemInit.ITEMS.register(modEventBus);

        modEventBus.addGenericListener(GlobalLootModifierSerializer.class, SideProxy::lootModifierRegistries);
        modEventBus.addGenericListener(IRecipeSerializer.class, SideProxy::registerRecipeSerializers);
        modEventBus.addListener(SideProxy::enqueue);
    }

    private static void enqueue(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace").size(1).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ring").size(2).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("bracelet").size(2).build());
        MagicalJewelry.LOGGER.info("enqueue method registered.");
    }

    private static void lootModifierRegistries(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event)
    {
        event.getRegistry().register(new JewelModifier.Serializer().setRegistryName(MagicalJewelry.getId("jewel_modifier")));
        MagicalJewelry.LOGGER.info("lootModifierRegistries method registered.");
    }

    public static void registerRecipeSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        CraftingHelper.register(MagicalJewelry.getId("upgrade"), UpgradeNBTIngredient.Serializer.INSTANCE);
        IForgeRegistry<IRecipeSerializer<?>> r = event.getRegistry();

        r.registerAll(JewelUpgradeRecipe.Serializer.SERIALIZER);
        MagicalJewelry.LOGGER.info("registerRecipeSerializers method registered.");
    }

    static class Client extends SideProxy
    {
        Client()
        {
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

            modEventBus.addListener(Client::clientSetup);
            modEventBus.addListener(ModColorHandler::registerItemColor);
        }

        private static void clientSetup(FMLClientSetupEvent event) { }
    }

    static class Server extends SideProxy
    {
        Server()
        {
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

            modEventBus.addListener(Server::serverSetup);
        }

        private static void serverSetup(FMLDedicatedServerSetupEvent event) { }
    }
}