package openq.ui

import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import openq.ApplicationStarter


/**
 * 程序主界面
 */
class MainView() : BorderPane() {

    private val graphArea: GraphAreaView by lazy {
        ApplicationStarter.context.getInstance(GraphAreaView::class.java)
    }

    private val resourceBrowserView: ResourceBrowserView by lazy {
        ApplicationStarter.context.getInstance(ResourceBrowserView::class.java)
    }

    private val channelView: ChannelView by lazy {
       ApplicationStarter.context.getInstance(ChannelView::class.java)
    }


    init {
        center = setupCenter()
    }

    private fun setupCenter(): Node {
        // Resource browser和Graph area为一个分隔区域
        val resourceAndGraphSplitPane = SplitPane()
        resourceAndGraphSplitPane.items.add(resourceBrowserView)
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
        return graphArea.getGraphArea()
    }

    fun getResourceNameList(): List<String> {
        return resourceBrowserView.getResourceNameList()
    }

}