package reprator.mobiquity.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import reprator.mobiquity.home.databinding.FragmentHomeBinding
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    companion object {
        private const val DATA_CONSTANT = "sendDataToHost"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        val navController = setupNavController()
        binding.fragmentHomeBottomNavView.setupWithNavController(navController)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            DATA_CONSTANT
        )?.observe(viewLifecycleOwner, {
            if (it)
                binding.fragmentHomeBottomNavView.visibility = View.GONE
            else
                binding.fragmentHomeBottomNavView.visibility = View.VISIBLE
        })
    }

    private fun setupNavController(): NavController {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragmentHome_container) as NavHostFragment
        return navHostFragment.navController
    }
}