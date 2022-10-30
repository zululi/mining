package com.github.zululi.mining.listener

import com.github.zululi.mining.Mining
import com.github.zululi.mining.Mining.vals.gamestart
import com.github.zululi.mining.Mining.vals.pointdouble
import com.github.zululi.mining.Mining.vals.score
import com.github.zululi.mining.Mining.vals.truedamage
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta








object listener : Listener {
    var redscore = Mining.vals.redscore
    var bluescore = Mining.vals.bluescore

    @EventHandler
    fun item(e: PlayerDropItemEvent){
        val player = e.player

            val item = e.itemDrop
            player.sendMessage("lore : "+item.itemStack.itemMeta?.lore.toString())
        player.sendMessage("lore2 : "+item.itemStack.type)
            when (item.itemStack.itemMeta?.lore.toString()) {
                "[${ChatColor.GOLD}SoulBound]"->{
                    e.isCancelled = true
                    when (item.name){
                        "Wooden Sword"->{
                            Bukkit.broadcastMessage("aaaaaa")
                            player.inventory.remove(item.itemStack)

                        }
                    }

                }
            }

    }
    @EventHandler
    fun move(e: PlayerMoveEvent) {
        val player = e.player
        if (player.gameMode == GameMode.SURVIVAL) {
            val material = player.location.add(0.0, -1.0, 0.0).block.type.toString()
            if (material!="WHITE_STAINED_GLASS"){
                if (material != "AIR"){
                    val chestplatecheck = player.inventory.chestplate?.type.toString()
                    if (chestplatecheck=="ELYTRA"){
                        val helmet = ItemStack(Material.AIR)
                        player.inventory.chestplate = helmet
                    }

                }
            }
        }
    }

    @EventHandler
    fun damage(e: EntityDamageEvent) {
        val entity = e.entity
        if (entity is Player) {
            e.isCancelled = truedamage
        }
    }

    @EventHandler
    fun nohunger(event: FoodLevelChangeEvent) {
        when (gamestart) {
            0 -> {
                event.isCancelled = true
            }

            1 -> {}
        }
    }

    @EventHandler
    fun playerdeath(e: PlayerDeathEvent) {
        redscore = Mining.vals.redscore
        bluescore = Mining.vals.bluescore

        val player = e.entity
        val uuid = player.uniqueId
        e.keepInventory = false
        //e.drops.clear()
        val tscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
        val teams = tscoreboard?.getPlayerTeam(player)
        if (teams != null) {
            if (teams == tscoreboard.getTeam("red")) {
                //red team everyone
                player.sendMessage("${ChatColor.RED}-100 points")
                val addpoint = -100
                score.put(uuid, score[uuid]?.plus(addpoint) ?: 0)
                redscore -= 100
                Mining.autoitem(player)

            } else if (teams == tscoreboard.getTeam("blue")) {
                //blue team everyone
                player.sendMessage("${ChatColor.RED}-100 points")
                bluescore -= 100
                val addpoint = -100
                score.put(uuid, score[uuid]?.plus(addpoint) ?: 0)
                Mining.autoitem(player)

            } else {
                Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Error:team")
            }
        }
        Mining.vals.redscore = redscore
        Mining.vals.bluescore = bluescore

    }

    @EventHandler
    fun onChatEvent(e: AsyncPlayerChatEvent) {

        val player = e.player
        val tscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
        val teams = tscoreboard?.getPlayerTeam(player)
        if (teams != null) {
            if (teams == tscoreboard.getTeam("red")) {
                e.format = "${ChatColor.RED}[R] %s" + ChatColor.WHITE + " : %s"
            } else if (teams == tscoreboard.getTeam("blue")) {
                e.format = "${ChatColor.BLUE}[B] %s" + ChatColor.WHITE + " : %s"
            } else {
                Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Error:team")
            }
        } else {
            e.format = "${ChatColor.WHITE}%s" + ChatColor.WHITE + " : %s"
        }
    }

