package it.flowzz.spawnertax;

import it.flowzz.spawnertax.listener.PlayerListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SpawnerTax extends JavaPlugin {

    private Economy economy;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        setupEconomy();
        new PlayerListener(this);
    }

    public double getPrice(String mob){
            return getConfig().getDouble("Prices." + mob) > 0 ? getConfig().getDouble("Prices." + mob) : getConfig().getDouble("DefaultSpawnerMinePrice");
    }


    public Economy getEconomy() {
        return economy;
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        if(economy == null){
            getLogger().log(Level.SEVERE,"Disabling SpawnerTax: Economy Hook not found!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
