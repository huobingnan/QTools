package openq.model

data class ChannelSetting(
    var channelName: String,
    var channelType: String,
    var showArea: String,
    var showType: String,
    var extSetting: HashMap<String, String> = HashMap()
) {
}