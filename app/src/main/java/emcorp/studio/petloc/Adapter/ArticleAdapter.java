package emcorp.studio.petloc.Adapter;

/**
 * Created by ASUS on 27/11/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import emcorp.studio.petloc.R;

public class ArticleAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> listrecid = new ArrayList<String>();
    List<String> listnama = new ArrayList<String>();
    List<String> listdeskripsi = new ArrayList<String>();
    List<String> listfoto = new ArrayList<String>();
    public ArticleAdapter(Activity context,
                          List<String> listrecid, List<String> listnama, List<String> listdeskripsi, List<String> listfoto) {
        super(context, R.layout.article_list, listrecid);

        this.context = context;
        this.listrecid = listrecid;
        this.listdeskripsi = listdeskripsi;
        this.listfoto = listfoto;
        this.listnama = listnama;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.article_list, null, true);
        TextView tvDeskripsi = (TextView) rowView.findViewById(R.id.tvDeskripsi);
        TextView tvNama = (TextView) rowView.findViewById(R.id.tvNama);
        if(listdeskripsi.get(position).length()>=120){
            tvDeskripsi.setText(listdeskripsi.get(position).substring(0,120)+"...");
        }else{
            tvDeskripsi.setText(listdeskripsi.get(position));
        }
        tvNama.setText(listnama.get(position));
        return rowView;
    }


}