    @EventHandler
    fun breakblockevent(event: BlockBreakEvent) {
        redscore = Mining.vals.redscore
        bluescore = Mining.vals.bluescore
        gamestart = gamestart
        val player = event.player
        val uuid = player.uniqueId
        val block = event.block.type
        if (player.gameMode == GameMode.SURVIVAL) {


            event.isCancelled = true
            when (gamestart) {
                0 -> {
                }

                1 -> {
                    val blockid = event.block
                    val item = blockid.drops
                    for (i in item) {
                        player.inventory.addItem(i)
                    }
                    blockid.type = Material.AIR
                    player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f)
                    val tscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
                    val teams = tscoreboard?.getPlayerTeam(player)
                    if (teams != null) {
                        if (teams == tscoreboard.getTeam("red")) {
                            when (block.toString()) {
                                "COAL_ORE" -> {


                                    redscore += 5
                                    var addpoint = 5
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 5
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0


                                }

                                "IRON_ORE" -> {

                                    redscore += 10
                                    var addpoint = 10
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 10
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_COAL_ORE" -> {


                                    var addpoint = 7
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 7
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_IRON_ORE" -> {

                                    redscore += 15
                                    var addpoint = 15
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 15
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "COPPER_ORE" -> {

                                    redscore += 10
                                    var addpoint = 10
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 10
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_COPPER_ORE" -> {

                                    redscore += 15
                                    var addpoint = 15
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 15
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "GOLD_ORE" -> {

                                    redscore += 25
                                    var addpoint = 25
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 25
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_GOLD_ORE" -> {

                                    redscore += 30
                                    var addpoint = 30
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 30
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "REDSTONE_ORE" -> {

                                    redscore += 20
                                    var addpoint = 20
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 20
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_REDSTONE_ORE" -> {

                                    redscore += 25
                                    var addpoint = 25
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 25
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "EMERALD_ORE" -> {

                                    redscore += 50
                                    var addpoint = 50
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 50
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_EMERALD_ORE" -> {

                                    redscore += 75
                                    var addpoint = 75
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 75
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "LAPIS_ORE" -> {

                                    redscore += 30
                                    var addpoint = 30
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                        redscore += 30
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_LAPIS_ORE" -> {

                                    redscore += 35
                                    var addpoint = 35
                                    redscore += 35
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DIAMOND_ORE" -> {

                                    redscore += 100
                                    var addpoint = 100
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        redscore += 100
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_DIAMOND_ORE" -> {

                                    redscore += 125
                                    var addpoint = 125
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        redscore += 125
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                            }

                        } else if (teams == tscoreboard.getTeam("blue")) {
                            when (block.toString()) {
                                "COAL_ORE" -> {


                                    bluescore += 5
                                    var addpoint = 5
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 5
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0


                                }

                                "IRON_ORE" -> {

                                    bluescore += 10
                                    var addpoint = 10
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 10
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_COAL_ORE" -> {

                                    bluescore += 7
                                    var addpoint = 7
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 7
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_IRON_ORE" -> {

                                    bluescore += 15
                                    var addpoint = 15
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 15
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "COPPER_ORE" -> {

                                    bluescore += 10
                                    var addpoint = 10
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 10
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_COPPER_ORE" -> {

                                    bluescore += 15
                                    var addpoint = 15
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 15
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "GOLD_ORE" -> {

                                    bluescore += 25
                                    var addpoint = 25
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 25
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_GOLD_ORE" -> {

                                    bluescore += 30
                                    var addpoint = 30
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 30
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "REDSTONE_ORE" -> {

                                    bluescore += 20
                                    var addpoint = 20
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 20
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_REDSTONE_ORE" -> {

                                    bluescore += 25
                                    var addpoint = 25
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 25
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "EMERALD_ORE" -> {

                                    bluescore += 50
                                    var addpoint = 50
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 50
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_EMERALD_ORE" -> {

                                    bluescore += 75
                                    var addpoint = 75
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 75
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "LAPIS_ORE" -> {

                                    bluescore += 30
                                    var addpoint = 30
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 30
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_LAPIS_ORE" -> {

                                    bluescore += 35
                                    var addpoint = 35
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 35
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DIAMOND_ORE" -> {

                                    bluescore += 100
                                    var addpoint = 100
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 100
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0

                                }

                                "DEEPSLATE_DIAMOND_ORE" -> {

                                    bluescore += 125
                                    var addpoint = 125
                                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                                    if (pointdouble) {
                                        bluescore += 125
                                        addpoint *= 2
                                    }
                                    val component = TextComponent()
                                    component.text = "$block を採掘しました。(+$addpoint points)"
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                    score[uuid] = score[uuid]?.plus(addpoint) ?: 0


                                }

                            }

                        }
                    } else {
                        Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Error:team")
                    }

                }
            }

            Mining.vals.redscore = redscore
            Mining.vals.bluescore = bluescore
        }
    }
}
