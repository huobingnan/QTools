package openq.ui

import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import openq.ApplicationStarter
import openq.dialog.ContcarDetailDialog
import openq.dialog.ExceptionDialog
import openq.model.Contcar
import openq.vasp.ContcarFileParser
import java.text.ParseException

/**
 * 程序主界面
 */
class MainView() : BorderPane() {

    // contcar文件解析缓存
    private val contcarFileCache = HashMap<String, Contcar>()

    private val graphArea: TabPane by lazy {
        setupGraphArea()
    }

    private val resourceBrowserListView: ListView<String> by lazy {
        setupResourceBrowserArea()
    }

    private val channelView: ChannelView by lazy {
        val channelView = ChannelView()
        channelView.prefHeight = 200.0
        channelView
    }

    private val exceptionDialog: ExceptionDialog by lazy {
        ExceptionDialog()
    }

    private val deleteConfirmationDialog: Alert by lazy {
        val r = Alert(Alert.AlertType.CONFIRMATION)
        r.title = "delete"
        r.headerText = "Are you sure to delete this file?"
        r
    }

    private val contcarDetailDialog: ContcarDetailDialog by lazy {
        ContcarDetailDialog()
    }


    init {
        center = setupCenter()
    }

    init {
        ApplicationStarter.registerSingletonComponent(this)
    }

    // ------------------------------Event handler-------------------------------
    private fun onImportMenuItemClicked(action: ActionEvent?) {
        val fileChooser = FileChooser()
        val file = fileChooser.showOpenDialog(scene.window)
        if (file != null) {
            try {
                var name = file.nameWithoutExtension
                // 查看是否已经存在这个文件
                // 提示用户，这个文件已经存在，是否重新命名
                val renameDialog = TextInputDialog()
                renameDialog.title = "please rename"
                renameDialog.contentText = "new name:"
                while (resourceBrowserListView.items.contains(name)) {
                    renameDialog.headerText = "$name is already exists!"
                    renameDialog.editor.text = "$name(copy)"
                    renameDialog.editor.selectAll()
                    val optional = renameDialog.showAndWait()
                    if (optional.isPresent) {
                        name = optional.get()
                    }else {
                        return // 终止导入
                    }
                }
                val contcar = ContcarFileParser.parse(file)
                contcarFileCache[name] = contcar
                // 显示
                resourceBrowserListView.items.add(name)
            }catch (ex: ParseException) {
                exceptionDialog.accept(ex)
                exceptionDialog.showAndWait()
            }
        }
    }

    private fun onDeleteMenuItemClicked(action: ActionEvent?) {
        val selectedItem = resourceBrowserListView.selectionModel.selectedItem
        if (selectedItem != null) {
            deleteConfirmationDialog.contentText = selectedItem
            val buttonTypes = deleteConfirmationDialog.showAndWait()
            if (buttonTypes.isPresent && buttonTypes.get() == ButtonType.OK) {
                // 删除文件
                resourceBrowserListView.items.remove(selectedItem)
                // 删除缓存
                contcarFileCache.remove(selectedItem)
            }
        }
    }

    private fun onDetailMenuItemClicked(action: ActionEvent?) {
        val selectedItem = resourceBrowserListView.selectionModel.selectedItem
        if (selectedItem != null && contcarFileCache[selectedItem] != null) {
            contcarDetailDialog.accept(contcarFileCache[selectedItem]!!)
            contcarDetailDialog.showAndWait()
        }
    }


    // ---------------------------------UI setup----------------------------------

    private fun setupResourceBrowserArea(): ListView<String> {
        val listView = ListView<String>(FXCollections.observableArrayList())
        listView.prefWidth = 200.0
        listView.placeholder = Label("Right click to import resource")

        // 设置list view的context menu
        val contextMenu = ContextMenu()
        // 导入menu
        val importMenuItem = MenuItem("import")
        importMenuItem.setOnAction {
            onImportMenuItemClicked(it)
        }
        // 删除menu
        val deleteMenuItem = MenuItem("delete")
        deleteMenuItem.setOnAction {
            onDeleteMenuItemClicked(it)
        }

        // 详情menu
        val detailMenuItem = MenuItem("detail")
        detailMenuItem.setOnAction {
            onDetailMenuItemClicked(it)
        }
        // 3D模型视图 TODO
        val threeDViewMenuItem = MenuItem("3D view")

        contextMenu.items.addAll(importMenuItem, detailMenuItem, deleteMenuItem, threeDViewMenuItem)
        listView.contextMenu = contextMenu
        // 设置listview的双击事件
        listView.setOnMouseClicked {
            if (it.clickCount == 2) {
                onDetailMenuItemClicked(null)
            }
        }
        return listView
    }

    private fun setupGraphArea(): TabPane {
        val pane = TabPane()
        pane.prefWidth = 800.0
        pane.tabs.add(Tab("Graph-1"))
        return pane
    }

    private fun setupCenter(): Node {
        // Resource browser和Graph area为一个分隔区域
        val resourceAndGraphSplitPane = SplitPane()
        resourceAndGraphSplitPane.items.add(resourceBrowserListView)
        resourceAndGraphSplitPane.items.add(graphArea)
        resourceAndGraphSplitPane.setDividerPositions(.25, .75)
        resourceAndGraphSplitPane.prefHeight = 600.0
        // 纵向分隔面板
        val pane = SplitPane()
        pane.orientation = Orientation.VERTICAL
        pane.setDividerPositions(.80, .20)
        pane.items.addAll(resourceAndGraphSplitPane, channelView)
        return pane
    }

    //----------------------------------Business method--------------------------------
    fun getGraphAreaNameList(): List<String> {
        return graphArea.tabs.map { it.text }
    }

}