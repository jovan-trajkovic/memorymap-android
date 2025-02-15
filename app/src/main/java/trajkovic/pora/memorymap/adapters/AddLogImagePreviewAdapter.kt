package trajkovic.pora.memorymap.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import trajkovic.pora.memorymap.databinding.SelectedImagePreviewBinding

class AddLogImagePreviewAdapter(private val imageUris: List<Uri>) :
    RecyclerView.Adapter<AddLogImagePreviewAdapter.ImageViewHolder>() {

    class ImageViewHolder(val binding: SelectedImagePreviewBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = SelectedImagePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = imageUris[position]
        Picasso.get().load(uri).into(holder.binding.selectedImage)
    }

    override fun getItemCount(): Int = imageUris.size
}