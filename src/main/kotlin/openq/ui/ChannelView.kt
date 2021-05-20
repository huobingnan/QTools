package openq.ui

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import openq.dialog.ChannelSettingDialog

class ChannelView(): BorderPane() {


    private val channelSettingDialog: ChannelSettingDialog by lazy {
        ChannelSettingDialog()
    }

    private lateinit var channelTabPane: TabPane

    private val showButton: Button by lazy {
        val result = Button("show")
        result
    }
    private val newButton: Button by lazy {
        val result = Button("new")
        result.setOnAction {
            channelSettingDialog.clear()
            channelSettingDialog.showAndWait()
        }
        result
    }
    private val settingButton = Button("setting")
    private val deleteButton = Button("delete")


    init {
        left = setupLeft()
        customButtons()
    }


    private fun setupLeft(): VBox {
        val box = VBox()
        box.children.addAll(showButton, newButton, settingButton, deleteButton)
        VBox.setMargin(showButton, Insets(8.0))
        VBox.setMargin(newButton, Insets(8.0))
        VBox.setMargin(settingButton, Insets(8.0))
        VBox.setMargin(deleteButton, Insets(8.0))
        return box
    }

    private fun setupCenter(): TabPane {
        channelTabPane = TabPane()
        return channelTabPane
    }


    private fun customButtons() {
        showButton.prefWidth = 80.0
        newButton.prefWidth = 80.0
        settingButton.prefWidth = 80.0
        deleteButton.prefWidth = 80.0
    }


}