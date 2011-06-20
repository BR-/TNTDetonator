package me.br_.minecraft.bukkit.tntdetonator;

import org.bukkit.Material;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TDListener extends BlockListener {
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getBlock().getType() == Material.TNT)
			TDMain.blocks.put(event.getPlayer(), event.getBlock());
	}
}