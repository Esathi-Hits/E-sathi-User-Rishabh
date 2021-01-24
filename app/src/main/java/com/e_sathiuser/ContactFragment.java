package com.e_sathiuser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {
    private static final String TAG = "ContactFragment";
  EditText to,msg,sub;
  TextView btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view =inflater.inflate(R.layout.fragment_contact,container,false) ;
      to=(EditText)view.findViewById(R.id.to);
      sub=(EditText)view.findViewById(R.id.subject);
      msg=(EditText)view.findViewById(R.id.msg);
      btn=(TextView)view.findViewById(R.id.send);
      to.setText("hitssolution@gmail.com");
       sub.requestFocus();
        InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(sub, InputMethodManager.SHOW_IMPLICIT);
    /*  btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+to.getText().toString()));
              intent.putExtra(Intent.EXTRA_SUBJECT,sub.getText().toString()) ;
              intent.putExtra(Intent.EXTRA_TEXT,msg.getText().toString());
              startActivity(intent);
              to.setText("");
              sub.setText("");
              msg.setText("");

          }
      });

     */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
               // intent.setData(Uri.parse("mailto:"));
               intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hitssolution@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, sub.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, msg.getText().toString());
                startActivity(intent);
                to.setText("");
                sub.setText("");
                msg.setText("");
            }
        });
        return view;


    }


}
