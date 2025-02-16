package trajkovic.pora.memorymap.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import trajkovic.pora.memorymap.data.ImagePaths
import trajkovic.pora.memorymap.databinding.LogImagesBinding

class LogDetailsImageAdapter(private var imagePaths: List<ImagePaths>) :
    RecyclerView.Adapter<LogDetailsImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(val binding: LogImagesBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = LogImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = imagePaths[position]
        Picasso.get().load(imagePath.imagePath).into(holder.binding.listImage)
    }

    override fun getItemCount(): Int = imagePaths.size

    fun submitList(paths: List<ImagePaths>) {
        this.imagePaths = paths
        notifyDataSetChanged()
    }
}