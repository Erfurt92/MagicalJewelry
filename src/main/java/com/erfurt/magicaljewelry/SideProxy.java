package com.erfurt.magicaljewelry;

import com.erfurt.magicaljewelry.command.JewelCommands;
import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.init.LootInit;
import com.erfurt.magicaljewelry.loot.JewelModifier;
import com.erfurt.magicaljewelry.loot.conditions.BossEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.HostileEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.WaterEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.WaterHostileEntityCondition;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfig;
import com.erfurt.magicaljewelry.util.handlers.ModColorHandler;
import com.erfurt.magicaljewelry.util.interfaces.IJewelAttributes;
import com.erfurt.magicaljewelry.util.interfaces.IJewelEffects;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

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
        LootInit.GLM.register(modEventBus);

        modEventBus.addGenericListener(GlobalLootModifierSerializer.class, SideProxy::lootModifierRegistries);
        MinecraftForge.EVENT_BUS.addListener(SideProxy::serverStarting);
        modEventBus.addListener(SideProxy::enqueue);
    }

    private static void enqueue(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("necklace"));
        InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring").setSize(2));
        InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("bracelet").setSize(2));
        InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_ICON, () -> new Tuple<>("bracelet", MagicalJewelry.getId("item/empty_bracelet_slot")));
        MagicalJewelry.LOGGER.info("enqueue method registered.");
    }

    private static void lootModifierRegistries(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event)
    {
        LootConditionManager.registerCondition(new HostileEntityCondition.Serializer());
        LootConditionManager.registerCondition(new BossEntityCondition.Serializer());
        LootConditionManager.registerCondition(new WaterEntityCondition.Serializer());
        LootConditionManager.registerCondition(new WaterHostileEntityCondition.Serializer());
        MagicalJewelry.LOGGER.info("lootModifierRegistries method registered.");
    }

    private static void serverStarting(FMLServerStartingEvent event)
    {
        CommandDispatcher<CommandSource> dispatcher = event.getServer().getCommandManager().getDispatcher();
        JewelCommands.register(dispatcher);
    }

    static class Client extends SideProxy
    {
        Client()
        {
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

            modEventBus.addListener(Client::clientSetup);
            modEventBus.addListener(ModColorHandler::registerItemColor);
            modEventBus.addListener(this::stitchTextures);
        }

        private static void clientSetup(FMLClientSetupEvent event) { }

        private void stitchTextures(TextureStitchEvent.Pre event)
        {
            if(event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE))
            {
                event.addSprite(MagicalJewelry.getId("item/empty_bracelet_slot"));
            }
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