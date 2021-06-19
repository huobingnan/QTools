package openq.graph

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import openq.model.BondDistanceTableRow
import openq.model.BondLengthResult
import java.lang.IllegalArgumentException
import java.util.*

class BondLengthTableViewBuilder: GraphBuilder {

    override fun build(result: Any): Node {
        if (result is BondLengthResult) {
            // 将将所有搜寻到的键取并集
            val finalBondSet: MutableSet<String> = TreeSet()
            for (stringDoubleHashMap in result.data) {
                finalBondSet.addAll(stringDoubleHashMap.keys)
            }
            // 构建TableView
            val tableView: TableView<BondDistanceTableRow> = TableView<BondDistanceTableRow>()

            // 创建化学键显示列
            val bondNameColumn: TableColumn<BondDistanceTableRow, String> =
                TableColumn<BondDistanceTableRow, String>("Bond Name")
            bondNameColumn.prefWidth = 100.0
            bondNameColumn.setCellValueFactory(PropertyValueFactory<BondDistanceTableRow, String>("bondName"))
            tableView.columns.add(bondNameColumn)
            // 根据frameList创建列
            for (i in result.analyseKeyFrameList.indices) {
                val frame = result.analyseKeyFrameList[i]
                val column: TableColumn<BondDistanceTableRow, String> = TableColumn(frame.name)
                val index: Int = i
                column.setCellValueFactory { cellDataFeatures: TableColumn.CellDataFeatures<BondDistanceTableRow, String> ->
                    val value: BondDistanceTableRow = cellDataFeatures.value
                    val distance: Double = value.bondDistance[index]
                    if (distance == -1.0) {
                        // 键已经断裂了，无法达到成键的距离
                        return@setCellValueFactory SimpleStringProperty("-")
                    }
                    SimpleStringProperty(String.format("%.2f", distance))
                }
                column.prefWidth = 200.0
                tableView.columns.add(column)
            }
            // 构建显示内容
            val items: ObservableList<BondDistanceTableRow> = FXCollections.observableArrayList<BondDistanceTableRow>()

            for (key in finalBondSet) {
                val row = BondDistanceTableRow()
                row.bondName = key
                for (map in result.data) {
                    val distance = map[key]
                    if (distance == null) {
                        row.bondDistance.add(-1.0)
                    } else {
                        row.bondDistance.add(distance)
                    }
                }
                items.add(row) // 添加到TableView中显示
            }
            // 设置数据
            tableView.items = items
            return tableView
        } else {
            throw IllegalArgumentException("result type mismatch")
        }
    }
}