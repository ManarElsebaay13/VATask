package eg.com.test.vatask.util

interface OnAskUserAction {
    fun onPositiveAction()
    fun onNegativeAction()
}

interface OnBottomSheetItemClickListener {
    fun onBottomSheetItemClickListener(position: Int)
}


interface OnRecyclerItemClickListener {
    fun onRecyclerItemClickListener(position: Int)
}