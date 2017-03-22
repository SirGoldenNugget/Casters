package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.minecraft.server.v1_11_R1.AttributeInstance;
import net.minecraft.server.v1_11_R1.EntityInsentient;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.PathEntity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Makes All Party Members Or Enemies Spawn On Pigs.
 */
public class CastHogRiders extends Active implements CommandInterface, Listener
{
	private List<UUID> hogs;

	private int duration;
	private int range;
	private int lookrange;
	private double speed;

	public CastHogRiders(String name, String description)
	{
		super(name, description);

		hogs = new ArrayList<UUID>();

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 100;
		range = 12;
		speed = 0.25;
		lookrange = 16;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration + " HP");
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
				warmup.start(caster, name);

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if (!caster.isInterrupted())
						{
							caster.setCasting(name, true);
							caster.setMana(manacost);

							Set<Caster> members = new HashSet<Caster>();

							if (caster.hasParty())
							{
								members.addAll(caster.getParty().getMembers());
							}

							else
							{
								members.add(caster);
							}


							for (Caster member : members)
							{
								if (caster.getPlayer().getLocation().distance(member.getPlayer().getLocation()) < range)
								{
									Pig hog = (Pig) member.getPlayer().getWorld().spawnEntity(member.getPlayer().getLocation(), EntityType.PIG);

									hog.setAdult();
									hog.setSaddle(true);
									hog.setAI(true);
									hog.addPassenger(member.getPlayer());

									MovePig(player, hog);

									hogs.add(hog.getUniqueId());

									member.setEffect("HogRiding", duration);

									new BukkitRunnable()
									{
										@Override
										public void run()
										{
											if (hog.isValid())
											{
												hogs.remove(hog.getUniqueId());
												member.getPlayer().leaveVehicle();
												hog.remove();
											}
										}

									}.runTaskLater(Casters.getInstance(), duration);
								}
							}

							cast(player);

							caster.setCasting(name, false);
						}

						cooldown.start(player.getName());
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}
		return true;
	}

	private void MovePig(Player player, Pig pig)
	{
		new BukkitRunnable()
		{
			public void run()
			{
				if (!pig.isValid())
				{
					this.cancel();
				}

				Location location = player.getTargetBlock((Set<Material>) null, lookrange).getLocation();

				net.minecraft.server.v1_11_R1.Entity pett = ((CraftEntity) pig).getHandle();
				((EntityInsentient) pett).getNavigation().a(2);
				Object petf = ((CraftEntity) pig).getHandle();
				PathEntity path = ((EntityInsentient) petf).getNavigation().a(location.getX(), location.getY(), location.getZ());

				if (path != null)
				{
					((EntityInsentient) petf).getNavigation().a(path, 1.0D);
					((EntityInsentient) petf).getNavigation().a(2.0D);
				}

				AttributeInstance attributes = ((EntityInsentient) ((CraftEntity) pig).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
				attributes.setValue(speed);
			}

		}.runTaskTimer(Casters.getInstance(), 0, 2);
	}

	@EventHandler
	public void onVehicleExitEvent(VehicleExitEvent event)
	{
		if (event.getVehicle() instanceof Pig)
		{
			if (hogs.contains(event.getVehicle().getUniqueId()))
			{
				hogs.remove(event.getVehicle().getUniqueId());
				event.getVehicle().remove();
			}
		}
	}
}

