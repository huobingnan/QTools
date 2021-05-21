package openq

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import openq.ui.MainView

class ApplicationStarter: Application(){

    companion object {
        private val singletonComponentCache: HashMap<Any, Any> = HashMap()

        fun registerSingletonComponent(component: Any) {
            singletonComponentCache[component.javaClass] = component
        }

        fun <T> getSingletonComponent(clazz: Class<T>): T? {
            return singletonComponentCache[clazz] as T?
        }
    }

    override fun start(primaryStage: Stage?) {
        primaryStage!!.width = 1000.0
        primaryStage.height = 800.0
        primaryStage.title = "GemoVASP"

        primaryStage.scene =  Scene(MainView())
        primaryStage.show()
    }


}