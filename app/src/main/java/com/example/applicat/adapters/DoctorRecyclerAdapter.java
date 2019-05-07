package com.example.applicat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applicat.R;
import com.example.applicat.models.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorRecyclerAdapter extends RecyclerView.Adapter<DoctorRecyclerAdapter.ViewHolder> {
    List<Doctor> doc_list;
    Context applicationContext;
    DatabaseReference rootRef;
    String user_id;
    public DoctorRecyclerAdapter(List<Doctor> doc_list, Context applicationContext, String user_id) {
        this.applicationContext = applicationContext;
        this.doc_list = doc_list;
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.doc_layout, viewGroup, false);
        rootRef = FirebaseDatabase.getInstance().getReference();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final String docs = doc_list.get(i).getName();
        final String docId = doc_list.get(i).getId();
        viewHolder.SetName(docs);

        viewHolder.dView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map docMap = new HashMap();
                docMap.put("name", docs);
                docMap.put("id", docId);
                docMap.put("pat_id", user_id);
               rootRef.child("Chosen").child(user_id).setValue(docMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Toast.makeText(applicationContext, "You have chosen Doctor " + docs, Toast.LENGTH_SHORT).show();
                   }
               });
            }
        });

    }

    @Override
    public int getItemCount() {
        return doc_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View dView;
        TextView docTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dView = itemView;
        }

        public void SetName(String doctor){
            docTxt = dView.findViewById(R.id.doc_view_id);
            docTxt.setText(doctor);
        }
    }
}
