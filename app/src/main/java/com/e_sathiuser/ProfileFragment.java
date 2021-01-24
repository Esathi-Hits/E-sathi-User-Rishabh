package com.e_sathiuser;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.e_sathiuser.notuse.Register;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
  private Bitmap bt;
  private FirebaseUser user;
    private ImageView iv;
    private String user_id;
    private FirebaseAuth mAuth;
    private DatabaseReference dbref;
    private ProgressDialog progress;
    private TextInputLayout e1;
    private TextInputEditText e11;
    private TextInputLayout e2;
    private TextInputLayout e3;
    private TextInputEditText e31;
    private TextView selectprofile;
    private Button submit;
    private String username;
    private String email;
    private String phone_no;
    private static final int pick=1;
    private Uri imageuri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        e1 = (TextInputLayout) view.findViewById(R.id.name);
        e11 = (TextInputEditText) view.findViewById(R.id.name_ed);
        e11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > e1.getCounterMaxLength())
                    e1.setError("Max character length is " + e1.getCounterMaxLength());
                else
                    e1.setError(null);
            }
        });


        e2 = (TextInputLayout) view.findViewById(R.id.email);


        e3 = (TextInputLayout) view.findViewById(R.id.number);
        e31 = (TextInputEditText) view.findViewById(R.id.number_ed);
        e31.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != e3.getCounterMaxLength())
                    e3.setError("Character length Must be " + e3.getCounterMaxLength());
                else
                    e3.setError(null);
            }
        });

        submit = (Button) view.findViewById(R.id.save);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = e1.getEditText().getText().toString();
                email = e2.getEditText().getText().toString();
                phone_no = e3.getEditText().getText().toString();

                if(TextUtils.isEmpty(username) && TextUtils.isEmpty(phone_no) && TextUtils.isEmpty(email))
                    Toast.makeText(getActivity(), "Please provide your info", Toast.LENGTH_SHORT).show();
                else if(iv.getDrawable()==null)
                    Toast.makeText(getActivity(), "Please select your image", Toast.LENGTH_SHORT).show();
              else{

                       add();
                }
            }
        });
       selectprofile = (TextView) view.findViewById(R.id.tv1);
        selectprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
       progress = new ProgressDialog(getActivity());
        iv=(ImageView)view.findViewById(R.id.profile_image);
        user = FirebaseAuth.getInstance().getCurrentUser();
        return view;
        }

    private void openGallery() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, pick);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==pick && resultCode==RESULT_OK && data!=null){
            imageuri=data.getData();
            try{
                InputStream inputstream=getActivity().getContentResolver().openInputStream(imageuri);
                bt=BitmapFactory.decodeStream(inputstream);
                iv.setImageURI(imageuri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        else{
            imageuri=null;
            iv.setImageResource(R.drawable.ic_profile);
        }
    }

  /*public void add(){
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        user_id = current_user.getUid();
        progress.setMessage(" Saving User info...");
        progress.show();
        progress.setCancelable(false);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Username", username);
        map.put("Email", email);
        map.put("Phone_no", phone_no);
       // map.put("gender", genderuser);
        String username=current_user.getDisplayName();
        if (username != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username).build();
            user.updateProfile(profileUpdates);
            StorageReference stref = FirebaseStorage.getInstance().getReference().child("E-Sathi_User").child(user_id).child(username).child("Profile Image");
            UploadTask uploadTask = (UploadTask) stref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dbref = FirebaseDatabase.getInstance().getReference().child("E-Sathi_User").child(user_id).child(username).child("Information");
                    dbref.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Saving User info done...", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Saving failed", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Saving Failed", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "Info already Saved", Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }
    }

   */
    public void add(){
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        user_id = current_user.getUid();
        progress.setMessage(" Saving User info...");
        progress.show();
        progress.setCancelable(false);
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference uploadimage=storage.getReference().child("E-Sathi_User").child(user_id).child(username).child("Profile Image");

        uploadimage.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              uploadimage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                    FirebaseDatabase db=FirebaseDatabase.getInstance();
                    DatabaseReference ref=db.getReference();
                    datauser data=new datauser(username,phone_no,email,uri.toString());
                    ref.child("E-Sathi_User").child(user_id).child(username).child("Info").setValue(data);
                      progress.dismiss();
                      Toast.makeText(getActivity(), "Saving User info done...", Toast.LENGTH_SHORT).show();

                  }
              });
            }
        });
    }

}