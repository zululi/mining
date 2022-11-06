package com.github.zululi.mining.listener

import com.github.zululi.mining.Mining
import com.github.zululi.mining.Mining.vals.gamestart
import com.github.zululi.mining.Mining.vals.pointdouble
import com.github.zululi.mining.Mining.vals.score
import com.github.zululi.mining.Mining.vals.truedamage
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*


object listener : Listener {
    private var redscore = Mining.vals.redscore
    private var bluescore = Mining.vals.bluescore

    @EventHandler
    fun item(e: PlayerDropItemEvent){
        val player = e.player
        val item = e.itemDrop
        val itemData = item.itemStack.itemMeta
        when (item.itemStack.itemMeta?.lore.toString()) {
            "[${ChatColor.GOLD}SoulBound]"->{
                when (item.name){
                    "Wooden Sword"->{
                        item.remove()
                        player.playSound(player.location,Sound.ENTITY_ITEM_BREAK,0.5f,1f)
                    }
                    "Wooden Shovel"->{
                        item.remove()
                        player.playSound(player.location,Sound.ENTITY_ITEM_BREAK,0.5f,1f)
                    }
                    "Wooden Pickaxe"->{
                        item.remove()
                        player.playSound(player.location,Sound.ENTITY_ITEM_BREAK,0.5f,1f)

                    }
                    "Wooden Axe"->{
                        item.remove()
                        player.playSound(player.location,Sound.ENTITY_ITEM_BREAK,0.5f,1f)
                    }
                    "Bread"->{
                        item.remove()
                        player.playSound(player.location,Sound.ENTITY_ITEM_BREAK,0.5f,1f)
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

            2 -> {event.isCancelled = true}
        }
    }

    @EventHandler
    fun playerdeath(e: PlayerDeathEvent) {

        redscore = Mining.vals.redscore
        bluescore = Mining.vals.bluescore
        val player = e.entity
        val world = player.location.world
        player.teleport(Location(world, 0.0, 256.0, 0.0))
        player.inventory.clear()





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



        val uuid = player.uniqueId
        e.keepInventory = false
        //e.drops.clear()
        val tscoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
        val teams = tscoreboard?.getPlayerTeam(player)
        if (teams != null) {
            if (teams == tscoreboard.getTeam("red")) {
                //red team everyone
                val addpoint = -1000
                score.put(uuid, score[uuid]?.plus(addpoint) ?: 0)
                redscore -= 1000

            } else if (teams == tscoreboard.getTeam("blue")) {
                //blue team everyone
                bluescore -= 1000
                val addpoint = -1000
                score.put(uuid, score[uuid]?.plus(addpoint) ?: 0)

            } else {
                Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Error:team")
            }
        }
        e.keepInventory = true
        Mining.vals.redscore = redscore
        Mining.vals.bluescore = bluescore
        player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 6))

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
        val player = event.player
        val uuid = player.uniqueId
        val block = event.block.type.toString()
        if (player.gameMode == GameMode.SURVIVAL) {


            event.isCancelled = true
            gamestart = Mining.vals.gamestart
            when (gamestart) {
                0 -> {
                }

                1 -> {
                    val blockid = event.block
                    val item = blockid.drops
                    for (i in item) {
                        player.inventory.addItem(i)
                    }
                    if (block != "WHITE_STAINED_GLASS"){
                        blockid.type = Material.AIR
                    }else{
                        blockid.type = Material.WHITE_STAINED_GLASS
                    }

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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore<bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                                    if (pointdouble&&redscore>bluescore) {
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
                2->{}
            }

            Mining.vals.redscore = redscore
            Mining.vals.bluescore = bluescore
        }
    }
    @EventHandler
    fun onplaceblock(event:BlockPlaceEvent) {
        val player = event.player
        when (gamestart){
            0 -> {
                if (player.gameMode == GameMode.SURVIVAL) {
                    event.isCancelled = true
                    player.sendMessage("${ChatColor.RED}今はブロックを置くことができません。")
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.0f)
                }

            }
        }
    }


    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent){
        val item = event.player.itemInHand.type
        val player = event.player
        player.sendMessage(""+item)
        }
    }

