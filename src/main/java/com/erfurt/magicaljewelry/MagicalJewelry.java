package com.erfurt.magicaljewelry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.init.LootInit;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfig;
import com.erfurt.magicaljewelry.util.handlers.ModColorHandler;
import com.erfurt.magicaljewelry.util.interfaces.IJewelEffects;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

@Mod(MagicalJewelry.MOD_ID)
public class MagicalJewelry
{
	public static final String MOD_ID = "magicaljewelry";
	public static final String MOD_NAME = "Magical Jewelry";
	public static final String VERSION = "1.15.2_1.0.0b";
	
	public static MagicalJewelry instance;
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	
	public MagicalJewelry()
	{
		instance = this;
		
		MagicalJewelryConfig.loadConfig(MagicalJewelryConfig.CONFIG, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + ".toml"));
		
		ItemInit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		
		IJewelEffects.init();
		LootInit.init();
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(ModColorHandler::registerItemColor);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueue);

		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event)
	{
		LOGGER.info("Setup method registered.");
	}
	
	private void clientRegistries(final FMLClientSetupEvent event)
	{
		LOGGER.info("clientRegistries method registered.");
	}
	
	private void enqueue(final InterModEnqueueEvent evt)
	{
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("necklace"));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring").setSize(2));
	}
	
	public static ResourceLocation getId(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
	
	/*@SubscribeEvent
	public static void onRegisterModifier(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event)
	{
		event.getRegistry().register(new JewelModifier.Serializer().setRegistryName(getId("jewel_modifier")));
	}*/
}