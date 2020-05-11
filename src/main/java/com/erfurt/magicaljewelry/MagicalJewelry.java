package com.erfurt.magicaljewelry;

import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.init.LootInit;
import com.erfurt.magicaljewelry.loot.JewelModifier;
import com.erfurt.magicaljewelry.loot.conditions.BossEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.HostileEntityCondition;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfig;
import com.erfurt.magicaljewelry.util.handlers.ModColorHandler;
import com.erfurt.magicaljewelry.util.interfaces.IJewelEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

@Mod(MagicalJewelry.MOD_ID)
public class MagicalJewelry
{
	public static final String MOD_ID = "magicaljewelry";
	public static final String MOD_NAME = "Magical Jewelry";
	
	public static MagicalJewelry instance;
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	
	public MagicalJewelry()
	{
		instance = this;

		MagicalJewelryConfig.loadConfig(MagicalJewelryConfig.CONFIG, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + "_client.toml"));
		
		IJewelEffects.init();
		LootInit.init();

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ItemInit.ITEMS.register(modEventBus);

		modEventBus.addGenericListener(GlobalLootModifierSerializer.class, this::lootModifierRegistries);
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientRegistries);
		modEventBus.addListener(ModColorHandler::registerItemColor);
		modEventBus.addListener(this::enqueue);

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
		InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("necklace"));
		InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring").setSize(2));
		LOGGER.info("enqueue method registered.");
	}
	
	private void lootModifierRegistries(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event)
	{
		LootConditionManager.registerCondition(new HostileEntityCondition.Serializer());
		LootConditionManager.registerCondition(new BossEntityCondition.Serializer());

		event.getRegistry().register(new JewelModifier.Serializer().setRegistryName(getId("jewel_modifier")));
		LOGGER.info("lootModifierRegistries method registered.");
	}

	public static ResourceLocation getId(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}