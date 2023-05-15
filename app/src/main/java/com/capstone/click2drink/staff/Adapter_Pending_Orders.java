package com.capstone.click2drink.staff;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capstone.click2drink.Partners;
import com.capstone.click2drink.R;
import com.capstone.click2drink.Truck;
import com.capstone.click2drink.view_partner;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.List;
import java.util.logging.LogRecord;

public class Adapter_Pending_Orders extends RecyclerView.Adapter<Adapter_Pending_Orders.MyViewHolder> {

    private static String STATUS_URL ="http://resources.click2drinkph.store/php/update_order_status.php";

    private Context mContext;
    private List<Invoice_Order_Fetcher> invoice_data;
    private List<Address_Order_Fetcher> address_data;
    private List<Name_Fetcher> user_name_list;

    String[] status = {"Processing", "Out for Delivery", "Delivered"};
    Dialog staff_dialog;
    int execute_once = 0;
    //Dialog
    Button Yes, No;
    TextView alert_message;
    String msg;
    TextView OR;

    public Adapter_Pending_Orders (Context context,List<Invoice_Order_Fetcher> invoice_data,
                                   List<Address_Order_Fetcher> address_data, List<Name_Fetcher> user_name_list){
        this.mContext = context;
        this.invoice_data = invoice_data;
        this.address_data = address_data;
        this.user_name_list = user_name_list;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView fullname;
        private TextView combined_address;
        private TextView phone_number;
        private TextView order_number;
        private TextView total_price;
        private TextView total_gallons;
        private TextView ordered_at;
        private Spinner drop_down;
        private ImageView status_logo;
        private Button update;
        private TextView view_details;



        public MyViewHolder (View view){
            super(view);

            fullname = view.findViewById(R.id.fullName);
            combined_address = view.findViewById(R.id.combined_address);
            phone_number = view.findViewById(R.id.phone_number);
            order_number = view.findViewById(R.id.order_number);
            total_price = view.findViewById(R.id.product_price);
            total_gallons = view.findViewById(R.id.order_quantity);
            ordered_at = view.findViewById(R.id.time_stamp);
            drop_down = view.findViewById(R.id.drop_down);
            status_logo = view.findViewById(R.id.status_logo);
            update = view.findViewById(R.id.update);
            view_details = view.findViewById(R.id.view_details);


        }
    }

    @NonNull
    @Override
    public Adapter_Pending_Orders.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.staff_pending_order_layout,parent,false);
        return new Adapter_Pending_Orders.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        final Invoice_Order_Fetcher new_invoice_data = invoice_data.get(position);
        final Address_Order_Fetcher new_address_data = address_data.get(position);
        final Name_Fetcher new_user_name_list = user_name_list.get(position);

        holder.fullname.setText(new_user_name_list.getCuser_name());
        holder.combined_address.setText(new_address_data.getCombine_address());
        holder.phone_number.setText(new_address_data.getCuser_number());
        holder.order_number.setText(new_invoice_data.getOrder_number());
        holder.total_price.setText(new_invoice_data.getTotal_price()+".00");
        holder.total_gallons.setText(new_invoice_data.getTotal_gallons());
        holder.ordered_at.setText(new_invoice_data.getDate_ordered());

        ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, R.layout.custom_spinner,status);
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner);
        holder.drop_down.setAdapter(arrayAdapter);

        String val = new_invoice_data.getOrderStatus();

        if(val.equals("Processing")){
            holder.drop_down.setSelection(0);
            holder.status_logo.setImageResource(R.drawable.ic_circle);
        }
        else if (val.equals("Out for Delivery")){
            holder.drop_down.setSelection(1);
            holder.status_logo.setImageResource(R.drawable.ic_circle_blue);
        }
        else if(val.equals("Delivered")){
            holder.drop_down.setSelection(2);
            holder.status_logo.setImageResource(R.drawable.ic_circle_red);
        }


        //Dialog
        staff_dialog = new Dialog(mContext);
        staff_dialog.setContentView(R.layout.staff_dialog_alert);
        staff_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        alert_message = staff_dialog.findViewById(R.id.dialog_message);
        Yes = staff_dialog.findViewById(R.id.yes);
        No = staff_dialog.findViewById(R.id.no);
        OR = staff_dialog.findViewById(R.id.order_number);

        holder.drop_down.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int prev = holder.drop_down.getSelectedItemPosition();
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                msg = status[position];

                if(execute_once<invoice_data.size()){
                    execute_once ++;
                }
                else if(prev == position){
                    holder.update.setVisibility(View.GONE);
                }

                else{
                    holder.update.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OR.setText(new_invoice_data.getOrder_number());
                alert_message.setText("[ "+msg+" ]");
                staff_dialog.show();
            }
        });

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_num = OR.getText().toString();
                updateStatus(order_num,msg);
            }
        });

        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staff_dialog.dismiss();
            }
        });


        holder.view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(mContext,view_pending_orders.class);
                String order_num = new_invoice_data.getOrder_number();
                x.putExtra("order_number",order_num);
                mContext.startActivity(x);
            }
        });


    }

    public void updateStatus(String order_number, String order_status){

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[2];
                field[0] = "order_number";
                field[1] = "order_status";

                String[] data = new String[2];
                data[0] = order_number;
                data[1] = order_status;

                PutData putData = new PutData(STATUS_URL, "POST", field, data);

                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();
                        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                        Intent x = new Intent(mContext, staff_pending_orders.class);
                        mContext.startActivity(x);
                        ((Activity)mContext).finish();
                    }
                }

            }
        });
    }



    @Override
    public int getItemCount() {
        return invoice_data.size();
    }
}
