package prajna.app.ui.adapter

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import prajna.app.R
import prajna.app.repository.model.StudentFeeInfo
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Normal

class TableViewAdapter(val context:Context,private val movieList: List<StudentFeeInfo>) : RecyclerView.Adapter<TableViewAdapter.RowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.timetable_item, parent, false)
        return RowViewHolder(itemView)
    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.table_header_cell_bg)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.drawable.table_content_cell_bg)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val rowPos = holder.adapterPosition

        if (rowPos == 0) {
            // Header Cells. Main Headings appear here
            holder.apply {
                setHeaderBg(txtRank)
                setHeaderBg(txtMovieName)
                setHeaderBg(txtYear)
                setHeaderBg(txtCost)
                setHeaderBg(txtPay)

                txtRank.text = ""
                txtMovieName.text = "Fee"
                txtYear.text = "Paid"
                txtCost.text = "Balance"
                txtPay.text = ""
            }
        } else {
            val modal = movieList[rowPos - 1]

            holder.apply {
                setContentBg(txtRank)
                setContentBg(txtMovieName)
                setContentBg(txtYear)
                setContentBg(txtCost)

                txtRank.text = "Term "+modal.Term.toString()
                txtMovieName.text = "Rs "+modal.Amount.toString()
                txtYear.text = "Rs "+modal.PaidAmount.toString()
                val mBal=modal.Amount-modal.PaidAmount
                txtCost.text = "Rs "+mBal.toString()
            }
        }
        holder.txtPay.setOnClickListener(View.OnClickListener {
            val data = movieList[position-1]
            val mPaid = "Rs" + data.PaidAmount
            val mTotal = "Rs" + data.Amount
            val mBalance = data.PaidAmount.toInt()-data.Amount.toInt()
//            Log.e("TAG", "balance*** : "+data.Term )
//            (context as FeeActivity).payNow(data.Term)

        })
    }

    override fun getItemCount(): Int {
        return movieList.size + 1 // one more to add header row
    }

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtRank: Text_Bold = itemView.findViewById(R.id.txtRank)
        val txtMovieName: Text_Normal = itemView.findViewById(R.id.txtMovieName)
        val txtYear: Text_Normal = itemView.findViewById(R.id.txtYear)
        val txtCost: Text_Normal = itemView.findViewById(R.id.txtCost)
        val txtPay: Text_Bold = itemView.findViewById(R.id.txtPay)
    }
}