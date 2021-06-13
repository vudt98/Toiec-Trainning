package com.example.toeictraining.ui.fragments.test.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.toeictraining.R
import com.example.toeictraining.ui.fragments.test.start_test.StartTestFragment
import com.example.toeictraining.ui.main.MainActivity
import kotlinx.android.synthetic.main.home_test_fragment.*

class HomeTestFragment : Fragment() {

    companion object {
        val TAG = HomeTestFragment::class.java.name
    }

    private lateinit var viewModel: TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_test_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TestViewModel::class.java)
        initViews()
    }

    private fun initViews() {
        (activity as MainActivity).setTitle(resources.getString(R.string.test_toeic))
        (activity as MainActivity).setRightButtonText("")

        configNavigationIcon()

        setRecyclerViewListPart()

//        drawerToggle.isDrawerIndicatorEnabled = true
//        toolbar.setNavigationIcon(R.drawable.menu_white_24dp)
    }

    private fun configNavigationIcon() {
        val actionBar = (activity as MainActivity).supportActionBar
        val actionBarDrawerToggle = (activity as MainActivity).getDrawerToggle()
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
    }

    private fun setRecyclerViewListPart() {
        val resourcesImage = arrayOf(
            R.drawable.part_1,
            R.drawable.part_2,
            R.drawable.part_3,
            R.drawable.part_4,
            R.drawable.part_5,
            R.drawable.part_6,
            R.drawable.part_7
        )
        recyclerViewPart.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        with(recyclerViewPart) {
            addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                ).apply {
                    setDrawable(context?.getDrawable(R.drawable.divider_recyclerview_vertical)!!)
                })
            addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.HORIZONTAL
                ).apply {
                    setDrawable(context?.getDrawable(R.drawable.divider_recyclerview_horizontal_10)!!)
                })
            adapter = activity?.let {
                ListPartAdapter(
                    it,
                    resourcesImage
                )
            }
        }
    }
}
