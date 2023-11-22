package jam.jam.jamscramblerquest

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jam.jam.jamscramblerquest.databinding.CardviewBinding

class ScramblerAdapter(
    private var mainlist: Array<String>,
    val adapterclick: ScramblerInterface) :
    RecyclerView.Adapter<ScramblerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("DiscouragedApi")
        fun bindIdea(dataCategory: String) {
            val category = dataCategory.replace(" " , "_")
            val firstWord = category.split(" ").firstOrNull()?.lowercase() ?: ""
            binding.title.apply {
                text = dataCategory
                setOnClickListener {
                    adapterclick.onItemClick(dataCategory)
                }
            }
            binding.icon.apply{
                val drawableResourceId = context.resources.getIdentifier(
                    firstWord,
                    "drawable",
                    context.packageName
                )

                Log.e("drawableResourceId",  firstWord)
                if (drawableResourceId != 0) {
                    setImageResource(drawableResourceId)
                } else {
                    // Handle case where drawable is not found
                    // You can set a default drawable or show an error message
                }
            }
            binding.root.setOnClickListener {
                adapterclick.onItemClick(dataCategory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mainlist[position]
        holder.bindIdea(data)
    }

    override fun getItemCount(): Int {
        return mainlist.size
    }

    interface ScramblerInterface {
        fun onItemClick(category: String)
    }
}
