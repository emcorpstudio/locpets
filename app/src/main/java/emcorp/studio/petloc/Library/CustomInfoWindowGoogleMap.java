package emcorp.studio.petloc.Library;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import emcorp.studio.petloc.R;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.marker_window, null);

        TextView tvTittle = view.findViewById(R.id.tvTittle);
        TextView tvDeskripsi = view.findViewById(R.id.tvDeskripsi);
        ImageView img = view.findViewById(R.id.imgPets);

        tvTittle.setText(marker.getTitle());
//        tvDeskripsi.setText(marker.getSnippet());


        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

//        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
//                "drawable", context.getPackageName());
//        img.setImageResource(imageId);
        Picasso.with((Activity)context)
                .load(Constant.PICT_URL+infoWindowData.getImage())
                .placeholder(R.drawable.ic_logo)
                .into(img);
        tvDeskripsi.setText(infoWindowData.getdescription());

        return view;
    }
}