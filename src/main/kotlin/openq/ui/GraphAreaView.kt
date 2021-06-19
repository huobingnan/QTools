package openq.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import openq.graph.GraphBuilder

/**
 * 结果展示区域
 */
class GraphAreaView(): BorderPane() {

    private val operatingButtonWidth = 80.0

    private val graphTabPane = setupCenter()

    private val refreshButton by lazy {
        val r = Button("refresh")
        r.prefWidth = operatingButtonWidth
        r.prefHeight = 50.0
        r
    }

    private val exportButton by lazy {
        val r = Button("export")
        r.prefHeight = 50.0
        r.prefWidth = operatingButtonWidth
        r
    }

    // 对显示区域进行操作
    private val operatingVBox by lazy {
        val r = VBox()
        r.alignment = Pos.TOP_CENTER
        r.children.addAll(refreshButton, exportButton)
        VBox.setMargin(exportButton, Insets(5.0))
        r
    }

    init {
        center = graphTabPane
        right = operatingVBox
    }

    private fun setupCenter(): TabPane {
        val pane = TabPane()
        pane.prefWidth = 800.0
        pane.tabs.add(Tab("Graph-1").apply {
            this.isClosable = false
        })
        return pane
    }

    // -------------------------- 业务逻辑方法 --------------------------

    fun getGraphArea(): List<String> {
        return graphTabPane.tabs.map { it.text }
    }

    fun buildGraph(builder: GraphBuilder, data: Any) {
        graphTabPane.tabs.first().content = builder.build(data)
    }
}