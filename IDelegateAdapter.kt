

import android.support.annotation.LayoutRes
import android.view.ViewGroup

interface IDelegateAdapter {

    fun createViewHolder(parent: ViewGroup, viewType: Int): KViewHolder

    fun bindViewHolder(holder: KViewHolder, item: Any, position: Int)

    fun onRecycled(holder: KViewHolder)


    fun isForViewType(items: List<Any>, position: Int): Boolean

    @LayoutRes
    fun getLayoutId(): Int
}
