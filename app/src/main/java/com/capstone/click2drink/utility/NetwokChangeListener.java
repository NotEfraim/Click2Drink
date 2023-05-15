package com.capstone.click2drink.utility;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.capstone.click2drink.R;

public class NetwokChangeListener extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.isConnectedToInternet(context)) { //No internet Connection
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View Layout_Dialog = LayoutInflater.from(context).inflate(R.layout.network_error, null);
            builder.setView(Layout_Dialog);

            Button retry = Layout_Dialog.findViewById(R.id.retry_btn);

            //Show Dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);

            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context, intent);

                }
            });
        }
    }
}
