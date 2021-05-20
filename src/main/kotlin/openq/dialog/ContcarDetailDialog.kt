package openq.dialog

import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.GridPane
import openq.model.Contcar
import java.util.*


class ContcarDetailDialog(): Dialog<Void>() {


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


    fun accept(contcar: Contcar) {
        // 清空显示内容
        headerInformationTableView.items.clear()
        componentCoordinateTableView.items.clear()
        // 添加信息
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