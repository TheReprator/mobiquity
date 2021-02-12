package reprator.mobiquity.saveCity.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import reprator.mobiquity.base.util.isNull
import reprator.mobiquity.base_android.util.ItemOffsetDecoration
import reprator.mobiquity.saveCity.R
import reprator.mobiquity.saveCity.databinding.FragmentBookmarkcityBinding
import reprator.mobiquity.saveCity.modal.LocationModal
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SaveCityFragment : Fragment(R.layout.fragment_bookmarkcity), BookMarkItemClicked {

    companion object{
        private const val DATA_CONSTANT = "sendDataToHost"
    }

    private var _binding: FragmentBookmarkcityBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var saveCityAdapter: SaveCityAdapter

    private val viewModel: SaveCityViewModal by viewModels()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentBookmarkcityBinding.bind(view).also {
            it.saveCityAdapter = saveCityAdapter
            it.saveCityViewModal = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        setUpRecyclerView()
        initializeObserver()

        if(savedInstanceState.isNull())
            viewModel.getSavedLocationList()
    }

    private fun setUpRecyclerView() {

        with(binding.saveCityRec) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(
                ItemOffsetDecoration(requireContext(), R.dimen.list_item_padding)
            )
        }

        saveCityAdapter.saveItemClicked(this)
    }

    private fun initializeObserver() {
        viewModel.bookMarkList.observe(viewLifecycleOwner, {
            saveCityAdapter.submitList(it)
        })
    }

    override fun selectedPosition(item: LocationModal) {
        requireParentFragment().requireParentFragment().findNavController().currentBackStackEntry!!.
        savedStateHandle.set(DATA_CONSTANT, true)

        requireParentFragment().findNavController().currentBackStackEntry!!.
        savedStateHandle.set(DATA_CONSTANT, true)

        val direction = SaveCityFragmentDirections.
            navigationHomeBookmarkLocationsToNavigationCityDetail(item.latLong, item.address)
        findNavController().navigate(direction)
    }
}