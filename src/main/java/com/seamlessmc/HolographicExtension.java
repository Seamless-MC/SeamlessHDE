package com.seamlessmc;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class HolographicExtension extends JavaPlugin {
    private static String version;
    private ProtocolHook protocolHook;

    public HolographicExtension() {
    }

    public void onEnable() {
        version = this.getDescription().getVersion();
        Bukkit.getConsoleSender().sendMessage("§e[SeamlessHDE] §fStarting v" + version);
        this.hookProtocolLib();
    }

    public void hookProtocolLib() {
            this.protocolHook = new ProtocolHook();
            this.protocolHook.enable(this);

    }

    public ProtocolHook getProtocolHook() {
        return this.protocolHook;
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§e[SeamlessHDE] §fDisabling v" + version);
    }
}