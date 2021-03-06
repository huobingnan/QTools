package openq

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import openq.graph.BondLengthTableViewBuilder
import openq.ui.ChannelView
import openq.ui.GraphAreaView

import openq.ui.MainView
import openq.ui.ResourceBrowserView

/**
 * 引入了Google Guice轻量级依赖注入框架，项目中部分对象的创建与注入
 * 将交由Guice管理。
 */
class BeansProvider : AbstractModule() {


    override fun configure() {
        // 主界面
        bind(MainView::class.java).`in`(Scopes.SINGLETON)
        // 资源浏览界面
        bind(ResourceBrowserView::class.java).`in`(Scopes.SINGLETON)
        // 结果展示界面
        bind(GraphAreaView::class.java).`in`(Scopes.SINGLETON)
        // 分析通道界面
        bind(ChannelView::class.java).`in`(Scopes.SINGLETON)

        // ----------------------- 图形构建器 -----------------------
        bind(BondLengthTableViewBuilder::class.java).`in`(Scopes.SINGLETON)
    }
}