package openq

import com.google.inject.Guice
import com.google.inject.Injector
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import openq.ui.MainView
import org.slf4j.LoggerFactory

class ApplicationStarter: Application(){

    private val log = LoggerFactory.getLogger(ApplicationStarter::class.java)

    companion object {
        @JvmStatic private val logger = LoggerFactory.getLogger(ApplicationStarter::class.java)
        // Application的IOC容器，存储着对象的实例
        @JvmStatic val context: Injector = Guice.createInjector(BeansProvider())
        init {
            logger.info("Context initialized successfully!")
        }
    }


    override fun start(primaryStage: Stage?) {
        log.info("Application starting...")
        primaryStage!!.width = 1000.0
        primaryStage.height = 800.0
        primaryStage.title = "QTools V1.0.0(Preview)"
        primaryStage.scene =  Scene(context.getInstance(MainView::class.java))
        primaryStage.show()
    }


}