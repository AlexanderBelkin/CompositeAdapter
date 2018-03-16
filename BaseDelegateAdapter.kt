

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseDelegateAdapter: IDelegateAdapter {

    final override fun createViewHolder(parent: ViewGroup, viewType: Int): KViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        return onCreateViewHolder(inflatedView)
    }

    override fun bindViewHolder(holder: KViewHolder, item: Any, position: Int) {
        holder.onBind(item)
    }

    open fun onCreateViewHolder(view: View): KViewHolder {
        return KViewHolder(view)
    }

    override fun onRecycled(holder: KViewHolder) {
        holder.onRecycled()
    }
}
