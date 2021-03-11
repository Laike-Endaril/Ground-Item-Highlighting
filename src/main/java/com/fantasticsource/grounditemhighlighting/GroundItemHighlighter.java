package com.fantasticsource.grounditemhighlighting;

import com.fantasticsource.mctools.PathedParticle;
import com.fantasticsource.mctools.component.path.CPathFollowEntity;
import com.fantasticsource.mctools.items.ItemFilter;
import com.fantasticsource.tools.component.path.CPath;
import com.fantasticsource.tools.component.path.CPathConstant;
import com.fantasticsource.tools.datastructures.Color;
import com.fantasticsource.tools.datastructures.VectorN;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class GroundItemHighlighter
{
    protected static final CPathConstant
            ITEM_CENTER_OFFSET = new CPathConstant(new VectorN(0, 0.5, 0)),
            BLOCK_ITEM_CENTER_OFFSET = new CPathConstant(new VectorN(0, 0.35, 0));

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

                if (GroundItemHighlightingConfig.particles && !Minecraft.getMinecraft().isGamePaused())
                {
                    CPath path = new CPathFollowEntity(item).add(item.getItem().getItem() instanceof ItemBlock ? BLOCK_ITEM_CENTER_OFFSET : ITEM_CENTER_OFFSET);

                    PathedParticle[] particles = new PathedParticle[3];
                    PathedParticle particle = new PathedParticle(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, path);
                    particle.xScale3D = 0.05;
                    particle.yScale3D = 5;
                    particle.zScale3D = 0.05;
                    particles[0] = particle;

                    particle = new PathedParticle(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, path);
                    particle.xScale3D = 3;
                    particle.yScale3D = 0.05;
                    particle.zScale3D = 0.05;
                    particles[1] = particle;

                    particle = new PathedParticle(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, path);
                    particle.xScale3D = 0.05;
                    particle.yScale3D = 0.05;
                    particle.zScale3D = 3;
                    particles[2] = particle;

                    for (PathedParticle particle2 : particles)
                    {
                        particle2.u1 = 32d / 128;
                        particle2.v1 = 16d / 128;
                        particle2.u2 = 64d / 128;
                        particle2.v2 = 48d / 128;
                        particle2.setAlphaF(0.2f);
                        particle2.setMaxAge(2);
                    }

                    if (GroundItemHighlighting.compatTiamatItems)
                    {
                        Color c = com.fantasticsource.grounditemhighlighting.CompatTiamatItems.getItemRarityColor(item.getItem()).copy().setVF(0.7f);
                        for (PathedParticle particle2 : particles)
                        {
                            particle2.setRBGColorF(c.rf(), c.gf(), c.bf());
                        }
                    }
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
