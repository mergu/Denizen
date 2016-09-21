package net.aufdemrand.denizen.nms.interfaces;

import net.aufdemrand.denizen.nms.util.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;

public interface BlockHelper {

    PlayerProfile getPlayerProfile(Skull skull);

    void setPlayerProfile(Skull skull, PlayerProfile playerProfile);

    BlockData getBlockData(short id, byte data);

    BlockData getBlockData(Block block);

    BlockData getBlockData(String compressedString);

    boolean isSafeBlock(Material material);
}
