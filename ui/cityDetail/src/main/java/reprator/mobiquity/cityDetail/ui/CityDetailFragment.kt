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
import javax.inject.Inject

@AndroidEntryPoint
class CityDetailFragment : Fragment(R.layout.fragment_city_detail) {

    companion object{
        private const val DATA_CONSTANT = "sendDataToHost"
    }

    private var _binding: FragmentCityDetailBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    @Inject
    lateinit var cityDetailAdapter: CityDetailAdapter

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

        binding.cityDetailToolBar.title =
            CityDetailFragmentArgs.fromBundle(requireArguments()).title
    }

    private fun handleBackButton() {
        requireParentFragment().requireParentFragment().findNavController().currentBackStackEntry!!.
        savedStateHandle.set(DATA_CONSTANT, false)

        requireParentFragment().findNavController().currentBackStackEntry!!.
        savedStateHandle.set(DATA_CONSTANT, false)

        findNavController().navigateUp()
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