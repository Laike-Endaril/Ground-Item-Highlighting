package com.fantasticsource.grounditemhighlighting;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

import static com.fantasticsource.grounditemhighlighting.GroundItemHighlightingConfig.glow;
import static com.fantasticsource.grounditemhighlighting.GroundItemHighlightingConfig.particles;

@SideOnly(Side.CLIENT)
public class GroundItemHighlighter
{
    public static ArrayList<EntityItem> groundItems = new ArrayList<>();

    @SubscribeEvent
    public static void joinWorld(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote) return;

        Entity entity = event.getEntity();
        if (!(entity instanceof EntityItem)) return;

        EntityItem item = (EntityItem) entity;
        groundItems.add(item);
        if (glow) item.setGlowing(true);
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START) return;

        World world = Minecraft.getMinecraft().world;
        if (world == null)
        {
            groundItems.clear();
            return;
        }


        for (EntityItem item : groundItems.toArray(new EntityItem[0]))
        {
            if (item.world != world || !world.loadedEntityList.contains(item))
            {
                groundItems.remove(item);
                continue;
            }

            if (particles && world.getWorldTime() % 5 == 0)
            {
                world.spawnParticle(EnumParticleTypes.END_ROD, item.posX, item.posY, item.posZ, (-0.5 + Math.random()) * 0.1, 0.25, (-0.5 + Math.random()) * 0.1);
            }
        }
    }
}
