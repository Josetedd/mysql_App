/*
 *
 *  * Copyright (C) 2017 Safaricom, Ltd.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package api;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import api.model.AccessToken;
import api.model.STKPush;

import eddie.icloud.com.mysql_app.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created  on 15/08/2018.
 * Created by Joseph
 */

public class MPESACheckout extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private ApiClient mApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressDialog = new ProgressDialog(this);
        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true);

        getAccessToken();
        showCheckoutDialog();

    }



    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    public void showCheckoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Phone Number Input");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setHint("Enter Your Phone number");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phone_number = input.getText().toString();
                performSTKPush(phone_number);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        builder.show();
    }

    public void performSTKPush(String phone_number) {
        mProgressDialog.setMessage("Payment");
        mProgressDialog.setTitle("Please wait..");
        mProgressDialog.show();
        String timestamp = getTimestamp();
        STKPush stkPush = new STKPush(
                "174379",//paybill
                getPassword("174379",//check method on line 166
                        "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919", timestamp),
                timestamp,
                "CustomerPayBillOnline",
                "15000",
                sanitizePhoneNumber(phone_number),
                "174379",//paybill
                sanitizePhoneNumber(phone_number),
                "http://modkenya.com/joseph/checkout.php?key=mimi",
                "test", //The account reference
                "test"  //The transaction description
        );

        mApiClient.setGetAccessToken(false);

        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {

                try {
                    if (response.isSuccessful()) {
                        mProgressDialog.dismiss();
                        Toast.makeText(MPESACheckout.this, "STK Pushed", Toast.LENGTH_SHORT).show();
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(MPESACheckout.this, "STK Not Pushed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();

            }
        });
    }



    //get Password
    public static String getPassword(String businessShortCode, String passkey, String timestamp){
        String str = businessShortCode + passkey + timestamp;
        //encode the password to Base64
        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }

    //help in getting the time stamp
    public static String getTimestamp(){
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
    }

    //check phone number meets the right format
    public static  String sanitizePhoneNumber(String phone) {

        if(phone.equals("")){
            return "";
        }

        if (phone.length() < 11 & phone.startsWith("0")) {
            String p = phone.replaceFirst("^0", "254");
            return p;
        }
        if(phone.length() == 13 && phone.startsWith("+")){
            String p = phone.replaceFirst("^+", "");
            return p;
        }
        return phone;
    }


}
