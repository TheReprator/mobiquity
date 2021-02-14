package reprator.mobiquity.saveCity.ui.bindingAdapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import reprator.mobiquity.saveCity.ui.DeleteSwipeItem
import reprator.mobiquity.saveCity.ui.SwipeToDeleteCallback

@BindingAdapter(value = ["saveCityListAdapter", "selectedPositionHandler"], requireAll = false)
fun RecyclerView.bindRecyclerViewAdapter(
    adapter: RecyclerView.Adapter<*>, deleteSwipeItem: DeleteSwipeItem
) {
    this.run {

        this.setHasFixedSize(true)
        this.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteSwipeItem.deletedItem(viewHolder.absoluteAdapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(this@bindRecyclerViewAdapter)
    }
}