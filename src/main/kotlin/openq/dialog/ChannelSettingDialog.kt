package openq.dialog

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.GridPane
import openq.model.ChannelSetting
import openq.model.ChannelSettingPair

/**
 * 对分析通道进行设置的对话框
 */
class ChannelSettingDialog() : Dialog<ChannelSetting>(){


    private val settingItemDialog: SettingItemDialog by lazy {
        SettingItemDialog()
    }

    private val channelNameTextField: TextField by lazy {
        val result = TextField()
        result.prefWidth = 300.0
        result
    }

    private val channelTypeComboBox: ComboBox<String> by lazy {
        val result = ComboBox<String>(FXCollections.observableArrayList())
        result.items.addAll("bond length", "bond angle")
        result.selectionModel.select(0)
        result.prefWidth = 300.0
        result
    }
    private val graphAreaComboBox: ComboBox<String> by lazy {
        val result = ComboBox<String>(FXCollections.observableArrayList())
        result.prefWidth = 300.0
        result
    }

    private val showTypeComboBox: ComboBox<String> by lazy {
        val result = ComboBox<String>(FXCollections.observableArrayList())
        result.prefWidth = 300.0
        result.items.addAll("table view", "line chart", "bar chart")
        result.selectionModel.select(0)
        result
    }

    // 设置项表格
    private val settingTableView: TableView<ChannelSettingPair> by lazy {
        val result = TableView<ChannelSettingPair>()
        result.prefHeight =300.0

        val settingNameColumn = TableColumn<ChannelSettingPair, String>("setting")
        settingNameColumn.cellValueFactory = PropertyValueFactory("settingName")
        settingNameColumn.isSortable = false
        settingNameColumn.isReorderable = false
        settingNameColumn.isResizable = false

        val settingValueColumn = TableColumn<ChannelSettingPair, String>("value")
        settingValueColumn.cellValueFactory = PropertyValueFactory("settingValue")
        settingValueColumn.isSortable = false
        settingValueColumn.isReorderable = false
        settingValueColumn.isResizable = false

        // 设置自适应列宽
        settingNameColumn.prefWidthProperty().bind(result.widthProperty().multiply(.40))
        settingValueColumn.prefWidthProperty().bind(result.widthProperty().multiply(.59))

        result.columns.addAll(settingNameColumn, settingValueColumn)
        result.placeholder = Label("Right click to new setting item")

        // 设置 TableView的ContextMenu
        result.contextMenu = ContextMenu()
        val newSettingMenuItem = MenuItem("new")
        val deleteSettingMenuItem = MenuItem("delete")

        // 新建设置项
        newSettingMenuItem.setOnAction {
            settingItemDialog.clear()
            val channelSettingPair = settingItemDialog.showAndWait()
            if (channelSettingPair.isPresent) {
                // 设置TableView显示
                // 查找这个是否已经存在在TableView中,若存在直接删除，重新添加
                val r = channelSettingPair.get()
                val findResult = result.items.find { it.settingName == r.settingName }
                if (findResult != null) {
                    result.items.remove(findResult)
                }
                result.items.add(r)
            }
        }
        // 删除设置项
        deleteSettingMenuItem.setOnAction {
            val selectedItem = settingTableView.selectionModel.selectedItem
            if (selectedItem != null) {
                settingTableView.items.remove(selectedItem)
            }
        }
        result.contextMenu.items.addAll(newSettingMenuItem, deleteSettingMenuItem)

        // 设置settingTableView的双击事件，双击之后，显示详情，并可修改
        result.setOnMouseClicked {
            if (it.clickCount == 2) {
                val selectedItem = settingTableView.selectionModel.selectedItem
                val selectedIndex = settingTableView.selectionModel.selectedIndex
                if (selectedItem != null) {
                    settingItemDialog.accept(selectedItem)
                    val modified = settingItemDialog.showAndWait()
                    if (modified.isPresent) {
                        // 出现了更改, 替换原来的显示项目
                        settingTableView.items.removeAt(selectedIndex)
                        settingTableView.items.add(selectedIndex, modified.get())
                    }
                }
            }
        }

        result
    }

    init {
        dialogPane.content = setupContentPane()
        dialogPane.buttonTypes.addAll(ButtonType.CANCEL, ButtonType.OK)
        title = "new channel options"
    }


    /**
     * 建立对话框面板
     */
    private fun setupContentPane(): Node {
        val pane = GridPane()
        pane.padding = Insets(10.0)
        pane.add(Label("channel name :"), 0, 0)
        pane.add(channelNameTextField, 1, 0)
        pane.add(Label("channel type :"), 0, 1)
        pane.add(channelTypeComboBox,1, 1)
        pane.add(Label("display area :"), 0, 2)
        pane.add(graphAreaComboBox, 1, 2)
        pane.add(Label("display form :"), 0, 3)
        pane.add(showTypeComboBox, 1, 3)
        pane.add(settingTableView, 0,4)
        GridPane.setColumnSpan(settingTableView, 2)

        GridPane.setMargin(channelNameTextField, Insets(10.0))
        GridPane.setMargin(channelTypeComboBox, Insets(10.0))
        GridPane.setMargin(graphAreaComboBox, Insets(10.0))
        GridPane.setMargin(showTypeComboBox, Insets(10.0))
        return pane
    }

    /**
     * 清楚对话框状态
     */
    fun clear() {
        channelNameTextField.text = ""
        settingTableView.items.clear()
    }

}