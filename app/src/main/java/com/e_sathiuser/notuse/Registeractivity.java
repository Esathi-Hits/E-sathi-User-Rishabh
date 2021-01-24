
package com.e_sathiuser.notuse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.e_sathiuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class Registeractivity extends AppCompatActivity {

    private Bitmap bt;
    private FirebaseUser user;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    private ByteArrayOutputStream baos;
    private byte[] data1;
    private ImageView iv;
    private Uri picUri;
    private String user_id;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;
    private TextInputLayout e1;
    private TextInputEditText e11;
    private TextInputLayout e6;
    private TextInputLayout e9;
    private TextInputLayout e7;
    private TextInputEditText e91;
    private String username;
    private String email;
    private String phone_no;
    private String pass;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       // mUserDatabase= FirebaseDatabase.getInstance().getReference().child("User");
        mAuth = FirebaseAuth.getInstance();
        e1 = (TextInputLayout) findViewById(R.id.name);
        e11 = (TextInputEditText) findViewById(R.id.name_ed);
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


        e6 = (TextInputLayout) findViewById(R.id.email);
        e7=(TextInputLayout)findViewById(R.id.pass);

        e9 = (TextInputLayout) findViewById(R.id.number);
        e91 = (TextInputEditText) findViewById(R.id.number_ed);
        e91.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != e9.getCounterMaxLength())
                    e9.setError("Character length Must be " + e9.getCounterMaxLength());
                else
                    e9.setError(null);
            }
        });
        progress = new ProgressDialog(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        iv=(ImageView)findViewById(R.id.profile_image);
        baos = new ByteArrayOutputStream();
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }
    public void submit(View view) {
        username = e1.getEditText().getText().toString();
        email = e6.getEditText().getText().toString();
        phone_no = e9.getEditText().getText().toString();
        pass=e7.getEditText().getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phone_no)|| TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please enter the require information", Toast.LENGTH_SHORT).show();
        }
        else{
            register_user(username, email, pass,phone_no);
        }

    }
    private void register_user(final String name, final String email, final String password,String phone_no) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    //send Verification Link

                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Registeractivity.this, "Verification link has been sent to your email address", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Registeractivity.this, "Error In Sending Verification Link", Toast.LENGTH_LONG).show();
                        }
                    });


                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("E-Sathi_User").child("User").child(username).child(uid);// User Is theName Of The Root of The dataBase


                    //String device_token = FirebaseInstanceId.getInstance().getToken();


                    HashMap<String, String> userMap = new HashMap<>();//for creating the data
                    userMap.put("users", username); // the string of display name will be stored in "Name"
                    userMap.put("email", email);
                    userMap.put("password", password);
                    userMap.put("phoneno",phone_no);
                    // userMap.put("device_token", device_token);


                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent mainIntent = new Intent(Registeractivity.this, EmailVerifyActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });// putting the Hash Map Value To The Database


//------------------------------------------------------------------------------------------------------------------------------------------

                } else {
                    Toast.makeText(Registeractivity.this, "Can't Sign Up, Please Try Again !", Toast.LENGTH_SHORT).show();

                }
                // ...
            }
        });
    }
    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_RESULT) {

                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    try {
                        bt = BitmapFactory.decodeFile(filePath);
                        iv.setImageBitmap(bt);
                        bt.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                        data1 = baos.toByteArray();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;
        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());
    }
    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }
    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("pic_uri", picUri);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        picUri = savedInstanceState.getParcelable("pic_uri");
    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }
    public void gotologin(View View){
        Intent intent=new Intent(this,Loginactivity.class);
        startActivity(intent);
        finish();
    }

    public void Select(View view) {
        startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
    }
}