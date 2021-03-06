package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CastRevive extends Active implements CommandInterface, Listener
{
	private List<Death> deaths;
	private int range;
	private int percentage;
	private int timer;

	public CastRevive(String name, String description)
	{
		super(name, description);

		deaths = new ArrayList<Death>();

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		range = 32;
		percentage = 100;
		timer = 30000;

		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Percentage: " + ChatColor.GRAY + percentage + "%");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length == 2)
			{
				if (args[1].equalsIgnoreCase("info"))
				{
					pages.display(player, args, 2);

					return true;
				}

				if (player.getServer().getPlayer(args[1]) != null)
				{
					Player target = player.getServer().getPlayer(args[1]);

					if (!caster.sameParty(target))
					{
						caster.getPlayer().sendMessage(
								header + ChatColor.WHITE + " You " + ChatColor.GRAY + "Must Be In The Same Party To" + ChatColor.WHITE + " Revive " + ChatColor.GRAY + "Someone!");

						return false;
					}

					if (caster.canCast(name, cooldown, manacost))
					{
						warmup.start(caster, target, name);

						new BukkitRunnable()
						{
							@SuppressWarnings("deprecation")
							@Override
							public void run()
							{
								if (!caster.isInterrupted())
								{
									for (Death death : deaths)
									{
										if (death.getPlayer().equals(target))
										{
											if (System.currentTimeMillis() - death.getTime() <= timer)
											{
												if (player.getLocation().distance(death.getLocation()) < range)
												{
													caster.setCasting(name, true);
													caster.setMana(manacost);

													target.teleport(death.getLocation());

													deaths.remove(death);

													target.getWorld().spigot().playEffect(target.getLocation(), Effect.HEART, 0, 0, 0.5F, 1.0F, 0.5F, 0.1F, 50, 16);
													target.getWorld().playSound(target.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 8.0F, 1.0F);
													cast(player, target);

													caster.setCasting(name, false);
												}

												else
												{
													player.sendMessage(
															header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Must Be Closer To Where " + ChatColor.WHITE + target.getName() +
																	" Died!");

													return;
												}
											}

											else
											{
												caster.getPlayer().sendMessage(
														header + ChatColor.GRAY + " You Were Too Late. " + ChatColor.WHITE + target.getName() + ChatColor.GRAY + " Could Not Be" +
																ChatColor.WHITE + " Revived.");
												return;
											}

											break;
										}
									}
								}

								cooldown.start(player.getName());
							}

						}.runTaskLater(Casters.getInstance(), warmup.getDuration());
					}

					else
					{
						player.sendMessage(
								header + ChatColor.WHITE + " " + args[1] + ChatColor.GRAY + " Must Be Dead In Order To Cast " + ChatColor.WHITE + name + ChatColor.GRAY + "!");
					}
				}

				else
				{
					player.sendMessage(header + ChatColor.WHITE + " " + args[1] + ChatColor.GRAY + " Is Not Online!");
				}

				return true;
			}

			player.sendMessage(header + ChatColor.GRAY + " Correct Usage: " + ChatColor.DARK_AQUA + "/cast"
					+ ChatColor.AQUA + " revive <player>");
		}

		return false;
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		for (int i = 0; i < deaths.size(); ++i)
		{
			if (deaths.get(i).getPlayer().equals(event.getEntity()))
			{
				deaths.remove(i);
			}
		}

		deaths.add(new Death(event.getEntity(), event.getEntity().getLocation(), System.currentTimeMillis()));
	}

	private class Death
	{
		private Player player;
		private Location location;
		private Long time;

		public Death(Player player, Location location, Long time)
		{
			this.player = player;
			this.location = location;
			this.time = time;
		}

		public Player getPlayer()
		{
			return player;
		}

		public Location getLocation()
		{
			return location;
		}

		public Long getTime()
		{
			return time;
		}
	}
}
