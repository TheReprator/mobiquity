package reprator.mobiquity.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import reprator.mobiquity.home.databinding.FragmentHomeBinding
import reprator.mobiquity.navigation.HOME_DATA_CONSTANT

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        val navController = setupNavController()
        binding.fragmentHomeBottomNavView.setupWithNavController(navController)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            HOME_DATA_CONSTANT
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