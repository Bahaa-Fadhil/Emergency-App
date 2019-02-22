package hsn.inf333finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {
    private MainActivity mA;
    private TextView txtName, txtPhonenumber, txtDiagnose;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Emergency");

        txtName = view.findViewById(R.id.txtName);
        txtPhonenumber = view.findViewById(R.id.txtPhonenumber);
        txtDiagnose = view.findViewById(R.id.txtDiagnose);

        User user = mA.db.getUser();
        txtName.setText(user.getName());
        txtPhonenumber.setText(user.getPhone());
        txtDiagnose.setText(user.getDiagnosis());

        view.findViewById(R.id.btnEmergency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmergencyActivity.class));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mA = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_main, null);
    }
}
