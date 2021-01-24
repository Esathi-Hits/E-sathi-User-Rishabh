package com.e_sathiuser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Verifyotp extends AppCompatActivity {
    private EditText input1,input2,input3,input4,input5,input6;
    String  verificationId;
    TextView verifybtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifyotp);
        input1=findViewById(R.id.otpNumberOne);
        input2=findViewById(R.id.optNumberTwo);
        input3=findViewById(R.id.otpNumberThree);
        input4=findViewById(R.id.otpNumberFour);
        input5=findViewById(R.id.otpNumberFive);
        input6=findViewById(R.id.optNumberSix);
        ProgressBar progressBar2=findViewById(R.id.progressBar2);
        verifybtn=findViewById(R.id.verifyotp);
        TextView resend=findViewById(R.id.resendotp);
        verificationId = getIntent().getStringExtra("verificationotp");
        setotpcursor();
       verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input1.getText().toString().trim().isEmpty()
                        || input2.getText().toString().trim().isEmpty()
                        ||input3.getText().toString().trim().isEmpty()
                        || input4.getText().toString().trim().isEmpty()
                        || input5.getText().toString().trim().isEmpty()
                        || input5.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Verifyotp.this, "Please enter Valid Code", Toast.LENGTH_SHORT).show();
                    return;
                }
                    String code = input1.getText().toString()+input2.getText().toString()+input3.getText().toString()+input4.getText().toString()+
                            input5.getText().toString()+input6.getText().toString();
                    if (verificationId != null)
                    {
                        progressBar2.setVisibility(View.VISIBLE);
                        verifybtn.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                verificationId,
                                code
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar2.setVisibility(View.GONE);
                                        verifybtn.setVisibility(View.VISIBLE);
                                        if (task.isSuccessful())
                                        {
                                            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);


                                        }
                                        else
                                        {
                                            Toast.makeText(Verifyotp.this, "The Verification Code Was invalid", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

            }
        });
       resend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               PhoneAuthProvider.getInstance().verifyPhoneNumber(
                       "+91" + getIntent().getStringExtra("mobile"),
                       60,                           // Timeout duration

                       TimeUnit.SECONDS,
                       Verifyotp.this,
                       new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                           @Override
                           public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                           }

                           @Override
                           public void onVerificationFailed(@NonNull FirebaseException e) {
                               Toast.makeText(Verifyotp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                             verificationId=s;
                               Toast.makeText(Verifyotp.this,"Code sent",Toast.LENGTH_SHORT).show();
                           }
                       }
               );
           }
       });
    }
    public void setotpcursor(){
        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    input2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    input3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().isEmpty())
                    input1.requestFocus();
            }
        });
        input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    input4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().isEmpty())
                    input2.requestFocus();

            }
        });
        input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    input5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().isEmpty())
                    input3.requestFocus();
            }
        });
        input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    input6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().isEmpty())
                    input4.requestFocus();
            }
        });
        input6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                    input6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().isEmpty())
                    input5.requestFocus();
            }
        });
    }
}
