package eg.com.test.vatask.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eg.com.test.vatask.R
import eg.com.test.vatask.adapter.RecyclerStringsBottomSheetAdapter
import eg.com.test.vatask.databinding.BottomSheetRecyclerBinding
import eg.com.test.vatask.ui.baseActivity.BaseActivity
import eg.com.test.vatask.util.OnBottomSheetItemClickListener

class BottomSheetStringsFragment : BottomSheetDialogFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity)
            activity = context
    }

    lateinit var activity: BaseActivity
    lateinit var binding: BottomSheetRecyclerBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_recycler, container, false)
        var view = binding.root
        return view
    }

    lateinit var onBottomSheetItemClickListener: OnBottomSheetItemClickListener

    fun setOnBottomSheetItemClickObserver(listener: OnBottomSheetItemClickListener) {
        this.onBottomSheetItemClickListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var arrayList = requireArguments()["list"] as ArrayList<String>
        binding.tvLabelBottomSheet.text = requireArguments()["title"] as String

        binding.rcBottomSheet.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        var myAdapter = RecyclerStringsBottomSheetAdapter(
            activity,
            arrayList,
            object : OnBottomSheetItemClickListener {
                override fun onBottomSheetItemClickListener(position: Int) {
                    onBottomSheetItemClickListener.onBottomSheetItemClickListener(position)
                    dismissAllowingStateLoss()
                }

            })
        binding.rcBottomSheet.adapter = myAdapter
        binding.ivCloseBottomSheet.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }
}