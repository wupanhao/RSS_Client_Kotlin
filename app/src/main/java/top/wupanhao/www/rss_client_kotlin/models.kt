package top.wupanhao.www.rss_client_kotlin

import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.feed_tab_fragment.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL
import java.util.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.webkit.WebView
import org.dom4j.io.SAXReader
import kotlin.collections.ArrayList


/**
 * Created by hao on 2018/7/25.
 */
class Rss{
    @JvmField var channel:Channel? = null
    @JvmField var item : Array<Item>? = null
}

class Item{
    @JvmField var title : String? = null
    @JvmField var link : String? = null
    @JvmField var pubDate : String? = null
    @JvmField var description : String? = null
}
class Channel{
    @JvmField var title : String? = null
    @JvmField var description : String? = null
    @JvmField var lastBuildDate : String? = null
    @JvmField var link : String? = null
}


object Data {

    var URLArrays = arrayOf("http://www.alibuybuy.com/feed",
            "http://www.xinhuanet.com/tech/news_tech.xml",
            "http://blog.sina.com.cn/rss/1286528122.xml",
            "http://blog.sina.com.cn/rss/1286528122.xml"
    )
    var titleArrays = arrayOf("alibuybuy", "新华tech","新浪","微软")
}



class RSSFragment: Fragment(){
    var path : String? = null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.feed_fragment,container,false)
        val list_view : RecyclerView = view.findViewById(R.id.list_view)
        list_view.layoutManager = LinearLayoutManager(this@RSSFragment.context, LinearLayoutManager.VERTICAL,true)
        if (arguments != null) {
            path = arguments.getString("url")
        }

        if(path == null)
            path = "http://feed.williamlong.info/"
        val url = path!!
        doAsync {
            val items = getItems(url)
            Log.d("items",items.toString())
            uiThread {
                if(items is List<Item>){
                    list_view.adapter = MyRSSAdapter(this@RSSFragment.context,R.layout.feed_item, items)
                }
            }
        }

        /*
        doAsync {
            if(path == null)
                path = "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=3&category=1&ot=0&nt=0";
            val s = URL(path).readText()
            Log.d("Json",s)
            var rss = XMLParser(Rss::class.java).fromXML(s)
            val lists = rss.item
            uiThread {
                if(lists is Array<Item>){
                    list_view.adapter = MyRSSAdapter(this@RSSFragment.context,R.layout.feed_item, lists)
                }
            }
        }
        */
        return view
    }
}

class MyRSSAdapter(val context: Context, val resource: Int, val items: List<Item>): RecyclerView.Adapter<MyRSSAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val image : ImageView = view.findViewById(R.id.feed_img)
        val title : TextView = view.findViewById(R.id.feed_title)
        val desc : TextView = view.findViewById(R.id.feed_desc)
        val time : TextView = view.findViewById(R.id.feed_time)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
//        if(data.img.isNotEmpty())
//            Picasso.with(context).load(data.img).into(holder.image )
        holder.title.text = data.title
        val desc = data.description
        if (desc is String )
            holder.desc.text = if(desc.length>250)desc.subSequence(0,250)else desc
//        holder.desc.loadDataWithBaseURL(null,data.description, "text/html", "utf-8", null)
        holder.time.text = data.pubDate
        holder.view.setOnClickListener { v ->
            val intent = Intent(context,WebActivity::class.java)
            intent.putExtra("url", data.link)
            context.startActivity(intent)
//            Toast.makeText(context,data.link,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(resource,parent,false)
        return ViewHolder(view)
    }
}


class MyRSSFragmentPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>,private val mTitles:Array<String>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
    override fun getCount(): Int {
        return mTitles.size
    }
    //用来设置tab的标题
    override fun getPageTitle(position: Int): CharSequence {
        return mTitles[position]
    }
}

class  RSSTabActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_tab_fragment)
        val fragments  = ArrayList<RSSFragment>()
        for (i in Data.URLArrays){
            val f = RSSFragment()
            val args = Bundle()
            args.putString("url", i)
            f.setArguments(args)
            fragments!!.add(f)
        }
        vp_menu_pager!!.offscreenPageLimit = 4
        vp_menu_pager!!.adapter = MyRSSFragmentPagerAdapter(this.supportFragmentManager, fragments!!,Data.titleArrays)
        //将TabLayout和ViewPager绑定在一起，使双方各自的改变都能直接影响另一方，解放了开发人员对双方变动事件的监听
        tab_main!!.setupWithViewPager(vp_menu_pager)
    }

    fun setPage(i:Int){
        vp_menu_pager.setCurrentItem(i)
    }

}

class  RSSTabFragment: Fragment(){
    var urls : Array<String> = Data.URLArrays
    var titles:Array<String> = Data.titleArrays

    val handler =  object: Handler() {
        override  fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == 1){
                val n = msg.obj.toString().toInt()
                if(n < urls.size )
                    vp_menu_pager.setCurrentItem(n)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.feed_tab_fragment,container,false)
        val vp_menu_pager = view.findViewById<ViewPager>(R.id.vp_menu_pager)
        val tab_main = view.findViewById<TabLayout>(R.id.tab_main)
        val fragments  = java.util.ArrayList<RSSFragment>()
        if (arguments != null) {
            urls = arguments.getStringArray("urls")
            titles = arguments.getStringArray("titles")
        }

        for (i in urls){
            val f = RSSFragment()
            val args = Bundle()
            args.putString("url", i)
            f.setArguments(args)
            fragments!!.add(f)
        }

        vp_menu_pager!!.offscreenPageLimit = 4
        vp_menu_pager!!.adapter = MyRSSFragmentPagerAdapter(activity.supportFragmentManager, fragments!!,titles)
        //将TabLayout和ViewPager绑定在一起，使双方各自的改变都能直接影响另一方，解放了开发人员对双方变动事件的监听
        tab_main!!.setupWithViewPager(vp_menu_pager)

        return view
    }


}

