package dev.vanutp.tgbridge.common

import dev.vanutp.tgbridge.common.ConfigManager.config
import dev.vanutp.tgbridge.common.converters.MinecraftToTelegramConverter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

fun String.escapeHTML(): String = this
    .replace("&", "&amp;")
    .replace(">", "&gt;")
    .replace("<", "&lt;")


fun Component.asString() = MinecraftToTelegramConverter.convert(this).text

fun String.formatLang(vararg args: Pair<String, String>): String {
    var res = this
    args.forEach {
        res = res.replace("{${it.first}}", it.second)
    }
    return res
}

private val mm = MiniMessage.miniMessage()

fun String.formatMiniMessage(
    plainPlaceholders: List<Pair<String, String>> = emptyList(),
    componentPlaceholders: List<Pair<String, Component>> = emptyList(),
): Component {
    var res = this
    plainPlaceholders.forEach {
        res = res.replace("{${it.first}}", mm.escapeTags(it.second))
    }
    return mm.deserialize(
        res,
        *plainPlaceholders.map { Placeholder.unparsed(it.first, it.second) }.toTypedArray(),
        *componentPlaceholders.map { Placeholder.component(it.first, it.second) }.toTypedArray()
    )
}


val XAERO_WAYPOINT_RGX =
    Regex("""xaero-waypoint:([^:]+):[^:]:([-\d]+):([-\d]+|~):([-\d]+):\d+:(?:false|true):\d+:Internal-(?:the-)?(overworld|nether|end)-waypoints""")

fun String.asBluemapLinkOrNone(): String? {
    XAERO_WAYPOINT_RGX.matchEntire(this)?.let {
        try {
            var waypointName = it.groupValues[1]
            if (waypointName == "gui.xaero-deathpoint-old" || waypointName == "gui.xaero-deathpoint") {
                waypointName = Component.translatable(waypointName).asString()
            }
            val x = Integer.parseInt(it.groupValues[2])
            val yRaw = it.groupValues[3]
            val y = Integer.parseInt(if (yRaw == "~") "100" else yRaw)
            val z = Integer.parseInt(it.groupValues[4])
            val worldName = it.groupValues[5]

            return """<a href="${config.messages.bluemapUrl}#$worldName:$x:$y:$z:50:0:0:0:0:perspective">$waypointName</a>"""
        } catch (_: NumberFormatException) {
        }
    }
    return null
}
