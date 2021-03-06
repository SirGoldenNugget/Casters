package Casters.Essentials;

import Casters.Casters;
import Casters.Casts.Cast;
import Casters.Configs.Config;
import Casters.Essentials.Effects.Effect;
import Casters.Essentials.Schedulers.Cooldown;
import Casters.Party.Invite;
import Casters.Party.Party;
import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.common.animate.HighlightedString;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public class Caster
{
	private static final String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + " ";
	private static final String tabheader =
			ChatColor.DARK_GRAY + "\n" + ChatColor.BOLD + "[" + ChatColor.DARK_AQUA + ChatColor.BOLD + "CasterCraft" + ChatColor.DARK_GRAY + ChatColor.BOLD + "]\n";
	private static final String tabfooter = ChatColor.YELLOW + "\nTOO MUCH SAUCE!";
	private static final DecimalFormat decimalformat = new DecimalFormat("##.#");

	private Player player;

	private BossBar bossbar;
	private Damageable bossbarentity;

	private int removetimer;
	private long combattimer;

	private Party party;
	private Invite invite;
	private Invite mergerequest;

	private Config config;

	private String channel;
	private String chattitle;
	private String tabtitle;

	private Type type;
	private Type race;
	private Type job;

	private int typelevel;
	private int typemaxlevel;
	private int racelevel;
	private int racemaxlevel;
	private int joblevel;
	private int jobmaxlevel;

	private double constant;
	private double scale;

	private float typeexp;
	private float typemaxexp;
	private float raceexp;
	private float racemaxexp;
	private float jobexp;
	private float jobmaxexp;

	private double health;
	private double maxhealth;
	private double basemaxhealth;
	private double healthregen;
	private double basehealthregen;

	private double mana;
	private double maxmana;
	private double basemaxmana;
	private double manaregen;
	private double basemanaregen;
	private double manatimer;

	private int points;
	private int strength;
	private int constitution;
	private int dexterity;
	private int intellect;
	private int wisdom;

	private Set<Material> armor;

	private HashMap<Material, Integer> weapon;
	private HashMap<String, Boolean> casting;
	private HashMap<String, Boolean> warmingup;
	private HashMap<String, Effect> effects;
	private HashMap<String, Integer> casts;

	private boolean interrupted;

	@SuppressWarnings("deprecation")
	public Caster(Player player)
	{
		this.player = player;

		armor = new HashSet<Material>();
		weapon = new HashMap<Material, Integer>();
		casting = new HashMap<String, Boolean>();
		warmingup = new HashMap<String, Boolean>();
		effects = new HashMap<String, Effect>();
		casts = new HashMap<String, Integer>();

		bossbar = this.player.getServer().createBossBar(ChatColor.RED + "" + ChatColor.BOLD + "YOU SHOULD NOT BE SEEING THIS!", BarColor.RED, BarStyle.SEGMENTED_6);
		bossbar.addPlayer(player);
		bossbar.setVisible(false);
		removetimer = 5000;

		party = null;
		invite = null;
		mergerequest = null;

		if (!Casters.getConfigManager().getFileExists(this.player.getName() + ".yml"))
		{
			config = Casters.getConfigManager().getNewConfig(this.player.getName() + ".yml", new String[]{this.player.getName() + " Config File."});
			setNewConfig();

			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Welcome " + ChatColor.WHITE + player.getName() + ChatColor.DARK_AQUA + " To CasterCraft!");
		}

		else
		{
			config = Casters.getConfigManager().getConfig(this.player.getName() + ".yml");
		}

		getConfigType();
		getConfigRace();
		getConfigJob();
		getConfigChat();
		getConfigExperience();
		getConfigStats();
		getConfigHealth();
		getConfigMana();

		effects.put("Stunned", new Effect());
		effects.put("Bleeding", new Effect());
		effects.put("Siphoning", new Effect());
		effects.put("Siphoned", new Effect());
		effects.put("Silencing", new Effect());
		effects.put("Silenced", new Effect());
		effects.put("Reflecting", new Effect());
		effects.put("Taunted", new Effect());
		effects.put("Taunting", new Effect());
		effects.put("Invisible", new Effect());
		effects.put("Backtabbing", new Effect());
		effects.put("Poisoning", new Effect());
		effects.put("Poisoned", new Effect());
		effects.put("Defending", new Effect());
		effects.put("Spinning", new Effect());
		effects.put("HogRiding", new Effect());
		effects.put("Inspired", new Effect());
		effects.put("Warcry", new Effect());
		effects.put("Rooted", new Effect());
		effects.put("Haunted", new Effect());

		interrupted = false;

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if (mana + manaregen <= maxmana)
				{
					mana += manaregen;
				}

				else
				{
					mana = maxmana;
				}
			}

		}.runTaskTimer(Casters.getInstance(), 0, (long) manatimer);

		Scoreboard scoreboard = ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler()
		{
			private final HighlightedString casterstext = new HighlightedString("Casters", ChatColor.DARK_AQUA.toString()
					+ ChatColor.BOLD.toString(), ChatColor.AQUA.toString() + ChatColor.BOLD.toString());
			private final HighlightedString nametext = new HighlightedString(player.getName(), "&6", "&e");

			@Override
			public String getTitle(Player player)
			{
				return null;
			}

			@Override
			public List<Entry> getEntries(Player player)
			{
				EntryBuilder entrybuilder = new EntryBuilder();
				entrybuilder.next("    " + ChatColor.DARK_GRAY + ChatColor.MAGIC + "###" + casterstext.next() + ChatColor.DARK_GRAY + ChatColor.MAGIC + "###");
				entrybuilder.next("    " + nametext.next());
				entrybuilder.blank();
				entrybuilder.next("    " + ChatColor.GREEN + "Class: " + type.getName());
				entrybuilder.next("    " + ChatColor.DARK_GREEN + "Race: " + race.getName());
				entrybuilder.next("    " + ChatColor.BLUE + "Job: " + job.getName());
				entrybuilder.blank();
				entrybuilder.next("    " + ChatColor.DARK_AQUA + "Cooldowns:");

				for (Cast cast : Casters.getCasts().values())
				{
					if (cast.getCooldown().hasCooldown(player.getName()))
					{
						entrybuilder.next("    " + ChatColor.AQUA + cast.getName() + ": " + cast.getCooldown().getCooldown(player.getName()));
					}
				}

				entrybuilder.blank();

				entrybuilder.next("    " + ChatColor.DARK_PURPLE + "Effects:");

				for (String effect : effects.keySet())
				{
					if (effects.get(effect).hasTime())
					{
						entrybuilder.next("    " + ChatColor.LIGHT_PURPLE + effect + " " + effects.get(effect).getTime());
					}
				}

				entrybuilder.blank();

				return entrybuilder.build();
			}
		}).setUpdateInterval(2);

		scoreboard.activate();

		Casters.getTabManager().setHeader(tabheader);

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if (bossbarentity != null)
				{
					if (System.currentTimeMillis() - combattimer > removetimer)
					{
						bossbar.setVisible(false);
					}

					else
					{
						if (!bossbarentity.isDead())
						{
							bossbar.setProgress(bossbarentity.getHealth() / bossbarentity.getMaxHealth());
							bossbar.setTitle(ChatColor.RED + "" + ChatColor.BOLD + bossbarentity.getName() + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY +
									Double.parseDouble(decimalformat.format(bossbarentity.getHealth())) + "/" +
									Double.parseDouble(decimalformat.format(bossbarentity.getMaxHealth())) + ChatColor.DARK_GRAY + "]");
						}

						else
						{
							bossbar.setProgress(0.0);
							bossbar.setTitle(ChatColor.RED + "" + ChatColor.BOLD + "RIP RIP POTATO CHIP");
						}

						bossbar.setVisible(true);
					}
				}

				Casters.getActionBarManager()
						.setBar(ChatColor.RED + "❤ " + Double.parseDouble(decimalformat.format(player.getHealth()))
								+ "/" + Double.parseDouble(decimalformat.format(player.getMaxHealth())) + "        "
								+ ChatColor.BLUE + "✦ " + Double.parseDouble(decimalformat.format(mana)) + "/"
								+ Double.parseDouble(decimalformat.format(maxmana)));
				Casters.getTabManager().setFooter(
						tabfooter + ChatColor.GRAY + "\nPING: " + ((CraftPlayer) player).getHandle().ping + "\n");

				Casters.getActionBarManager().send(player);
				Casters.getTabManager().send(player);
			}

		}.runTaskTimer(Casters.getInstance(), 0, 2);
	}

	private void setNewConfig()
	{
		config.set("Type", "Wanderer");
		config.set("Race", "Soul");
		config.set("Job", "Unemployed");
		config.set("Channel", "Global");
		config.set("Title.Chat", "");
		config.set("Title.Tab", "");
		config.set("Level.Type.Current", 1);
		config.set("Level.Type.Max", 20);
		config.set("Level.Race.Current", 1);
		config.set("Level.Race.Max", 20);
		config.set("Level.Job.Current", 1);
		config.set("Level.Job.Max", 20);
		config.set("Health.Current", 20.0);
		config.set("Health.Max", 20.0);
		config.set("Mana.Current", 20.0);
		config.set("Mana.Max", 20.0);
		config.set("Mana.Regen", 1.0);
		config.set("Mana.Timer", 20.0);
		config.set("Exp.Type.Current", 0);
		config.set("Exp.Type.Max", Integer.MAX_VALUE);
		config.set("Exp.Race.Current", 0);
		config.set("Exp.Race.Max", Integer.MAX_VALUE);
		config.set("Exp.Job.Current", 0);
		config.set("Exp.Job.Max", Integer.MAX_VALUE);
		config.set("Stats.Points", 0);
		config.set("Stats.Strength", 0);
		config.set("Stats.Constitution", 0);
		config.set("Stats.Dexterity", 0);
		config.set("Stats.Intellect", 0);
		config.set("Stats.Wisdom", 0);
		config.save();
	}

	private void getConfigType()
	{
		for (Type type : Casters.getClasses())
		{
			if (type.getName().equals(config.getString("Type")))
			{
				this.type = type;

				armor.addAll(type.getArmor());
				weapon.putAll(type.getWeapon());
				casts.putAll(type.getCasts());

				break;
			}
		}
	}

	private void getConfigRace()
	{
		for (Type race : Casters.getRaces())
		{
			if (race.getName().equals(config.getString("Race")))
			{
				this.race = race;

				armor.addAll(race.getArmor());
				weapon.putAll(race.getWeapon());
				casts.putAll(race.getCasts());

				break;
			}
		}
	}

	private void getConfigJob()
	{
		for (Type job : Casters.getJobs())
		{
			if (job.getName().equals(config.getString("Job")))
			{
				this.job = job;

				armor.addAll(job.getArmor());
				weapon.putAll(job.getWeapon());
				casts.putAll(job.getCasts());

				break;
			}
		}
	}

	private void getConfigChat()
	{
		channel = config.getString("Channel");
		chattitle = config.getString("Title.Chat");
		tabtitle = config.getString("Title.Tab");
	}

	private void getConfigExperience()
	{
		typelevel = config.getInt("Level.Type.Current");
		typemaxlevel = config.getInt("Level.Type.Max");
		racelevel = config.getInt("Level.Race.Current");
		racemaxlevel = config.getInt("Level.Race.Max");
		joblevel = config.getInt("Level.Job.Current");
		jobmaxlevel = config.getInt("Level.Job.Max");

		typeexp = (float) config.getDouble("Exp.Type.Current");
		typemaxexp = (float) config.getDouble("Exp.Type.Max");
		raceexp = (float) config.getDouble("Exp.Race.Current");
		racemaxexp = (float) config.getDouble("Exp.Race.Max");
		jobexp = (float) config.getDouble("Exp.Job.Current");
		jobmaxexp = (float) config.getDouble("Exp.Job.Max");

		constant = 100.0;
		scale = 3.0 / 2.0;

		player.setLevel(typelevel);
		player.setExp(typeexp / typemaxexp);
	}

	private void getConfigStats()
	{
		points = config.getInt("Stats.Points");
		strength = config.getInt("Stats.Strength");
		constitution = config.getInt("Stats.Constitution");
		dexterity = config.getInt("Stats.Dexterity");
		intellect = config.getInt("Stats.Intellect");
		wisdom = config.getInt("Stats.Wisdom");
	}

	@SuppressWarnings("deprecation")
	private void getConfigHealth()
	{
		health = config.getDouble("Health.Current");
		basemaxhealth = config.getDouble("Health.Max");
		maxhealth = basemaxhealth + constitution * type.getMaxHealthScale();
		basehealthregen = config.getDouble("Health.Regen");
		healthregen = basehealthregen + constitution * type.getHealthRegenScale();

		if (health > maxhealth)
		{
			health = maxhealth;
		}

		player.setMaxHealth(maxhealth);
		player.setHealth(health);
		player.setHealthScale(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
	}

	private void getConfigMana()
	{
		mana = config.getDouble("Mana.Current");
		basemaxmana = config.getDouble("Mana.Max");
		maxmana = basemaxmana + wisdom * type.getMaxManaScale();

		basemanaregen = config.getDouble("Mana.Regen");
		manaregen = basemanaregen + wisdom * type.getManaRegenScale();
		manatimer = config.getDouble("Mana.Timer");

		if (mana > maxmana)
		{
			mana = maxmana;
		}
	}

	public Caster(UUID uuid)
	{
		this.player = Bukkit.getPlayer(uuid);
	}

	public boolean canCast(String name, Cooldown cooldown, double manacost)
	{
		return hasCast(name) && !isCasting(name) && !isWarmingUp() && !isSilenced(name) && !isStunned(name)
				&& !cooldown.hasCooldown(player, name) && hasMana(manacost, name);
	}

	public boolean hasCast(String name)
	{
		if (!casts.containsKey(name))
		{
			player.sendMessage(
					ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + " You" + ChatColor.GRAY + " Cannot Cast " +
							ChatColor.WHITE + name + ChatColor.GRAY + "!");
			return false;
		}

		else if (casts.get(name) > typelevel)
		{
			player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + "You" + ChatColor.GRAY + " Must Be Level " +
					ChatColor.WHITE + casts.get(name)
					+ ChatColor.GRAY + " To Use " + ChatColor.WHITE + name + ChatColor.GRAY + "!");
			return false;
		}

		return true;
	}

	public boolean isCasting(String name)
	{
		if (casting.containsKey(name))
		{
			return casting.get(name);
		}

		return false;
	}

	public boolean isWarmingUp()
	{
		for (boolean bool : warmingup.values())
		{
			if (bool)
			{
				return true;
			}
		}

		return false;
	}

	public boolean isSilenced(String name)
	{
		if (effects.get("Silenced").hasTime())
		{
			player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + "You" + ChatColor.GRAY + " Cannot Cast " +
					ChatColor.WHITE
					+ name + ChatColor.GRAY + " While Silenced!");
			return true;
		}

		return false;
	}

	public boolean isStunned(String name)
	{
		if (effects.get("Stunned").hasTime())
		{
			player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + "You" + ChatColor.GRAY + " Cannot Cast " +
					ChatColor.WHITE
					+ name + ChatColor.GRAY + " While Stunned!");
			return true;
		}

		return false;
	}

	public boolean hasMana(double manacost, String name)
	{
		if (mana >= manacost)
		{
			return true;
		}

		player.sendMessage(
				ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + " " + name + ChatColor.GRAY + " - Not Enough Mana!");

		return false;
	}

	public boolean canCastPassive(String name, Cooldown cooldown, double manacost)
	{
		return type.getCasts().containsKey(name) && !isCasting(name) && !isWarmingUp() && !isSilenced(name) && !isStunned(name) && !cooldown.hasCooldown(player, name) &&
				hasMana(manacost, name);
	}

	public BossBar getBossBar()
	{
		return bossbar;
	}

	public void setBossBarEntity(Damageable entity)
	{
		bossbarentity = entity;
		combattimer = System.currentTimeMillis();
	}

	public boolean isLeader()
	{
		return party.getLeader().equals(this);
	}

	public boolean sameParty(Entity entity)
	{
		if (entity instanceof Player)
		{
			Caster caster = Casters.getCasters().get(entity.getUniqueId());

			return hasParty() && (party.getMembers().contains(caster) || party.equals(caster.getParty()));
		}

		return false;
	}

	public boolean hasParty()
	{
		return party != null;
	}

	public Party getParty()
	{
		return party;
	}

	public void setParty(Party party)
	{
		this.party = party;
	}

	public Invite getInvite()
	{
		return invite;
	}

	public void setInvite(Invite invite)
	{
		this.invite = invite;
	}

	public boolean hasInvite()
	{
		return invite != null;
	}

	public Invite getMergeRequest()
	{
		return mergerequest;
	}

	public void setMergeRequest(Invite mergerequest)
	{
		this.mergerequest = mergerequest;
	}

	public boolean hasMergeRequest()
	{
		return mergerequest != null;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public String getChatTitle()
	{
		return chattitle;
	}

	public String getTabTitle()
	{
		return tabtitle;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
		config.set("Type", this.type.getName());
		getConfigs();

		player.sendMessage(header + ChatColor.GRAY + "You Have Chosen The Path Of The " + ChatColor.WHITE + this.type.getName() + ChatColor.GRAY + "!");
	}

	public void getConfigs()
	{
		armor.clear();
		weapon.clear();
		casts.clear();

		getConfigJob();
		getConfigRace();
		getConfigType();

		player.leaveVehicle();
	}

	public Type getRace()
	{
		return race;
	}

	public void setRace(Type race)
	{
		this.race = race;
		config.set("Race", this.race.getName());
		getConfigs();

		player.sendMessage(header + ChatColor.GRAY + "You Have Chosen The Path Of The " + ChatColor.WHITE + this.race.getName() + ChatColor.GRAY + "!");
	}

	public Type getJob()
	{
		return job;
	}

	public void setJob(Type job)
	{
		this.job = job;
		config.set("Job", this.job.getName());
		getConfigs();

		player.sendMessage(header + ChatColor.GRAY + "You Have Chosen The Path Of The " + ChatColor.WHITE + this.job.getName() + ChatColor.GRAY + "!");
	}

	public boolean hasEffect(String name)
	{
		if (effects.containsKey(name))
		{
			return effects.get(name).hasTime();
		}

		return false;
	}

	public int getTypeLevel()
	{
		return typelevel;
	}

	public void setTypeLevel(int typelevel)
	{
		this.typelevel = typelevel;
	}

	public int getTypeMaxLevel()
	{
		return typemaxlevel;
	}

	public float getTypeExp()
	{
		return typeexp;
	}

	public void setTypeExp(float typeexp)
	{
		this.typeexp = typeexp;

		if (this.typeexp < 0)
		{
			this.typeexp = 0;
		}
	}

	public float getTypeMaxExp()
	{
		return typemaxexp;
	}

	public int getRaceLevel()
	{
		return racelevel;
	}

	public void setRaceLevel(int racelevel)
	{
		this.racelevel = racelevel;
	}

	public int getRaceMaxLevel()
	{
		return racemaxlevel;
	}

	public float getRaceExp()
	{
		return raceexp;
	}

	public void setRaceExp(float raceexp)
	{
		this.raceexp = raceexp;
	}

	public float getRaceMaxExp()
	{
		return racemaxexp;
	}

	public int getJobLevel()
	{
		return joblevel;
	}

	public void setJobLevel(int joblevel)
	{
		this.joblevel = joblevel;
	}

	public int getJobMaxLevel()
	{
		return jobmaxlevel;
	}

	public float getJobExp()
	{
		return jobexp;
	}

	public void setJobExp(float jobexp)
	{
		this.jobexp = jobexp;
	}

	public float getJobMaxExp()
	{
		return jobmaxexp;
	}

	public double getHealth()
	{
		return health;
	}

	public void setHealth(double health)
	{
		this.health = health;
	}

	public double getMaxHealth()
	{
		return maxhealth;
	}

	public void setMaxHealth(double maxhealth)
	{
		this.maxhealth = maxhealth;
	}

	public double getHealthRegen()
	{
		return healthregen;
	}

	public double getMaxMana()
	{
		return maxmana;
	}

	public void setMaxMana(double maxmana)
	{
		this.maxmana = maxmana;
	}

	public int getPoints()
	{
		return points;
	}

	public void setPoints(int points)
	{
		this.points = points;
	}

	public int getStrength()
	{
		return strength;
	}

	public void setStrength(int strength)
	{
		this.strength = strength;
	}

	public int getConstitution()
	{
		return constitution;
	}

	public void setConstitution(int constitution)
	{
		this.constitution = constitution;
	}

	public int getDexterity()
	{
		return dexterity;
	}

	public void setDexterity(int dexterity)
	{
		this.dexterity = dexterity;
	}

	public int getIntellect()
	{
		return intellect;
	}

	public void setIntellect(int intellect)
	{
		this.intellect = intellect;
	}

	public int getWisdom()
	{
		return wisdom;
	}

	public void setWisdom(int wisdom)
	{
		this.wisdom = wisdom;
	}

	public Set<Material> getArmor()
	{
		return armor;
	}

	public HashMap<Material, Integer> getWeapons()
	{
		return weapon;
	}

	public int getCastLevel(String name)
	{
		if (casts.containsKey(name))
		{
			return casts.get(name);
		}

		return 0;
	}

	public void interruptCasts(LivingEntity target)
	{
		if (target instanceof Player)
		{
			Caster caster = Casters.getCasters().get(target.getUniqueId());

			if (caster.getPlayer().leaveVehicle())
			{
				caster.setEffect("HogRiding", 0);
			}

			if (caster.isWarmingUp() || caster.isCasting())
			{
				caster.setInterrupted(true);

				if (caster.isWarmingUp())
				{
					caster.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				}

				if (caster.isCasting())
				{
					for (String cast : caster.getCasts().keySet())
					{
						if (caster.isCasting(cast))
						{
							caster.setCasting(cast, false);
						}
					}
				}

				List<Entity> entities = player.getNearbyEntities(16, 16, 16);

				for (Entity entity : entities)
				{
					if (entity instanceof Player)
					{
						entity.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + " " + player.getName() +
								ChatColor.GRAY + " Interrupts " + ChatColor.WHITE + caster.getPlayer().getName() + "'s" + ChatColor.GRAY + " Casting!");
					}
				}

				player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + " You" + ChatColor.GRAY +
						" Interrupt " + ChatColor.WHITE + caster.getPlayer().getName() + "'s" + ChatColor.GRAY + " Casting!");
			}
		}
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setEffect(String name, double duration)
	{
		if (effects.containsKey(name))
		{
			effects.get(name).setDuration(duration);
		}
	}

	public boolean isCasting()
	{
		return casting.containsValue(true);
	}

	public HashMap<String, Integer> getCasts()
	{
		return casts;
	}

	public void setCasting(String name, boolean casting)
	{
		this.casting.put(name, casting);
	}

	public boolean isInterrupted()
	{
		if (interrupted)
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					interrupted = false;
				}

			}.runTaskLater(Casters.getInstance(), 2);
		}

		return interrupted;
	}

	public void setInterrupted(boolean interrupted)
	{
		this.interrupted = interrupted;
	}

	public void setConfig()
	{
		config.set("Type", type.getName());
		config.set("Race", race.getName());
		config.set("Job", job.getName());
		config.set("Channel", channel);
		config.set("Title.Chat", chattitle);
		config.set("Title.Tab", tabtitle);
		config.set("Level.Type.Current", typelevel);
		config.set("Level.Type.Max", typemaxlevel);
		config.set("Level.Race.Current", racelevel);
		config.set("Level.Race.Max", racemaxlevel);
		config.set("Level.Job.Current", joblevel);
		config.set("Level.Job.Max", jobmaxlevel);
		config.set("Health.Current", player.getHealth());
		config.set("Health.Max", basemaxhealth);
		config.set("Mana.Current", mana);
		config.set("Mana.Max", basemaxmana);
		config.set("Mana.Regen", basemanaregen);
		config.set("Exp.Type.Current", typeexp);
		config.set("Exp.Type.Max", typemaxexp);
		config.set("Exp.Race.Current", raceexp);
		config.set("Exp.Race.Max", racemaxexp);
		config.set("Exp.Job.Current", jobexp);
		config.set("Exp.Job.Max", jobmaxexp);
		config.set("Stats.Points", points);
		config.set("Stats.Strength", strength);
		config.set("Stats.Constitution", constitution);
		config.set("Stats.Dexterity", dexterity);
		config.set("Stats.Intellect", intellect);
		config.set("Stats.Wisdom", wisdom);
		config.save();
	}

	public void setWarmingUp(String name, boolean warmingup)
	{
		this.warmingup.put(name, warmingup);
	}

	public void setTypeMaxExp()
	{
		typemaxexp = (float) (constant * Math.pow(typelevel, scale));
	}

	public void setRaceMaxExp()
	{
		racemaxexp = (float) (constant * Math.pow(racelevel, scale));
	}

	public void setJobMaxExp() // TODO: Check If Leveling Up Works.
	{
		jobmaxexp = (float) (constant * Math.pow(joblevel, scale));
	}

	public void setScale(double scale)
	{
		this.scale = scale;
	}

	public void setMana(double manacost)
	{
		mana -= manacost;
	}
}
