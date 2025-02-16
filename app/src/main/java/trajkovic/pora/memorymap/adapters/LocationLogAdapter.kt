package trajkovic.pora.memorymap.adapters

import android.app.AlertDialog
import android.content.Context
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
    private val onItemClick: (Int) -> Unit,
    private val onDelete: (LocationLog) -> Unit
) :
    RecyclerView.Adapter<LocationLogAdapter.LocationViewHolder>() {

    class LocationViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateLogs(logs: List<LocationLog>) {
        this.logs = logs
        notifyDataSetChanged()
    }

    private fun deleteConfirmationDialog(context: Context, log: LocationLog) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure you want to delete ${log.name}?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            onDelete(log)
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
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
            Picasso.get().load(log.thumbnailPath).placeholder(R.drawable.travel_journal).
            resize(300,300).centerCrop().into(listImage)

            root.setOnClickListener { onItemClick(position) }

            root.setOnLongClickListener {
                deleteConfirmationDialog(holder.itemView.context, log)
                true//sets that the long click has ended
            }
        }
    }

    override fun getItemCount() = logs.size
}