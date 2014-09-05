package com.connorlinfoot.autotnt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


public class Main extends JavaPlugin implements Listener {
    private static Plugin instance;

    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        Server server = getServer();
        ConsoleCommandSender console = server.getConsoleSender();

        console.sendMessage(ChatColor.GREEN + "============= AutoTNT! =============");
        console.sendMessage(ChatColor.GREEN + "=========== VERSION: 1.0 ===========");
        console.sendMessage(ChatColor.GREEN + "======== BY CONNOR LINFOOT! ========");

        Bukkit.getPluginManager().registerEvents(this,this);
    }

    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }

    public static Plugin getInstance() {
        return instance;
    }

    @EventHandler
    public void onBlockPlace( BlockPlaceEvent e ){
        if( e.getBlockPlaced() == null || e.getBlockPlaced().getType() != Material.TNT ) return;

        if( !getConfig().getBoolean("Auto Ignite") ) return;

        e.getBlockPlaced().setType(Material.AIR);

        Entity tnt = e.getPlayer().getWorld().spawn(e.getBlockPlaced().getLocation(), TNTPrimed.class);
        ((TNTPrimed) tnt).setFuseTicks(getConfig().getInt("TNT Delay") * 20);
    }

    @EventHandler
    public void onExplosion( EntityExplodeEvent e ){
        if( !getConfig().getBoolean("Disable Block Destroy") ) return;
        e.blockList().clear();
    }

    @EventHandler
    public void onTNTDamage( EntityDamageEvent e ){
        if( getConfig().getInt("Damage") == -1 ) return;

        if( e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION ){
            e.setDamage((double) getConfig().getInt("Damage"));
        }
    }
}
