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

    @Config.Name("021 Particle Size")
    @Config.LangKey(GroundItemHighlighting.MODID + ".config.particles")
    @Config.Comment("How large emitted particles are")
    public static double particleSize = 5;

    @Config.Name("030 Whitelist Mode")
    @Config.LangKey(GroundItemHighlighting.MODID + ".config.whitelist")
    @Config.Comment("Whether the filter is a whitelist or a blacklist.  True means whitelist, false means blacklist")
    public static boolean whitelist = false;

    @Config.Name("001 Blacklist / Whitelist")
    @Config.LangKey(GroundItemHighlighting.MODID + ".config.filter")
    @Config.Comment(
            {
                    "A list of items to filter by.  Behavior depends on the 'whitelist mode' setting",
                    "",
                    "Syntax is...",
                    "domain:item:meta > nbtkey1 = nbtvalue1 & nbtkey2 = nbtvalue2",
                    "",
                    "Examples:",
                    "dirt",
                    "stone",
                    "diamond_sword",
                    "ebwizardry:arcane_workbench",
                    "tetra:duplex_tool_modular > duplex/sickle_left_material & duplex/butt_right_material"
            })
    public static String[] filter = new String[0];
}
