package com.e_sathiuser;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Authotp extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authotp);
        final EditText mob=(EditText)findViewById(R.id.Phone_no_ed);
      final TextView generateotp=(TextView)findViewById(R.id.generateotp);
        ProgressBar progressBar=findViewById(R.id.progressBar);
        generateotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mob.getText().toString().trim().isEmpty()){
                    Toast.makeText(Authotp.this,"Please enter phone Number",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                generateotp.setVisibility(View.INVISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + mob.getText().toString(),
                        60,                           // Timeout duration

                        TimeUnit.SECONDS,
                        Authotp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                generateotp.setVisibility(View.VISIBLE);
                                Toast.makeText(Authotp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                generateotp.setVisibility(View.VISIBLE);
                                Intent intent=new Intent(Authotp.this,Verifyotp.class);
                                intent.putExtra("mobile",mob.getText().toString());
                                intent.putExtra("verificationotp",s);
                                startActivity(intent);
                            }
                        }
                );

            }
        });
    }
}
