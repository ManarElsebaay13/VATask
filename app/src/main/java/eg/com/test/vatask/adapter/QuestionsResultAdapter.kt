package eg.com.test.vatask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import eg.com.test.vatask.R
import eg.com.test.vatask.databinding.ItemProgressLoadingBinding
import eg.com.test.vatask.databinding.ItemRecyclerQuestionBinding
import eg.com.test.vatask.model.entities.QuestionsModel
import eg.com.test.vatask.util.*

class QuestionsResultAdapter    (var questionModels: ArrayList<QuestionsModel>,
var onRecyclerItemClickListener: OnRecyclerItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    val ItemViewData = 1
    val ItemViewProgress = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        if (viewType == ItemViewData) {
            val binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.item_recycler_question,
                    parent,
                    false
            ) as ItemRecyclerQuestionBinding
            return ViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.item_progress_loading,
                    parent,
                    false
            ) as ItemProgressLoadingBinding
            return ProgressViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (questionModels[position].holderType!!.isEmpty()) ItemViewData else ItemViewProgress
    }

    override fun getItemCount() = questionModels.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ProgressViewHolder)
            return

        when (questionModels[holder.adapterPosition].operatorText) {
            AddOperator -> questionModels[holder.adapterPosition].operator = "+"
            SubOperator -> questionModels[holder.adapterPosition].operator = "-"
            MultiplyOperator -> questionModels[holder.adapterPosition].operator = "*"
            DivideOperator -> questionModels[holder.adapterPosition].operator = "/"
        }

        var holder = holder as ViewHolder

        holder.binding.model = questionModels[holder.adapterPosition]

    }

    fun setList(list: ArrayList<QuestionsModel>) {
        this.questionModels = list
        notifyDataSetChanged()
    }


    class ViewHolder(var binding: ItemRecyclerQuestionBinding) :
            RecyclerView.ViewHolder(binding.root)

    class ProgressViewHolder(var binding: ItemProgressLoadingBinding) :
            RecyclerView.ViewHolder(binding.root)

}