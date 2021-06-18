package openq.ui

import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import openq.ApplicationStarter
import openq.dialog.DetailDialog
import openq.dialog.ExceptionDialog
import openq.model.Resource
import openq.vasp.ContcarFileParser
import openq.vasp.GaussianFileParser
import java.text.ParseException

/**
 * 资源浏览视图
 * 视图主要负责资源的导入和相关操作
 */
class ResourceBrowserView(): BorderPane() {

    // 显示区域
    private val resourceBrowserListView by lazy {
        setupResourceBrowserArea()
    }

    // 导入异常时弹出的对话框
    private val exceptionDialog: ExceptionDialog by lazy {
        ExceptionDialog()
    }

    // 删除确认时的对话框
    private val deleteConfirmationDialog: Alert by lazy {
        val r = Alert(Alert.AlertType.CONFIRMATION)
        r.title = "delete"
        r.headerText = "Are you sure to delete this file?"
        r
    }

    // 展示详情的对话框
    private val detailDialog: DetailDialog by lazy {
        DetailDialog()
    }

    // 导入资源类型选择框
    private val resourceTypeChoiceDialog: ChoiceDialog<String> by lazy {
        val choice = FXCollections.observableArrayList("VASP output file", "Gaussian log file")
        val r = ChoiceDialog<String>("VASP output file", choice)
        r.title = "import options"
        r.headerText = "Please chose a resource type"
        r.contentText = "type :"
        r
    }

    private val resourceCache = HashMap<String, Resource>()


    init {
        center = resourceBrowserListView
    }

    init {
        ApplicationStarter.registerSingletonComponent(this)
    }

    // ------------------------------ ListView ContextMenu的点击事件处理 -------------------------------
    private fun onImportMenuItemClicked(action: ActionEvent?) {
        // 2021-06-15 添加了对高斯文件解析的支持，所以这里在导入之前会弹出一个对话框，确定导入文件的类型
        val optional = resourceTypeChoiceDialog.showAndWait()
        if (!optional.isPresent) return
        val resourceType = optional.get() // 获取到导入资源的类型

        val fileChooser = FileChooser()
        val file = fileChooser.showOpenDialog(scene.window)
        if (file != null) {
            try {
                var name:String? = null
                if (resourceType == "VASP output file") {
                    name = "${file.nameWithoutExtension} [vasp]"
                }else if (resourceType == "Gaussian log file") {
                    name = "${file.nameWithoutExtension} [gaussian]"
                }else {
                    return
                }
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
                // 处理具体的解析业务
                val resource = Resource()
                if (resourceType == "VASP output file") {
                    val contcar = ContcarFileParser.parse(file)
                    resource.instance = contcar
                    resource.type = Resource.VASP
                    resource.name = name!!
                }else if (resourceType == "Gaussian log file") {
                    val gaussianLog = GaussianFileParser.parse(file)
                    resource.instance = gaussianLog
                    resource.type = Resource.GAUSSIAN
                    resource.name = name!!
                }else {
                    return
                }
                // 缓存解析好的资源文件
                resourceCache[name] = resource
                // 显示
                resourceBrowserListView.items.add(resource.name)
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
                resourceCache.remove(selectedItem)
            }
        }
    }

    private fun onDetailMenuItemClicked(action: ActionEvent?) {
        val selectedItem = resourceBrowserListView.selectionModel.selectedItem
        if (selectedItem != null && resourceCache[selectedItem] != null) {
            detailDialog.accept(resourceCache[selectedItem]!!)
            detailDialog.showAndWait()
        }
    }


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


    // ---------------------------- 业务逻辑方法 -----------------------------------
    fun getResourceNameList(): List<String> {
        return resourceCache.keys.toList()
    }

    fun getResources(): Map<String, Resource> {
        return resourceCache
    }

}