package sbin.com.simplefragmentandroid;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import sbin.com.simplefragmentandroid.data.Flower;
import sbin.com.simplefragmentandroid.data.FlowerData;

/**
 * Created by sbin on 10/11/2016.
 */

public class FlowerListFragment  extends ListFragment {

    List<Flower> flowers = new FlowerData().getFlowers();
    private Callbacks activity;

    public FlowerListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlowerArrayAdapter adapter = new FlowerArrayAdapter(getActivity(),
                R.layout.flower_listitem,
                flowers);
        setListAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.flower_list_fragment,container,false);
        return rootView;
    }

    // Fragment view needs to communicate thru call backs, not thru Intent..
    public interface Callbacks{
        public void onItemSelected(Flower flower);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Flower flower = flowers.get(position);
        activity.onItemSelected(flower);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (Callbacks) activity;
    }
}
