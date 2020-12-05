package it.flowzz.spawnertax.listener;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import it.flowzz.spawnertax.SpawnerTax;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerListener implements Listener {

    private SpawnerTax plugin;

    public PlayerListener(SpawnerTax plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpawnerBreak(BlockBreakEvent event){
        if(!(event.getBlock().getType() == Material.SPAWNER) || event.isCancelled())
            return;

        int multiplier = 1;
        Player player = event.getPlayer();
        //WildStacker break whole stack check
        if(player.isSneaking()) {
            CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
            multiplier = Math.max(WildStackerAPI.getStackedSpawner(spawner).getStackAmount(), 1);
        }
        double price = plugin.getPrice(getMobName(event.getBlock())) * multiplier;

        if(!(plugin.getEconomy().getBalance(player) >= price)){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.nomoney")));
            event.setCancelled(true);
            return;
        }
        //Withdraw money
        plugin.getEconomy().withdrawPlayer(player, price);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Lang.spawnermined"))
                .replace("%money%", String.valueOf(price)));

    }

    private String getMobName(Block block){
        return ((CreatureSpawner)block.getState()).getSpawnedType().name().toLowerCase();
    }

}
