package Cast.Casts.Actives;

import Cast.Casts.Types.ActiveCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastTaunt extends ActiveCast implements CommandInterface, Listener
{
	private int range;
	private int duration;
	private int amplifier;

	public CastTaunt(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		range = 10;
		duration = 100;
		amplifier = 2;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Weakness: " + ChatColor.GRAY + amplifier);
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}
			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				warmup.start(caster, name);

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						List<Entity> targets = player.getNearbyEntities(range, range, range);

						caster.setCasting(name, true);
						caster.setEffect("Taunting", duration);
						caster.setMana(manacost);

						cast(player);

						for (Entity target : targets)
						{
							if (target instanceof LivingEntity)
							{
								((LivingEntity) target).addPotionEffect(
										new PotionEffect(PotionEffectType.WEAKNESS, duration, amplifier));

								if (target instanceof Creature)
								{
									((Creature) target).setTarget(player);
								}

								if (target instanceof Player)
								{
									Caster tcaster = Main.getCasters().get(target.getUniqueId());
									tcaster.setEffect("Taunted", duration);
								}
							}
						}

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								decast(player);
								caster.setCasting(name, false);
							}
						}.runTaskLater(Main.getInstance(), duration);
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
		{
			Caster attacker = Main.getCasters().get(event.getDamager().getUniqueId());
			Caster defender = Main.getCasters().get(event.getEntity().getUniqueId());

			if (attacker.hasEffect("Taunted") && !defender.hasEffect("Taunting"))
			{
				attacker.getPlayer().sendMessage(header + ChatColor.WHITE + "You" + ChatColor.GRAY
						+ " Cannot Attack That Player While " + ChatColor.WHITE + "Taunted" + ChatColor.GRAY + "!");

				event.setCancelled(true);
			}

			return;
		}
	}
}