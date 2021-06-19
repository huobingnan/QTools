package openq.ui

import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.BorderPane


/**
 * 程序主界面
 */
class MainView() : BorderPane() {

    private val graphArea: TabPane by lazy {
        setupGraphArea()
    }

    private val resourceBrowserListView: ResourceBrowserView by lazy {
        ResourceBrowserView()
    }

    private val channelView: ChannelView by lazy {
        val channelView = ChannelView()
        channelView.prefHeight = 200.0
        channelView
    }


    init {
        center = setupCenter()
    }

    private fun setupGraphArea(): TabPane {
        val pane = TabPane()
        pane.prefWidth = 800.0
        pane.tabs.add(Tab("Graph-1"))
        //GraphAreaProperties.graphTabList.bind(pane.tabs) // 双向绑定 TODO
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

    fun getResourceNameList(): List<String> {
        return resourceBrowserListView.getResourceNameList()
    }

}