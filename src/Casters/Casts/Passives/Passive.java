package Casters.Casts.Passives;

import Casters.Casters;
import Casters.Casts.Cast;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class Passive extends Cast implements CommandInterface, Listener
{
	public Passive(String name, String description)
	{
		super(name, description);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			@SuppressWarnings("unused")
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}
			else if (args.length == 1)
			{
				player.sendMessage(header + ChatColor.GRAY + " You Cannot Cast Passives! They Are Always Active.");
			}
		}

		return true;
	}
}
