package org.example.pluhin.reservationdown;

import command.CommandHandler;
import command.CommandTab;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.Timer;

public final class ReservationDown extends JavaPlugin{
    private final Timer timer = new Timer();

    private BukkitTask shutdownTask;
    @Override
    public void onEnable() {
        PluginCommand shutDown = Objects.requireNonNull(this.getCommand("shutdown"));
        shutDown.setExecutor(new CommandHandler(this));
        shutDown.setTabCompleter(new CommandTab());

        getLogger().info("ReservationDown is enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ReservationDown is disabled.");
    }
}
