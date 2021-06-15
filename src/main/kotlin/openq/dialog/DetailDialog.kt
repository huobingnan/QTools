package openq.dialog

import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.GridPane
import openq.model.Contcar
import openq.model.GaussianLog
import openq.model.Resource


class DetailDialog(): Dialog<Void>() {


    private val headerInformationTableView: TableView<Pair<String, String>> by lazy {
        val r = TableView<Pair<String, String>>()
        r.prefHeight = 230.0
        r.prefWidth = 620.0
        val nameColumn = TableColumn<Pair<String, String>, String>("property")
        nameColumn.cellValueFactory = PropertyValueFactory("first")
        nameColumn.isResizable = false
        nameColumn.isSortable = false
        nameColumn.isReorderable = false
        nameColumn.prefWidthProperty().bind(r.widthProperty().multiply(.4))
        val valueColumn = TableColumn<Pair<String, String>, String>("value")
        valueColumn.cellValueFactory = PropertyValueFactory("second")
        valueColumn.isResizable = false
        valueColumn.isSortable = false
        valueColumn.isReorderable = false
        valueColumn.prefWidthProperty().bind(r.widthProperty().multiply(.59))
        r.columns.addAll(nameColumn, valueColumn)
        r
    }

    private val componentCoordinateTableView: TableView<Pair<String, String>> by lazy {
        val r = TableView<Pair<String, String>>()
        r.prefHeight = 400.0
        r.prefWidth = 620.0
        val componentColumn = TableColumn<Pair<String, String>, String>("component")
        componentColumn.cellValueFactory = PropertyValueFactory("first")
        componentColumn.isResizable = false
        componentColumn.isSortable = false
        componentColumn.isReorderable = false
        componentColumn.prefWidthProperty().bind(r.widthProperty().multiply(.20))
        val coordinateColumn = TableColumn<Pair<String, String>, String>("coordinate")
        coordinateColumn.cellValueFactory = PropertyValueFactory("second")
        coordinateColumn.isResizable = false
        coordinateColumn.isSortable = false
        coordinateColumn.isReorderable = false
        coordinateColumn.prefWidthProperty().bind(r.widthProperty().multiply(.79))
        r.columns.addAll(componentColumn, coordinateColumn)
        r
    }

    init {
        title = "information"
        dialogPane.content = setupContentPane()
        dialogPane.buttonTypes.addAll(ButtonType.OK)
    }

    private fun setupContentPane(): Node {
        val gridPane = GridPane()
        gridPane.add(headerInformationTableView, 0, 0)
        gridPane.add(componentCoordinateTableView, 0, 1)
        return gridPane
    }


    // 接收Resource更新界面
    fun accept(resource: Resource) {
        // 清空显示内容
        headerInformationTableView.items.clear()
        componentCoordinateTableView.items.clear()

        if (resource.type == "gaussian") {
            // 高斯文件
            val instance = resource.instance!! as GaussianLog
            headerInformationTableView.items.addAll(
                Pair("input", instance.input),
                Pair("output", instance.output),
                Pair("calculation routine", instance.routine),
                Pair("calculation level", instance.calculationLevel),
                Pair("calculation basic group", instance.calculationBasicGroup),
                Pair("maximum force reached", if (instance.maximumForceReached) "YES" else "NO"),
                Pair("RMS force reached", if (instance.RMSForceReached) "YES" else "NO"),
                Pair("maximum displacement reached", if (instance.maximumDisplacementReached) "YES" else "NO"),
                Pair("RMS displacement reached", if (instance.RMSDisplacementReached) "YES" else "NO")
            )
            val lastCoordinate = instance.lastAtomCoordinateList
            lastCoordinate.forEach {
                componentCoordinateTableView.items.add(
                    Pair("${it.symbol}${it.sequenceNumber}", "[${it.x}, ${it.y}, ${it.z}]")
                )
            }
        }else if (resource.type == "vasp") {
            // vasp 输出文件
            val contcar = resource.instance!! as Contcar
            headerInformationTableView.items.addAll(
                Pair("name", contcar.name),
                Pair("scale", contcar.scale.toString()),
                Pair("vector a", "[${contcar.matrix!![0][0]}, ${contcar.matrix!![1][0]}, ${contcar.matrix!![2][0]}]"),
                Pair("vector b", "[${contcar.matrix!![0][1]}, ${contcar.matrix!![1][1]}, ${contcar.matrix!![2][1]}]"),
                Pair("vector c", "[${contcar.matrix!![0][2]}, ${contcar.matrix!![1][2]}, ${contcar.matrix!![2][2]}]"),
                Pair("components", contcar.componentsNameList.toString()),
                Pair("components number", contcar.componentsNumberList.toString()),
                Pair("components total number", contcar.componentAmount.toString())
            )
            var i = 0
            val size = contcar.componentsCoordinate.size
            var componentCnt = 1
            var componentCursor = 0
            while (i < size) {
                val componentNow = contcar.componentsNameList!![componentCursor]
                componentCoordinateTableView.items.add(
                    Pair("$componentNow$componentCnt", contcar.componentsCoordinate[i].contentToString())
                )
                i++
                componentCnt++
                if (componentCnt > contcar.componentsNumberList!![componentCursor]) {
                    componentCnt = 1
                    componentCursor++
                }
            }
        }
    }
}