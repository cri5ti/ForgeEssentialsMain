package com.forgeessentials.remote.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.remote.GenericRemoteHandler;
import com.forgeessentials.api.remote.RemoteHandler;
import com.forgeessentials.api.remote.RemoteRequest;
import com.forgeessentials.api.remote.RemoteResponse;
import com.forgeessentials.api.remote.RemoteSession;

public class QueryServerHealth extends GenericRemoteHandler<QueryServerHealth.Request> {

    public static final String ID = "query_server";

    public static final String PERM = RemoteHandler.PERM + ".query.server";

    public QueryServerHealth()
    {
        super(ID, PERM, QueryServerHealth.Request.class);
        APIRegistry.perms.registerPermission(PERM, RegisteredPermValue.OP, "Allows querying server health");
    }

    @Override
    protected RemoteResponse<Response> handleData(RemoteSession session, RemoteRequest<QueryServerHealth.Request> request)
    {
        Response response = new Response();
        
        response.playerCount = MinecraftServer.getServer().getCurrentPlayerCount();
        response.playerMax = MinecraftServer.getServer().getMaxPlayers();
        response.playerList = MinecraftServer.getServer().getAllUsernames();
        response.playerTotal = MinecraftServer.getServer().getConfigurationManager().getAvailablePlayerDat().length;
        
        response.tickCounter = MinecraftServer.getServer().getTickCounter();
        
        response.tickTimesDimensions = new HashMap<Integer, Double>();
        for (Integer dimId : DimensionManager.getIDs())
        {
            double worldTickTime = mean(MinecraftServer.getServer().worldTickTimes.get(dimId));
            response.tickTimesDimensions.put(dimId, worldTickTime);
        }
        
        response.tickTimesWorld = (long) MathHelper.average(MinecraftServer.getServer().tickTimeArray);
        
        response.memoryMax = Runtime.getRuntime().maxMemory();
        response.memoryTotal = Runtime.getRuntime().totalMemory();
        response.memoryFree = Runtime.getRuntime().freeMemory();
        
		return new RemoteResponse<Response>(request, response);
    }


    public static class Request {
    }

    public static class Response {

    	public long memoryTotal;
		public long memoryMax;
    	public long memoryFree;
    	
		public int playerCount;
		public int playerMax;
		public int playerTotal;
		public String[] playerList;
		
		public int tickCounter;
		public long tickTimesWorld;
		public Map<Integer,Double> tickTimesDimensions;
		
    }

    private static long mean(long[] values)
    {
        long sum = 0l;
        for (long v : values)
        {
            sum+=v;
        }

        return sum / values.length;
    }
    
}
