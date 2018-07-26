package top.wupanhao.www.rss_client_kotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import top.wupanhao.www.rss_client_kotlin.fragment.FeedTabActivity
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.feed_tab_fragment.*
import kotlinx.coroutines.experimental.async
import org.dom4j.DocumentException
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.xml.sax.SAXParseException
import java.io.*
import java.net.URL

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val file_name = "sub3.json"
    var subscriptions =  ArrayList<Subscription>()
    var urls = ArrayList<String>().toMutableList()
    var titles = ArrayList<String>().toMutableList()

    var adapter :MyListAdapter? = null

    fun initData(){
//        for (i in 0 until urls.size)
//            subscriptions.add(Subscription(titles[i],urls[i]))
//        val data = Gson().toJson(subscriptions)
//        outputFilePrivate(file_name,data)
        urls = Data.URLArrays.toMutableList()
        titles = Data.titleArrays.toMutableList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        initData()

        val data = inputFile(file_name)

        if (data != null){
            Log.d("Main",data)
            val listType = object : TypeToken<ArrayList<Subscription>>(){}.type
            val gson = Gson()
            val result : ArrayList<Subscription> = gson.fromJson(data, listType)
            if (result.isNotEmpty()){
                subscriptions = result
                urls = urls.plus( subscriptions.map { it.url }.toTypedArray()).toMutableList()
                titles = titles.plus(subscriptions.map { it.title }.toTypedArray()).toMutableList()
            }
//            Log.d("Main",result.toString())
        }
        adapter = MyListAdapter(this,R.layout.item_delete,titles)

        refresh()

/*
        val manager = getSupportFragmentManager();
        val transaction = manager.beginTransaction();
        val f = RSSTabFragment()
        if (subscriptions.size > 0){
            val args = Bundle()
            args.putStringArray("urls", subscriptions.map { it.url }.toTypedArray() )
            args.putStringArray("titles", subscriptions.map { it.title }.toTypedArray() )
            f.setArguments(args)
        }
        transaction.add(R.id.fragment_container,f)
        transaction.commit()
*/


        fab.setOnClickListener { _ ->
            addRSS()
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
        }

        action_add.setOnClickListener { _ ->
            addRSS()
        }

        action_news.setOnClickListener { _ ->
            val intent = Intent(this,FeedTabActivity::class.java)
            startActivity(intent)
        }

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    fun setPage(i:Int){
        vp_menu_pager.setCurrentItem(i)
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    fun refresh(){
        var fragments = ArrayList<RSSFragment>()

        for (i in urls){
            val f = RSSFragment()
            val args = Bundle()
            args.putString("url", i)
            f.setArguments(args)
            fragments.add(f)
        }

        vp_menu_pager!!.offscreenPageLimit = 4
        vp_menu_pager!!.adapter = MyRSSFragmentPagerAdapter(this.supportFragmentManager, fragments,titles.toTypedArray())
        //将TabLayout和ViewPager绑定在一起，使双方各自的改变都能直接影响另一方，解放了开发人员对双方变动事件的监听
        tab_main!!.setupWithViewPager(vp_menu_pager)
        adapter!!.notifyDataSetChanged()
        list_view_rss.adapter =MyListAdapter(this,R.layout.rss_item,titles)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                deleteChannel()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {
                val intent = Intent(this,WebActivity::class.java)
                startActivity(intent)
                return false
            }
            R.id.nav_share -> {
                val intent = Intent(this,RSSTabActivity::class.java)
                startActivity(intent)
                return false
            }
            R.id.nav_send -> {
                val intent = Intent(this,FeedTabActivity::class.java)
                startActivity(intent)
                return false
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    fun deleteChannel(){
        val inflater = getLayoutInflater();
        val dialog = inflater.inflate(R.layout.delete_dialog, findViewById(R.id.dialog));
        val list_view :ListView =  dialog.findViewById(R.id.list_view);
        list_view.adapter = adapter
        val builder = AlertDialog.Builder(this)
        builder.setTitle("删除channel")
        val listener = DialogInterface.OnClickListener { _, _ ->

        }
        builder.setPositiveButton("确定",listener )
        builder.setView(dialog)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.show()
    }

    fun addRSS(){
        val inflater = getLayoutInflater();
        val dialog = inflater.inflate(R.layout.dialog, findViewById(R.id.dialog));
        val title :EditText =  dialog.findViewById(R.id.title);
        val url :EditText =  dialog.findViewById(R.id.url);
        val builder = AlertDialog.Builder(this)
        builder.setTitle("添加RSS源")
        val listener = DialogInterface.OnClickListener { _, _ ->
//            Toast.makeText(this@MainActivity, title.text.toString(), Toast.LENGTH_SHORT).show()
            if(title.text.isNotEmpty() && url.text.isNotEmpty()){
                doAsync {

                    val lists = getItems(url.text.toString())
                    uiThread {
                        if (lists == null){
                            Toast.makeText(this@MainActivity,"invalid url",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val sub = Subscription(title.text.toString(),url.text.toString())
                            urls.add(sub.url)
                            titles.add(sub.title)
                            subscriptions.add(sub)
                            val data = Gson().toJson(subscriptions)
                            outputFilePrivate(file_name,data)
                            Toast.makeText(this@MainActivity," 添加成功",Toast.LENGTH_SHORT).show()
                            refresh()
                            vp_menu_pager.setCurrentItem(titles.size-1)
                        }
                    }

                }

            }
        }
        builder.setPositiveButton("添加",listener )
        builder.setView(dialog)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.show()
    }

    fun outputFilePrivate(name:String,data : String) {
        var flieOS: FileOutputStream? = null
        var bfWriter: BufferedWriter? = null
        try {
            flieOS = openFileOutput(name, Context.MODE_PRIVATE)
            bfWriter = BufferedWriter(OutputStreamWriter(flieOS))
            bfWriter.write(data)
            Log.d("Main","Write data $data")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bfWriter!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun inputFile(name:String):String? {
        var fileIS: FileInputStream? = null
        var bfReader: BufferedReader? = null
        try {
            fileIS = openFileInput(name)
            bfReader = BufferedReader(InputStreamReader(fileIS))
            val text = bfReader.readText()
            return text
//            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if(bfReader != null)
                    bfReader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    inner class MyListAdapter(private val context:Context,val resource:Int,val items:List<String>): ArrayAdapter<String>(context,resource,items) {
        override fun getContext(): Context {
            return super.getContext()
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val data = getItem(position)
            val view = LayoutInflater.from(getContext()).inflate(resource,parent,false)
            val name : TextView = view.findViewById(R.id.title)
            name.text = data
            view.setOnClickListener { _ ->
                setPage(position)
            }
            var delete : View? = null
            try {
                 delete = view.findViewById(R.id.delete_button)
            }
            catch (e : Exception){
            }
            if(delete != null){
                delete.setOnClickListener { _ ->
                    urls.removeAt(position)
                    titles.removeAt(position)
                    adapter!!.notifyDataSetChanged()
                    val e = subscriptions.find { it.title == data }
                    if(e != null){
                        subscriptions.remove(e)
                        val data = Gson().toJson(subscriptions)
                        outputFilePrivate(file_name,data)
                        Toast.makeText(this@MainActivity," 删除成功",Toast.LENGTH_SHORT).show()
                    }
                    refresh()
                }
            }
            return view
        }
    }
}

data class Subscription(val title: String,val url:String)
