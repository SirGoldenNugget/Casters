package Cast.Casters;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Essentials.Chat.Pages;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CastersWeapon implements CommandInterface
{
	private Pages pages = new Pages();
	private String fill = "-------------------";
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters Weapon" + ChatColor.DARK_GRAY
			+ "]";

	private List<String> weapon = new ArrayList<String>();

	public CastersWeapon()
	{
		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setError("Casters Weapon");
		pages.setCommand("casters weapon");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			weapon.clear();
			pages.clear();

			for (Material material : caster.getWeapon().keySet())
			{
				weapon.add(ChatColor.DARK_AQUA + material.toString() + ChatColor.GRAY + " - "
						+ caster.getWeapon().get(material));
			}

			pages.setPage(weapon);

			pages.display(player, args, 1);
		}

		return true;
	}
}
