package top.wupanhao.www.rss_client_kotlin.fragment

/**
 * Created by hao on 2018/7/24.
 */
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList
import android.R.attr.fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.feed_tab_fragment.*
import top.wupanhao.www.rss_client_kotlin.R


object ContentURL {

    var URLArrays = arrayOf("http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=3&category=1&ot=0&nt=0",
            "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=31&category=1&ot=0&nt=0",
            "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=45&category=1&ot=0&nt=0",
            "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=9000&category=1&ot=0&nt=0",
            "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=11&category=1&ot=0&nt=0",
            "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=1&category=1&ot=0&nt=0",
            "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=5&category=1&ot=0&nt=0",
            "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=12&category=1&ot=0&nt=0",
            "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=10&category=1&ot=0&nt=0",
            "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=48&category=1&ot=0&nt=0",
            "http://interfacev5.vivame.cn/x1-interface-v5/json/newdatalist.json?platform=android&installversion=5.6.7.1&channelno=XMSDA2320480700&mid=88cbade258b8e25f2dc582d3e95e16d6&latlng=40.112442,116.244871&uid=11545120&sid=0d680d2b-240a-4b4e-8427-44ca9109d822&type=1&id=25000&category=1&ot=0&nt=0")

    var titleArrays = arrayOf("娱乐", "精读", "历史", "艺术", "军事", "国际", "体育", "数码", "星座", "动漫", "电影")
}

/**
 * Created by hao on 2018/7/20.
 */
class FeedTabFragment : Fragment() {

    private var mViewPager: ViewPager? = null
    private var mMyTabFragmentPagerAdapter: MyTabFragmentPagerAdapter? = null
    private var fragments: MutableList<Fragment>? = null
    private var mTabLayout: TabLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.feed_tab_fragment, container, false)

        mTabLayout = view.findViewById(R.id.tab_main)
        mViewPager = view.findViewById(R.id.vp_menu_pager)

        initdata()
        return view
    }

    //初始化数据
    private fun initdata() {
        fragments = ArrayList()
        for (i in ContentURL.URLArrays){
            val f = FeedFragment()
            val args = Bundle()
            args.putString("url", i)
            f.setArguments(args)
            fragments!!.add(f)
        }
        mMyTabFragmentPagerAdapter = MyTabFragmentPagerAdapter(activity.supportFragmentManager, fragments!!)
        mViewPager!!.offscreenPageLimit = 4
        mViewPager!!.adapter = mMyTabFragmentPagerAdapter
        //将TabLayout和ViewPager绑定在一起，使双方各自的改变都能直接影响另一方，解放了开发人员对双方变动事件的监听
        mTabLayout!!.setupWithViewPager(mViewPager)
    }
}

class MyTabFragmentPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(fm) {
    private val mTitles = ContentURL.titleArrays

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

class  FeedTabActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_tab_fragment)
        val fragments  = ArrayList<FeedFragment>()
        for (i in ContentURL.URLArrays){
            val f = FeedFragment()
            val args = Bundle()
            args.putString("url", i)
            f.setArguments(args)
            fragments!!.add(f)
        }
        vp_menu_pager!!.offscreenPageLimit = 4
        vp_menu_pager!!.adapter = MyTabFragmentPagerAdapter(this.supportFragmentManager, fragments!!)
        //将TabLayout和ViewPager绑定在一起，使双方各自的改变都能直接影响另一方，解放了开发人员对双方变动事件的监听
        tab_main!!.setupWithViewPager(vp_menu_pager)
    }
}

