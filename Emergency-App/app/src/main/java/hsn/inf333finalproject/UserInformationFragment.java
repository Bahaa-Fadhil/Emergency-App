package hsn.inf333finalproject;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class UserInformationFragment extends Fragment {
    private MainActivity mA;
    private FragmentTransaction fragmentTransaction;

    private EditText txtName, txtPhone, txtDiagnose;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Emergency - User information");

        txtName = view.findViewById(R.id.txtName);
        txtPhone = view.findViewById(R.id.txtPhonenumber);
        txtDiagnose = view.findViewById(R.id.txtDiagnose);

        User user = mA.db.getUser();
        final String oldPhone = user.getPhone();
        txtName.setText(user.getName());
        txtPhone.setText(user.getPhone());
        txtDiagnose.setText(user.getDiagnosis());

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mA.db.updateUser(new User(txtName.getText().toString(), txtPhone.getText().toString(), txtDiagnose.getText().toString()), oldPhone);

                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.screen, new MainFragment());
                fragmentTransaction.commit();
            }
        });

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.screen, new MainFragment());
                fragmentTransaction.commit();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mA = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_user_information, null);
    }
}
