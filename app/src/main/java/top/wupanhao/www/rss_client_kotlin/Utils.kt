package top.wupanhao.www.rss_client_kotlin

import android.util.Log
import kotlinx.coroutines.experimental.async
import org.dom4j.io.SAXReader
import top.wupanhao.www.rss_client_kotlin.R.id.url


/**
 * Created by hao on 2018/7/26.
 */

fun getItems(url:String) : List<Item>?{

    var lists = java.util.ArrayList<Item>()
    val reader = SAXReader()
    try {
        val document = reader.read(url)
        val ele = document.rootElement.element("channel")
        for(i in ele.elements("item")){
            var item = Item()
            item.title = i.elementText("title")
            item.link = i.elementText("link")
            item.description = i.elementText("description")
            item.pubDate = i.elementText("pubDate")
            lists.add(item)
        }
        return lists
    } catch (e: Exception) {
        // TODO Auto-generated catch block
        e.printStackTrace()
        return null
    }
}

fun test(){
    var xml = "<bookstore>\n" +
            "  <book category=\"children\">\n" +
            "    <title>Harry Potter</title>\n" +
            "    <author>J K. Rowling</author>\n" +
            "    <author>K. Kongsin</author>\n" +
            "    <year>2005</year>\n" +
            "    <price>29.99</price>\n" +
            "  </book>\n" +
            "  <book category=\"web\">\n" +
            "    <title>Learning XML</title>\n" +
            "    <author>Erik T. Ray</author>\n" +
            "    <year>2003</year>\n" +
            "    <price>39.95</price>\n" +
            "  </book>\n" +
            "</bookstore>";

    class Book {
        @JvmField var title: String? = null
        @JvmField var author: String? = null
        @JvmField var year: String? = null
        @JvmField var price: String? = null
    }
    class Bookstore {
        @JvmField var book: Array<Book>? = null
    }

    var book = XMLParser(Bookstore::class.java).fromXML(xml)

// Print object to see the result
    book.book!!.forEach {  book ->
        println("---------------------")
        println("TITLE : " + book.title)
        println("AUTHOR : " + book.author)
        println("YEAR : " + book.year)
        println("PRICE : " + book.price)
    }
    val reader = SAXReader()
    val document = reader.read("http://feed.williamlong.info/")
    Log.d("doc4j",document.toString())


}