package com.erfurt.magicaljewelry;

import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.init.LootInit;
import com.erfurt.magicaljewelry.loot.JewelModifier;
import com.erfurt.magicaljewelry.loot.conditions.BossEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.HostileEntityCondition;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfig;
import com.erfurt.magicaljewelry.util.handlers.ModColorHandler;
import com.erfurt.magicaljewelry.util.interfaces.IJewelAttributes;
import com.erfurt.magicaljewelry.util.interfaces.IJewelEffects;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

public class SideProxy
{
    SideProxy()
    {
        MagicalJewelryConfig.loadConfig(MagicalJewelryConfig.CONFIG, FMLPaths.CONFIGDIR.get().resolve(MagicalJewelry.MOD_ID + "_client.toml"));
        IJewelEffects.init();
        IJewelAttributes.init();
        LootInit.init();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(modEventBus);

        modEventBus.addGenericListener(GlobalLootModifierSerializer.class, SideProxy::lootModifierRegistries);
        modEventBus.addListener(SideProxy::setup);
        modEventBus.addListener(SideProxy::clientRegistries);
        modEventBus.addListener(SideProxy::enqueue);
        modEventBus.addListener(SideProxy::process);

        MinecraftForge.EVENT_BUS.addListener(SideProxy::serverStarting);
    }

    private static void setup(final FMLCommonSetupEvent event)
    {
        MagicalJewelry.LOGGER.info("Setup method registered.");
    }

    private static void clientRegistries(final FMLClientSetupEvent event)
    {
        MagicalJewelry.LOGGER.info("clientRegistries method registered.");
    }

    private static void enqueue(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("necklace"));
        InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring").setSize(2));
        InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("bracelet").setSize(2));
        MagicalJewelry.LOGGER.info("enqueue method registered.");
    }

    private static void process(final InterModProcessEvent event)
    {
        MagicalJewelry.LOGGER.info("process method registered.");
    }

    private static void serverStarting(FMLServerStartingEvent event)
    {
        MagicalJewelry.LOGGER.info("serverStarting method registered.");
    }

    private static void lootModifierRegistries(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event)
    {
        LootConditionManager.registerCondition(new HostileEntityCondition.Serializer());
        LootConditionManager.registerCondition(new BossEntityCondition.Serializer());

        event.getRegistry().register(new JewelModifier.Serializer().setRegistryName(MagicalJewelry.getId("jewel_modifier")));
        MagicalJewelry.LOGGER.info("lootModifierRegistries method registered.");
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