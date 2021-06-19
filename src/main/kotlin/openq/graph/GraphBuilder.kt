package openq.graph

import javafx.scene.Node

interface GraphBuilder {

    fun build(result: Any): Node
}