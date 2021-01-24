package com.e_sathiuser.notuse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e_sathiuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Loginactivity extends AppCompatActivity {
    private final static String TAG = "loginactivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mStateListener;
    private Context mContext = Loginactivity.this;
    private EditText mEmail, mPassword,mconfirmpass;
    private TextView textView;
    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      mAuth = FirebaseAuth.getInstance();
        mEmail = (EditText) findViewById(R.id.emaillogin_ed);
        mPassword = (EditText) findViewById(R.id.passlog_ed);
        mconfirmpass = (EditText) findViewById(R.id.passconfirm_ed);
        textView = (TextView) findViewById(R.id.goregister);
        mButton = (Button) findViewById(R.id.logbtn);

        mStateListener = new FirebaseAuth.AuthStateListener() {
            FirebaseUser mfirebaseuser = mAuth.getCurrentUser();
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mfirebaseuser != null) {
                    Toast.makeText(Loginactivity.this, "you are logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, Registeractivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Loginactivity.this, "please login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String pass = mPassword.getText().toString();
                String confirmpass=mconfirmpass.getText().toString();
                if (email.equals("") && pass.equals("")&&confirmpass.equals(""))
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                else if(!pass.equals(confirmpass)){
                    Toast.makeText(mContext, "You password does not match", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(Loginactivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {

                                        Log.d(TAG, "Authentication fail");
                                        Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        mEmail.setText("");
                                        mPassword.setText("");
                                        mconfirmpass.setText("");
                                        mEmail.requestFocus();
                                        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInput(mEmail, InputMethodManager.SHOW_IMPLICIT);
                                    }
                                    //else {
                                    //  Toast.makeText(mContext, "email is not verified", Toast.LENGTH_SHORT).show();
                                    //mProgressbar.setVisibility(View.GONE);
                                    //mAuth.signOut();
                                    //}
                                    //   }catch(NullPointerException e) {
                                    //     Log.e(TAG, "exception is" + e.getMessage());

                                    //   }
                                    else {
                                        // If sign in fails, display a message to the user
                                        Intent intent = new Intent(mContext, Register.class);
                                        startActivity(intent);
                                        mEmail.setText("");
                                        mPassword.setText("");
                                        mconfirmpass.setText("");
                                        mEmail.requestFocus();
                                        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInput(mEmail, InputMethodManager.SHOW_IMPLICIT);
                                        finish();

                                    }
                                }
                                // ...

                            });
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Registeractivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

