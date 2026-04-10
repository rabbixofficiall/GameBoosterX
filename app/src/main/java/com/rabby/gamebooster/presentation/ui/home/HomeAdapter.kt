package com.rabby.gamebooster.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rabby.gamebooster.data.model.GameModel
import com.rabby.gamebooster.databinding.ItemGameBinding

class HomeAdapter(
    private val list: List<GameModel>,
    private val onClick: (GameModel) -> Unit
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = list[position]
        holder.binding.gameName.text = game.name

        holder.binding.root.setOnClickListener {
            onClick(game)
        }
    }

    override fun getItemCount() = list.size
}
