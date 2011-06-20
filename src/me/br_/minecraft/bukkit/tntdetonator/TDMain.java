package me.br_.minecraft.bukkit.tntdetonator;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.server.EntityTNTPrimed;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftTNTPrimed;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class TDMain extends JavaPlugin {
	Logger log = Logger.getLogger("minecraft");
	public static Map<Player, Block> blocks;

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if ((sender instanceof ConsoleCommandSender)) {
			log.info("[TNTDetonator] You can't use this command!");
			return true;
		}
		if (args.length != 0) {
			return false;
		}
		if ((blocks.containsKey((Player) sender))
				&& (((Block) blocks.get((Player) sender)).getType() == Material.TNT)) {
			getServer().getScheduler().scheduleSyncDelayedTask(
					this,
					new TDTimer((Player) sender, 3,
							getServer().getScheduler(), this), 20L);
			Block b = (Block) blocks.get((Player) sender);
			Server server = getServer();
			CraftWorld cWorld = (CraftWorld) b.getWorld();
			EntityTNTPrimed tnt = new EntityTNTPrimed(cWorld.getHandle(), b
					.getLocation().getX(), b.getLocation().getY(), b
					.getLocation().getZ());
			tnt.setPositionRotation(b.getLocation().getBlockX() + 0.5D, b
					.getLocation().getBlockY(),
					b.getLocation().getBlockZ() + 0.5D, 0.0F, 0.0F);
			cWorld.getHandle().addEntity(tnt);
			new CraftTNTPrimed((CraftServer) server, tnt);
			b.setType(Material.AIR);
		} else {
			sender.sendMessage("[TNTDetonator] Please place a TNT and run away.");
		}
		return true;
	}

	public void onDisable() {
		log.info("TNT Detonator Disabled :(");
	}

	public void onEnable() {
		log.info("TNT Detonator Enabled!!!! :)");
		blocks = new HashMap<Player, Block>();
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACE,
				new TDListener(), Event.Priority.Normal, this);
	}

	private class TDTimer implements Runnable {
		private Player p;
		private int i;
		private BukkitScheduler s;
		private TDMain m;

		public TDTimer(Player p, int i, BukkitScheduler s, TDMain m) {
			this.p = p;
			this.i = i;
			this.s = s;
			this.m = m;
		}

		public void run() {
			if (i == 0) {
				p.sendMessage("" + ChatColor.DARK_RED + "BOOM!!!!!!!!!");
			} else {
				p.sendMessage("" + ChatColor.YELLOW + i-- + ChatColor.WHITE
						+ "...");
				s.scheduleSyncDelayedTask(m, new TDTimer(p, i, s, m), 20L);
			}
		}
	}
}