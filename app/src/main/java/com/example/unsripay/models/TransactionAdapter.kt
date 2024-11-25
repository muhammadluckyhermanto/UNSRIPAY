package com.example.unsripay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.unsripay.utils.CurrencyUtils

class TransactionAdapter(
    private val context: Context,
    private val transactions: List<Transaction>
) : BaseAdapter() {

    override fun getCount(): Int = transactions.size

    override fun getItem(position: Int): Transaction = transactions[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_transaction, parent, false)

        val transaction = getItem(position)

        // Format nominal menjadi Rupiah
        val nominalFormatted = CurrencyUtils.formatRupiah(transaction.nominal)

        // Bind data ke komponen layout
        val tvTransaction = view.findViewById<TextView>(R.id.tv_transaction)
        val tvTransactionId = view.findViewById<TextView>(R.id.tv_transaction_id)
        val tvDate = view.findViewById<TextView>(R.id.tv_date)
        val tvNominal = view.findViewById<TextView>(R.id.tv_nominal)

        tvTransaction.text = "Transaksi"
        tvTransactionId.text = transaction.metode
        tvDate.text = transaction.timestamp
        tvNominal.text = nominalFormatted

        return view
    }
}
