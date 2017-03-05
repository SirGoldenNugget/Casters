package Cast.Casts.Actives;

import Cast.Casts.Types.ActiveCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CastReflect extends ActiveCast implements CommandInterface, Listener
{
	private HashMap<String, Long> reflects = new HashMap<String, Long>();
	private int duration;
	private int percentage;

	public CastReflect(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 100;
		percentage = 100;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Percentage: " + ChatColor.GRAY + percentage + " %");

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
						caster.setCasting(name, true);
						caster.setEffect("Reflecting", duration);
						caster.setMana(manacost);

						reflects.put(player.getName(), System.currentTimeMillis());

						player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8F,
								1.0F);

						cast(player);

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								decast(player);
								caster.setCasting(name, false);
							}

						}.runTaskLater(Main.getInstance(), duration);

						cooldown.start(player.getName());
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player)
		{
			Caster caster = Main.getCasters().get(event.getDamager().getUniqueId());
			LivingEntity target = (LivingEntity) event.getEntity();

			if (caster.isCasting(name))
			{
				if ((System.currentTimeMillis() / 1000.0) - (reflects.get(caster.getPlayer().getName()) / 1000.0) < duration / 20.0)
				{
					((LivingEntity) target).damage(event.getDamage() * (percentage / 100));

					// TODO: make sure that there are party checks.

					caster.setBossBarEntity((LivingEntity) target);

					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
