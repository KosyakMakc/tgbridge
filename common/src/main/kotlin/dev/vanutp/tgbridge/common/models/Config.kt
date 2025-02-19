package dev.vanutp.tgbridge.common.models

import com.charleskorn.kaml.YamlComment
import kotlinx.serialization.Serializable

@Serializable
data class GeneralConfig(
    @YamlComment(
        "Open https://t.me/BotFather to create a bot.",
        "Make sure to *disable* group privacy in the \"Bot Settings\" menu.",
    )
    val botToken: String = "your bot token",
    @YamlComment(
        "To get the chat id, right click/tap on any message and choose \"Copy Message Link\".",
        "If there is no such option, enable \"Chat history for new members\" in the group settings.",
        "It can then be disabled.",
        "The copied link will be in the format \"https://t.me/c/<chat_id>/<topic_id>/<message_id>\".",
        "Topic id will only be present if the chat has topics enabled.",
    )
    var chatId: Long = 0,
    @YamlComment(
        "If the specified chat has topics enabled, specify the id of the topic that will be",
        "synchronized with the game. Commands will have no effect in other topics.",
        "If you don't set this, the bot will send all messages to the \"General\" topic",
        "and the /list command will have weird behaviour when sent in other topics.",
        "Default value: null (disabled)",
    )
    val topicId: Int? = null,
)

@Serializable
data class GameMessagesConfig(
    @YamlComment(
        "If this value is set, waypoints shared from Xaero's Minimap/World Map will be rendered",
        "as links to a specified Bluemap instance.",
        "Note that shared waypoints will be sent regardless of the requirePrefixInMinecraft setting.",
        "Example: https://map.example.com",
        "Default value: null (disabled)",
    )
    val bluemapUrl: String? = null,
    @YamlComment(
        "Deprecated, install a compatible chat plugin or use incompatiblePluginPrefix",
        "If this value is set, messages without specified prefix won't be forwarded to Telegram.",
        "Only set this if you don't have a chat plugin installed",
        "Example: \"!\" (quotes are required)",
        "Default value: \"\" (disabled)",
    )
    val requirePrefixInMinecraft: String? = "",
    @YamlComment(
        "Use this if you have an incompatible plugin, such as CMI or AdvancedChat installed.",
        "Will register a legacy chat listener with LOWEST priority",
        "and only forward messages that start with the specified string.",
        "Currently this only has an effect on Paper. See the wiki for more information.",
        "Example: \"!\" (quotes are required)",
        "Default value: null (disabled)",
    )
    val incompatiblePluginChatPrefix: String? = null,
    @YamlComment(
        "Specify the chat name to forward messages from.",
        "Only has an effect when a chat plugin with support for named chats, such as Chatty, is installed.",
        "Default value: global"
    )
    val globalChatName: String = "global",
    @YamlComment(
        "Set to true to keep the prefix specified in the above setting in the message"
    )
    val keepPrefix: Boolean = false,
    @YamlComment(
        "Chat messages sent within the specified interval will be merged in one.",
        "The value is specified in seconds",
        "Default value: 0 (disabled)",
    )
    val mergeWindow: Int? = 0,
    @YamlComment("Use real player username instead of display name in all Telegram messages")
    val useRealUsername: Boolean = false,
)

@Serializable
data class AdvancementsConfig(
    val enable: Boolean = true,
    @YamlComment("Configure forwarding of each advancement type")
    val enableTask: Boolean = true,
    val enableGoal: Boolean = true,
    val enableChallenge: Boolean = true,
    @YamlComment("Include advancement descriptions in Telegram messages")
    val showDescription: Boolean = true,
)

@Serializable
data class GameEventsConfig(
    val advancementMessages: AdvancementsConfig = AdvancementsConfig(),
    val enableDeathMessages: Boolean = true,
    val enableJoinMessages: Boolean = true,
    val enableLeaveMessages: Boolean = true,
    @YamlComment(
        "If a player leaves and then joins within the specified time interval,",
        "the leave and join messages will be deleted.",
        "This is useful when players frequently re-join, for example because of connection problems.",
        "Only has effect when both enableJoinMessages and enableLeaveMessages are set to true",
        "The value is specified in seconds",
        "Default value: 0 (disabled)",
    )
    val leaveJoinMergeWindow: Int? = 0,
    @YamlComment("Whether to send a Telegram message when the server starts/stops")
    val enableStartMessages: Boolean = true,
    val enableStopMessages: Boolean = true,
)

@Serializable
data class AdvancedConfig(
    val botApiUrl: String = "https://api.telegram.org",
)

@Serializable
data class Config(
    @YamlComment(
        "It's enough to set botToken and chatId for the plugin to work.",
        "When your group has topics enabled, you should also set topicId.",
        "See https://tgbridge.vanutp.dev for more information."
    )
    val general: GeneralConfig = GeneralConfig(),
    val messages: GameMessagesConfig = GameMessagesConfig(),
    val events: GameEventsConfig = GameEventsConfig(),
    val advanced: AdvancedConfig = AdvancedConfig(),
    @YamlComment("Don't change the version manually")
    val version: Int = 1,
) {
    fun hasDefaultValues() =
        general.botToken == Config().general.botToken || general.chatId == Config().general.chatId
}
