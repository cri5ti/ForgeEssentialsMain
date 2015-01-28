package com.forgeessentials.permissions.core;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.GroupEntry;
import com.forgeessentials.api.permissions.Zone;
import com.forgeessentials.util.UserIdent;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.lang.reflect.Field;
import java.util.Map;

public class FakePlayerHelper
{
    private static Map<GameProfile, FakePlayer> fakePlayers = Maps.newHashMap();

    public FakePlayerHelper()
    {
        APIRegistry.getFEEventBus().register(this);
    }

    public void registerFakePlayers()
    {
        try
        {
            Field fpList = FakePlayerFactory.class.getDeclaredField("fakePlayers");
            fpList.setAccessible(true);
            fakePlayers = (Map<GameProfile, FakePlayer>)fpList.get(null);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        if (!fakePlayers.isEmpty())
        {
            for (FakePlayer i : fakePlayers.values())
            {
                APIRegistry.perms.addPlayerToGroup(new UserIdent(i), Zone.GROUP_OPERATORS);
            }
        }
    }
}
