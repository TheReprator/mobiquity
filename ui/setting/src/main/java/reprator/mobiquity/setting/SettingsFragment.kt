package reprator.mobiquity.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import reprator.mobiquity.base_android.extensions.shortToast
import reprator.mobiquity.setting.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModal: SettingsViewModal by viewModels()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSettingsBinding.bind(view).also {
            it.viewModel = viewModal
            it.lifecycleOwner = viewLifecycleOwner
        }

        viewModal.isSuccess.observe(viewLifecycleOwner, {
            requireContext().shortToast("Data Saved Successfully")
        })
    }


}