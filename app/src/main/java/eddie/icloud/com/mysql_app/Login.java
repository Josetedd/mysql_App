package eddie.icloud.com.mysql_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // inititalize views
        final EditText email = findViewById(R.id.editLoginEmail);
        final EditText pass = findViewById(R.id.editLoginPass);
        Button loginBtn = findViewById(R.id.buttonLogin);
        TextView registerText = findViewById(R.id.textRegister);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if button is clicked login user

                //TODO: confirm Pass


                AsyncHttpClient client = new AsyncHttpClient(); // initialize AsyncHttpClient from loopj

                // request Parameters

                RequestParams params = new RequestParams();

                // add user input to params


                params.add("email", email.getText().toString());
                params.add("password", pass.getText().toString());

                //map how values are sent to server

                // show a progress dialog when saving data
                final ProgressDialog dialog = new ProgressDialog(Login.this);
                dialog.setTitle("Login");
                dialog.setMessage("Please wait... Logging in");
                dialog.show();

                // server connection
                client.post("http://modkenya.com/Joemwas/login.php", params, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // if data is saved
                        dialog.dismiss(); //dismiss dialog

                        // get response from php and store in a string variable
                        String response = new String(responseBody);

                        // trim to remove anyspace then check if its 1 or 0
                        if(response.trim().equals("1")){
                            Toast.makeText(Login.this, "User Logged in Successfully", Toast.LENGTH_SHORT).show();

                        }
                        else if (response.trim().equals("0")) {
                            Toast.makeText(Login.this, "Wrong Password/ Email", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Login.this, "Could Not Login Contact Admin", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        dialog.dismiss(); //dismiss Dialog
                        Toast.makeText(Login.this, "Could not Login", Toast.LENGTH_SHORT).show();

                    }
                }); 
            }
        }); // end of login


        // WH
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, RegisterUser.class);
                startActivity(i);
            }
        });

    }
}
