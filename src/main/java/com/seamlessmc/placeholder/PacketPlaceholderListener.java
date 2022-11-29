package com.seamlessmc.placeholder;


import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketPlaceholderListener extends PacketAdapter {
    private boolean useOptional = true;

    public PacketPlaceholderListener(PacketAdapter.AdapterParameteters params) {
        super(params);
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        String majorVersion = version.split("_")[1];
        if (majorVersion.contains("_")) {
            majorVersion = majorVersion.split("_")[0];
        }

        if (Integer.parseInt(majorVersion) < 13) {
            this.useOptional = false;
        }

    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        if (packet.getType() == Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata(packet.deepClone());
            List<WrappedWatchableObject> dataWatcherValues = entityMetadataPacket.getEntityMetadata();
            if (dataWatcherValues == null) {
                return;
            }

            Iterator var5 = dataWatcherValues.iterator();

            while(var5.hasNext()) {
                WrappedWatchableObject watchableObject = (WrappedWatchableObject)var5.next();
                if (watchableObject.getIndex() == 2) {
                    if (this.replacePlaceholders(watchableObject, event.getPlayer())) {
                        event.setPacket(entityMetadataPacket.getHandle());
                    }

                    return;
                }
            }
        }

    }

    private boolean replacePlaceholders(WrappedWatchableObject customNameWatchableObject, Player player) {
        if (customNameWatchableObject == null) {
            return true;
        } else {
            Object customNameWatchableObjectValue = customNameWatchableObject.getValue();
            String customName;
            if (this.useOptional) {
                if (!(customNameWatchableObjectValue instanceof Optional)) {
                    return false;
                }

                Optional<?> customNameOptional = (Optional)customNameWatchableObjectValue;
                if (!customNameOptional.isPresent()) {
                    return false;
                }

                WrappedChatComponent componentWrapper = (WrappedChatComponent) customNameOptional.get();
                customName = componentWrapper.getJson();
            } else {
                customName = (String)customNameWatchableObjectValue;
            }

            customName = PlaceholderAPI.setPlaceholders(player, customName);
            if (this.useOptional) {
                customNameWatchableObject.setValue(Optional.of(WrappedChatComponent.fromJson(customName).getHandle()));
            } else {
                customNameWatchableObject.setValue(customName);
            }

            return true;
        }
    }
}
