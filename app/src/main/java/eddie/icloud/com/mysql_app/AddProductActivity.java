package eddie.icloud.com.mysql_app;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        // initialize views
        final EditText productName = findViewById(R.id.prodNameEdit);
        final EditText productType = findViewById(R.id.prodTypeEdit);
        final EditText productCost = findViewById(R.id.prodCostEdit);
        final EditText productContact = findViewById(R.id.prodContactEdit);
        final EditText productDesc = findViewById(R.id.prodDescType);
        Button productSave = findViewById(R.id.saveButton);

        // save data when save button is clicked
        productSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // push to php
                // create a new AsyncHttp client instance from loop j
                AsyncHttpClient client = new AsyncHttpClient();

                // request parameters
                RequestParams params = new RequestParams();

                // add inputs to params
                params.add("name", productName.getText().toString());
                params.add("type", productType.getText().toString());
                params.add("cost", productCost.getText().toString());
                params.add("description", productDesc.getText().toString());
                params.add("contact", productContact.getText().toString());

                //map how values will be sent to PHP
                final ProgressDialog dialog = new ProgressDialog(AddProductActivity.this);
                dialog.setTitle("Dialog");
                dialog.setMessage("Please wait... Uploading your record");
                dialog.show();

                //modkenya.com/cpanel
                //modkenya
                //Je^^kksr7tMF

                client.post("http://modkenya.com/Joemwas/insert.php", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // on successul registration
                        dialog.dismiss(); // dismiss dialog
                        Toast.makeText(AddProductActivity.this, "product Saved successfully", Toast.LENGTH_SHORT).show(); // toast user saved

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // if saving not successfull
                        dialog.dismiss(); // dismiss dialog
                        Toast.makeText(AddProductActivity.this, "product Not Saved!"+statusCode, Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }
}
