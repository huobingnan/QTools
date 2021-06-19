package openq.model

/**
 * 分析通道设置
 * channelName：分析通道的名称
 * channelType: 分析通道的类型
 * showArea: 结果展示的区域
 * showType: 结果展示的形式
 * extSetting: 额外的设置信息
 */
data class ChannelSetting(
    var channelName: String,
    var channelType: String,
    var showArea: String,
    var showType: String,
    var extSetting: HashMap<String, String> = HashMap()
) {

    companion object {
        const val TYPE_BOND_LENGTH = "bond length"
        const val TYPE_BOND_ANGLE = "bond angle"

        const val SHOW_TABLE_VIEW = "table view"
        const val SHOW_LINE_CHART = "line chart"
        const val SHOW_BAR_CHART = "bar chart"


    }
}