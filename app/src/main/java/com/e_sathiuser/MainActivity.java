package com.e_sathiuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Animation bottom_anim;
    private TextView TV2;
    private Handler handler;
    private FirebaseUser user;
    private String user_id;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        user=FirebaseAuth.getInstance().getCurrentUser();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
             /*   if (user != null) {
                    user_id = user.getUid();
                    username = user.getDisplayName();
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("E-Sathi_User").child("User").child(username).child(user_id);
                    dbref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("E-Sathi_User").child(user_id).child(username);
                                dbref.orderByChild("All_okay").equalTo("True").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                        if(dataSnapshot1.exists()){
                                            Intent intent1 = new Intent(MainActivity.this, HomeActivity.class);
                                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent1);
                                            finish();
                                        }
                                        else{
                                            Intent intent1 = new Intent(MainActivity.this, Register_1.class);
                                            Toast.makeText(MainActivity.this, "Please upload some important document..", Toast.LENGTH_SHORT).show();
                                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent1.putExtra("top", "1");
                                            startActivity(intent1);
                                            finish();

                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }else {
                                Intent intent = new Intent(MainActivity.this, Register.class);
                                Toast.makeText(MainActivity.this, "Please add some important personal info..", Toast.LENGTH_SHORT).show();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
                else {

              */

                if(user!=null)
                {
                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, Intro_slider.class);
                    startActivity(intent);
                    finish();
                }
                }

        }, 3000);

        bottom_anim = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        bottom_anim.setDuration(2000);
        TV2 = (TextView) findViewById(R.id.tv2);
        TV2.setAnimation(bottom_anim);
    }
}