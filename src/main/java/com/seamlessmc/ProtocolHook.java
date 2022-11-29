package com.seamlessmc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.seamlessmc.placeholder.PacketPlaceholderListener;
import org.bukkit.plugin.Plugin;

public class ProtocolHook {
    private Plugin plugin;

    public ProtocolHook() {
    }

    public void enable(Plugin plugin) {
        this.plugin = plugin;
        PacketAdapter.AdapterParameteters params = PacketAdapter.params().plugin(plugin).types(new PacketType[]{Server.ENTITY_METADATA}).serverSide().listenerPriority(ListenerPriority.NORMAL);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketPlaceholderListener(params));
    }

    public void disable() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this.plugin);
    }
}