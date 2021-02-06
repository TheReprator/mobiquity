package reprator.mobiquity.city.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import reprator.mobiquity.base_android.util.GeneralDiffUtil
import reprator.mobiquity.city.databinding.RowWeatherDataBinding
import reprator.mobiquity.city.modals.LocationModal
import javax.inject.Inject

class CityDetailAdapter @Inject constructor() :
    ListAdapter<LocationModal, VHCurrencyRates>(GeneralDiffUtil<LocationModal>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHCurrencyRates {
        val binding = RowWeatherDataBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return VHCurrencyRates(binding)
    }

    override fun onBindViewHolder(holder: VHCurrencyRates, position: Int) {
        holder.binding.locationModal = getItem(position)
        holder.binding.executePendingBindings()
    }
}

class VHCurrencyRates(val binding: RowWeatherDataBinding) :
    RecyclerView.ViewHolder(binding.root)
