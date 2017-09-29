package bd.com.madmind.rentmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import bd.com.madmind.rentmaster.networkHelper.ConnectivityReceiver;
import bd.com.madmind.rentmaster.networkHelper.MyApplication;

public class LogInActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private EditText email , password;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private Button login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);




        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        email = (EditText) findViewById(R.id.userNameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        login = (Button) findViewById(R.id.logInButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = email.getText().toString().trim() ;String pass = password.getText().toString().trim();
                if(!e.isEmpty() && !pass.isEmpty()) signIn(e , pass);
                else Toast.makeText(getApplicationContext() , "Input valid id or pass!!!" , Toast.LENGTH_SHORT).show();

            }
        });

        checkConnection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(LogInActivity.this , HomeActivity.class));
            finish();
        }
    }

    public void signIn(String email , String pass){
        dialog.setTitle("Authenticating.....");
        dialog.show();
        auth.signInWithEmailAndPassword(email , pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                dialog.dismiss();
                startActivity(new Intent(LogInActivity.this , HomeActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Log.d("error" , e.toString());
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }




    private void showSnack(boolean isConnected) {
        String message = "You're online";
        login.setEnabled(true);
        int color = Color.WHITE;
        if (!isConnected){
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            login.setEnabled(false);
        }


        Snackbar snackbar = Snackbar
                .make((findViewById(R.id.loginMainLayout)), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        showSnack(isConnected);

    }
}