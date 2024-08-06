package com.rappytv.headowner.v1_21;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.rappytv.headowner.api.ISkullApi;
import com.rappytv.headowner.config.HeadOwnerConfig;
import java.util.UUID;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.models.Implements;
import net.labymod.api.util.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.SkullBlock.Types;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

@Implements(ISkullApi.class)
public class SkullApiImpl implements ISkullApi {

    @Override
    public String getDisplay(HeadOwnerConfig config) {
        return new Skull(Skull.getBlockLooking(config.distance())).getDisplay(config.copyKey());
    }

    @Override
    public String getCopy(HeadOwnerConfig config) {
        Skull skull = new Skull(Skull.getBlockLooking(config.distance()));
        if(skull.getSkullTypeName() == null) return null;
        return skull.getCopy();
    }

    public static class Skull {

        private Types type = null;
        private String username;
        private UUID uuid;
        private String value;

        public Skull(BlockEntity blockEntity) {
            if(!(blockEntity instanceof SkullBlockEntity skullBlockEntity))
                return;

            this.type = (Types) ((SkullBlock) skullBlockEntity.getBlockState().getBlock()).getType();
            ResolvableProfile owner = skullBlockEntity.getOwnerProfile();

            if(owner != null && owner.isResolved()) {
                GameProfile profile = owner.gameProfile();
                this.username = profile.getName();
                this.uuid = profile.getId();

                for (Property property : owner.properties().get("textures"))
                    this.value = property.value();
            }
        }

        public static BlockEntity getBlockLooking(int distance) {
            try {
                Minecraft minecraft = Minecraft.getInstance();
                if(minecraft.player == null) return null;
                HitResult hitResult = minecraft.player.pick(distance, 0F, false);

                if(minecraft.level == null) return null;

                return minecraft.level.getBlockEntity(((BlockHitResult) hitResult).getBlockPos());
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
            if(this.type == null) return null;
            return switch (this.type) {
                case SKELETON -> I18n.translate("headowner.types.skeleton");
                case WITHER_SKELETON -> I18n.translate("headowner.types.wither_skeleton");
                case ZOMBIE -> I18n.translate("headowner.types.zombie");
                case PLAYER -> I18n.translate("headowner.types.player");
                case CREEPER -> I18n.translate("headowner.types.creeper");
                case DRAGON -> I18n.translate("headowner.types.dragon");
                case PIGLIN -> I18n.translate("headowner.types.piglin");
            };
        }
    }
}
