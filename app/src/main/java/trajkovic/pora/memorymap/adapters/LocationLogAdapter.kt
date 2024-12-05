package trajkovic.pora.memorymap.adapters

import android.os.LocaleList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import trajkovic.pora.memorymap.data.LocationLog
import trajkovic.pora.memorymap.databinding.ListItemBinding

class LocationLogAdapter(
    private val logs: List<LocationLog>,
    private val onItemClick: (LocationLog) -> Unit
) :
    RecyclerView.Adapter<LocationLogAdapter.LocationViewHolder>() {

    class LocationViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val log = logs[position]
        val locale = LocaleList.getDefault().get(0)
        with(holder.binding) {
            locationName.text = log.name
            locationRating.text = String.format(locale, log.rating.toString())
            //TODO: Set image from log.thumbnailPath
            root.setOnClickListener { onItemClick(log) }
        }
        //holder.itemView.setOnClickListener { onItemClick(log) }
    }

    override fun getItemCount() = logs.size
}