package eddie.icloud.com.mysql_app;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ViewProducts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);


        // dialog to show when fetching dialog
        final ProgressDialog dialog = new ProgressDialog(ViewProducts.this);
        dialog.setTitle("Products");
        dialog.setMessage("Loading Products");
        dialog.show(); // show dialog

        final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>(); // to store the hashmap


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://modkenya.com/joe/view.php", new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {



                        //------------------------ loop through the response to get json objects
                        for (int i=0; i< response.length(); i++) {
                            try {
                                JSONObject productObj = response.getJSONObject(i); // get the oject

                                // extract the values
                                //HashMap -stores key - value <K, V>
                                HashMap<String, String> map = new HashMap<>();
                                map.put("name", productObj.getString("name"));
                                map.put("type", productObj.getString("type"));
                                map.put("cost", productObj.getString("cost"));
                                map.put("contact", productObj.getString("contact"));

                                // add the Hashmap in the ArrayList
                                arrayList.add(map);
                            } catch (Exception e) {
                                Toast.makeText(ViewProducts.this, "Error Loading Products", Toast.LENGTH_SHORT).show();
                            }
                        }// end of for loop

                        dialog.dismiss(); // dismis dialog
                        //-------------------display arraylist to list view-----------------
                        SimpleAdapter adapter = new SimpleAdapter(
                                ViewProducts.this,
                                arrayList, // items
                                R.layout.product_item_layout, // layout to put items

                                // set item details to views
                                new String[]{"name", "type","cost", "contact"},
                                new int[]{R.id.text_view_prodName, R.id.text_view_prodType, R.id.text_view_prodCost, R.id.text_view_prodContact}
                                );
                        // set the adapter to the listview
                        ListView productList = findViewById(R.id.products_list);

                        productList.setAdapter(adapter);


                    }// end of onSuccess

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        dialog.dismiss();
                        Toast.makeText(ViewProducts.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                    }
                }


        );// end of get json array
    }
}
