package com.github.zululi.mining.listener

import com.github.zululi.mining.Mining

import com.github.zululi.mining.Mining.vals.score
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent


object listener : Listener {
    var redscore = Mining.vals.redscore
    var bluescore = Mining.vals.bluescore
    private var gamestart = Mining.vals.gamestart



    @EventHandler
    fun playerdeath(e: PlayerDeathEvent) {
        redscore = Mining.vals.redscore
        bluescore = Mining.vals.bluescore
        val player = e.entity
        val playername = e.entity.name
        e.keepInventory = true
        e.drops.clear()
        val tscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
        val teams = tscoreboard?.getPlayerTeam(player)
        if (teams != null) {
            if (teams == tscoreboard.getTeam("red")) {
                //red team everyone
                player.sendMessage("${ChatColor.RED}-100 points")
                redscore -= 100
                Bukkit.broadcastMessage("${ChatColor.DARK_RED}$redscore")

            } else if (teams == tscoreboard.getTeam("blue")) {
                //blue team everyone
                player.sendMessage("${ChatColor.RED}-100 points")
                bluescore -= 100
                Bukkit.broadcastMessage("${ChatColor.DARK_BLUE}$bluescore")



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
            if (teams.equals(tscoreboard.getTeam("red"))) {
                e.format = "${ChatColor.RED}[R] %s" + ChatColor.WHITE + " : %s"
            } else if (teams.equals(tscoreboard.getTeam("blue"))) {
                e.format = "${ChatColor.BLUE}[B] %s" + ChatColor.WHITE + " : %s"
            } else {
                Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Error:team")
            }
        } else { e.format = "${ChatColor.WHITE}%s" + ChatColor.WHITE + " : %s"
        }
    }

    @EventHandler
    fun breakblockevent(event: BlockBreakEvent) {
        redscore = Mining.vals.redscore
        bluescore = Mining.vals.bluescore
        gamestart = Mining.vals.gamestart
        val player = event.player
        val uuid = event.player.name
        val block = event.block.type
        player.sendMessage(block.toString())
        when (gamestart) {
            0 -> {
                event.isCancelled = true
            }

            1 -> {
                val tscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
                val teams = tscoreboard?.getPlayerTeam(player)
                if (teams != null) {
                    if (teams == tscoreboard.getTeam("red")) {
                        when (block.toString()) {
                            "COAL_ORE" -> {


                                redscore += 5
                                val addpoint = 5
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)

                                score = mutableMapOf(uuid to (score[uuid]?.plus(addpoint) ?: 0))
                                player.sendMessage(""+(score[uuid]?.plus(addpoint) ?: 0))


                            }

                            "IRON_ORE" -> {

                                redscore += 10
                                val addpoint = 10
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_COAL_ORE" -> {

                                redscore += 7
                                val addpoint = 7
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_IRON_ORE" -> {

                                redscore += 15
                                val addpoint = 15
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "COPPER_ORE" -> {

                                redscore += 10
                                val addpoint = 10
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_COPPER_ORE" -> {

                                redscore += 15
                                val addpoint = 15
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "GOLD_ORE" -> {

                                redscore += 25
                                val addpoint = 25
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_GOLD_ORE" -> {

                                redscore += 30
                                val addpoint = 30
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "REDSTONE_ORE" -> {

                                redscore += 20
                                val addpoint = 20
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_REDSTONE_ORE" -> {

                                redscore += 25
                                val addpoint = 25
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "EMERALD_ORE" -> {

                                redscore += 50
                                val addpoint = 50
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_EMERALD_ORE" -> {

                                redscore += 75
                                val addpoint = 75
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "LAPIS_ORE" -> {

                                redscore += 30
                                val addpoint =30
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_LAPIS_ORE" -> {

                                redscore += 35
                                val addpoint = 35
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DIAMOND_ORE" -> {

                                redscore += 100
                                val addpoint = 100
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_DIAMOND_ORE" -> {

                                redscore += 125
                                val addpoint = 125
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                        }

                    } else if (teams == tscoreboard.getTeam("blue")) {
                        when (block.toString()) {
                            "COAL_ORE" -> {


                                bluescore += 5
                                val addpoint = 5
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)

                            }

                            "IRON_ORE" -> {

                                bluescore += 10
                                val addpoint = 10
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_COAL_ORE" -> {

                                bluescore += 7
                                val addpoint = 7
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_IRON_ORE" -> {

                                bluescore += 15
                                val addpoint = 15
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "COPPER_ORE" -> {

                                bluescore += 10
                                val addpoint = 10
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_COPPER_ORE" -> {

                                bluescore += 15
                                val addpoint = 15
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "GOLD_ORE" -> {

                                bluescore += 25
                                val addpoint = 25
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_GOLD_ORE" -> {

                                bluescore += 30
                                val addpoint = 30
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "REDSTONE_ORE" -> {

                                bluescore += 20
                                val addpoint = 20
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_REDSTONE_ORE" -> {

                                bluescore += 25
                                val addpoint = 25
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "EMERALD_ORE" -> {

                                bluescore += 50
                                val addpoint = 50
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_EMERALD_ORE" -> {

                                bluescore += 75
                                val addpoint = 75
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "LAPIS_ORE" -> {

                                bluescore += 30
                                val addpoint =30
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_LAPIS_ORE" -> {

                                bluescore += 35
                                val addpoint = 35
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DIAMOND_ORE" -> {

                                bluescore += 100
                                val addpoint = 100
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
                            }

                            "DEEPSLATE_DIAMOND_ORE" -> {

                                bluescore += 125
                                val addpoint = 125
                                val component = TextComponent()
                                component.text = "$block を採掘しました。(+$addpoint points)"
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
                                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f)
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
