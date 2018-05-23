package mobi.pooh3.viewpagermultiactionmodule

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide


class PlaceholderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_main, container, false)
        Glide
                .with(this)
                .load(arguments!!.getString(LINK_KEY))
                .into(view.findViewById<ImageView>(R.id.image))
        return view
    }

    companion object {
        val LINK_KEY = "link_key"
        fun newInstance(link: String): PlaceholderFragment {
            return PlaceholderFragment().apply {
                this.arguments = Bundle(1).apply {
                    putString(LINK_KEY, link)
                }
            }
        }
    }
}

class BlackFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {
        fun newInstance(): BlackFragment {
            return BlackFragment()
        }
    }
}