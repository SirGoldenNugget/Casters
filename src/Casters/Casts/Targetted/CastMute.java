package Casters.Casts.Targetted;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastMute extends Targetted implements CommandInterface
{
	private double damage;
	private int range;
	private int duration;

	public CastMute(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		damage = 3;
		range = 4;
		duration = 80;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}
			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				LivingEntity target = getTarget(player, range, false, false);

				if (target != null && !target.equals(player))
				{
					warmup.start(caster, target, name);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							if (!caster.isInterrupted())
							{
								caster.setCasting(name, true);
								caster.setMana(manacost);

								target.damage(damage);
								caster.setBossBarEntity(target);

								if (target instanceof Player)
								{
									Caster tcaster = Casters.getCasters().get(target.getUniqueId());

									tcaster.setEffect("Silenced", duration);
									caster.setEffect("Silencing", duration);

									new BukkitRunnable()
									{
										@Override
										public void run()
										{
											if (target instanceof Player)
											{
												target.sendMessage(header + org.bukkit.ChatColor.WHITE + " You" + org.bukkit.ChatColor.GRAY
														+ " Have Stopped Being " + org.bukkit.ChatColor.WHITE + "Muted" + org.bukkit.ChatColor.GRAY
														+ "!");
											}

											List<Entity> e = target.getNearbyEntities(16, 16, 16);

											for (Entity player : e)
											{
												if (player instanceof Player)
												{
													player.sendMessage(header + org.bukkit.ChatColor.WHITE + " " + target.getName()
															+ org.bukkit.ChatColor.GRAY + " Has Stopped Being " + org.bukkit.ChatColor.WHITE
															+ "Muted" + org.bukkit.ChatColor.GRAY + "!");
												}
											}
										}
									}.runTaskLater(Casters.getInstance(), duration);
								}

								cast(player, target);

								caster.setCasting(name, false);
							}

							cooldown.start(player.getName());
						}

					}.runTaskLater(Casters.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}
}
