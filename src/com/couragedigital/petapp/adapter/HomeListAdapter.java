package com.couragedigital.petapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.couragedigital.petapp.*;
import com.couragedigital.petapp.Connectivity.PetFetchClinicList;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.model.DialogListInformaion;
import com.couragedigital.petapp.model.IndexListInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private static ArrayList<IndexListInfo> dataSet;
    public static List<DialogListInformaion> dialogListForViewPets = new ArrayList<DialogListInformaion>();
    public static List<DialogListInformaion> dialogListForViewPetMets = new ArrayList<DialogListInformaion>();
    public static List<DialogListInformaion> dialogListForPetClinic = new ArrayList<DialogListInformaion>();
    public static CoordinatorLayout homeListCoordinatorLayout;

    public HomeListAdapter(ArrayList<IndexListInfo> indexListInfoList, List<DialogListInformaion> dialogListForViewPets,
                           List<DialogListInformaion> dialogListForViewPetMets, List<DialogListInformaion> dialogListForpetClinic, CoordinatorLayout homeListCoordinatorLayout) {
        this.dataSet = indexListInfoList;
        this.dialogListForViewPets = dialogListForViewPets;
        this.dialogListForViewPetMets = dialogListForViewPetMets;
        this.homeListCoordinatorLayout = homeListCoordinatorLayout;
        this.dialogListForPetClinic = dialogListForpetClinic;
    }

    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_homemenu, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(HomeListAdapter.ViewHolder viewHolder, int i) {
        IndexListInfo fp = dataSet.get(i);
        viewHolder.cv.setTag(i);
        viewHolder.tittle.setText(fp.getTittle());
        viewHolder.description.setText(fp.getDescription());
        viewHolder.background.setBackgroundResource(fp.getThumbnail());
        viewHolder.feed = fp;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public TextView tittle;
        public TextView description;
        public RelativeLayout background;
        public IndexListInfo feed;
        public TextView textDialog;
        private AlertDialog alertDialog;
        public com.couragedigital.petapp.Adapter.DialogListAdapter adapter;
        public CardView cv;

        private static String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
        private ProgressDialog progressDialog;

        static String urlForFetch;
        private int current_page = 1;
        SessionManager sessionManager;
        private String userEmail;
        Context context;


        Snackbar accessoriesSnackBar;
        Snackbar doctorSnackBar;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            tittle = (TextView) itemView.findViewById(R.id.titletxt);
            description = (TextView) itemView.findViewById(R.id.descriptiontext);
            background = (RelativeLayout) itemView.findViewById(R.id.relativelayouthome);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(v.getContext(), R.style.HomePageDialogboxCustom));

            if (position == 0) {
                adapter = new DialogListAdapter(dialogListForViewPets);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent gotoformupload = new Intent(v.getContext(), PetForm.class);
                            v.getContext().startActivity(gotoformupload);
                        } else if (i == 1) {
                            Intent gotolistofpet = new Intent(v.getContext(), PetList.class);
                            v.getContext().startActivity(gotolistofpet);
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
            if (position == 1) {
                adapter = new DialogListAdapter(dialogListForViewPetMets);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent gotoformupload = new Intent(v.getContext(), PetMet.class);
                            v.getContext().startActivity(gotoformupload);
                        } else if (i == 1) {
                            Intent gotolistofpet = new Intent(v.getContext(), PetMetList.class);
                            v.getContext().startActivity(gotolistofpet);
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
            if (position == 2) {
                adapter = new DialogListAdapter(dialogListForPetClinic);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            sessionManager = new SessionManager(context);
                            HashMap<String, String> user = sessionManager.getUserDetails();
                            String email = user.get(SessionManager.KEY_EMAIL);
                            userEmail = email;
                            url = url + "?method=ClinicByAddress&format=json&currentPage=" + current_page + userEmail + "";
                            graburl();

                        } else if (i == 1) {
                            Intent gotoPetClinic = new Intent(v.getContext(), PetClinic.class);
                            v.getContext().startActivity(gotoPetClinic);
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
            if (position == 3) {

                accessoriesSnackBar = Snackbar
                        .make(homeListCoordinatorLayout, "This Feature is yet to come!", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                accessoriesSnackBar.dismiss();
                            }
                        });
                accessoriesSnackBar.show();
            }
        }

        private void graburl() {
            new FetchListFromServer().execute(url);
        }

        private class FetchListFromServer extends AsyncTask<String, String, String> {
            @Override
            protected String doInBackground(String... url) {
                try {
                    urlForFetch = url[0];
                    //PetFetchClinicList.petFetchClinicList(userEmail,urlForFetch, progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                return null;
            }
        }

        private void hideProgressDialog() {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }

    }
}

