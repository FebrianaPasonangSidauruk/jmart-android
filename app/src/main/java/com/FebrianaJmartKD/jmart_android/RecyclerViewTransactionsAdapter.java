package com.FebrianaJmartKD.jmart_android;

/**
 * class RecyclerViewTransactionsAdapter
 *
 * @author Febriana Pasonang Sidauruk
 *
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

import com.FebrianaJmartKD.jmart_android.model.Product;
import com.FebrianaJmartKD.jmart_android.model.Payment;
import com.FebrianaJmartKD.jmart_android.request.RequestFactory;

//Custom Adapter to handle Product RecycleView List
public class RecyclerViewTransactionsAdapter extends RecyclerView.Adapter<RecyclerViewTransactionsAdapter.ViewHolder> {
    private static final Gson gson = new Gson();
    private List<Payment> mData;
    private List<Product> transactionProducts;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // data is passed into the constructor
    RecyclerViewTransactionsAdapter(Context context, List<Payment> data, List<Product> transactionProducts) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.transactionProducts = transactionProducts;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_rowtransactions, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Payment paymentName = mData.get(position);
        Product productName = transactionProducts.get(position);
        if(productName.toString().length() >= 38){
            holder.rv_tv_invoiceNameT.setTextSize(12.0f);
            holder.rv_tv_invoiceNameT.setMaxEms(10);
        }
        holder.rv_tv_invoiceNameT.setText(productName.toString());
        holder.rv_tv_invoiceStatusT.setText(paymentName.history.get(paymentName.history.size() - 1).status.toString());
        holder.rv_tv_invoiceAmountT.setText("x"+String.valueOf(paymentName.productCount));
        holder.rv_tv_invoiceTotalPriceT.setText(String.valueOf(Math.round(productName.price * paymentName.productCount* 100.00)/100.00));
        switch (paymentName.shipment.plan){
            case 0:
                holder.rv_tv_invoiceShipmentPlanT.setText(("INSTANT"));
                break;
            case 1:
                holder.rv_tv_invoiceShipmentPlanT.setText(("SAME_DAY"));
                break;
            case 2:
                holder.rv_tv_invoiceShipmentPlanT.setText(("NEXT_DAY"));
                break;
            case 4:
                holder.rv_tv_invoiceShipmentPlanT.setText(("KARGO"));
                break;
            default:
                holder.rv_tv_invoiceShipmentPlanT.setText(("REGULER"));
                break;
        }
        holder.rv_tv_productIdT.setText("Product ID: "+ paymentName.productId);
        holder.rv_tv_invoiceStore.setText("Seller ID: " + productName.accountId);
        holder.rv_tv_invoiceShipmentAddressT.setText(paymentName.shipment.address);
        holder.rv_image_productInvoiceT.setImageResource(ProductDetailActivity.getDrawableId(productName.category.toString()));

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    //Refresh the list by notify if list data has been updated
    public void refresh(List<Payment> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void refreshProducts(List<Product> transactionProducts) {
        this.transactionProducts = transactionProducts;
        notifyDataSetChanged();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rv_tv_invoiceNameT;
        TextView rv_tv_invoiceStatusT;
        TextView rv_tv_invoiceAmountT;
        TextView rv_tv_invoiceTotalPriceT;
        TextView rv_tv_invoiceShipmentPlanT;
        TextView rv_tv_productIdT;
        TextView rv_tv_invoiceStore;
        TextView rv_tv_invoiceShipmentAddressT;
        ImageView rv_image_productInvoiceT;
        Button rv_btnCancelTransactionT;

        ViewHolder(View itemView) {
            super(itemView);
            rv_tv_invoiceNameT = itemView.findViewById(R.id.rv_tv_invoiceNameT);
            rv_tv_invoiceStatusT = itemView.findViewById(R.id.rv_tv_invoiceStatusT);
            rv_tv_invoiceAmountT = itemView.findViewById(R.id.rv_tv_invoiceAmountT);
            rv_tv_invoiceTotalPriceT = itemView.findViewById(R.id.rv_tv_invoiceTotalPriceT);
            rv_tv_invoiceShipmentPlanT = itemView.findViewById(R.id.rv_tv_invoiceShipmentPlanT);
            rv_tv_productIdT = itemView.findViewById(R.id.rv_tv_productIdT);
            rv_tv_invoiceStore = itemView.findViewById(R.id.rv_tv_invoiceStore);
            rv_tv_invoiceShipmentAddressT = itemView.findViewById(R.id.rv_tv_invoiceShipmentAddressT);
            rv_image_productInvoiceT = itemView.findViewById(R.id.rv_image_productInvoiceT);
            rv_btnCancelTransactionT = itemView.findViewById(R.id.rv_btnCancelTransactionT);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).toString();
    }
    int getClickedItemId(int id){ return mData.get(id).id;}

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }
}
