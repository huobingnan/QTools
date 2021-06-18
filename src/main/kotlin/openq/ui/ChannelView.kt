package openq.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import openq.ApplicationStarter
import openq.component.KeyFrameButton
import openq.constants.ImageAsset
import openq.dialog.ChannelSettingDialog
import openq.dialog.FrameSettingDialog
import openq.model.AnalyseKeyFrame
import openq.model.ChannelSetting
import openq.model.property.GraphAreaProperties
import openq.vasp.BondLength

/**
 * 分析通道视图
 */
class ChannelView(): BorderPane() {


    private val channelSettingDialog: ChannelSettingDialog by lazy {
        ChannelSettingDialog()
    }

    private val frameSettingDialog: FrameSettingDialog by lazy {
        FrameSettingDialog()
    }

    private val channelCache: HashMap<String, ChannelSetting> by lazy {
        HashMap()
    }

    // 建立起Channel和其中存储KeyFrame的HBox的映射关系
    private val channelFrameListCache: HashMap<String, HBox> by lazy {
        HashMap()
    }

    // 展示分析结果按钮
    private val showButton: Button by lazy {
        val result = Button("show")
        result.prefWidth = 80.0
        result.setOnAction {
            val selectedItem = channelTabPane.selectionModel.selectedItem
            if (selectedItem != null) {
                // 获取到当前正在显示的tab
                val channel = selectedItem.text
                val keyFramesButtonContainer = channelFrameListCache[channel]!!
                // 获取到关键帧列表
                val keyFrameList = keyFramesButtonContainer.children.map {
                    (it as KeyFrameButton).getAnalyseKeyFrame()
                }
                // 获取到当前分析通道的设置
                val channelSetting = channelCache[channel]!!
                // 获取资源信息
                val resources = ApplicationStarter.getSingletonComponent(ResourceBrowserView::class.java)!!.getResources()

                // 根据分析通道的设置，展示分析结果
                if (channelSetting.channelType == ChannelSetting.TYPE_BOND_LENGTH) {
                    BondLength.perform(keyFrameList, resources, channelSetting)
                }

            }else {
                // 没有任何显示的分析通道，所以不可以展示信息
                // TODO 设置一个交互反馈
            }
        }
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
                    // 更新channel缓存
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
                    // 删除分析关键帧的缓存
                    channelFrameListCache.remove(selectedTab.text)
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
        box.alignment = Pos.TOP_CENTER
        box.children.addAll(showButton, newButton, settingButton, deleteButton)
        VBox.setMargin(showButton, Insets(8.0))
        VBox.setMargin(newButton, Insets(8.0))
        VBox.setMargin(settingButton, Insets(8.0))
        VBox.setMargin(deleteButton, Insets(8.0))
        return box
    }

    // 新建关键帧按钮
    private fun buildFrameButton(analyseKeyFrame: AnalyseKeyFrame): Button {
        val frameButton = KeyFrameButton()
        frameButton.name = analyseKeyFrame.name
        frameButton.resourceName = analyseKeyFrame.resourceName

        frameButton.index = channelFrameListCache[channelTabPane.selectionModel.selectedItem.text]!!.children.size - 1
        // 设置Button的ContextMenu
        val contextMenu = ContextMenu()
        val deleteMenuItem = MenuItem("delete")
        val forwardMenuItem = MenuItem("forward")
        val backMenuItem = MenuItem("back")
        contextMenu.items.addAll(forwardMenuItem, backMenuItem, deleteMenuItem)
        frameButton.contextMenu = contextMenu

        // 设置Button点击之后的动作
        frameButton.setOnAction {
            // 按钮点击的时候，要显示详情
            val selectedItemString = channelTabPane.selectionModel.selectedItem.text
            val channelSetting = channelCache[selectedItemString]!!
            val resourceNameList = ApplicationStarter.getSingletonComponent(MainView::class.java)!!.getResourceNameList()
            frameSettingDialog.accept(channelSetting)
            frameSettingDialog.accept(resourceNameList)
            frameSettingDialog.accept(frameButton.getAnalyseKeyFrame())
            val modification = frameSettingDialog.showAndWait()
            // 查看是否发生了更改
            if (!modification.isPresent) return@setOnAction
            // 更新UI显示
            val newSetting = modification.get()
            frameButton.name = newSetting.name
            frameButton.resourceName = newSetting.resourceName
        }
        return frameButton
    }

    // 新建分析通道Tab
    private fun buildChannelTab(channelSetting: ChannelSetting):Tab {
        val tab = Tab(channelSetting.channelName)
        tab.isClosable = false
        val box = HBox() // 新建一个水平的Box
        val scrollPane = ScrollPane(box) // 新建一个滚动条面板
        val newFrameButton = Button()
        newFrameButton.prefWidth = 80.0
        newFrameButton.prefHeight = 80.0
        newFrameButton.graphic = ImageView("/asset/${ImageAsset.ADD_FRAME}")
        newFrameButton.setOnAction {
            // 新建一个关键帧
            frameSettingDialog.accept(channelSetting)
            val resourceNameList = ApplicationStarter.getSingletonComponent(MainView::class.java)!!.getResourceNameList()
            frameSettingDialog.accept(resourceNameList)
            val frameSettingOptional = frameSettingDialog.showAndWait()
            if (!frameSettingOptional.isPresent) return@setOnAction
            val frameSetting = frameSettingOptional.get()
            // 更新关键帧通道列表UI显示
            val frameButton = buildFrameButton(frameSetting)
            box.children.add(box.children.size - 1, frameButton)

        }
        box.children.add(newFrameButton)
        box.padding = Insets(10.0)
        box.spacing = 20.0
        box.alignment = Pos.CENTER_LEFT
        tab.content = scrollPane
        // 写入缓存
        channelFrameListCache[channelSetting.channelName] = box
        return tab
    }




}