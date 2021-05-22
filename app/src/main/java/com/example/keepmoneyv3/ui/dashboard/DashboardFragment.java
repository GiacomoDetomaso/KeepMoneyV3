package com.example.keepmoneyv3.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.activities.NavigationActivity;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.dialogs.DialogEntries;
import com.example.keepmoneyv3.utility.User;
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class DashboardFragment extends Fragment {
    public interface DashboardFragmentListener{
        User GetUserFromSavedBundle();
    }

    private DashboardViewModel dashboardViewModel;
    private DashboardFragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {

            listener = (DashboardFragment.DashboardFragmentListener) context;//casting the interface

        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "Must implement the interface");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        /*final TextView textView = root.findViewById(R.id.text_dashboard);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        /*
        * calling the listener we have in NavigationActivity, it is possible to get the current logged user to grab his balance
        * from the database
        * */
        User user = listener.GetUserFromSavedBundle();

        TextView txtToEntriesBox = root.findViewById(R.id.txtValEntr);
        TextView txtToUscBox = root.findViewById(R.id.txtValUsc);
        TextView txtBudgetBox = root.findViewById(R.id.txtBudget);

        float entries,purchases;
        entries = loadValuesInTheActivity(user.getUsername(),0);
        purchases = loadValuesInTheActivity(user.getUsername(),1);

        txtToEntriesBox.setText(Float.toString(entries));
        txtToUscBox.setText(Float.toString(purchases));
        txtBudgetBox.setText(Float.toString(user.getTotal()));

        return root;
    }

    private float loadValuesInTheActivity(String username, int choice){
        DbManager dbManager = new DbManager(getContext());
        Cursor cursor;
        float value = 0;
        switch (choice) {
            case 0:
                cursor = dbManager.sumEntriesQuery(username);
                if (cursor!=null){
                    while(cursor.moveToNext()) {
                        value = cursor.getFloat(cursor.getColumnIndex("sumEntr"));
                    }
                }
                break;
            case 1:
                cursor = dbManager.sumPurchasesQuery(username);
                if (cursor!=null){
                    while(cursor.moveToNext()) {
                        value = cursor.getFloat(cursor.getColumnIndex("sumPurch"));
                    }
                }
                break;
            default:
                break;
        }
        return value;
    }

}