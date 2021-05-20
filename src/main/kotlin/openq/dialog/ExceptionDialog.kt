package openq.dialog

import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import java.io.PrintWriter
import java.io.StringWriter

/**
 * 异常显示对话框
 */
class ExceptionDialog(): Dialog<Void>() {


    private val textArea: TextArea by lazy {
        val textArea = TextArea()
        textArea.isEditable = false
        textArea.isWrapText = true

        textArea.maxWidth = Double.MAX_VALUE
        textArea.maxHeight = Double.MAX_VALUE
        textArea
    }


    init {
        title = "exception"
        headerText = "An exception occur!!!"


        GridPane.setVgrow(textArea, Priority.ALWAYS)
        GridPane.setHgrow(textArea, Priority.ALWAYS)

        val expContent = GridPane()
        expContent.maxWidth = Double.MAX_VALUE
        expContent.add(textArea, 0, 0);
        dialogPane.expandableContent = expContent
        dialogPane.buttonTypes.add(ButtonType.OK)
    }

    fun accept(exception: Exception) {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        exception.printStackTrace(printWriter)
        textArea.text = stringWriter.toString()
        contentText = "The exception stacktrace was:\n ${exception.message}"

    }

}