package hsn.inf333finalproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment {
    private MainActivity mA;
    private FragmentTransaction fragmentTransaction;
    private ListView lstContacts;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Emergency - Contact information");

        lstContacts = view.findViewById(R.id.lstContacts);
        final List<Contact> contacts = mA.db.getContacts();
        final List<String> strContacts = new ArrayList<>();
        for(Contact contact: contacts) {
            strContacts.add(contact.toString());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strContacts);
        lstContacts.setAdapter(adapter);
        lstContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mA.selectedContact = contacts.get(position);

                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.screen, new ContactInformationFragment());
                fragmentTransaction.commit();
            }
        });

        view.findViewById(R.id.btnNewContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.screen, new NewContactFragment());
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
        return inflater.inflate(R.layout.fragment_contact_list, null);
    }
}
