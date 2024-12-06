package trajkovic.pora.memorymap.adapters

import android.os.LocaleList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import trajkovic.pora.memorymap.R
import trajkovic.pora.memorymap.data.LocationLog
import trajkovic.pora.memorymap.databinding.ListItemBinding

class LocationLogAdapter(
    private var logs: List<LocationLog>,
    private val onItemClick: (LocationLog) -> Unit
) :
    RecyclerView.Adapter<LocationLogAdapter.LocationViewHolder>() {

    class LocationViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateLogs(logs: List<LocationLog>) {
        this.logs = logs
        notifyDataSetChanged()
    }

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
            //TODO: Set image from log.thumbnailPath - cannot load, probably permissions
            Picasso.get().load(log.thumbnailPath).placeholder(R.drawable.travel_journal).fit().into(listImage)
            root.setOnClickListener { onItemClick(log) }
        }
    }

    override fun getItemCount() = logs.size
}