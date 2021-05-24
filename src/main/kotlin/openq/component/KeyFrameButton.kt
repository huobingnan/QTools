package openq.component

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.text.Font
import openq.model.AnalyseKeyFrame

/**
 * 关键帧按钮
 */
class KeyFrameButton(): Button() {

    private val analyseKeyFrame: AnalyseKeyFrame = AnalyseKeyFrame("", "")
    // 关键帧的名称，更新此变量的值会自动触发更新UI界面
    var name: String = "undefined"
    set(value) {
        field = value
        nameLabel.text = value
    }

    // 关键帧在列表中的索引值，更新这个数值会自动触发更新UI界面
    var index: Int = 0
    set(value) {
        field = value
        numberLabel.text = (value + 1).toString()
    }

    // 关键帧资源名称
    var resourceName: String = ""



    // 关键帧编号标签
    private val numberLabel: Label by lazy {
        val r = Label()
        r.prefWidth = 60.0
        r.font = Font.font(30.0)
        r
    }

    // 关键帧名称标签
    private val nameLabel: Label by lazy {
        val r = Label()
        r.prefWidth = 60.0
        r.isWrapText = true
        r
    }

    // 按钮的图形界面
    private val buttonGraphic: GridPane by lazy {
        val r = GridPane()
        r.add(numberLabel, 0, 0)
        r.add(nameLabel, 0, 1)
        r
    }


    init {
        prefWidth = 80.0
        prefHeight = 80.0
        graphic = buttonGraphic
    }

    fun getAnalyseKeyFrame(): AnalyseKeyFrame {
        analyseKeyFrame.name = name
        analyseKeyFrame.resourceName = resourceName
        return analyseKeyFrame
    }



}