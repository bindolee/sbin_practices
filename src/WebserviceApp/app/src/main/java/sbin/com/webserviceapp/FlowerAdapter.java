package sbin.com.webserviceapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import sbin.com.webserviceapp.model.Flower;


public class FlowerAdapter extends ArrayAdapter<Flower> {

	private Context context;
	private List<Flower> flowerList;

    // Need to cache downloaded image bit map for large amount of bit map..
    // There is volley built in cache for image, but I am still using generic cache..
    private LruCache<Integer, Bitmap> imageCache;

    //Using Volley instead of Http handler.
    private RequestQueue requestQueue;

	public FlowerAdapter(Context context, int resource, List<Flower> objects) {
		super(context, resource, objects);
		this.context = context;
		this.flowerList = objects;

        //Get the run time max memory avaialble and based on it, assign to cache memory
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory / 8 ;
        imageCache = new LruCache<>(cacheSize);

        requestQueue = Volley.newRequestQueue(context);
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_flower, parent, false);

		//Display flower name in the TextView widget
        // Need to use final keyword since this same image view reference needs to be used in
        //both if-else statement -- same as line 67 final Imageview
        final Flower flower = flowerList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(flower.getName());

        // if there is image in memory , just simple view it
        Bitmap bitmap = imageCache.get(flower.getProductId());

        // Need to use final keyword since this same image view reference needs to be used in
        //both if-else statement
        final ImageView image = (ImageView) view.findViewById(R.id.imageView1);
        if (bitmap != null){
            image.setImageBitmap(flower.getBitmap());
        }
        // if not, then pack it up and create async task to download and displayit.
        else{
            String imageUrl = MainActivity.PHOTOS_BASE_URL + flower.getPhoto();
            ImageRequest request = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    image.setImageBitmap(response);
                    imageCache.put(flower.getProductId(), response);
                }
            }, 80, 80, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("FlowerAdapter", error.getMessage());
                }
            }
            );
            requestQueue.add(request);
        }
		return view;
	}
}
