package com.avion.app;

import com.avion.app.action.EmailConf;
import com.avion.app.unit.NekConfigs;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainApi {

    protected FirebaseFirestore db;


    public MainApi() {

    }


    public String getEmail() {
        final ArrayList<String> list = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        list.add(0, "undefined");

        db.collection("users_mails")

                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        EmailConf email_data = documentSnapshot.toObject(EmailConf.class);
                        email_data.setPhone(documentSnapshot.getId());
                        if (email_data.getPhone().equals(NekConfigs.getUserPgone())) {
                            list.set(0, email_data.getEmail());
                            MainActivity.logstr("EMAIL: " + list.get(0));
                        } else {
                            MainActivity.logstr(email_data.getPhone() + " != " + NekConfigs.getUserPgone());
                        }
                    }

                });
        return list.get(0);
    }

}
