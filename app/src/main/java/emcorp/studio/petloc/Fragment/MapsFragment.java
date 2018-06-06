package emcorp.studio.petloc.Fragment;

import android.app.ProgressDialog;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emcorp.studio.petloc.Library.Constant;
import emcorp.studio.petloc.Library.CustomInfoWindowGoogleMap;
import emcorp.studio.petloc.Library.GPSTracker;
import emcorp.studio.petloc.Library.InfoWindowData;
import emcorp.studio.petloc.R;


public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    GPSTracker gps;
    private ProgressDialog progressDialog;
    LocationManager locationManager;
    String distance = "";
    List<String> listrecid = new ArrayList<String>();
    List<String> listtipe = new ArrayList<String>();
    List<String> listnama = new ArrayList<String>();
    List<String> listlatitude = new ArrayList<String>();
    List<String> listlongitude = new ArrayList<String>();
    List<String> listalamat = new ArrayList<String>();
    List<String> listweb = new ArrayList<String>();
    List<String> listtelephone = new ArrayList<String>();
    List<String> listpraktek = new ArrayList<String>();
    List<String> listfoto = new ArrayList<String>();
    String idLoc = "", location = "";
    private List<Polyline> polylinePaths = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActivity().setTitle("Clinic and Pet Shop");
        setHasOptionsMenu(true);
        gps = new GPSTracker(getContext());
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LoadProcess();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
//        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.menu_refresh) {
//            LoadProcess();
//            return true;
//        }else if (id == R.id.action_logut) {
//            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
//            builder.setTitle(null);
//            builder.setMessage("Anda yakin ingin logout?");
//
//            String positiveText = getString(android.R.string.ok);
//            builder.setPositiveButton(positiveText,
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("LabPattimura", Context.MODE_PRIVATE).edit();
//                            editor.clear();
//                            editor.commit();
//                            SharedFunction.getInstance(getContext()).openActivityFinish(LoginActivity.class);
//                            Toast.makeText(getContext(),"Logout berhasil", Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//            String negativeText = getString(android.R.string.cancel);
//            builder.setNegativeButton(negativeText,
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//
//            android.support.v7.app.AlertDialog dialog = builder.create();
//            // display dialog
//            dialog.show();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    public void LoadProcess(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CETAK",response);
//                        Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        listrecid.clear();
                        listtipe.clear();
                        listnama.clear();
                        listlatitude.clear();
                        listlongitude.clear();
                        listalamat.clear();
                        listweb.clear();
                        listtelephone.clear();
                        listpraktek.clear();
                        listfoto.clear();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(response.indexOf("null")>0){
                                Toast.makeText(getContext(),"Tidak ada lokasi", Toast.LENGTH_SHORT).show();
                            }else{
                                JSONArray jsonArray = obj.getJSONArray("hasil");
                                if(jsonArray.length()==0){
                                    Toast.makeText(getContext(),"Tidak ada lokasi", Toast.LENGTH_SHORT).show();
                                }else{
                                    for (int i=0; i<jsonArray.length(); i++) {
                                        JSONObject isiArray = jsonArray.getJSONObject(i);
                                        String recid = isiArray.getString("recid");
                                        String tipe = isiArray.getString("tipe");
                                        String nama = isiArray.getString("nama");
                                        String latitude = isiArray.getString("latitude");
                                        String longitude = isiArray.getString("longitude");
                                        String alamat = isiArray.getString("alamat");
                                        String web = isiArray.getString("web");
                                        String telephone = isiArray.getString("telephone");
                                        String praktek = isiArray.getString("praktek");
                                        String foto = isiArray.getString("foto");
                                        listrecid.add(recid);
                                        listtipe.add(tipe);
                                        listnama.add(nama);
                                        listlatitude.add(latitude);
                                        listlongitude.add(longitude);
                                        listalamat.add(alamat);
                                        listweb.add(web);
                                        listtelephone.add(telephone);
                                        listpraktek.add(praktek);
                                        listfoto.add(foto);
                                    }
                                    MapsProcess();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                getContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", Constant.FUNCTION_LISTLOC);
                params.put("key", Constant.KEY);
                params.put("nama", "");
                params.put("tipe", "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
//        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        MapsProcess();
    }

    public void MapsProcess(){
        mMap.clear();

//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//            @Override
//            public View getInfoWindow(Marker arg0) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//
//                LinearLayout info;
//                info = new LinearLayout(getActivity());
//                info.setOrientation(LinearLayout.VERTICAL);
//
//                TextView title = new TextView(getActivity());
//                title.setTextColor(Color.BLACK);
//                title.setGravity(Gravity.CENTER);
//                title.setTypeface(null, Typeface.BOLD);
//                title.setText(marker.getTitle());
//
//                TextView snippet = new TextView(getActivity());
//                snippet.setTextColor(Color.GRAY);
//                snippet.setGravity(Gravity.CENTER);
//                snippet.setText(marker.getSnippet());
//
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);
//
//                ImageView img = new ImageView(getActivity());
//                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//
//                info.addView(title);
//                info.addView(snippet);
//                info.addView(img);
//
//                Picasso.with(getContext())
//                        .load(Constant.PICT_URL+"petshop.jpg")
//                        .placeholder(R.drawable.ic_logo)
//                        .into(img);
//
//                return info;
//            }
//        });

        Marker markerMap;
        //Lokasi Absen
        for(int i=0;i<listlongitude.size();i++){
//            Log.d("LONGLAT",String.valueOf(i));
            double latitude = Double.valueOf(listlatitude.get(i));
            double longitude = Double.valueOf(listlongitude.get(i));
            Log.d("LONGLAT", String.valueOf(latitude)+" , "+ String.valueOf(longitude));

            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(listnama.get(i));

            InfoWindowData info = new InfoWindowData();
            info.setImage(listfoto.get(i));
            info.setdescription(listalamat.get(i)+"\n"+listtelephone.get(i)+"\n"+listpraktek.get(i)+"\n"+listweb.get(i));

            if(listtipe.get(i).equals("Pet Shop")){
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }else{
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }

            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getActivity());
            mMap.setInfoWindowAdapter(customInfoWindow);

            Marker m = mMap.addMarker(marker);
            m.setTag(info);
//            m.showInfoWindow();

//            mMap.addMarker(marker.snippet(listalamat.get(i)+"\n"+listtelephone.get(i)+"\n"+listpraktek.get(i)+"\n"+listweb.get(i)+"ListFoto"+listfoto.get(i)));
//        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_peta_small));

        }

        //Lokasi User
        final double latitude = gps.getLatitude();
        final double longitude = gps.getLongitude();
        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Lokasi Anda")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
//        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_peta_small));
        mMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                if(!marker.getTitle().equals("Lokasi Anda")){
//                    findIdLoc(String.valueOf(marker.getPosition().latitude), String.valueOf(marker.getPosition().longitude));
//                    String origin = String.valueOf(latitude) + "," + String.valueOf(longitude);
//                    String destination = String.valueOf(marker.getPosition().latitude) + "," + String.valueOf(marker.getPosition().longitude);
//                    LoadRoute(origin,destination);
//                }
                return false;
            }
        });
    }

//    public void LoadRoute(String origin, String destination){
//        try {
//            new DirectionFinder(this, origin, destination).execute();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onDirectionFinderStart() {
//        progressDialog = ProgressDialog.show(getContext(), null,
//                "Finding route...", true);
//
//        if (polylinePaths != null) {
//            for (Polyline polyline : polylinePaths) {
//                polyline.remove();
//            }
//        }
//    }
//
//    @Override
//    public void onDirectionFinderSuccess(List<Route> routes) {
//        progressDialog.dismiss();
//        polylinePaths = new ArrayList<>();
//
//        for (Route route : routes) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.endLocation, 10.0f));
//            PolylineOptions polylineOptions = new PolylineOptions()
//                    .geodesic(true)
//                    .color(Color.BLUE)
//                    .width(15);
//
//            for (int i = 0; i < route.points.size(); i++){
//                polylineOptions.add(route.points.get(i));
//            }
//
//            polylinePaths.add(mMap.addPolyline(polylineOptions));
//        }
//    }

    public void findIdLoc(String latitude, String longitude){
        for(int i=0;i<listnama.size();i++){
            String lat = listlatitude.get(i);
            String lon = listlongitude.get(i);
            if(lat.equals(latitude)&&lon.equals(longitude)){
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
