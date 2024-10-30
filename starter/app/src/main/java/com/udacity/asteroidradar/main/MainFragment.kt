package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.AsteroidsAdapter
import com.udacity.asteroidradar.AstroDatabase
import com.udacity.asteroidradar.MainActivity
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import java.util.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel //by lazy {
        //ViewModelProvider(this).get(MainViewModel::class.java)
    //}

    lateinit var  dateList :ArrayList<String>

    //private lateinit var thisContext : Context

    private var viewModeId   = R.id.show_buy_menu

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)

        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val datasource =  AstroDatabase.getInstance(application).AstroDatabaseDAO
        val viewModelFactory = MainViewModelFactory(datasource,application,requireContext())
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel ::class.java)

        dateList  = viewModel.getNextSevenDaysFormattedDates()

        binding.lifecycleOwner = this
        binding.setLifecycleOwner(this)

        binding.viewModel = viewModel

        //thisContext = requireContext()

        val adapter = AsteroidsAdapter()
        adapter.activity = activity as MainActivity


        binding.asteroidRecycler.adapter = adapter

        viewModel.asteriods.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer{
                    newAstros ->
                if(newAstros != null) {
                    when (viewModeId) {
                        R.id.show_all_menu -> newAstros.retainAll { dateList.contains(it.closeApproachDate) }
                        R.id.show_rent_menu -> newAstros.retainAll { it.closeApproachDate == dateList[0] }
                    }
                    adapter.data = newAstros.toList()
                }
            } )

        viewModel.image.observe(viewLifecycleOwner,androidx.lifecycle.Observer {
                newImage -> if(newImage!= null) {
            Picasso.get().load(newImage.url).into(binding.activityMainImageOfTheDay)
            binding.textView.text = newImage.title
            binding.activityMainImageOfTheDay.contentDescription = newImage.title
                }
        })



        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        viewModeId = item.itemId

        viewModel.update()


        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}


