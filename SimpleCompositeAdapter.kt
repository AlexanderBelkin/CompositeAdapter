
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import com.alexnikola.supernotes.custom.touch_helper.ItemTouchHelperAdapter
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class SimpleCompositeAdapter
private constructor(private val typeToAdapterMap: SparseArray<IDelegateAdapter>):
        RecyclerView.Adapter<KViewHolder>(), ItemTouchHelperAdapter {

    companion object {
        private const val FIRST_VIEW_TYPE = 0
    }

    private var data = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KViewHolder {
        return typeToAdapterMap.get(viewType).createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: KViewHolder, position: Int) {
        val delegateAdapter = typeToAdapterMap.get(holder.itemViewType)
        if (delegateAdapter != null) {
            delegateAdapter.bindViewHolder(holder, data[position], position)
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
    }

    override fun getItemViewType(position: Int): Int {
        for (i in FIRST_VIEW_TYPE until typeToAdapterMap.size()) {
            val delegate = typeToAdapterMap.valueAt(i)
            if (delegate.isForViewType(data, position)) {
                return typeToAdapterMap.keyAt(i)
            }
        }
        throw NullPointerException("Can not get viewType for position $position")
    }

    override fun onViewRecycled(holder: KViewHolder) {
        typeToAdapterMap.get(holder.itemViewType).onRecycled(holder)
    }






    fun swapData(data: List<Any>?) {
        if (data == null) {
            return
        }
        this.data = data as ArrayList<Any>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class Builder {

        private var count: Int = 0
        private val typeToAdapterMap: SparseArray<IDelegateAdapter> = SparseArray()

        fun add(delegateAdapter: IDelegateAdapter): Builder {
            typeToAdapterMap.put(count++, delegateAdapter)
            return this
        }

        fun build(): SimpleCompositeAdapter {
            if (count == 0) {
                throw IllegalArgumentException("Register at least one adapter")
            }
            return SimpleCompositeAdapter(typeToAdapterMap)
        }
    }

    fun removeAt(position: Int) {
        Timber.d("before %s, %s", data.size, position)
        val isRemoved = data.removeAt(position)
        Timber.d("after %s, %s", data.size, isRemoved)
        notifyItemRemoved(position)
    }

    fun removeOne(function: (Any) -> Boolean) {
        for(i in data.indices) {
            val item = data[i]
            if (function(item)) {
                removeAt(i)
                break
            }
        }
    }

    fun removeMultiple(function: Function1<Any, Boolean>) {
        for(i in data.indices.reversed()) {
            val item = data[i]
            if (function.invoke(item)) {
                removeAt(i)
            }
        }
    }







    override fun onItemDismiss(position: Int) {
        removeAt(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }
}
