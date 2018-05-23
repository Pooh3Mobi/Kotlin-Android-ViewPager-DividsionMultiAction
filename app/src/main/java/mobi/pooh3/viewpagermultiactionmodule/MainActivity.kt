package mobi.pooh3.viewpagermultiactionmodule

import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View


sealed class FragmentType
data class Normal(val link: String) : FragmentType()
object Black : FragmentType()

class MainActivity : AppCompatActivity() {

    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var mViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mViewPager = (findViewById<View>(R.id.container) as ViewPager).apply {
            mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
            adapter = mSectionsPagerAdapter
        }
        val deleteActVC = DeleteActionViewController(viewPager = mViewPager, adapter = mSectionsPagerAdapter)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            deleteActVC.deleteCurrentItem()
        }

    }
}
class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val links = arrayListOf(
            "https://media1.giphy.com/media/tXL4FHPSnVJ0A/giphy.gif",
            "https://media0.giphy.com/media/o5oLImoQgGsKY/giphy.gif",
            "https://media2.giphy.com/media/u5eXlkXWkrITm/giphy.gif",
            "https://media3.giphy.com/media/o0vwzuFwCGAFO/giphy.gif",
            "https://media1.giphy.com/media/7NoNw4pMNTvgc/giphy.gif",
            "https://media0.giphy.com/media/tBxyh2hbwMiqc/giphy.gif"
    )
    val types: MutableList<FragmentType> = (1..links.size).map { Normal(links[it-1]) }.toMutableList()
    override fun getItem(position: Int): Fragment {
        val type = types[position]
        return when(type) {
            is Normal -> PlaceholderFragment.newInstance(type.link)
            else -> BlackFragment.newInstance()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return types.size
    }
}

class DeleteActionViewController(val viewPager: ViewPager, val adapter: SectionsPagerAdapter) {
    val handler: Handler by lazy { Handler() }
    fun deleteCurrentItem() {

        if (adapter.types.isEmpty()) return

        // クラウドにある画像を消すなどで成功時のリアクション

        val curPos = viewPager.currentItem
        val nextPos = when {
            adapter.hasNextAt(curPos) -> viewPager.currentItem + 1
            adapter.hasPrevAt(curPos) -> viewPager.currentItem - 1
            else -> viewPager.currentItem
        }

        val removed = adapter.types.removeAt(curPos)
        adapter.types.add(curPos, Black)
        adapter.notifyDataSetChanged()

        val deleteAction: () -> Unit = {
            adapter.types.removeAt(curPos)
            adapter.notifyDataSetChanged()
            viewPager.setCurrentItem(curPos, false)
        }

        val movingNextAndDelete: () -> Unit = {
            viewPager.currentItem = nextPos
            handler.postDelayed(deleteAction, 300)
        }

        handler.postDelayed(movingNextAndDelete, 300)
    }
}

@Suppress("unused")
private fun SectionsPagerAdapter.hasPrevAt(curPos: Int): Boolean =
        curPos != 0
private fun SectionsPagerAdapter.hasNextAt(curPos: Int): Boolean =
        curPos < types.size - 1
