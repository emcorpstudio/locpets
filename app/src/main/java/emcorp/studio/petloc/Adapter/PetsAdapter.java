package emcorp.studio.petloc.Adapter;

/**
 * Created by ASUS on 27/11/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import emcorp.studio.petloc.Library.Constant;
import emcorp.studio.petloc.R;

public class PetsAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> listrecid = new ArrayList<String>();
    List<String> listnama = new ArrayList<String>();
    List<String> listdeskripsi = new ArrayList<String>();
    List<String> listfoto = new ArrayList<String>();
    public PetsAdapter(Activity context,
                       List<String> listrecid, List<String> listnama, List<String> listdeskripsi, List<String> listfoto) {
        super(context, R.layout.pets_list, listrecid);

        this.context = context;
        this.listrecid = listrecid;
        this.listdeskripsi = listdeskripsi;
        this.listfoto = listfoto;
        this.listnama = listnama;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.pets_list, null, true);
        TextView tvDeskripsi = (TextView) rowView.findViewById(R.id.tvDeskripsi);
        TextView tvNama = (TextView) rowView.findViewById(R.id.tvNama);
        ImageView imgPets = (ImageView) rowView.findViewById(R.id.imgPets);
        if(listdeskripsi.get(position).length()>=80){
            tvDeskripsi.setText(listdeskripsi.get(position).substring(0,80)+"...");
        }else{
            tvDeskripsi.setText(listdeskripsi.get(position));
        }
        tvNama.setText(listnama.get(position));
        Picasso.with(getContext())
                .load(Constant.PICT_URL+listfoto.get(position))
                .error(R.drawable.ic_logo)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imgPets);
        return rowView;
    }


}