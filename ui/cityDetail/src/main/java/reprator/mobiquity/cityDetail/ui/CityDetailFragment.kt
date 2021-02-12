package reprator.mobiquity.cityDetail.ui

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import reprator.mobiquity.base.util.isNull
import reprator.mobiquity.base_android.util.ItemOffsetDecoration
import reprator.mobiquity.cityDetail.CityDetailViewModal
import reprator.mobiquity.cityDetail.R
import reprator.mobiquity.cityDetail.databinding.FragmentCityDetailBinding
import reprator.mobiquity.navigation.CityDetailNavigator
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CityDetailFragment : Fragment(R.layout.fragment_city_detail) {

    private var _binding: FragmentCityDetailBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    @Inject
    lateinit var cityDetailAdapter: CityDetailAdapter

    @Inject
    lateinit var cityDetailNavigator: CityDetailNavigator

    private val viewModel: CityDetailViewModal by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            handleBackButton()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCityDetailBinding.bind(view).also {
            it.weatherAdapter = cityDetailAdapter
            it.cityDetailViewModal = viewModel
            it.title =  CityDetailFragmentArgs.fromBundle(requireArguments()).title
            it.lifecycleOwner = viewLifecycleOwner
        }

        setToolBar()

        setUpRecyclerView()
        initializeObserver()

        if(savedInstanceState.isNull()) {
            viewModel.getForeCastWeatherUse()
        }
    }

    private fun setToolBar() {
        binding.cityDetailToolBar.setOnClickListener {
            handleBackButton()
        }
    }

    private fun handleBackButton() {
        Timber.e("back button handle cityDetail")
        cityDetailNavigator.savedStateHandleCurrentBackStackEntry(requireParentFragment().requireParentFragment().findNavController(),
            reprator.mobiquity.navigation.DATA_CONSTANT, false)

        cityDetailNavigator.navigateToBack(findNavController())
    }

    private fun setUpRecyclerView() {
        with(binding.cityDetailForecastRec) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            addItemDecoration(
                ItemOffsetDecoration(requireContext(), R.dimen.list_item_padding)
            )
        }
    }

    private fun initializeObserver() {

        viewModel._foreCastWeatherList.observe(viewLifecycleOwner, {
            cityDetailAdapter.submitList(it)
        })

    }
}