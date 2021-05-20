package openq.model

data class ChannelSettingPair(
    var settingName: String,
    var settingValue: String
) {

    constructor(): this("", "")
}