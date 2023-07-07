package com.erfurt.magicaljewelry;

import com.erfurt.magicaljewelry.capability.JewelItemCapability;
import com.erfurt.magicaljewelry.command.JewelCommands;
import com.erfurt.magicaljewelry.init.CreativeTabInit;
import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.init.LootInit;
import com.erfurt.magicaljewelry.init.RecipeInit;
import com.erfurt.magicaljewelry.objects.items.JewelAmuletItem;
import com.erfurt.magicaljewelry.recipes.UpgradeNBTIngredient;
import com.erfurt.magicaljewelry.render.JewelLayerDefinitions;
import com.erfurt.magicaljewelry.render.model.JewelAmuletModel;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfig;
import com.erfurt.magicaljewelry.util.handlers.ModColorHandler;
import com.erfurt.magicaljewelry.util.interfaces.IJewelAttributes;
import com.erfurt.magicaljewelry.util.interfaces.IJewelEffects;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import static com.erfurt.magicaljewelry.MagicalJewelry.getId;

public class SideProxy
{
    SideProxy()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        modLoadingContext.registerConfig(ModConfig.Type.COMMON, MagicalJewelryConfig.COMMON_CONFIG);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, MagicalJewelryConfig.CLIENT_CONFIG);

        ItemInit.ITEMS.register(modEventBus);
        LootInit.GLM.register(modEventBus);
        modEventBus.addListener(LootInit::init);
        RecipeInit.SERIALIZERS.register(modEventBus);
        CraftingHelper.register(getId("upgrade"), UpgradeNBTIngredient.Serializer.INSTANCE);

        CreativeTabInit.TABS.register(modEventBus);
        modEventBus.addListener(CreativeTabInit::addCreative);

        MinecraftForge.EVENT_BUS.addListener(SideProxy::registerCommands);
        modEventBus.addListener(SideProxy::init);
        modEventBus.addListener(SideProxy::enqueue);
        modEventBus.addListener(SideProxy::registerCapabilities);
    }

    public static void init(FMLCommonSetupEvent event)
    {
        IJewelEffects.init();
        IJewelAttributes.init();
    }

    private static void enqueue(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace").size(1).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ring").size(2).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("bracelet").size(2).build());
        MagicalJewelry.LOGGER.info("enqueue method registered.");
    }

    public static void registerCommands(RegisterCommandsEvent event)
    {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        JewelCommands.register(dispatcher);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(JewelItemCapability.class);
    }

    static class Client extends SideProxy
    {
        Client()
        {
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

            modEventBus.addListener(Client::clientSetup);
            modEventBus.addListener(Client::registerLayers);
            modEventBus.addListener(ModColorHandler::registerItemColor);
        }

        private static void clientSetup(FMLClientSetupEvent event)
        {
            CuriosRendererRegistry.register(ItemInit.GOLD_AMULET.get(), () -> (JewelAmuletItem) ItemInit.GOLD_AMULET.get());
            CuriosRendererRegistry.register(ItemInit.SILVER_AMULET.get(), () -> (JewelAmuletItem) ItemInit.SILVER_AMULET.get());
        }

        private static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event)
        {
            event.registerLayerDefinition(JewelLayerDefinitions.AMULET, JewelAmuletModel::createLayer);
        }
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