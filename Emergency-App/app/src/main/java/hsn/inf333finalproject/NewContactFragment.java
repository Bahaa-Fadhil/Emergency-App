package hsn.inf333finalproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NewContactFragment extends Fragment {
    private MainActivity mA;
    private FragmentTransaction fragmentTransaction;

    private EditText txtName, txtPhone;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Emergency - New contact");

        txtName = view.findViewById(R.id.txtName);
        txtPhone = view.findViewById(R.id.txtPhonenumber);

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact contact = new Contact(txtName.getText().toString(), txtPhone.getText().toString());
                mA.db.insertContact(contact);

                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.screen, new ContactListFragment());
                fragmentTransaction.commit();
            }
        });

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.screen, new ContactListFragment());
                fragmentTransaction.commit();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mA = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_new_contact, null);
    }
}
