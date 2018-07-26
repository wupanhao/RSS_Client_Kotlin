package top.wupanhao.www.rss_client_kotlin.fragment

/**
 * Created by hao on 2018/7/24.
 */

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import top.wupanhao.www.homework.model.Items
import top.wupanhao.www.homework.model.Result
import top.wupanhao.www.rss_client_kotlin.R
import top.wupanhao.www.rss_client_kotlin.WebActivity
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by hao on 2018/7/23.
 */

class FeedFragment:Fragment(){
    var path : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.feed_fragment,container,false)
        val list_view : RecyclerView = view.findViewById(R.id.list_view)
        list_view.layoutManager = LinearLayoutManager(this@FeedFragment.context, LinearLayoutManager.VERTICAL,true)
        if (arguments != null) {
            path = arguments.getString("url")
        }
        doAsync {
            if(path == null)
                path = "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=3&category=1&ot=0&nt=0";
            val s = URL(path).readText()
            val gson = Gson()
            val result = gson.fromJson(s, Result::class.java)
            Log.d("Json",s)
            val lists = result.data.feedlist.map { it.items[0] }
            uiThread {
                if(lists is List<Items>){
                    val data = if(lists.size < 8)lists else lists.subList(0,7)
                    list_view.adapter = MyFeedlistAdapter(this@FeedFragment.context,R.layout.feed_item, data)
                }
            }
        }
        return view
    }
}

class MyFeedlistAdapter(val context: Context, val resource: Int, val items: List<Items>): RecyclerView.Adapter<MyFeedlistAdapter.ViewHolder>() {

    class ViewHolder(val view:View) : RecyclerView.ViewHolder(view){
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
        if(data.img.isNotEmpty())
            Picasso.with(context).load(data.img).into(holder.image )
        holder.title.text = data.title
        holder.desc.text = data.desc
        holder.view.setOnClickListener { _ ->
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("url", data.fileurl)
            context.startActivity(intent)
        }

        val c = Calendar.getInstance()
        c.timeInMillis = data.time
        holder.time.text = SimpleDateFormat("yyyy-MM-dd").format(c.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(resource,parent,false)
        return ViewHolder(view)
    }
}
