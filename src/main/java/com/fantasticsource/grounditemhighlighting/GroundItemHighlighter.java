package com.fantasticsource.grounditemhighlighting;

import com.fantasticsource.mctools.ClientTickTimer;
import com.fantasticsource.mctools.items.ItemFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class GroundItemHighlighter
{
    public static ArrayList<ItemFilter> filters = new ArrayList<>();

    public static void sync()
    {
        filters.clear();
        for (String filterString : GroundItemHighlightingConfig.filter)
        {
            ItemFilter filter = ItemFilter.getInstance(filterString);
            if (filter != null) filters.add(filter);
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START) return;

        World world = Minecraft.getMinecraft().world;

        if (world == null) return;


        for (EntityItem item : world.getEntities(EntityItem.class, o -> true))
        {
            if (matchesFilters(item) == GroundItemHighlightingConfig.whitelist)
            {
                item.setGlowing(GroundItemHighlightingConfig.glow);

                if (GroundItemHighlightingConfig.particles && !Minecraft.getMinecraft().isGamePaused() && ClientTickTimer.currentTick() % 5 == 0)
                {
                    world.spawnParticle(EnumParticleTypes.END_ROD, item.posX, item.posY, item.posZ, (-0.5 + Math.random()) * 0.1, 0.25, (-0.5 + Math.random()) * 0.1);
                }
            }
            else item.setGlowing(false);
        }
    }

    protected static boolean matchesFilters(EntityItem item)
    {
        for (ItemFilter filter : filters)
        {
            if (filter.matches(item.getItem())) return true;
        }

        return false;
    }
}
