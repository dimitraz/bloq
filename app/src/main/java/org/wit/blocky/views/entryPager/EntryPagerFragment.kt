package org.wit.blocky.views.entryPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.pager_fragment.*
import org.wit.blocky.R
import org.wit.blocky.adapters.PagerAdapter
import org.wit.blocky.views.home.HomeViewModel

class EntryPagerFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pager_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = PagerAdapter(activity!!.supportFragmentManager, viewModel.entries)
        viewPager.adapter = pagerAdapter

        val bundle = arguments
        if (bundle != null) {
            val position = bundle.getInt("position")
            viewPager.setCurrentItem(position, false)
        }
    }
}
