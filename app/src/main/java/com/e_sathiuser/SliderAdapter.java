package com.e_sathiuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }
    public int[] images={
        R.drawable.information,R.drawable.map,R.drawable.payment
    };
    public String[] heading={
            "About Us","Easy to Locate","Easy Payments"
    };
    public String[] Description={
            "E-Sathi Driver is an user-friendly android application that supplies the motorist with it's rides in an easy to access manner.",
            "You can locate your trajectory in an efficient manner providing the fastest and the shortest route to the destination.",
            "Easily transfer/withdraw the amount to any of your account, without any minimum balance conditions."
    }
            ;
    @Override
    public int getCount() {
        return Description.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout) object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);
        ImageView IV =(ImageView) view.findViewById(R.id.Image);
        TextView Heading =(TextView) view.findViewById(R.id.heading);
        TextView Desc =(TextView) view.findViewById(R.id.desc);

        IV.setImageResource(images[position]);
        Heading.setText(heading[position]);
        Desc.setText(Description[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}

