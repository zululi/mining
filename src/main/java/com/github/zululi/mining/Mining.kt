package com.github.zululi.mining

import com.github.zululi.mining.Mining.vals.score
import com.github.zululi.mining.listener.listener
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Team
import java.util.concurrent.TimeUnit


class Mining : JavaPlugin() , CommandExecutor {
    object vals {
        var redscore = 0
        var bluescore = 0
        var gamestart = 0
        var score: Map<String, Int> = HashMap()
    }
    var redscore = Mining.vals.redscore
    var bluescore = Mining.vals.bluescore
    var gamestart = vals.gamestart

    var startsec = -1
    var x = -5
    var z = -6

    private val board = Bukkit.getScoreboardManager()?.mainScoreboard
    private var red = board?.registerNewTeam("red")
    private var blue = board?.registerNewTeam("blue")


    private fun prepare() {
        object : BukkitRunnable() {
            override fun run() {
                if (x in -5..5 && z in -6..5) {
                    if (x in -5..4 && z == 5) {
                        x++
                        z = -6
                    }
                    z++
                } else {
                    this.cancel()
                }
                Bukkit.getConsoleSender().sendMessage("x: $x z:$z")
                val world = Bukkit.getWorld("world")
                val b: Block = world!!.getBlockAt(x, 149, z)
                val block = Material.getMaterial("WHITE_STAINED_GLASS")!!.createBlockData()
                b.blockData = block
            }
        }.runTaskTimer(this, 0, 0)
    }

