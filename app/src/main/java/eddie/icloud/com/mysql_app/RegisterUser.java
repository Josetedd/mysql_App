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

public class RegisterUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // initialize views

        final EditText userName = findViewById(R.id.editUserName);
        final EditText userEmail = findViewById(R.id.editEmail);
        final EditText userPassword = findViewById(R.id.editPassword);
        EditText confirmPassword = findViewById(R.id.editConfirmPass);
        Button registerBtn = findViewById(R.id.buttonRegister);
        TextView loginText = findViewById(R.id.textLogin);


        // set onClick listener for Register Button

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if button is clicked register user

                //TODO: confirm Pass


                AsyncHttpClient client = new AsyncHttpClient(); // initialize AsyncHttpClient from loopj

                // request Parameters

                RequestParams params = new RequestParams();

                // add user input to params

                params.add("name", userName.getText().toString());
                params.add("email", userEmail.getText().toString());
                params.add("password", userPassword.getText().toString());

                //map how values are sent to server

                // show a progress dialog when saving data
                final ProgressDialog dialog = new ProgressDialog(RegisterUser.this);
                dialog.setTitle("Registering User");
                dialog.setMessage("Please wait... Uploading your record");
                dialog.show();

                // server connection
                client.post("http://modkenya.com/Joemwas/user_insert.php", params, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // if data is saved
                        dialog.dismiss(); //dismiss dialog
                        Toast.makeText(RegisterUser.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        dialog.dismiss(); //dismiss Dialog
                        Toast.makeText(RegisterUser.this, "User not Registered", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }); // end of register button click listener

        // when user clicks on login text, start login form
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterUser.this, Login.class);
                startActivity(i);
            }
        });

    }
}
