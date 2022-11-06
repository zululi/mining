package com.github.zululi.mining

import com.github.zululi.mining.Mining.vals.min
import com.github.zululi.mining.Mining.vals.pointdouble
import com.github.zululi.mining.Mining.vals.score
import com.github.zululi.mining.Mining.vals.sec
import com.github.zululi.mining.Mining.vals.truedamage
import com.github.zululi.mining.listener.listener
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Team
import java.util.*


class Mining : JavaPlugin() , CommandExecutor {
    object vals {
        var redscore = 0
        var bluescore = 0
        var gamestart = 0
        var score= mutableMapOf<UUID,Int>()
        var ranking: MutableMap<UUID, Long> = mutableMapOf()
        var min = 15
        var sec = 0
        var truedamage = true
        var pointdouble = false
    }

    var redscore = vals.redscore
    var bluescore = vals.bluescore
    var gamestart = vals.gamestart
    var startsec = -1
    var x = -5
    var z = -6


    private val board = Bukkit.getScoreboardManager()?.mainScoreboard
    private var red = board?.registerNewTeam("red")
    private var blue = board?.registerNewTeam("blue")

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

    }

    override fun onDisable() {
        gamestart = 0
        Bukkit.getScoreboardManager()?.mainScoreboard?.getObjective("player")?.unregister()
        Bukkit.getScoreboardManager()?.mainScoreboard?.getObjective("main")?.unregister()
        red?.unregister()
        blue?.unregister()
    }



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
                val b: Block = world!!.getBlockAt(x, 255, z)
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
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = 99.0
                    val world: World = player.world
                    world.setSpawnLocation(0,256,0)
                    world.difficulty = Difficulty.EASY
                    world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
                    world.setGameRule(GameRule.SPAWN_RADIUS, 0)
                    world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                    world.setGameRule(GameRule.DO_INSOMNIA, false)

                }


                //
                when (gamestart) {
                    0 -> {

                        val allplayer = Bukkit.getOnlinePlayers().size
                        Bukkit.getScoreboardManager()?.mainScoreboard?.getObjective("player")?.unregister()
                        val pscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard?.registerNewObjective(
                            "player",
                            "Dummy",
                            " ${ChatColor.GOLD}Mining Battle ${ChatColor.GRAY}1.1 "
                        )
                        pscoreboard?.displaySlot = DisplaySlot.SIDEBAR
                        pscoreboard?.getScore(ChatColor.GOLD.toString() + " サーバー人数: " + allplayer)?.score = 0
                        for (player in Bukkit.getOnlinePlayers()) {
                            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = -5.0
                            val world = player.world
                            world.time = 6000;
                            player.health = (20.0)
                        }



                        if (startsec > -2) {

                            if (startsec > 0) {

                                Bukkit.broadcastMessage("${ChatColor.GOLD}試合開始まで${ChatColor.RED}" + startsec + "${ChatColor.GOLD}秒")
                                for (all in Bukkit.getOnlinePlayers()) {
                                    all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                                }

                                startsec--


                            } else if (startsec == 0) {
                                Bukkit.broadcastMessage("${ChatColor.GOLD}試合開始")
                                for (all in Bukkit.getOnlinePlayers()) {
                                    all.health = (20.0)
                                    all.saturation = (20.0F)
                                    all.playSound(all.location, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                                    all.teleport(Location(all.location.world, 0.0, 256.0, 0.0))
                                    all.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 6))


                                    val helmet = ItemStack(Material.ELYTRA)
                                    val metadata = helmet.itemMeta
                                    metadata?.isUnbreakable = true
                                    helmet.itemMeta = metadata
                                    all.inventory.chestplate = helmet

                                    val woodensword = ItemStack(Material.WOODEN_SWORD)
                                    val metadatasword = woodensword.itemMeta
                                    metadatasword?.isUnbreakable = true
                                    val l0: MutableList<String> = ArrayList()
                                    l0.add("${ChatColor.GOLD}SoulBound")
                                    metadatasword?.lore = (l0)
                                    woodensword.itemMeta = metadatasword
                                    all.inventory.setItem(0,woodensword)

                                    val woodenpickaxe = ItemStack(Material.WOODEN_PICKAXE)
                                    val metadatapickaxe = woodenpickaxe.itemMeta
                                    metadatapickaxe?.isUnbreakable = true
                                    val l1: MutableList<String> = ArrayList()
                                    l1.add("${ChatColor.GOLD}SoulBound")
                                    metadatapickaxe?.lore = (l1)
                                    woodenpickaxe.itemMeta = metadatapickaxe
                                    all.inventory.setItem(1,woodenpickaxe)
                                    
                                    val woodenaxe = ItemStack(Material.WOODEN_AXE)
                                    val metadataaxe = woodenaxe.itemMeta
                                    val modifier = AttributeModifier(
                                        UUID.randomUUID(),
                                        "generic.attackDamage",
                                        2.0,
                                        AttributeModifier.Operation.ADD_NUMBER,
                                        EquipmentSlot.HAND
                                    )
                                    metadataaxe?.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier)
                                    metadataaxe?.isUnbreakable = true
                                    val l2: MutableList<String> = ArrayList()
                                    l2.add("${ChatColor.GOLD}SoulBound")
                                    metadataaxe?.lore = (l2)
                                    woodenaxe.itemMeta = metadataaxe
                                    all.inventory.setItem(2,woodenaxe)

                                    
                                    val woodenshovel = ItemStack(Material.WOODEN_SHOVEL)
                                    val metadatashovel = woodenshovel.itemMeta
                                    metadatashovel?.isUnbreakable = true
                                    val l3: MutableList<String> = ArrayList()
                                    l3.add("${ChatColor.GOLD}SoulBound")
                                    metadatashovel?.lore = (l3)
                                    woodenshovel.itemMeta = metadatashovel
                                    all.inventory.setItem(3,woodenshovel)

                                    all.inventory.setItem(4,ItemStack(Material.BREAD,10))

                                    all.gameMode = GameMode.SURVIVAL
                                    score = mutableMapOf(all.uniqueId to 0)
                                    min = 15
                                    sec = 0
                                }
                                gamestart = 1
                                Mining.vals.gamestart = 1
                                gametick()




                            }
                        }

                    }

                    1 -> {
                        if (min > -1) {
                            sec--
                            score()
                        }
                        if (sec < 0 && min > 0) {
                            sec = 59
                            min--

                        } else if (sec == 0 && min == 0) {
                            finish()
                        }
                        if (min == 15 && sec in 1..5){
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 10 && sec == 0) {
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$min${ChatColor.YELLOW}分")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if (min == 5&& sec in 1..5){
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 5 && sec == 0){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$min${ChatColor.YELLOW}分")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 3 && sec == 0){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$min${ChatColor.YELLOW}分")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 2 && sec == 0){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$min${ChatColor.YELLOW}分")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 1 && sec == 0){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$min${ChatColor.YELLOW}分")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 0 && sec == 30){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$sec${ChatColor.YELLOW}秒")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 0 && sec == 10){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$sec${ChatColor.YELLOW}秒")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 0 && sec == 5){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$sec${ChatColor.YELLOW}秒")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 0 && sec == 4){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$sec${ChatColor.YELLOW}秒")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 0 && sec == 3){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$sec${ChatColor.YELLOW}秒")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 0 && sec == 2){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$sec${ChatColor.YELLOW}秒")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }else if(min == 0 && sec == 1){
                            Bukkit.broadcastMessage("${ChatColor.YELLOW}試合終了まであと${ChatColor.RED}$sec${ChatColor.YELLOW}秒")
                            for (all in Bukkit.getOnlinePlayers()) {
                                all.playSound(all.location, Sound.UI_BUTTON_CLICK, 0.1f, 1f)
                            }
                        }
                        for (player in Bukkit.getOnlinePlayers()) {
                            val team: Team? = Bukkit.getScoreboardManager()?.mainScoreboard?.getEntryTeam(player.name)
                            val tscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard


                            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 0.75

                            if (team == null) {
                                if (tscoreboard?.getTeam("blue")?.size!! >= tscoreboard.getTeam("red")?.size!!) {
                                    red?.addEntry(player.name)
                                    val world = player.location.world
                                    player.teleport(Location(world, 0.0, 256.0, 0.0))
                                    player.inventory.clear()




                                    player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 6))
                                    val helmet = ItemStack(Material.ELYTRA)
                                    val metadata = helmet.itemMeta
                                    metadata?.isUnbreakable = true
                                    helmet.itemMeta = metadata
                                    player.inventory.chestplate = helmet

                                    val woodensword = ItemStack(Material.WOODEN_SWORD)
                                    val metadatasword = woodensword.itemMeta
                                    metadatasword?.isUnbreakable = true
                                    val l01: MutableList<String> = ArrayList()
                                    l01.add("${ChatColor.GOLD}SoulBound")
                                    metadatasword?.lore = (l01)
                                    woodensword.itemMeta = metadatasword
                                    player.inventory.setItem(0,woodensword)

                                    val woodenpickaxe = ItemStack(Material.WOODEN_PICKAXE)
                                    val metadatapickaxe = woodenpickaxe.itemMeta
                                    metadatapickaxe?.isUnbreakable = true
                                    val l11: MutableList<String> = ArrayList()
                                    l11.add("${ChatColor.GOLD}SoulBound")
                                    metadatapickaxe?.lore = (l11)
                                    woodenpickaxe.itemMeta = metadatapickaxe
                                    player.inventory.setItem(1,woodenpickaxe)

                                    val woodenaxe = ItemStack(Material.WOODEN_AXE)
                                    val metadataaxe = woodenaxe.itemMeta
                                    metadataaxe?.isUnbreakable = true
                                    val modifier = AttributeModifier(
                                        UUID.randomUUID(),
                                        "generic.attackDamage",
                                        2.0,
                                        AttributeModifier.Operation.ADD_NUMBER,
                                        EquipmentSlot.HAND
                                    )
                                    metadataaxe?.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier)
                                    val l21: MutableList<String> = ArrayList()
                                    l21.add("${ChatColor.GOLD}SoulBound")
                                    metadataaxe?.lore = (l21)
                                    woodenaxe.itemMeta = metadataaxe
                                    player.inventory.setItem(2,woodenaxe)


                                    val woodenshovel = ItemStack(Material.WOODEN_SHOVEL)
                                    val metadatashovel = woodenshovel.itemMeta
                                    metadatashovel?.isUnbreakable = true
                                    val l31: MutableList<String> = ArrayList()
                                    l31.add("${ChatColor.GOLD}SoulBound")
                                    metadatashovel?.lore = (l31)
                                    woodenshovel.itemMeta = metadatashovel
                                    player.inventory.setItem(3,woodenshovel)


                                    player.inventory.setItem(4,ItemStack(Material.BREAD,10))
                                } else {
                                    blue?.addEntry(player.name)
                                    val world = player.location.world
                                    player.teleport(Location(world, 0.0, 256.0, 0.0))
                                    player.inventory.clear()


                                    player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 6))
                                    val helmet = ItemStack(Material.ELYTRA)
                                    val metadata = helmet.itemMeta
                                    metadata?.isUnbreakable = true
                                    helmet.itemMeta = metadata
                                    player.inventory.chestplate = helmet

                                    val woodensword = ItemStack(Material.WOODEN_SWORD)
                                    val metadatasword = woodensword.itemMeta
                                    metadatasword?.isUnbreakable = true
                                    val l01: MutableList<String> = ArrayList()
                                    l01.add("${ChatColor.GOLD}SoulBound")
                                    metadatasword?.lore = (l01)
                                    woodensword.itemMeta = metadatasword
                                    player.inventory.setItem(0,woodensword)

                                    val woodenpickaxe = ItemStack(Material.WOODEN_PICKAXE)
                                    val metadatapickaxe = woodenpickaxe.itemMeta
                                    metadatapickaxe?.isUnbreakable = true
                                    val l11: MutableList<String> = ArrayList()
                                    l11.add("${ChatColor.GOLD}SoulBound")
                                    metadatapickaxe?.lore = (l11)
                                    woodenpickaxe.itemMeta = metadatapickaxe
                                    player.inventory.setItem(1,woodenpickaxe)

                                    val woodenaxe = ItemStack(Material.WOODEN_AXE)
                                    val metadataaxe = woodenaxe.itemMeta
                                    metadataaxe?.isUnbreakable = true
                                    val modifier = AttributeModifier(
                                        UUID.randomUUID(),
                                        "generic.attackDamage",
                                        2.0,
                                        AttributeModifier.Operation.ADD_NUMBER,
                                        EquipmentSlot.HAND
                                    )
                                    metadataaxe?.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier)
                                    val l21: MutableList<String> = ArrayList()
                                    l21.add("${ChatColor.GOLD}SoulBound")
                                    metadataaxe?.lore = (l21)
                                    woodenaxe.itemMeta = metadataaxe
                                    player.inventory.setItem(2,woodenaxe)


                                    val woodenshovel = ItemStack(Material.WOODEN_SHOVEL)
                                    val metadatashovel = woodenshovel.itemMeta
                                    metadatashovel?.isUnbreakable = true
                                    val l31: MutableList<String> = ArrayList()
                                    l31.add("${ChatColor.GOLD}SoulBound")
                                    metadatashovel?.lore = (l31)
                                    woodenshovel.itemMeta = metadatashovel
                                    player.inventory.setItem(3,woodenshovel)


                                    player.inventory.setItem(4,ItemStack(Material.BREAD,10))
                                }

                            }





                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, 20)
    }

    fun gametick() {
        object : BukkitRunnable() {
            override fun run() {
                Bukkit.getScoreboardManager()?.mainScoreboard?.getObjective("main")?.unregister()
                val mscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard?.registerNewObjective(
                    "main",
                    "Dummy",
                    " ${ChatColor.GOLD}Mining Battle ${ChatColor.GRAY}1.1 "
                )
                mscoreboard?.displaySlot = DisplaySlot.SIDEBAR
                mscoreboard?.getScore("${ChatColor.GOLD}  残り時間")?.score = 3
                if (sec in 0..9) {
                    mscoreboard?.getScore("${ChatColor.YELLOW}               $min : 0$sec")?.score = 2
                }else{
                    mscoreboard?.getScore("${ChatColor.YELLOW}               $min : $sec")?.score = 2
                }

                mscoreboard?.getScore(ChatColor.GOLD.toString() + "${ChatColor.RED}  レッド ${ChatColor.GRAY} >   ${ChatColor.YELLOW}" + redscore)?.score =
                    12
                mscoreboard?.getScore(ChatColor.GOLD.toString() + "${ChatColor.BLUE}  ブルー ${ChatColor.GRAY} >   ${ChatColor.YELLOW}" + bluescore)?.score =
                    11
                mscoreboard?.getScore(ChatColor.GOLD.toString() + "")?.score = 15
                mscoreboard?.getScore(ChatColor.GOLD.toString() + " ")?.score = 13
                mscoreboard?.getScore(ChatColor.GOLD.toString() + "  ")?.score = 9
                mscoreboard?.getScore(ChatColor.GOLD.toString() + "   ")?.score = 1
                if (min>9&&sec in 10..59){
                    mscoreboard?.getScore("  ${ChatColor.YELLOW}無敵時間終了まで  ${ChatColor.GREEN}"+(min-10)+":"+sec)?.score = 14
                }else if(min>9&&sec in 0..9) {
                    mscoreboard?.getScore("  ${ChatColor.YELLOW}無敵時間終了まで  ${ChatColor.GREEN}" + (min - 10)+":0" + sec)?.score =
                        14
                }else if (min>4&&sec in 10..59){
                    mscoreboard?.getScore("  ${ChatColor.YELLOW}ポイント2倍まで  ${ChatColor.GREEN}"+(min-5)+":"+sec)?.score = 14
                }else if (min>4&&sec in 0..9){
                    mscoreboard?.getScore("  ${ChatColor.YELLOW}ポイント2倍まで  ${ChatColor.GREEN}"+(min-5)+":0"+sec)?.score = 14
                }
                redscore = vals.redscore
                bluescore = vals.bluescore

            }
        }.runTaskTimer(this, 0, 5)
    }








    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (command.name) {
            "start" -> {
                if (sender.isOp) {
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.inventory.clear()
                    }
                    startsec = 10
                    val world = Bukkit.getWorld("world")
                    val b: Block = world!!.getBlockAt(5, 255, 6)
                    val block = Material.getMaterial("AIR")!!.createBlockData()
                    b.blockData = block
                }
            }

            "team-auto-warifuri" -> {
                if (sender.isOp) {
                    if (args.isEmpty()) {
                        sender.sendMessage("${ChatColor.GREEN}自動で割り振りをしました。")
                        autowarifuri()


                    } else {
                        sender.sendMessage("${ChatColor.RED}コマンドが不完全です。")
                    }
                }
            }

            "team-red-warifuri" -> {
                if (sender.isOp) {
                    if (args.isEmpty()) {
                        sender.sendMessage("${ChatColor.RED}プレイヤーを入力してください")
                    } else {
                        val player = args[0]
                        red?.addEntry(player)
                        sender.sendMessage("${args[0]}を赤チームにしました")
                    }
                }
            }

            "team-blue-warifuri" -> {
                if (sender.isOp) {
                if (args.isEmpty()) {
                    sender.sendMessage("${ChatColor.RED}プレイヤーを入力してください")
                } else {
                    val player = args[0]
                    blue?.addEntry(player)
                    sender.sendMessage("${args[0]}を青チームにしました")
                }
                }
            }

            "team-empty" -> {
                if (sender.isOp) {
                    removeentry()
                    sender.sendMessage("${ChatColor.GREEN}全員をチームから外しました。")
                }
            }

            "quick-start" -> {
                if (sender.isOp) {

                    Bukkit.broadcastMessage("${ChatColor.GOLD}試合開始")
                    for (all in Bukkit.getOnlinePlayers()) {
                        all.inventory.clear()
                        all.playSound(all.location, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
                        all.teleport(Location(all.location.world, 0.0, 256.0, 0.0))
                        val helmet = ItemStack(Material.ELYTRA)
                        all.inventory.chestplate = helmet
                        all.inventory.setItem(0, ItemStack(Material.WOODEN_SWORD, 1))
                        all.inventory.setItem(1, ItemStack(Material.WOODEN_PICKAXE, 1))
                        all.inventory.setItem(2, ItemStack(Material.WOODEN_AXE, 1))
                        all.inventory.setItem(3, ItemStack(Material.WOODEN_SHOVEL, 1))
                        all.inventory.setItem(4, ItemStack(Material.BREAD, 10))
                        all.gameMode = GameMode.SURVIVAL
                        score = mutableMapOf(all.uniqueId to 0)
                        min = 15
                        sec = 0
                    }
                    gamestart = 1
                    vals.gamestart = gamestart

                    gametick()
                }

            }
            "set-time"->{
                if (sender.isOp) {
                    if (sender.isOp) {
                        min = args[0].toInt()
                        sec = args[1].toInt()
                    }
                }
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
                if (teams == tscoreboard.getTeam("red")) {
                    all.sendMessage("${ChatColor.RED}レッド${ChatColor.YELLOW}になりました。")
                } else if (teams == tscoreboard.getTeam("blue")) {
                    all.sendMessage("${ChatColor.BLUE}ブルー${ChatColor.YELLOW}になりました。")
                } else {
                    Bukkit.broadcastMessage("ccccc")
                }
            } else {
                Bukkit.broadcastMessage("")
            }

        }


    }

    fun finish() {
        gamestart = 2
        vals.gamestart = gamestart
        for (all in Bukkit.getOnlinePlayers()) {
            all.playSound(all.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.3f, 1f)
            all.playSound(all.location, Sound.ENTITY_GENERIC_EXPLODE, 0.1f, 1f)
            all.health = (20.0)
            all.saturation = (20.0F)
            truedamage = true
            all.gameMode = GameMode.CREATIVE
        }
        Bukkit.broadcastMessage("${ChatColor.GOLD}試合終了")

        if (redscore>bluescore) {
            Bukkit.broadcastMessage("${ChatColor.GOLD}------------------")
            Bukkit.broadcastMessage("${ChatColor.RED}   レッド${ChatColor.GRAY} > ${ChatColor.YELLOW}$redscore")
            Bukkit.broadcastMessage("${ChatColor.BLUE}   ブルー${ChatColor.GRAY} > ${ChatColor.YELLOW}$bluescore")
            Bukkit.broadcastMessage("${ChatColor.GOLD}  よって${ChatColor.RED}赤${ChatColor.GOLD}の勝ちです")
            Bukkit.broadcastMessage("${ChatColor.GOLD}------------------")
        } else if (redscore<bluescore){
            Bukkit.broadcastMessage("${ChatColor.GOLD}------------------")
            Bukkit.broadcastMessage("${ChatColor.RED}   レッド${ChatColor.GRAY} > ${ChatColor.YELLOW}$redscore")
            Bukkit.broadcastMessage("${ChatColor.BLUE}   ブルー${ChatColor.GRAY} > ${ChatColor.YELLOW}$bluescore")
            Bukkit.broadcastMessage("${ChatColor.GOLD}  よって${ChatColor.BLUE}青${ChatColor.GOLD}の勝ちです")
            Bukkit.broadcastMessage("${ChatColor.GOLD}------------------")
        } else if (redscore==bluescore){
            Bukkit.broadcastMessage("${ChatColor.GOLD}------------------")
            Bukkit.broadcastMessage("${ChatColor.RED}   レッド${ChatColor.GRAY} > ${ChatColor.YELLOW}$redscore")
            Bukkit.broadcastMessage("${ChatColor.BLUE}   ブルー${ChatColor.GRAY} > ${ChatColor.YELLOW}$bluescore")
            Bukkit.broadcastMessage("${ChatColor.GOLD}  よって${ChatColor.LIGHT_PURPLE}引き分け${ChatColor.GOLD}です")
            Bukkit.broadcastMessage("${ChatColor.GOLD}------------------")
        }






        val ranking: MutableMap<Int?, String> = TreeMap { m: Int?, n: Int? ->
            m!!.compareTo(
               n!!
            ) * -1
        }

        for (name in score.keys) {
            ranking[score[name]] = name.toString()
        }
        for (player in Bukkit.getOnlinePlayers()) {
            var i = 1
            for (nKey in ranking.keys) {
                val playername = Bukkit.getPlayer(UUID.fromString(ranking[nKey]))?.name
                player.sendMessage(ChatColor.YELLOW.toString() + i + ". " + ChatColor.AQUA + playername + ChatColor.WHITE + "(" + nKey + ")")
                i += 1
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
                    scoreboard.getEntryTeam("blue")?.removeEntry(player.name)

                }
            } else {
                Bukkit.getConsoleSender().sendMessage("nullっぽい")
            }
        }
    }


    fun score(){

        if (min==10&&sec==0){
            Bukkit.broadcastMessage("${ChatColor.GOLD} 無敵時間が終了しました。")
            truedamage = false
            for (all in Bukkit.getOnlinePlayers()){
                all.playSound(all.location, Sound.ENTITY_WITHER_AMBIENT,0.25f,2.0f)
            }
        }else if (min==5&&sec==0){
            Bukkit.broadcastMessage("${ChatColor.GOLD} ポイントが2倍になりました。")
            pointdouble = true
            for (all in Bukkit.getOnlinePlayers()){
                all.playSound(all.location, Sound.ENTITY_WITHER_AMBIENT,0.25f,2.0f)
            }
        }
    }

}
