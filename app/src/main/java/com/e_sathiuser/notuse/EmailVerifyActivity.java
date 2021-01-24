package com.e_sathiuser.notuse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.e_sathiuser.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerifyActivity extends AppCompatActivity {
    private TextView resendCode, loginbtn;
    private TextView verifyMsg;
    String userId;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        resendCode = (TextView) findViewById(R.id.resend_link__btn);
        verifyMsg = (TextView) findViewById(R.id.verify_email);
        loginbtn = (TextView) findViewById(R.id.login_acc_btn);

        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        final FirebaseUser user = fAuth.getCurrentUser();


        if (user.isEmailVerified()) {

            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(EmailVerifyActivity.this, Register.class);
                    startActivity(loginIntent);
                    finish();
                }
            });
        } else {
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);
            //loginbtn.setVisibility(View.INVISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //  FirebaseUser user = fAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification link has been sent to your Email", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(v.getContext(), "Error In Sending Verification Link", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });


            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   Toast.makeText(v.getContext(), "Email Not Verified", Toast.LENGTH_LONG).show();
                    Intent loginIntent = new Intent(EmailVerifyActivity.this, Loginactivity.class);
                    startActivity(loginIntent);
                    finish();

                }
            });
        }
    }
}