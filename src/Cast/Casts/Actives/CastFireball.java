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
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CastFireball extends ActiveCast implements CommandInterface, Listener
{
	private double timer;
	private double damage;
	private double velocity;
	private boolean gravity;
	private int fireballfireticks;
	private int targetfireticks;
	private int areaofeffect;
	private int explosion;
	private boolean incendiary;
	private boolean singletarget;

	public CastFireball(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		timer = 100;
		damage = 2;
		velocity = 1.5;
		gravity = true;
		fireballfireticks = 100;
		targetfireticks = 50;
		areaofeffect = 1;
		explosion = 0;
		incendiary = false;
		singletarget = true;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "FireTicks: " + ChatColor.GRAY + targetfireticks / 20);

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
					@SuppressWarnings("deprecation")
					@Override
					public void run()
					{
						caster.setCasting(name, true);
						caster.setMana(manacost);

						Snowball fireball = player.launchProjectile(Snowball.class);
						fireball.setVelocity(fireball.getVelocity().normalize().multiply(velocity));
						fireball.setFireTicks(fireballfireticks);
						fireball.setGravity(gravity);
						fireball.setShooter(player);

						cast(player);

						player.getWorld().spigot().playEffect(player.getLocation(), Effect.BLAZE_SHOOT);

						cooldown.start(player.getName());

						caster.setCasting(name, false);

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								if (!fireball.isDead())
								{
									fireball.remove();
								}
							}

						}.runTaskLater(Main.getInstance(), (long) (timer));
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event)
	{
		Projectile projectile = event.getEntity();

		if (projectile instanceof Snowball)
		{
			Snowball fireball = (Snowball) projectile;

			if (fireball.getShooter() instanceof Player)
			{
				List<Entity> e = fireball.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

				for (Entity target : e)
				{
					if (!target.equals(fireball.getShooter()))
					{
						Caster caster = Main.getCasters().get(((Player) fireball.getShooter()).getUniqueId());

						if (target instanceof LivingEntity)
						{
							if (target instanceof Player)
							{
								Caster ctarget = Main.getCasters().get(target.getUniqueId());

								if (caster.sameParty(ctarget))
								{
									return;
								}
							}

							((Damageable) target).damage(damage);
							target.setFireTicks(targetfireticks);

							target.getWorld().spigot().playEffect(target.getLocation().add(0.0D, 1.0D, 0.0D), Effect.FLAME,
									0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
							target.getWorld().playSound(target.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 8.0F, 1.0F);

							fireball.remove();

							if (singletarget)
							{
								return;
							}
						}
					}
				}

				if (explosion > 0)
				{
					fireball.getWorld().createExplosion(fireball.getLocation(), explosion, incendiary);

					return;
				}
			}
		}
	}
}