    private fun tick() {
        object : BukkitRunnable() {
            override fun run() {
                //always
                for (player in Bukkit.getOnlinePlayers()) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 40.0
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 99999.0
                    player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 0.75
                    Bukkit.broadcastMessage(startsec.toString())
                }






                //
                when (gamestart) {
                    0 -> {

                        val allplayer = Bukkit.getOnlinePlayers().size
                        Bukkit.getScoreboardManager()?.mainScoreboard?.getObjective("player")?.unregister()
                        val pscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard?.registerNewObjective(
                            "player",
                            "Dummy",
                            "${ChatColor.GOLD}Mining Battle"
                        )
                        pscoreboard?.displaySlot = DisplaySlot.SIDEBAR
                        pscoreboard?.getScore(ChatColor.GOLD.toString() + "サーバー人数: " + allplayer)?.score = 0



                        if (startsec > -2) {

                            if (startsec > 0) {

                                    Bukkit.broadcastMessage("${ChatColor.GOLD}試合開始まで${ChatColor.RED}" + startsec + "${ChatColor.GOLD}秒")
                                    for (all in Bukkit.getOnlinePlayers()) {
                                        all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.25f, 1f)
                                    }

                                startsec--



                            } else if (startsec == 0) {
                                Bukkit.broadcastMessage("${ChatColor.GOLD}試合開始")
                                for (all in Bukkit.getOnlinePlayers()) {
                                    all.playSound(all.location, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                                    all.teleport(Location(all.location.world, 0.0, 150.0, 0.0))
                                    val helmet = ItemStack(Material.ELYTRA)
                                    all.inventory.chestplate = helmet
                                    all.inventory.setItem(0, ItemStack(Material.WOODEN_SWORD, 1))
                                    all.inventory.setItem(1, ItemStack(Material.WOODEN_PICKAXE, 1))
                                    all.inventory.setItem(2, ItemStack(Material.WOODEN_AXE, 1))
                                    all.inventory.setItem(3, ItemStack(Material.WOODEN_SHOVEL, 1))
                                    all.inventory.setItem(4, ItemStack(Material.BREAD, 10))
                                    all.gameMode = GameMode.SURVIVAL
                                    score = mutableMapOf(all.name to 0)
                                }
                                gamestart = 1
                                vals.gamestart = gamestart

                                gametick()



                            }
                        }

                    }

                    1 -> {

                        for (player in Bukkit.getOnlinePlayers()) {
                            val team: Team? = Bukkit.getScoreboardManager()?.mainScoreboard?.getEntryTeam(player.name)
                            val tscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
                            if (team == null) {
                                if (tscoreboard?.getTeam("blue")?.size!! >= tscoreboard.getTeam("red")?.size!!) {
                                    red?.addEntry(player.name)
                                } else {
                                    blue?.addEntry(player.name)
                                }

                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, 20)
    }
    fun gametick(){
        object : BukkitRunnable() {
            override fun run() {
                Bukkit.getScoreboardManager()?.mainScoreboard?.getObjective("main")?.unregister()
                val mscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard?.registerNewObjective(
                    "main",
                    "Dummy",
                    "${ChatColor.GOLD}Mining Battle"
                )
                mscoreboard?.displaySlot = DisplaySlot.SIDEBAR
                mscoreboard?.getScore("")?.score = 14
                mscoreboard?.getScore(ChatColor.GOLD.toString() + "${ChatColor.RED}Red : " + redscore)?.score =
                    13
                mscoreboard?.getScore(ChatColor.GOLD.toString() + "${ChatColor.BLUE}Blue : " + bluescore)?.score =
                    12
                mscoreboard?.getScore("")?.score =
                    11
                redscore = Mining.vals.redscore
                bluescore = Mining.vals.bluescore
            }
        }.runTaskTimer(this, 0, 1)
    }




    override fun onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage("plugin loaded")
        server.pluginManager.registerEvents(listener, this)
        val allplayer = Bukkit.getOnlinePlayers().size
        Bukkit.getScoreboardManager()?.mainScoreboard?.getObjective("player")?.unregister()
        val pscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard?.registerNewObjective(
            "player",
            "Dummy",
            "${ChatColor.GOLD}Mining Battle"
        )

        red?.setAllowFriendlyFire(false)
        blue?.setAllowFriendlyFire(false)
        red?.color = ChatColor.RED
        blue?.color = ChatColor.BLUE
        red?.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
        blue?.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
        red?.prefix = "${ChatColor.RED}[R] "
        blue?.prefix = "${ChatColor.BLUE}[B] "



        pscoreboard?.displaySlot = DisplaySlot.SIDEBAR
        pscoreboard?.getScore(ChatColor.GOLD.toString() + "サーバー人数: " + allplayer)?.score = 0

        tick()
        prepare()
        return
    }


    override fun onDisable() {
        gamestart = 0
        Bukkit.getScoreboardManager()?.mainScoreboard?.getObjective("player")?.unregister()
        Bukkit.getScoreboardManager()?.mainScoreboard?.getObjective("main")?.unregister()
        red?.unregister()
        blue?.unregister()
    }



    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (command.name) {
            "start" -> {
                startsec = 10
                val world = Bukkit.getWorld("world")
                val b: Block = world!!.getBlockAt(5, 149, 6)
                val block = Material.getMaterial("AIR")!!.createBlockData()
                b.blockData = block
            }

            "team-auto-warifuri" -> {
                if (args.isEmpty()) {
                    sender.sendMessage("${ChatColor.GREEN}自動で割り振りをしました。")
                    autowarifuri()


                } else {
                    sender.sendMessage("${ChatColor.RED}コマンドが不完全です。")
                }

            }
            "team-red-warifuri" ->{
                if (args.isEmpty()) {
                    sender.sendMessage("${ChatColor.RED}プレイヤーを入力してください")
                }else {
                    val player = args[0]
                    red?.addEntry(player)
                    sender.sendMessage("${args[0]}を赤チームにしました")
                }
            }
            "team-blue-warifuri" ->{
                if (args.isEmpty()) {
                    sender.sendMessage("${ChatColor.RED}プレイヤーを入力してください")
                }else {
                    val player = args[0]
                    blue?.addEntry(player)
                    sender.sendMessage("${args[0]}を青チームにしました")
                }
            }
            "team-empty"->{

                    removeentry()
                    sender.sendMessage("${ChatColor.GREEN}全員をチームから外しました。")

            }

        }
        return false
    }

    private fun autowarifuri() {
        for (all in Bukkit.getOnlinePlayers()) {
            val tscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
            if (tscoreboard != null) {
                if (tscoreboard.getTeam("blue")?.size!! >= tscoreboard.getTeam("red")?.size!!) {
                    red?.addEntry(all.name)
                } else {
                    blue?.addEntry(all.name)
                }
            }
            val intred = tscoreboard?.getTeam("red")?.size
            val intblue = tscoreboard?.getTeam("blue")?.size
            Bukkit.getConsoleSender().sendMessage("$intred|$intblue")
            Bukkit.getConsoleSender().sendMessage(all.name)


            val teams = tscoreboard?.getPlayerTeam(all)
            if (teams != null) {
                if (teams.equals(tscoreboard.getTeam("red"))) {
                    val redplayer = teams.name
                    all.sendMessage("${ChatColor.RED}レッド${ChatColor.YELLOW}になりました。")
                } else if (teams.equals(tscoreboard.getTeam("blue"))) {
                    all.sendMessage("${ChatColor.BLUE}ブルー${ChatColor.YELLOW}になりました。")
                } else {
                    Bukkit.broadcastMessage("ccccc")
                }
            }else{
            Bukkit.broadcastMessage("")
        }

        }


    }





    private fun removeentry() {
        for (player in Bukkit.getOnlinePlayers()) {
            if (player != null) {
                Bukkit.getConsoleSender().sendMessage("a")
                val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
                Bukkit.getConsoleSender().sendMessage("1")
                if (scoreboard?.getEntryTeam(player.name) != null) {
                    scoreboard.getEntryTeam("red")?.removeEntry(player.name)
                    red?.removeEntry(player.name)
                    blue?.removeEntry(player.name)

                }
            } else {
                Bukkit.getConsoleSender().sendMessage("nullっぽい")
            }
        }
    }
}