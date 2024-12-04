package trajkovic.pora.memorymap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocationLogAdapter(
    private val logs: List<LocationLog>,
    private val onItemClick: (LocationLog) -> Unit
) :
    RecyclerView.Adapter<LocationLogAdapter.LocationViewHolder>() {

    class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val locationName: TextView = view.findViewById(R.id.locationName)
        val locationRating: TextView = view.findViewById(R.id.locationRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val log = logs[position]
        holder.locationName.text = log.name
        holder.locationRating.text = "Rating: ${log.rating}"
        holder.itemView.setOnClickListener { onItemClick(log) }
    }

    override fun getItemCount() = logs.size
}