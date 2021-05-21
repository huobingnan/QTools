package openq.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import openq.ApplicationStarter
import openq.dialog.ChannelSettingDialog
import openq.model.ChannelSetting

/**
 * 分析通道视图
 */
class ChannelView(): BorderPane() {


    private val channelSettingDialog: ChannelSettingDialog by lazy {
        ChannelSettingDialog()
    }


    private val channelCache: HashMap<String, ChannelSetting> by lazy {
        HashMap()
    }

    // 展示分析结果按钮
    private val showButton: Button by lazy {
        val result = Button("show")
        result.prefWidth = 80.0
        result
    }

    // 新建分析通道按钮
    private val newButton: Button by lazy {
        val result = Button("new")
        result.prefWidth = 80.0
        result.setOnAction {
            // 清除对话框之前的显示内容
            channelSettingDialog.clear()
            // 查看当前的可选的显示区域
            val graphAreaName = ApplicationStarter.getSingletonComponent(MainView::class.java)!!.getGraphAreaNameList()
            channelSettingDialog.accept(graphAreaName)
            var renameAlert: Alert? = null
            // 显示对话框
            while (true) {
                val channelSettingOptional = channelSettingDialog.showAndWait()
                if (channelSettingOptional.isPresent) {
                    val channelSetting = channelSettingOptional.get()
                    if (channelCache.containsKey(channelSetting.channelName)) {
                        // 重复的分析通道，分析通道的名字必须是全局唯一的
                        if (renameAlert == null) {
                            renameAlert = Alert(Alert.AlertType.ERROR)
                        }
                        renameAlert.title = "error"
                        renameAlert.headerText = "duplicated channel name"
                        renameAlert.contentText = "channel ${channelSetting.channelName} has already exists!!"
                        renameAlert.showAndWait()
                    }else {
                        channelCache[channelSetting.channelName] = channelSetting // 缓存channel设置的信息
                        // 更新UI界面显示
                        channelTabPane.tabs.add(buildChannelTab(channelSetting))
                        break
                    }
                } else {
                    break
                }
            }

        }
        result
    }
    // 设置区域
    private val settingButton:Button by lazy {
        val result = Button("setting")
        result.prefWidth = 80.0
        result.setOnAction {
            val selectedTab = channelTabPane.selectionModel.selectedItem ?: return@setOnAction
            val channelSetting = channelCache[selectedTab.text] // 查询缓存，找到channel的设置
            if (channelSetting != null) {
                channelSettingDialog.accept(channelSetting)
                val channelSettingOptional = channelSettingDialog.showAndWait()
                if (channelSettingOptional.isPresent) {
                    // 更新缓存
                    channelCache[selectedTab.text] = channelSettingOptional.get()
                }
            }

        }
        result
    }

    // 删除分析通道按钮
    private val deleteButton:Button by lazy {
        val result = Button("delete")
        result.prefWidth = 80.0
        result.setOnAction {
            val selectedTab = channelTabPane.selectionModel.selectedItem
            if (selectedTab != null) {
                val alert = Alert(Alert.AlertType.CONFIRMATION)
                alert.title = "warning"
                alert.headerText = "Are you sure to delete channel ${selectedTab.text}?"
                val buttonTypeOptional = alert.showAndWait()
                if (buttonTypeOptional.isPresent && buttonTypeOptional.get() == ButtonType.OK) {
                    // 删除缓存
                    channelCache.remove(selectedTab.text)
                    // UI界面上删除
                    channelTabPane.tabs.remove(selectedTab)
                }
            }
        }
        result
    }

    // channel显示区域
    private val channelTabPane: TabPane by lazy {
        TabPane()
    }


    init {
        left = setupLeft()
        center = channelTabPane
    }


    private fun setupLeft(): VBox {
        val box = VBox()
        box.children.addAll(showButton, newButton, settingButton, deleteButton)
        VBox.setMargin(showButton, Insets(8.0))
        VBox.setMargin(newButton, Insets(8.0))
        VBox.setMargin(settingButton, Insets(8.0))
        VBox.setMargin(deleteButton, Insets(8.0))
        return box
    }

    // 新建分析通道Tab
    private fun buildChannelTab(channelSetting: ChannelSetting):Tab {
        val tab = Tab(channelSetting.channelName)
        tab.isClosable = false
        val box = HBox() // 新建一个水平的Box
        val newFrameButton = Button("add")
        newFrameButton.prefWidth = 100.0
        newFrameButton.prefHeight = 100.0
        HBox.setMargin(newFrameButton, Insets(10.0))
        box.children.add(newFrameButton)
        box.alignment = Pos.CENTER_LEFT
        tab.content = box
        return tab
    }




}