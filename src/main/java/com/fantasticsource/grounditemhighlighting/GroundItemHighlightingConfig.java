package com.fantasticsource.grounditemhighlighting;

import net.minecraftforge.common.config.Config;

@Config(modid = GroundItemHighlighting.MODID)
public class GroundItemHighlightingConfig
{
    @Config.Name("010 Glow")
    @Config.LangKey(GroundItemHighlighting.MODID + ".config.glow")
    @Config.Comment("Whether ground items glow or not")
    public static boolean glow = true;

    @Config.Name("020 Particles")
    @Config.LangKey(GroundItemHighlighting.MODID + ".config.particles")
    @Config.Comment("Whether ground items emit particles or not")
    public static boolean particles = true;
}
