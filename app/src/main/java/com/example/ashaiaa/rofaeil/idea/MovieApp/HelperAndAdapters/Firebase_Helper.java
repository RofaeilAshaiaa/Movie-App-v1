package com.example.ashaiaa.rofaeil.idea.MovieApp.HelperAndAdapters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Firebase_Helper {

    private static DataSnapshot mDataSnapshot;

    private static FirebaseDatabase mDatabase;

    /*
    * this method and all its overwritten family takes an string
    * which is node name  or more , creates a refernce on that node,
    *  then return that reference
    */
    public static DatabaseReference get_database_reference(String node) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(node);
        return myRef;
    }

    public static DatabaseReference get_database_reference(String node1, String node2) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(node1).child(node2);
        return myRef;
    }

    public static DatabaseReference get_database_reference(String node1, String node2, String node3) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(node1).child(node2).child(node3);
        return myRef;
    }

    public static DatabaseReference get_database_reference(String node1, String node2, String node3, String node4) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(node1).child(node2).child(node3).child(node4);
        return myRef;
    }

    //takes DatabaseReference and saves it to isntance object then returns its DataSnapshot
    public static DataSnapshot get_dataSnapshot(DatabaseReference databaseReference) {

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataSnapshot = dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        databaseReference.addListenerForSingleValueEvent(listener);
        return mDataSnapshot;
    }


    public static DataSnapshot get_snapshot_lisner(DataSnapshot dataSnapshot) {
        DataSnapshot snapshot = dataSnapshot;
        return snapshot;
    }



    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
