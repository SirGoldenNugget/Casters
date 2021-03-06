package Casters;

import Casters.Casts.Actives.*;
import Casters.Casts.Actives.Projectiles.*;
import Casters.Casts.Cast;
import Casters.Casts.CastList;
import Casters.Casts.CastsCommands;
import Casters.Casts.CastsInventory;
import Casters.Casts.Passives.Firearms.PassiveBlunderbuss;
import Casters.Casts.Passives.Firearms.PassiveFlintlock;
import Casters.Casts.Passives.Firearms.PassiveMusket;
import Casters.Casts.Passives.PassiveBackstab;
import Casters.Casts.Passives.PassiveBerserk;
import Casters.Casts.Passives.PassiveFlameshield;
import Casters.Casts.Targetted.*;
import Casters.Commands.*;
import Casters.Configs.ConfigManager;
import Casters.Essentials.*;
import Casters.Essentials.Chat.Chat;
import Casters.Essentials.Chat.ChatChannel;
import Casters.Essentials.Chat.ChatTitles;
import Casters.Party.*;
import com.dbsoftware.titletabandbarapi.barapi.ActionBarManager;
import com.dbsoftware.titletabandbarapi.tabapi.TabManager;
import com.dbsoftware.titletabandbarapi.titleapi.TitleManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Casters extends JavaPlugin implements Listener
{
	private static Casters instance;

	private static ConfigManager manager;

	private static ActionBarManager actionbarmanager;
	private static TitleManager titlemanager;
	private static TabManager tabmanager;

	private static List<Type> types;
	private static List<Type> classes;
	private static List<Type> races;
	private static List<Type> jobs;
	private static List<Mob> mobs;

	private static HashMap<UUID, Caster> casters;

	private static CastersCommands casterscmd;
	private static CastersInfo castersinfo;
	private static CastersLevel casterslevel;
	private static CastersStats castersstats;
	private static CastersChoose casterschoose;
	private static CastersClasses castersclasses;
	private static CastersRaces castersraces;
	private static CastersJobs castersjobs;
	private static CastersArmor castersarmor;
	private static CastersWeapons castersweapon;
	private static CastersWhoIs casterswhois;
	private static CastersRecipes castersrecipes;

	private static Experience experience;
	private static Enchant enchant;
	private static Armor armor;
	private static Attack attack;
	private static Regen regen;

	private static Chat chat;
	private static ChatTitles chattitles;
	private static ChatChannel chatchannel;

	private static HashMap<String, Cast> casts;

	private static CastsCommands cast;
	private static CastList castlist;
	private static CastFireball castfireball;
	private static CastDarkBomb castdarkbomb;
	private static CastBolt castbolt;
	private static CastRevive castrevive;
	private static CastFireBomb castfirebomb;
	private static CastFireCharge castfirecharge;
	private static CastCharge castcharge;
	private static CastStrike caststrike;
	private static CastBandage castbandage;
	private static CastBeasts castbeasts;
	private static CastLightningStorm castlightningstorm;
	private static CastChainLightning castchainlightning;
	private static CastReflect castreflect;
	private static CastSiphon castsiphon;
	private static CastTaunt casttaunt;
	private static CastVanish castvanish;
	private static CastBomb castbomb;
	private static CastMount castmount;
	private static CastPoison castpoison;
	private static CastBash castbash;
	private static CastMute castmute;
	private static CastDefensiveStance castdefensivestance;
	private static CastChomp castchomp;
	private static CastSeeker castseeker;
	private static CastPressurePoint castpressurepoint;
	private static CastTrample casttrample;
	private static CastFrostFire castfrostfire;
	private static CastArcaneShot castarcaneshot;
	private static CastFireSpit castfirespit;
	private static CastDemonSpawn castdemonspawn;
	private static CastStampede caststampede;
	private static CastSpear castspear;
	private static CastFlash castflash;
	private static CastBlink castblink;
	private static CastWhirlwind castwhirlwind;
	private static CastHogRiders casthogriders;
	private static CastCleanse castcleanse;
	private static CastInspire castinspire;
	private static CastWarcry castwarcry;
	private static CastEntangle castentangle;
	private static CastDeepBreath castdeepbreath;
	private static CastHook casthook;

	private static PassiveBackstab passivebackstab;
	private static PassiveFlameshield passiveflameshield;
	private static PassiveFlintlock passiveflintlock;
	private static PassiveBlunderbuss passiveblunderbuss;
	private static PassiveMusket passivemusket;
	private static PassiveBerserk passiveberserk;

	private static CastsInventory castsinventory;

	private static List<Party> parties;

	private static PartyCommands partycmd;
	private static PartyCreate partycreate;
	private static PartyMembers partymembers;
	private static PartyInvite partyinvite;
	private static PartyRemove partyremove;
	private static PartyAccept partyaccept;
	private static PartyDecline partydecline;
	private static PartyChat partychat;
	private static PartyLeader partyleader;
	private static PartyLeave partyleave;
	private static PartyDisband partydisband;

	public static Casters getInstance()
	{
		return instance;
	}

	public static ActionBarManager getActionBarManager()
	{
		return actionbarmanager;
	}

	public static TitleManager getTitleManager()
	{
		return titlemanager;
	}

	public static TabManager getTabManager()
	{
		return tabmanager;
	}

	public static List<Type> getTypes()
	{
		return types;
	}

	public static List<Type> getClasses()
	{
		return classes;
	}

	public static List<Type> getRaces()
	{
		return races;
	}

	public static List<Type> getJobs()
	{
		return jobs;
	}

	public static List<Mob> getMobs()
	{
		return mobs;
	}

	public static HashMap<UUID, Caster> getCasters()
	{
		return casters;
	}

	public static HashMap<String, Cast> getCasts()
	{
		return casts;
	}

	public static ConfigManager getConfigManager()
	{
		return manager;
	}

	public static List<Party> getParties()
	{
		return parties;
	}

	@Override
	public void onDisable()
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			casters.get(player.getUniqueId()).setConfig();
			casters.remove(player.getUniqueId());
			player.getPlayer().leaveVehicle();

			player.kickPlayer(ChatColor.DARK_AQUA + "  " + ChatColor.BOLD + "CasterCraft" + ChatColor.GRAY + " Is Restarting!");
		}
	}

	@Override
	public void onEnable()
	{
		instance = this;

		manager = new ConfigManager(this);

		actionbarmanager = new ActionBarManager();
		titlemanager = new TitleManager();
		tabmanager = new TabManager();

		types = new ArrayList<Type>();
		classes = new ArrayList<Type>();
		races = new ArrayList<Type>();
		jobs = new ArrayList<Type>();

		Type wanderer = new Type("Wanderer", "Description");
		wanderer.setStrength(0);
		wanderer.setConstitution(0);
		wanderer.setDexterity(0);
		wanderer.setIntellect(0);
		wanderer.setWisdom(0);

		Type paladin = new Type("Paladin", "Description");
		paladin.setStrength(1);
		paladin.setConstitution(1);
		paladin.setDexterity(1);
		paladin.setIntellect(1);
		paladin.setWisdom(1);
		paladin.getArmor().add(Material.DIAMOND_HELMET);
		paladin.getArmor().add(Material.DIAMOND_CHESTPLATE);
		paladin.getArmor().add(Material.DIAMOND_LEGGINGS);
		paladin.getArmor().add(Material.DIAMOND_BOOTS);
		paladin.getWeapon().put(Material.SHIELD, 3);
		paladin.getWeapon().put(Material.DIAMOND_SWORD, 5);
		paladin.getWeapon().put(Material.IRON_SWORD, 4);
		paladin.getWeapon().put(Material.GOLD_SWORD, 3);
		paladin.getWeapon().put(Material.STONE_SWORD, 2);
		paladin.getWeapon().put(Material.WOOD_SWORD, 1);
		paladin.getCasts().put("Reflect", 1);
		paladin.getCasts().put("Bandage", 1);

		Type cavalier = new Type("Cavalier", "Description");
		cavalier.getArmor().add(Material.IRON_HELMET);
		cavalier.getArmor().add(Material.DIAMOND_CHESTPLATE);
		cavalier.getArmor().add(Material.CHAINMAIL_LEGGINGS);
		cavalier.getArmor().add(Material.DIAMOND_BOOTS);
		cavalier.getWeapon().put(Material.DIAMOND_SPADE, 10);
		cavalier.getWeapon().put(Material.IRON_SPADE, 7);
		cavalier.getWeapon().put(Material.GOLD_SPADE, 6);
		cavalier.getWeapon().put(Material.STONE_SPADE, 5);
		cavalier.getWeapon().put(Material.WOOD_SPADE, 4);
		cavalier.getCasts().put("Charge", 1);
		cavalier.getCasts().put("Mount", 1);
		cavalier.getCasts().put("Trample", 1);
		cavalier.getCasts().put("Stampede", 1);
		cavalier.getCasts().put("Spear", 1);

		Type barbarian = new Type("Barbarian", "Description");
		barbarian.getArmor().add(Material.DIAMOND_HELMET);
		barbarian.getArmor().add(Material.IRON_CHESTPLATE);
		barbarian.getArmor().add(Material.CHAINMAIL_LEGGINGS);
		barbarian.getArmor().add(Material.CHAINMAIL_BOOTS);
		barbarian.getWeapon().put(Material.DIAMOND_AXE, 10);
		barbarian.getWeapon().put(Material.IRON_AXE, 5);
		barbarian.getWeapon().put(Material.GOLD_AXE, 5);
		barbarian.getWeapon().put(Material.STONE_AXE, 5);
		barbarian.getWeapon().put(Material.WOOD_AXE, 5);
		barbarian.getCasts().put("Warcry", 1);
		barbarian.getCasts().put("Whirlwind", 1);
		barbarian.getCasts().put("HogRiders", 1);
		barbarian.getCasts().put("Berserk", 1);

		Type blackguard = new Type("Blackguard", "Description");
		blackguard.getArmor().add(Material.CHAINMAIL_HELMET);
		blackguard.getArmor().add(Material.IRON_CHESTPLATE);
		blackguard.getArmor().add(Material.IRON_LEGGINGS);
		blackguard.getArmor().add(Material.CHAINMAIL_BOOTS);
		blackguard.getWeapon().put(Material.DIAMOND_SWORD, 10);
		blackguard.getWeapon().put(Material.IRON_SWORD, 8);
		blackguard.getWeapon().put(Material.GOLD_SWORD, 7);
		blackguard.getWeapon().put(Material.STONE_SWORD, 6);
		blackguard.getWeapon().put(Material.WOOD_SWORD, 5);
		blackguard.getCasts().put("Strike", 1);
		blackguard.getCasts().put("Taunt", 1);
		blackguard.getCasts().put("Bash", 1);
		blackguard.getCasts().put("Mute", 1);
		blackguard.getCasts().put("Hook", 1);

		Type assassin = new Type("Assassin", "Description");
		assassin.getArmor().add(Material.GOLD_HELMET);
		assassin.getArmor().add(Material.LEATHER_CHESTPLATE);
		assassin.getArmor().add(Material.LEATHER_LEGGINGS);
		assassin.getArmor().add(Material.LEATHER_BOOTS);
		assassin.getWeapon().put(Material.DIAMOND_SWORD, 10);
		assassin.getWeapon().put(Material.IRON_SWORD, 8);
		assassin.getWeapon().put(Material.GOLD_SWORD, 7);
		assassin.getWeapon().put(Material.STONE_SWORD, 6);
		assassin.getWeapon().put(Material.WOOD_SWORD, 5);
		assassin.getCasts().put("Vanish", 1);
		assassin.getCasts().put("Backstab", 1);
		assassin.getCasts().put("Poison", 1);

		Type duelist = new Type("Duelist", "Description");
		duelist.getArmor().add(Material.CHAINMAIL_HELMET);
		duelist.getArmor().add(Material.LEATHER_CHESTPLATE);
		duelist.getArmor().add(Material.LEATHER_LEGGINGS);
		duelist.getArmor().add(Material.IRON_BOOTS);
		duelist.getWeapon().put(Material.DIAMOND_SWORD, 10);
		duelist.getWeapon().put(Material.IRON_SWORD, 8);
		duelist.getWeapon().put(Material.GOLD_SWORD, 7);
		duelist.getWeapon().put(Material.STONE_SWORD, 6);
		duelist.getWeapon().put(Material.WOOD_SWORD, 5);
		duelist.getCasts().put("Strike", 1);

		Type fletcher = new Type("Fletcher", "Description");
		fletcher.getArmor().add(Material.GOLD_HELMET);
		fletcher.getArmor().add(Material.LEATHER_CHESTPLATE);
		fletcher.getArmor().add(Material.LEATHER_LEGGINGS);
		fletcher.getArmor().add(Material.GOLD_BOOTS);
		fletcher.getWeapon().put(Material.BOW, 3);
		fletcher.getWeapon().put(Material.IRON_SWORD, 8);
		fletcher.getWeapon().put(Material.GOLD_SWORD, 7);
		fletcher.getWeapon().put(Material.STONE_SWORD, 6);
		fletcher.getWeapon().put(Material.WOOD_SWORD, 5);
		fletcher.getCasts().put("Beasts", 1);

		Type musketeer = new Type("Musketeer", "Description");
		musketeer.getArmor().add(Material.IRON_HELMET);
		musketeer.getArmor().add(Material.CHAINMAIL_CHESTPLATE);
		musketeer.getArmor().add(Material.LEATHER_LEGGINGS);
		musketeer.getArmor().add(Material.LEATHER_BOOTS);
		musketeer.getWeapon().put(Material.DIAMOND_BARDING, 7);
		musketeer.getWeapon().put(Material.IRON_BARDING, 6);
		musketeer.getWeapon().put(Material.GOLD_BARDING, 5);
		musketeer.getWeapon().put(Material.IRON_SWORD, 8);
		musketeer.getWeapon().put(Material.GOLD_SWORD, 7);
		musketeer.getWeapon().put(Material.STONE_SWORD, 6);
		musketeer.getWeapon().put(Material.WOOD_SWORD, 5);
		musketeer.getCasts().put("Bomb", 1);
		musketeer.getCasts().put("Flintlock", 1);
		musketeer.getCasts().put("Blunderbuss", 1);
		musketeer.getCasts().put("Musket", 1);

		Type distorter = new Type("Distorter", "Description");
		distorter.getArmor().add(Material.LEATHER_HELMET);
		distorter.getArmor().add(Material.LEATHER_CHESTPLATE);
		distorter.getArmor().add(Material.LEATHER_LEGGINGS);
		distorter.getArmor().add(Material.DIAMOND_BOOTS);
		distorter.getWeapon().put(Material.DIAMOND_HOE, 5);
		distorter.getWeapon().put(Material.IRON_HOE, 5);
		distorter.getWeapon().put(Material.GOLD_HOE, 5);
		distorter.getWeapon().put(Material.STONE_HOE, 5);
		distorter.getWeapon().put(Material.WOOD_HOE, 5);
		distorter.getCasts().put("DarkBomb", 1);
		distorter.getCasts().put("ArcaneShot", 1);
		distorter.getCasts().put("Blink", 1);

		Type inferno = new Type("Inferno", "Description");
		inferno.getArmor().add(Material.LEATHER_HELMET);
		inferno.getArmor().add(Material.LEATHER_CHESTPLATE);
		inferno.getArmor().add(Material.LEATHER_LEGGINGS);
		inferno.getArmor().add(Material.LEATHER_BOOTS);
		inferno.getWeapon().put(Material.DIAMOND_HOE, 5);
		inferno.getWeapon().put(Material.IRON_HOE, 5);
		inferno.getWeapon().put(Material.GOLD_HOE, 5);
		inferno.getWeapon().put(Material.STONE_HOE, 5);
		inferno.getWeapon().put(Material.WOOD_HOE, 5);
		inferno.getCasts().put("Fireball", 1);
		inferno.getCasts().put("FireCharge", 1);
		inferno.getCasts().put("FireBomb", 1);
		inferno.getCasts().put("Flameshield", 1);
		inferno.getCasts().put("FireSpit", 1);
		inferno.getCasts().put("Flash", 1);

		Type shaman = new Type("Shaman", "Description");
		shaman.getArmor().add(Material.CHAINMAIL_HELMET);
		shaman.getArmor().add(Material.CHAINMAIL_CHESTPLATE);
		shaman.getArmor().add(Material.LEATHER_LEGGINGS);
		shaman.getArmor().add(Material.LEATHER_BOOTS);
		shaman.getWeapon().put(Material.DIAMOND_HOE, 5);
		shaman.getWeapon().put(Material.IRON_HOE, 5);
		shaman.getWeapon().put(Material.GOLD_HOE, 5);
		shaman.getWeapon().put(Material.STONE_HOE, 5);
		shaman.getWeapon().put(Material.WOOD_HOE, 5);
		shaman.getCasts().put("Bolt", 1);
		shaman.getCasts().put("LightningStorm", 1);
		shaman.getCasts().put("ChainLightning", 1);
		shaman.getCasts().put("FrostFire", 1);

		Type warlock = new Type("Warlock", "Description");
		warlock.getArmor().add(Material.LEATHER_HELMET);
		warlock.getArmor().add(Material.LEATHER_CHESTPLATE);
		warlock.getArmor().add(Material.IRON_LEGGINGS);
		warlock.getArmor().add(Material.LEATHER_BOOTS);
		warlock.getWeapon().put(Material.DIAMOND_HOE, 5);
		warlock.getWeapon().put(Material.IRON_HOE, 5);
		warlock.getWeapon().put(Material.GOLD_HOE, 5);
		warlock.getWeapon().put(Material.STONE_HOE, 5);
		warlock.getWeapon().put(Material.WOOD_HOE, 5);
		warlock.getCasts().put("DarkBomb", 1);
		warlock.getCasts().put("Siphon", 1);
		warlock.getCasts().put("DemonSpawn", 1);

		Type oracle = new Type("Oracle", "Description");
		oracle.getArmor().add(Material.LEATHER_HELMET);
		oracle.getArmor().add(Material.LEATHER_CHESTPLATE);
		oracle.getArmor().add(Material.IRON_LEGGINGS);
		oracle.getArmor().add(Material.LEATHER_BOOTS);
		oracle.getWeapon().put(Material.DIAMOND_SPADE, 10);
		oracle.getWeapon().put(Material.IRON_SPADE, 7);
		oracle.getWeapon().put(Material.GOLD_SPADE, 6);
		oracle.getWeapon().put(Material.STONE_SPADE, 5);
		oracle.getWeapon().put(Material.WOOD_SPADE, 4);
		oracle.getCasts().put("Chomp", 1);
		oracle.getCasts().put("Seeker", 1);
		oracle.getCasts().put("Entangle", 1);

		Type bloodmage = new Type("Bloodmage", "Description");
		bloodmage.getArmor().add(Material.LEATHER_HELMET);
		bloodmage.getArmor().add(Material.LEATHER_CHESTPLATE);
		bloodmage.getArmor().add(Material.IRON_LEGGINGS);
		bloodmage.getArmor().add(Material.LEATHER_BOOTS);
		bloodmage.getWeapon().put(Material.DIAMOND_SPADE, 10);
		bloodmage.getWeapon().put(Material.IRON_SPADE, 7);
		bloodmage.getWeapon().put(Material.GOLD_SPADE, 6);
		bloodmage.getWeapon().put(Material.STONE_SPADE, 5);
		bloodmage.getWeapon().put(Material.WOOD_SPADE, 4);
		bloodmage.getCasts().put("Siphon", 1);

		Type monk = new Type("Monk", "Description");
		monk.getArmor().add(Material.LEATHER_HELMET);
		monk.getArmor().add(Material.LEATHER_CHESTPLATE);
		monk.getArmor().add(Material.IRON_LEGGINGS);
		monk.getArmor().add(Material.LEATHER_BOOTS);
		monk.getWeapon().put(Material.DIAMOND_SPADE, 10);
		monk.getWeapon().put(Material.IRON_SPADE, 7);
		monk.getWeapon().put(Material.GOLD_SPADE, 6);
		monk.getWeapon().put(Material.STONE_SPADE, 5);
		monk.getWeapon().put(Material.WOOD_SPADE, 4);
		monk.getCasts().put("DefensiveStance", 1);
		monk.getCasts().put("PressurePoint", 1);
		monk.getCasts().put("Inspire", 1);

		Type cleric = new Type("Cleric", "Description");
		cleric.getArmor().add(Material.LEATHER_HELMET);
		cleric.getArmor().add(Material.LEATHER_CHESTPLATE);
		cleric.getArmor().add(Material.IRON_LEGGINGS);
		cleric.getArmor().add(Material.LEATHER_BOOTS);
		cleric.getWeapon().put(Material.DIAMOND_SPADE, 10);
		cleric.getWeapon().put(Material.IRON_SPADE, 7);
		cleric.getWeapon().put(Material.GOLD_SPADE, 6);
		cleric.getWeapon().put(Material.STONE_SPADE, 5);
		cleric.getWeapon().put(Material.WOOD_SPADE, 4);
		cleric.getCasts().put("Revive", 1);
		cleric.getCasts().put("Cleanse", 1);

		classes.add(wanderer);
		classes.add(paladin);
		classes.add(cavalier);
		classes.add(barbarian);
		classes.add(blackguard);
		classes.add(assassin);
		classes.add(duelist);
		classes.add(fletcher);
		classes.add(musketeer);
		classes.add(distorter);
		classes.add(inferno);
		classes.add(shaman);
		classes.add(warlock);
		classes.add(oracle);
		classes.add(bloodmage);
		classes.add(monk);
		classes.add(cleric);

		Type soul = new Type("Soul", "Description");

		Type dwarf = new Type("Dwarf", "Description");
		dwarf.getArmor().add(Material.DIAMOND_HELMET);
		dwarf.getWeapon().put(Material.DIAMOND_PICKAXE, 5);

		Type human = new Type("Human", "Description");
		human.getCasts().put("Bandage", 1);

		Type elf = new Type("Elf", "Description");
		elf.getArmor().add(Material.GOLD_HELMET);
		elf.getWeapon().put(Material.BOW, 3);

		Type troll = new Type("Troll", "Description");
		troll.getArmor().add(Material.IRON_LEGGINGS);

		Type goblin = new Type("Goblin", "Description");
		goblin.getArmor().add(Material.LEATHER_CHESTPLATE);

		Type giant = new Type("Giant", "Description");
		giant.getArmor().add(Material.IRON_CHESTPLATE);

		Type undead = new Type("Undead", "Description");
		undead.getArmor().add(Material.GOLD_HELMET);
		undead.getCasts().put("DeepBreath", 1);

		Type demon = new Type("Demon", "Description");
		demon.getArmor().add(Material.GOLD_HELMET);

		races.add(soul);
		races.add(dwarf);
		races.add(human);
		races.add(elf);
		races.add(troll);
		races.add(goblin);
		races.add(giant);
		races.add(demon);
		races.add(undead);

		Type unemployed = new Type("Unemployed", "Description");

		Type alchemist = new Type("Alchemist", "Description");
		alchemist.getArmor().add(Material.GOLD_HELMET);

		Type enchanter = new Type("Enchanter", "Description");
		enchanter.getArmor().add(Material.GOLD_HELMET);

		Type blacksmith = new Type("Blacksmith", "Description");
		blacksmith.getArmor().add(Material.GOLD_HELMET);

		Type engineer = new Type("Engineer", "Description");
		engineer.getArmor().add(Material.GOLD_HELMET);

		Type artisan = new Type("Artisan", "Description");
		artisan.getArmor().add(Material.GOLD_HELMET);

		Type farmer = new Type("Farmer", "Description");
		farmer.getArmor().add(Material.GOLD_HELMET);

		Type miner = new Type("Miner", "Description");
		miner.getArmor().add(Material.GOLD_HELMET);

		jobs.add(unemployed);
		jobs.add(alchemist);
		jobs.add(enchanter);
		jobs.add(blacksmith);
		jobs.add(engineer);
		jobs.add(artisan);
		jobs.add(farmer);
		jobs.add(miner);

		types.addAll(classes);
		types.addAll(races);
		types.addAll(jobs);

		mobs = new ArrayList<Mob>();

		mobs.add(new Mob(EntityType.ZOMBIE, 20, 2));
		mobs.add(new Mob(EntityType.ENDERMAN, 20, 10));
		mobs.add(new Mob(EntityType.SKELETON, 20, 10));
		mobs.add(new Mob(EntityType.SPIDER, 20, 10));
		mobs.add(new Mob(EntityType.CAVE_SPIDER, 20, 10));

		casters = new HashMap<UUID, Caster>();

		casterscmd = new CastersCommands();
		castersinfo = new CastersInfo();
		casterslevel = new CastersLevel();
		castersstats = new CastersStats();
		casterschoose = new CastersChoose();
		castersclasses = new CastersClasses();
		castersraces = new CastersRaces();
		castersjobs = new CastersJobs();
		castersarmor = new CastersArmor();
		castersweapon = new CastersWeapons();
		casterswhois = new CastersWhoIs();
		castersrecipes = new CastersRecipes();

		cast = new CastsCommands();
		castlist = new CastList();
		casts = new HashMap<String, Cast>();

		casts.put("Fireball", castfireball = new CastFireball("Fireball", "Casts A Fireball"));
		casts.put("DarkBomb", castdarkbomb = new CastDarkBomb("DarkBomb", "Casts A Dark Bomb"));
		casts.put("Bolt", castbolt = new CastBolt("Bolt", "Strike Down Lightning"));
		casts.put("Revive", castrevive = new CastRevive("Revive", "Revive A Player"));
		casts.put("FireBomb", castfirebomb = new CastFireBomb("FireBomb", "Casts A Fire Bomb"));
		casts.put("FireCharge", castfirecharge = new CastFireCharge("FireCharge", "Casts A Fire Charge"));
		casts.put("Charge", castcharge = new CastCharge("Charge", "Charge Your Opponent"));
		casts.put("Strike", caststrike = new CastStrike("Strike", "Strike Your Opponent"));
		casts.put("Bandage", castbandage = new CastBandage("Bandage", "Bandage Yourself Or An Ally"));
		casts.put("Beasts", castbeasts = new CastBeasts("Beasts", "Summon A Pack Of Wolves"));
		casts.put("LightningStorm", castlightningstorm = new CastLightningStorm("LightningStorm", "Casts A Lightning Storm"));
		casts.put("ChainLightning", castchainlightning = new CastChainLightning("ChainLightning", "Consecutively Strikes Opponenets"));
		casts.put("Reflect", castreflect = new CastReflect("Reflect", "Relects All Incoming Damage"));
		casts.put("Siphon", castsiphon = new CastSiphon("Siphon", "Siphons Health From Your Opponent"));
		casts.put("Taunt", casttaunt = new CastTaunt("Taunt", "Taunt All Nearby Opponents"));
		casts.put("Vanish", castvanish = new CastVanish("Vanish", "Vanish In A Cloud Of Smoke"));
		casts.put("Bomb", castbomb = new CastBomb("Bomb", "Places A Explosive Device"));
		casts.put("Mount", castmount = new CastMount("Mount", "Mounts Onto A Horse"));
		casts.put("Poison", castpoison = new CastPoison("Poison", "Poisons Your Opponent"));
		casts.put("Bash", castbash = new CastBash("Bash", "Bash Your Opponent And Interrupt Casts"));
		casts.put("Mute", castmute = new CastMute("Mute", "Silence Your Opponent"));
		casts.put("DefensiveStance", castdefensivestance = new CastDefensiveStance("DefensiveStance", "Reduces The Damage Party Members Take."));
		casts.put("Chomp", castchomp = new CastChomp("Chomp", "Eat Up Your Opponent"));
		casts.put("Seeker", castseeker = new CastSeeker("Seeker", "Send A Shulker Bullet Onto Your Opponent"));
		casts.put("PressurePoint", castpressurepoint = new CastPressurePoint("PressurePoint", "Hit A Pressure Point And Stun"));
		casts.put("Trample", casttrample = new CastTrample("Trample", "Trample Your Opponent While On A Horse"));
		casts.put("FrostFire", castfrostfire = new CastFrostFire("FrostFire", "Fire A Frost Orb"));
		casts.put("ArcaneShot", castarcaneshot = new CastArcaneShot("ArcaneShot", "Launch A Enderpearl Wherever"));
		casts.put("FireSpit", castfirespit = new CastFireSpit("FireSpit", "Spit A Fireball Somewhere"));
		casts.put("DemonSpawn", castdemonspawn = new CastDemonSpawn("DemonSpawn", "Spawns A Demon For Your Opponents (Or Friend)"));
		casts.put("Stampede", caststampede = new CastStampede("Stampede", "Summon A Stampede Of Horses"));
		casts.put("Spear", castspear = new CastSpear("Spear", "Spear Your Enemy And Bring Them Over There"));
		casts.put("Flash", castflash = new CastFlash("Flash", "Flash A Ways Away"));
		casts.put("Blink", castblink = new CastBlink("Blink", "Blink A Ways Away"));
		casts.put("Whirlwind", castwhirlwind = new CastWhirlwind("Whirlwind", "Spin Furiously Damaging All Nearby Enemies"));
		casts.put("HogRiders", casthogriders = new CastHogRiders("HogRiders", "Mount All Your Party Members On Hogs"));
		casts.put("Cleanse", castcleanse = new CastCleanse("Cleanse", "Remove Debuffs From Your Party Members"));
		casts.put("Inspire", castinspire = new CastInspire("Inspire", "Inspire Your Allies And Strengthen Them"));
		casts.put("Warcry", castwarcry = new CastWarcry("Warcry", "Shout And March With Your Army"));
		casts.put("Entangle", castentangle = new CastEntangle("Entangle", "Root Your Opponent In Place"));
		casts.put("DeepBreath", castdeepbreath = new CastDeepBreath("DeepBreath", "Hold Your Breath For An Extended Period Of Time"));
		casts.put("Hook", casthook = new CastHook("Hook", "Pull Your Opponent Towards You"));

		casts.put("Backstab", passivebackstab = new PassiveBackstab("Backstab", "Attacks From Behind Deal More"));
		casts.put("Flameshield", passiveflameshield = new PassiveFlameshield("Flameshield", "Reduces Fire Damage Dealt To You"));
		casts.put("Flintlock", passiveflintlock = new PassiveFlintlock("Flintlock", "Fire A Flintlock Pistol"));
		casts.put("Blunderbuss", passiveblunderbuss = new PassiveBlunderbuss("Blunderbuss", "Fire A Blunderbuss Shotgun"));
		casts.put("Musket", passivemusket = new PassiveMusket("Musket", "Fire A Single Shot Musket"));
		casts.put("Berserk", passiveberserk = new PassiveBerserk("Berserk", "Deal More Damage As You Lose Health"));

		castsinventory = new CastsInventory();

		experience = new Experience();
		enchant = new Enchant();
		armor = new Armor();
		attack = new Attack();
		regen = new Regen();

		chat = new Chat();
		chattitles = new ChatTitles();
		chatchannel = new ChatChannel();

		parties = new ArrayList<Party>();
		partycmd = new PartyCommands();
		partycreate = new PartyCreate();
		partymembers = new PartyMembers();
		partyinvite = new PartyInvite();
		partyremove = new PartyRemove();
		partyaccept = new PartyAccept();
		partydecline = new PartyDecline();
		partychat = new PartyChat();
		partyleader = new PartyLeader();
		partyleave = new PartyLeave();
		partydisband = new PartyDisband();

		registerCommands();

		registerEvents(this, this, experience, enchant, armor, attack, regen, chat, castsinventory, castfireball, castdarkbomb, castrevive, castfirebomb, castfirecharge,
				castbeasts, castreflect, castvanish, castbomb, castmount, castpoison, castchomp, castseeker, castfrostfire, castarcaneshot, castfirespit, castdemonspawn,
				casthogriders, castentangle, casthook, passivebackstab, passiveflameshield, passiveflintlock, passiveblunderbuss, passivemusket, passiveberserk);
	}

	private void registerCommands()
	{
		CommandHandler castershandler = new CommandHandler();
		CommandHandler casthandler = new CommandHandler();
		CommandHandler castshandler = new CommandHandler();
		CommandHandler chathandler = new CommandHandler();
		CommandHandler partyhandler = new CommandHandler();

		castershandler.register("casters", casterscmd);
		castershandler.register("info", castersinfo);
		castershandler.register("level", casterslevel);
		castershandler.register("stats", castersstats);
		castershandler.register("choose", casterschoose);
		castershandler.register("classes", castersclasses);
		castershandler.register("races", castersraces);
		castershandler.register("jobs", castersjobs);
		castershandler.register("armor", castersarmor);
		castershandler.register("weapons", castersweapon);
		castershandler.register("whois", casterswhois);
		castershandler.register("recipes", castersrecipes);

		casthandler.register("cast", cast);
		casthandler.register("list", castlist);
		casthandler.register("fireball", castfireball);
		casthandler.register("darkbomb", castdarkbomb);
		casthandler.register("bolt", castbolt);
		casthandler.register("revive", castrevive);
		casthandler.register("firebomb", castfirebomb);
		casthandler.register("firecharge", castfirecharge);
		casthandler.register("charge", castcharge);
		casthandler.register("strike", caststrike);
		casthandler.register("bandage", castbandage);
		casthandler.register("beasts", castbeasts);
		casthandler.register("lightningstorm", castlightningstorm);
		casthandler.register("chainlightning", castchainlightning);
		casthandler.register("reflect", castreflect);
		casthandler.register("siphon", castsiphon);
		casthandler.register("taunt", casttaunt);
		casthandler.register("vanish", castvanish);
		casthandler.register("bomb", castbomb);
		casthandler.register("mount", castmount);
		casthandler.register("poison", castpoison);
		casthandler.register("bash", castbash);
		casthandler.register("mute", castmute);
		casthandler.register("defensivestance", castdefensivestance);
		casthandler.register("chomp", castchomp);
		casthandler.register("seeker", castseeker);
		casthandler.register("pressurepoint", castpressurepoint);
		casthandler.register("trample", casttrample);
		casthandler.register("frostfire", castfrostfire);
		casthandler.register("arcaneshot", castarcaneshot);
		casthandler.register("firespit", castfirespit);
		casthandler.register("demonspawn", castdemonspawn);
		casthandler.register("stampede", caststampede);
		casthandler.register("spear", castspear);
		casthandler.register("flash", castflash);
		casthandler.register("blink", castblink);
		casthandler.register("whirlwind", castwhirlwind);
		casthandler.register("hogriders", casthogriders);
		casthandler.register("cleanse", castcleanse);
		casthandler.register("inspire", castinspire);
		casthandler.register("warcry", castwarcry);
		casthandler.register("entangle", castentangle);
		casthandler.register("deepbreath", castdeepbreath);
		casthandler.register("hook", casthook);

		casthandler.register("backstab", passivebackstab);
		casthandler.register("flameshield", passiveflameshield);
		casthandler.register("flintlock", passiveflintlock);
		casthandler.register("blunderbuss", passiveblunderbuss);
		casthandler.register("musket", passivemusket);
		casthandler.register("berserk", passiveberserk);

		castshandler.register("casts", castsinventory);

		chathandler.register("chat", chat);
		chathandler.register("titles", chattitles);
		chathandler.register("channel", chatchannel);

		partyhandler.register("party", partycmd);
		partyhandler.register("create", partycreate);
		partyhandler.register("members", partymembers);
		partyhandler.register("invite", partyinvite);
		partyhandler.register("remove", partyremove);
		partyhandler.register("accept", partyaccept);
		partyhandler.register("decline", partydecline);
		partyhandler.register("chat", partychat);
		partyhandler.register("leader", partyleader);
		partyhandler.register("leave", partyleave);
		partyhandler.register("disband", partydisband);

		getCommand("casters").setExecutor(castershandler);
		getCommand("cast").setExecutor(casthandler);
		getCommand("casts").setExecutor(castshandler);
		getCommand("chat").setExecutor(chathandler);
		getCommand("party").setExecutor(partyhandler);
	}

	private void registerEvents(Plugin plugin, Listener... listeners)
	{
		for (Listener listener : listeners)
		{
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		casters.put(event.getPlayer().getUniqueId(), new Caster(event.getPlayer()));
		event.getPlayer().leaveVehicle();
		event.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "CasterCraft" + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + " >> " + ChatColor.WHITE +
				event.getPlayer().getName() + ChatColor.GREEN + " Has Joined The Server.");
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		casters.get(event.getPlayer().getUniqueId()).setConfig();
		casters.remove(event.getPlayer().getUniqueId());
		event.getPlayer().leaveVehicle();
		event.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "CasterCraft" + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + " >> " + ChatColor.WHITE +
				event.getPlayer().getName() + ChatColor.RED + " Has Left The Server.");
	}

	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event)
	{
		event.setLeaveMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "CasterCraft" + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + " >> " + ChatColor.WHITE +
				event.getPlayer().getName() + ChatColor.GRAY + " Was Kicked From The Server.");
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		event.setDeathMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Death" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " " +
				WordUtils.capitalize(event.getDeathMessage().replaceFirst(event.getEntity().getName(), ChatColor.WHITE + event.getEntity().getName() + ChatColor.GRAY)) + ".");
	}
}
