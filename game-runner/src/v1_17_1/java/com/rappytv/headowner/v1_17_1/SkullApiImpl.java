package com.rappytv.headowner.v1_17_1;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.rappytv.headowner.api.ISkullApi;
import com.rappytv.headowner.config.HeadOwnerConfig;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.SkullBlock.Types;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.UUID;

@Implements(ISkullApi.class)
public class SkullApiImpl implements ISkullApi {

    @Override
    public String getDisplay(HeadOwnerConfig config) {
        return new Skull(Skull.getBlockLooking(config.distance())).getDisplay(config.copyKey());
    }

    @Override
    public String getCopy(HeadOwnerConfig config) {
        return new Skull(Skull.getBlockLooking(config.distance())).getCopy();
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
            GameProfile owner = skullBlockEntity.getOwnerProfile();

            if(owner != null) {
                this.username = owner.getName();
                this.uuid = owner.getId();

                for (Property property : owner.getProperties().get("textures"))
                    this.value = property.getValue();
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
                    return "Unknown Head (created by texture value, press " + key.getName() + " to copy skull data)";

                return "Unknown Head (created by texture value)";
            }

            return getSkullTypeName();
        }

        public String getCopy() {
            String uuid = this.uuid == null ? "Unknown" : this.uuid.toString();
            String username = this.username == null ? "Unknown" : this.username;
            String value = this.value == null ? "Unknown" : this.value;
            String type = getSkullTypeName();


            return String.format("Skull type: %s, Username: %s, UUID: %s, Texture value: %s", type, username, uuid, value);
        }

        private String getSkullTypeName() {
            if(this.type == null) return null;
            return switch (this.type) {
                case SKELETON -> "Skeleton Skull";
                case WITHER_SKELETON -> "Wither Skeleton Skull";
                case ZOMBIE -> "Zombie Head";
                case PLAYER -> "Player Head";
                case CREEPER -> "Creeper Head";
                case DRAGON -> "Dragon Head";
            };
        }
    }
}
