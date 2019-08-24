package com.fantasticsource.grounditemhighlighting;

import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.fantasticsource.grounditemhighlighting.GroundItemHighlightingConfig.glow;

@Mod(modid = GroundItemHighlighting.MODID, name = GroundItemHighlighting.NAME, version = GroundItemHighlighting.VERSION, acceptableRemoteVersions = "*")
public class GroundItemHighlighting
{
    public static final String MODID = "grounditemhighlighting";
    public static final String NAME = "Ground Item Highlighting";
    public static final String VERSION = "1.12.2.002";

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(GroundItemHighlighting.class);

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            //Physical client
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
            for (EntityItem item : GroundItemHighlighter.groundItems) item.setGlowing(glow);
        }
    }
}
