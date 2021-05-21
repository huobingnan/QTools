package openq.dialog

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import openq.model.AnalyseKeyFrame
import openq.model.ChannelSetting

/**
 * 关键帧显示对话框
 */
class FrameSettingDialog(): Dialog<AnalyseKeyFrame>() {

    private val channelNameTextField: TextField by lazy {
        val r = TextField()
        r.isEditable = false
        r.prefWidth = 230.0
        r
    }

    private val channelTypeTextField: TextField by lazy {
        val r = TextField()
        r.isEditable = false
        r.prefWidth = 230.0
        r
    }

    private val keyFrameTextField: TextField by lazy {
        val r = TextField()
        r.prefWidth = 230.0
        r
    }

    private val resourceNameComboBox: ComboBox<String> by lazy {
        val r = ComboBox<String>(FXCollections.observableArrayList())
        r.prefWidth = 230.0
        r
    }

    // Dialog的主界面
    private val contentPane: Node by lazy {
        val gridPane = GridPane()
        gridPane.add(Label("channel name :"), 0, 0)
        gridPane.add(channelNameTextField, 1, 0)
        gridPane.add(Label("channel type :"), 0, 1)
        gridPane.add(channelTypeTextField, 1, 1)
        gridPane.add(Label("key frame name :"), 0, 2)
        gridPane.add(keyFrameTextField, 1, 2)
        gridPane.add(Label("resource name :"), 0, 3)
        gridPane.add(resourceNameComboBox, 1, 3)


        gridPane.padding = Insets(10.0)
        gridPane.vgap = 10.0
        GridPane.setMargin(channelNameTextField, Insets(10.0))
        GridPane.setMargin(channelTypeTextField, Insets(10.0))
        GridPane.setMargin(keyFrameTextField, Insets(10.0))
        GridPane.setMargin(resourceNameComboBox, Insets(10.0))
        gridPane
    }

    init {
        dialogPane.content = contentPane
        dialogPane.buttonTypes.addAll(ButtonType.CANCEL, ButtonType.OK)
        title = "frame options"

        setResultConverter {
            if (it == ButtonType.OK) {
                AnalyseKeyFrame(keyFrameTextField.text, resourceNameComboBox.selectionModel.selectedItem)
            } else {
                null
            }
        }
    }

    fun accept(channelSetting: ChannelSetting) {
        channelNameTextField.text = channelSetting.channelName
        channelTypeTextField.text = channelSetting.channelType
    }

    fun accept(resourceNameList: List<String>) {
        resourceNameComboBox.items.clear()
        resourceNameComboBox.items.addAll(resourceNameList)
        if (resourceNameList.isNotEmpty()) resourceNameComboBox.selectionModel.select(0)
    }

    fun accept(analyseKeyFrame: AnalyseKeyFrame) {
        keyFrameTextField.text = analyseKeyFrame.name
        resourceNameComboBox.selectionModel.select(analyseKeyFrame.resourceName)
    }
}