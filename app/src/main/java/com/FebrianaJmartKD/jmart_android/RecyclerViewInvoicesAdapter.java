package com.FebrianaJmartKD.jmart_android;

/**
 * class RecyclerViewInvoicesAdapter
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

import com.FebrianaJmartKD.jmart_android.model.Payment;
import com.FebrianaJmartKD.jmart_android.model.Product;
import com.FebrianaJmartKD.jmart_android.request.RequestFactory;

//Custom Adapter to handle Product RecycleView List
public class RecyclerViewInvoicesAdapter extends RecyclerView.Adapter<RecyclerViewInvoicesAdapter.ViewHolder> {
    private static final Gson gson = new Gson();
    private List<Payment> mData;
    private List<Product> invoiceProducts;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // data is passed into the constructor
    RecyclerViewInvoicesAdapter(Context context, List<Payment> data, List<Product> invoiceProducts) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.invoiceProducts = invoiceProducts;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row2, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Payment paymentName = mData.get(position);
        Product productName = invoiceProducts.get(position);
        if(productName.toString().length() >= 38){
            holder.rv_tv_invoiceName.setTextSize(12.0f);
            holder.rv_tv_invoiceName.setMaxEms(10);
        }else{ }
        holder.rv_tv_invoiceName.setText(productName.toString());
        holder.rv_tv_invoiceStatus.setText(paymentName.history.get(paymentName.history.size() - 1).status.toString());
        holder.rv_tv_invoiceAmount.setText("x"+String.valueOf(paymentName.productCount));
        holder.rv_tv_invoiceTotalPrice.setText(String.valueOf(Math.round(productName.price * paymentName.productCount * 100.00)/100.00));
        switch (paymentName.shipment.plan){
            case 0:
                holder.rv_tv_invoiceShipmentPlan.setText(("INSTANT"));
                break;
            case 1:
                holder.rv_tv_invoiceShipmentPlan.setText(("SAME_DAY"));
                break;
            case 2:
                holder.rv_tv_invoiceShipmentPlan.setText(("NEXT_DAY"));
                break;
            case 4:
                holder.rv_tv_invoiceShipmentPlan.setText(("KARGO"));
                break;
            default:
                holder.rv_tv_invoiceShipmentPlan.setText(("REGULER"));
                break;
        }
        holder.rv_tv_productId.setText("Product ID: "+paymentName.productId);
        holder.rv_tv_invoiceBuyer.setText("Buyer ID: " + paymentName.buyerId);
        holder.rv_tv_invoiceShipmentAddress.setText(paymentName.shipment.address);
        holder.rv_image_productInvoice.setImageResource(ProductDetailActivity.getDrawableId(productName.category.toString()));

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

    public void refreshProducts(List<Product> invoiceProducts) {
        this.invoiceProducts = invoiceProducts;
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rv_tv_invoiceName;
        TextView rv_tv_invoiceStatus;
        TextView rv_tv_invoiceAmount;
        TextView rv_tv_invoiceTotalPrice;
        TextView rv_tv_invoiceShipmentPlan;
        TextView rv_tv_productId;
        TextView rv_tv_invoiceBuyer;
        TextView rv_tv_invoiceShipmentAddress;
        ImageView rv_image_productInvoice;
        Button rv_btnAcceptInvoice, rv_btnCancelTransaction;

        ViewHolder(View itemView) {
            super(itemView);
            rv_tv_invoiceName = itemView.findViewById(R.id.rv_tv_invoiceName);
            rv_tv_invoiceStatus = itemView.findViewById(R.id.rv_tv_invoiceStatus);
            rv_tv_invoiceAmount = itemView.findViewById(R.id.rv_tv_invoiceAmount);
            rv_tv_invoiceTotalPrice = itemView.findViewById(R.id.rv_tv_invoiceTotalPrice);
            rv_tv_invoiceShipmentPlan = itemView.findViewById(R.id.rv_tv_invoiceShipmentPlan);
            rv_tv_productId = itemView.findViewById(R.id.rv_tv_productId);
            rv_tv_invoiceBuyer = itemView.findViewById(R.id.rv_tv_invoiceBuyerId);
            rv_tv_invoiceShipmentAddress = itemView.findViewById(R.id.rv_tv_invoiceShipmentAddress);
            rv_image_productInvoice = itemView.findViewById(R.id.rv_image_productInvoice);
            rv_btnAcceptInvoice = itemView.findViewById(R.id.rv_btnAcceptInvoice);
            rv_btnCancelTransaction = itemView.findViewById(R.id.rv_btnCancelTransaction);
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
