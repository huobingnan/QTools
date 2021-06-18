package openq

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import openq.ui.MainView
import org.slf4j.LoggerFactory

class ApplicationStarter: Application(){

    private val log = LoggerFactory.getLogger(ApplicationStarter::class.java)

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
        log.info("Application starting...")
        primaryStage!!.width = 1000.0
        primaryStage.height = 800.0
        primaryStage.title = "GemoVASP"
        primaryStage.scene =  Scene(MainView())
        primaryStage.show()
    }


}