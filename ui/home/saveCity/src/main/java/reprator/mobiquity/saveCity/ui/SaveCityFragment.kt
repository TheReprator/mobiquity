package reprator.mobiquity.saveCity.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import reprator.mobiquity.base.util.isNull
import reprator.mobiquity.base_android.util.ItemOffsetDecoration
import reprator.mobiquity.navigation.SavedCityNavigator
import reprator.mobiquity.saveCity.R
import reprator.mobiquity.saveCity.databinding.FragmentBookmarkcityBinding
import reprator.mobiquity.saveCity.modal.LocationModal
import javax.inject.Inject


@AndroidEntryPoint
class SaveCityFragment : Fragment(R.layout.fragment_bookmarkcity), SearchView.OnQueryTextListener,
    BookMarkItemClicked {

    private var _binding: FragmentBookmarkcityBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var saveCityAdapter: SaveCityAdapter

    @Inject
    lateinit var savedCityNavigator: SavedCityNavigator

    private val viewModel: SaveCityViewModal by viewModels()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.searchmenu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val mSearchMenuItem = menu.findItem(R.id.action_search)
        val searchView = mSearchMenuItem.actionView as SearchView

        searchView.setOnQueryTextListener(this)

        mSearchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                savedCityNavigator.hideBottomNavigationView(
                    getNavController()
                )
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                savedCityNavigator.showBottomNavigationView(
                    getNavController()
                )

                return true
            }
        })

        val searchQuery = viewModel.searchQuery.value
        if (searchQuery.isEmpty())
            return

        mSearchMenuItem.expandActionView()
        searchView.setQuery(searchQuery, true)
        searchView.clearFocus()
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

        if (savedInstanceState.isNull())
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
        viewModel.bookMarkListManipulated.observe(viewLifecycleOwner, {
            saveCityAdapter.submitList(it)
        })
    }

    override fun selectedPosition(item: LocationModal) {
        savedCityNavigator.hideBottomNavigationView(
            getNavController()
        )

        savedCityNavigator.navigateToCityDetailScreen(
            findNavController(),
            item.latLong,
            item.address
        )
    }

    private fun getNavController() =
        requireParentFragment().requireParentFragment().findNavController()

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.setSearchQuery(newText)
        return true
    }
}