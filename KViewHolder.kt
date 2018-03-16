
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer

open class KViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    open fun onBind(item: Any) {

    }

    open fun onRecycled() {

    }
}
