package reprator.mobiquity.saveCity.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import reprator.mobiquity.base_android.util.GeneralDiffUtil
import reprator.mobiquity.saveCity.databinding.RowSavedCityBinding
import reprator.mobiquity.saveCity.modal.LocationModal
import javax.inject.Inject

class SaveCityAdapter @Inject constructor() :
    ListAdapter<LocationModal, VHCurrencyRates>(GeneralDiffUtil<LocationModal>()) {

    private lateinit var bookMarkItemClicked: BookMarkItemClicked

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHCurrencyRates {
        val binding = RowSavedCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return VHCurrencyRates(binding)
    }

    override fun onBindViewHolder(holder: VHCurrencyRates, position: Int) {
        holder.binding.locationModal = getItem(position)
        holder.binding.rowSavedCityTvItem.setOnClickListener {
            bookMarkItemClicked.selectedPosition(getItem(position))
        }
        holder.binding.executePendingBindings()
    }

    fun saveItemClicked(bookMarkItemClicked: BookMarkItemClicked) {
        this.bookMarkItemClicked = bookMarkItemClicked
    }
}

class VHCurrencyRates(
    val binding: RowSavedCityBinding
) :
    RecyclerView.ViewHolder(binding.root)


interface BookMarkItemClicked {
    fun selectedPosition(item: LocationModal)
}