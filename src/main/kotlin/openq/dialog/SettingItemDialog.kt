package openq.dialog

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import openq.constants.ChannelExtSettingKey
import openq.model.ChannelSettingPair


class SettingItemDialog(): Dialog<ChannelSettingPair>() {

    private val settingItemComboBox: ComboBox<String> by lazy {
        val result = ComboBox<String>(FXCollections.observableArrayList())
        result.items.addAll(ChannelExtSettingKey.BOND_LENGTH_MAX, ChannelExtSettingKey.BOND_NUMBER_CONSTRAIN)
        result.selectionModel.select(0)
        result.prefWidth = 300.0
        result
    }

    private val settingValueTextField: TextField by lazy {
        val result = TextField()
        result.prefWidth = 300.0
        result
    }

    init {
        dialogPane.content = setupContentPane()
        dialogPane.buttonTypes.addAll(ButtonType.CANCEL, ButtonType.OK)
        title = "setting option"
        setResultConverter {
            if (it == ButtonType.OK) {
                // 点击确定键，生成结果返回
                val channelSettingPair = ChannelSettingPair()
                channelSettingPair.settingName = settingItemComboBox.selectionModel.selectedItem
                channelSettingPair.settingValue = settingValueTextField.text
                channelSettingPair
            } else {
                null
            }
        }
    }

    private fun setupContentPane(): Node {
        val gridPane = GridPane()
        gridPane.add(Label("setting :"), 0, 0)
        gridPane.add(settingItemComboBox, 1, 0)
        gridPane.add(Label("value :"), 0, 1)
        gridPane.add(settingValueTextField, 1, 1)
        gridPane.padding = Insets(10.0)
        GridPane.setMargin(settingItemComboBox, Insets(10.0))
        GridPane.setMargin(settingValueTextField, Insets(10.0))
        return gridPane
    }

    fun clear() {
        settingValueTextField.text = ""
    }

    // 接收一个ChannelSettingPair对象，并显示详情
    fun accept(settingPair: ChannelSettingPair) {
        settingItemComboBox.selectionModel.select(settingPair.settingName)
        settingValueTextField.text = settingPair.settingValue
    }

}