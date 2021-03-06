package Casters.Casts.Targetted;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Essentials.Effects.Root;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CastEntangle extends Targetted implements CommandInterface, Listener
{
	private Root root;

	private int duration;
	private int range;

	public CastEntangle(String name, String description)
	{
		super(name, description);

		warmup.setDuration(40);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 60;
		range = 8;

		root = new Root(duration);

		info.add(ChatColor.DARK_AQUA + "Root: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");

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
						@SuppressWarnings("deprecation")
						@Override
						public void run()
						{
							if (!caster.isInterrupted())
							{
								caster.setCasting(name, true);
								caster.setMana(manacost);

								caster.setBossBarEntity(target);

								root.start(target);

								player.getWorld().spawnParticle(Particle.FALLING_DUST, target.getLocation(), 50, 0.5, 0.2F, 0.5F);
								player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SHULKER_HURT, 8.0F, 1.0F);

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

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			Caster caster = Casters.getCasters().get(event.getEntity());

			if (caster.hasEffect("Rooted"))
			{
				root.stop(caster.getPlayer());
			}
		}
	}
}
