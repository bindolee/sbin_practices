package sbin.com.simplefragmentandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

import sbin.com.simplefragmentandroid.data.Flower;

public class FlowerDetailFragment extends Fragment {

    Flower flower;

//    Required no-args constructor
    public FlowerDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if(b != null && b.containsKey(Flower.FLOWER_NAME)){
            flower = new Flower(b);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        Load the layout
        View view = inflater.inflate(R.layout.flower_detail_fragment, container, false);

        if (flower != null) {

            //Display values and image
            TextView tvName = (TextView) view.findViewById(R.id.tvFlowerName);
            tvName.setText(flower.getFlowerName());

            TextView tvInstructions = (TextView) view.findViewById(R.id.tvInstructions);
            tvInstructions.setText(flower.getInstructions());

            NumberFormat fmt = NumberFormat.getCurrencyInstance();
            TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvPrice.setText(fmt.format(flower.getPrice()));

            ImageView ivPicture = (ImageView) view.findViewById(R.id.ivFlowerImage);
            ivPicture.setImageResource(flower.getImageResource());

        }

        return view;
    }

}
