package eddie.icloud.com.mysql_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {

    //Fragments can be attached in an activity

    // fragment uses onCreateView as the class entry point while activity uses onCreate
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // connect fragment layout
        final View root = inflater.inflate(R.layout.home_fragment, container, false);

        // dialog to show when fetching dialog
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Products");
        dialog.setMessage("Loading Products");
        dialog.show(); // show dialog

        final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>(); // to store the hashmap


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://modkenya.com/Joemwas/view_products.php", new JsonHttpResponseHandler()
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
                                map.put("name", productObj.getString("product_name"));
                                map.put("type", productObj.getString("product_type"));
                                map.put("cost", productObj.getString("product_cost"));
                                map.put("contact", productObj.getString("product_contact"));

                                // add the Hashmap in the ArrayList
                                arrayList.add(map);
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Error Loading Products", Toast.LENGTH_SHORT).show();
                            }
                        }// end of for loop

                        dialog.dismiss(); // dismis dialog
                        //-------------------display arraylist to list view-----------------
                        final SimpleAdapter adapter = new SimpleAdapter(
                                getActivity(),
                                arrayList, // items
                                R.layout.product_item_layout, // layout to put items

                                // set item details to views
                                new String[]{"name", "type","cost", "contact"},
                                new int[]{R.id.text_view_prodName, R.id.text_view_prodType, R.id.text_view_prodCost, R.id.text_view_prodContact}
                        );
                        // set the adapter to the listview
                        ListView productList = root.findViewById(R.id.list_home);
                        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity(), AddProductActivity.class);
                                startActivity(i);
                            }
                        });
                        productList.setAdapter(adapter);



//                        Button buy = root.findViewById(R.id.button_view_buy);
//                        buy.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
//                            }
//                        });


                    }// end of onSuccess

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                    }
                }


        );// end of get json array

        // return the layout
        return root;
    }
}// end
