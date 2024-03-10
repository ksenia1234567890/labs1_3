package com.example.labs1_3.FragmentsMenu;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.labs1_3.Adapters.AdapterPhoneContacts;
import com.example.labs1_3.Models.PhoneContact;
import com.example.labs1_3.R;

import java.util.ArrayList;

public class FragmentPhoneContacts extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        this.setEnterTransition(inflater.inflateTransition(R.transition.slide_left));
        this.setExitTransition(inflater.inflateTransition(R.transition.slide_right));
    }

    private static final int REQUEST_CODE_READ_CONTACTS=1;
    private static boolean READ_CONTACTS_GRANTED =false;
    RecyclerView recyclerPhoneContacts;
    AdapterPhoneContacts adapterPhoneContacts;
    ArrayList<PhoneContact> phoneContacts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_contacts, container, false);

        // получаем разрешения
        int hasReadContactPermission = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS);
        // если устройство до API 23, устанавливаем разрешение
        if(hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            READ_CONTACTS_GRANTED = true;
        }
        else{
            // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
        // если разрешение установлено, загружаем контакты
        if (READ_CONTACTS_GRANTED){
            recyclerPhoneContacts = view.findViewById(R.id.recyclerPhoneContacts);
            phoneContacts = new ArrayList<PhoneContact>();
            ContentResolver contentResolver = getContext().getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if(cursor != null){
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
                    @SuppressLint("Range") String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    if(contactName != null){
                        // получаем каждый контакт
                        PhoneContact phoneContact = new PhoneContact(
                                contactName,
                                contactNumber
                        );
                        // добавляем контакт в список
                        phoneContacts.add(phoneContact);
                    }
                }
                cursor.close();
            }

            adapterPhoneContacts = new AdapterPhoneContacts(getContext(), phoneContacts);
            recyclerPhoneContacts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            recyclerPhoneContacts.setAdapter(adapterPhoneContacts);
        }

        return view;
    }
}