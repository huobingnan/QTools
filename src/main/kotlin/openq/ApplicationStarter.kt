package openq

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import openq.ui.MainView

class ApplicationStarter: Application(){

//    companion object {
//        private var fxmlFileLocation = "fxml"
//        private val controllerCache: HashMap<String, Any> = HashMap()
//        private val fxmlLoaderCache: HashMap<String, FXMLLoader> = HashMap()
//
//        fun setFXMLFileLocation(location: String) {
//            fxmlFileLocation = location
//        }
//
//        fun registerFXML(fxmlName: String) {
//            var fxml = fxmlName
//            if (!fxml.endsWith(".fxml")) fxml = "${fxml}.fxml"
//            if (!fxml.startsWith("/")) fxml = "/${fxml}"
//            fxml = "/${fxmlFileLocation}${fxml}"
//            println(fxml)
//            val loader = FXMLLoader()
//            loader.location = javaClass.getResource(fxml)
//            println(loader.location.toExternalForm())
//            val name = fxmlName.replace(".fxml", "").replace("/", "")
//            fxmlLoaderCache[name] = loader
//        }
//
//        fun <T> getController(name: String, clazz: Class<T>): T? {
//            return controllerCache[name] as T
//        }
//    }


    override fun init() {
        super.init()
//        fxmlLoaderCache.forEach { (k, v) -> controllerCache[k] = v.load() }
    }

    override fun start(primaryStage: Stage?) {
        primaryStage!!.width = 1000.0
        primaryStage.height = 800.0
        primaryStage.title = "GemoVASP"

        primaryStage.scene =  Scene(MainView())
        primaryStage.show()
    }


}