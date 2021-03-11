package com.fantasticsource.grounditemhighlighting;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = GroundItemHighlighting.MODID, name = GroundItemHighlighting.NAME, version = GroundItemHighlighting.VERSION, acceptableRemoteVersions = "*", dependencies = "required-after:fantasticlib@[1.12.2.044zn,)")
public class GroundItemHighlighting
{
    public static final String MODID = "grounditemhighlighting";
    public static final String NAME = "Ground Item Highlighting";
    public static final String VERSION = "1.12.2.003a";

    public static boolean compatTiamatItems = false;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(GroundItemHighlighting.class);

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            //Physical client
            GroundItemHighlighter.sync();
            MinecraftForge.EVENT_BUS.register(GroundItemHighlighter.class);
        }
    }

    @SubscribeEvent
    public static void saveConfig(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent.PostConfigChangedEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            GroundItemHighlighter.sync();
        }
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
        compatTiamatItems = Loader.isModLoaded("tiamatitems");
    }
}
