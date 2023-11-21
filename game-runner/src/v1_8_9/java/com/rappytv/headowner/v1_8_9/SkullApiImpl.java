package com.rappytv.headowner.v1_8_9;

import com.mojang.authlib.properties.Property;
import com.rappytv.headowner.api.ISkullApi;
import com.rappytv.headowner.config.HeadOwnerConfig;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.models.Implements;
import net.labymod.api.util.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@Implements(ISkullApi.class)
public class SkullApiImpl implements ISkullApi {

    @Override
    public String getDisplay(HeadOwnerConfig config) {
        return new Skull(Skull.getTileLooking(config.distance())).getDisplay(config.copyKey());
    }

    @Override
    public String getCopy(HeadOwnerConfig config) {
        Skull skull = new Skull(Skull.getTileLooking(config.distance()));
        if(skull.getSkullTypeName() == null) return null;
        return skull.getCopy();
    }

    public static class Skull {

        private int type = -1;
        private String username;
        private UUID uuid;
        private String value;

        public Skull(TileEntity tileEntity) {
            if(!(tileEntity instanceof TileEntitySkull tileEntitySkull))
                return;

            this.type = tileEntitySkull.getSkullType();

            if(tileEntitySkull.getPlayerProfile() != null) {
                this.username = tileEntitySkull.getPlayerProfile().getName();
                this.uuid = tileEntitySkull.getPlayerProfile().getId();

                for (Property property : tileEntitySkull.getPlayerProfile().getProperties().get("textures"))
                    this.value = property.getValue();
            }
        }

        public static TileEntity getTileLooking(int distance) {
            try {
                if(Minecraft.getMinecraft().thePlayer == null) return null;
                MovingObjectPosition movingObjectPosition = Minecraft.getMinecraft().thePlayer.rayTrace(distance, 1.0F);
                if(movingObjectPosition == null) return null;

                BlockPos blockPos = movingObjectPosition.getBlockPos();

                if(blockPos == null) return null;

                return Minecraft.getMinecraft().theWorld.getTileEntity(blockPos);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public String getDisplay(Key key) {
            if(this.username != null)
                return this.username;

            if(this.value != null) {
                if(key != Key.NONE)
                    return I18n.translate("headowner.messages.unknownHeadKey", key.getName());

                return I18n.translate("headowner.messages.unknownHead");
            }

            return getSkullTypeName();
        }

        public String getCopy() {
            String uuid = this.uuid == null ? I18n.translate("headowner.messages.unknown") : this.uuid.toString();
            String username = this.username == null ? I18n.translate("headowner.messages.unknown") : this.username;
            String value = this.value == null ? I18n.translate("headowner.messages.unknown") : this.value;
            String type = getSkullTypeName();

            return I18n.translate("headowner.messages.copyFormat", type, username, uuid, value);
        }

        private String getSkullTypeName() {
            if(this.type == -1) return null;
            return switch (this.type) {
                case 0 -> I18n.translate("headowner.types.skeleton");
                case 1 -> I18n.translate("headowner.types.wither_skeleton");
                case 2 -> I18n.translate("headowner.types.zombie");
                case 3 -> I18n.translate("headowner.types.player");
                case 4 -> I18n.translate("headowner.types.creeper");
                case 5 -> I18n.translate("headowner.types.dragon");
                default -> "Unknown Skull Type: " + this.type;
            };
        }
    }
}
