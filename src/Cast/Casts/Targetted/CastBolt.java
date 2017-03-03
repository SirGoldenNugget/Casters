package Cast.Casts.Targetted;

import Cast.Casts.Types.TargettedCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class CastBolt extends TargettedCast implements CommandInterface, Listener
{
	private double damage;
	private int range;
	private int explosion;
	private boolean explode;
	private boolean incendiary;

	public CastBolt(String name, String description)
	{
		super(name, description);

		warmup.setDuration(40);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");

		damage = 10;
		range = 8;
		explosion = 5;
		explode = false;
		incendiary = false;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
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
				LivingEntity target = getTarget(player, range, false);

				if (target != null && !target.equals(player))
				{
					warmup.start(caster, target, name);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							caster.setCasting(name, true);
							caster.setMana(manacost);

							target.getWorld().spigot().strikeLightningEffect(target.getLocation(), true);
							target.getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1.0F,
									1.0F);
							target.damage(damage);

							if (explode)
							{
								target.getWorld().createExplosion(target.getLocation(), explosion, incendiary);
							}

							cast(player, target);

							cooldown.start(player.getName());

							caster.setCasting(name, false);
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}
				else if (explode)
				{
					warmup.start(caster, name);

					Block block = player.getTargetBlock((Set<Material>) null, range);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							block.getWorld().strikeLightningEffect(block.getLocation());
							block.getWorld().playSound(block.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1.0F, 1.0F);
							block.getWorld().createExplosion(block.getLocation(), explosion, incendiary);

							cast(player);

							cooldown.start(player.getName());
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}
}
