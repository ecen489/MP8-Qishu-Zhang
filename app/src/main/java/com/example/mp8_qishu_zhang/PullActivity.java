package com.example.mp8_qishu_zhang;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.*;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



public class PullActivity extends AppCompatActivity {

    //firebase/database stuff
    FirebaseDatabase fbdb;
    DatabaseReference dbrf;
    DatabaseReference dbrgrades;
    FirebaseAuth auth;
    FirebaseUser user;
//    private DBAccesObj db;

    //ui stuff
    RecyclerView recView;
    Button button_query1, button_query2, button_push, button_exit;
    EditText IDText;
    int studID;

    //ui + recycler view stuff
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Grade> getGradeList;

    //hard-coding the ID's :)
    Map<Integer, String> studentIDs = new HashMap<Integer, String>()
    {
        {
            put(123, "Bart");
            put(404, "Ralph");
            put(456, "Milhouse");
            put(888, "Lisa");
        }
    };

    //recyclerview stuff
//    private RecyclerView recyclerView;
//    private RecyclerView.Adapter adapter;
//    private List<ListItem> listItems;

//    //JSON stuff
//    ArrayList<String> courseList = new ArrayList<>();
//    ArrayList<String> gradeList = new ArrayList<>();
//    ArrayList<String> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        Intent intent = getIntent();

        //firebase/database stuff
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fbdb = FirebaseDatabase.getInstance();
        dbrf = fbdb.getReference();
        dbrgrades = dbrf.child("simpsons/grades/");

        //recyclerview stuff
        this.recView = findViewById(R.id.recyclerView);

        //student id
        this.IDText = findViewById(R.id.studentID_editTxt);

        //button stuff
        this.button_query1 = findViewById(R.id.query1_button);
        this.button_query2 = findViewById(R.id.query2_button);
        this.button_push= findViewById(R.id.push_button);
        this.button_exit = findViewById(R.id.exit_button);

    }

//    public void getInfo(String student_id){
//        String json;
//        try{
//            Resources res = getResources();
//            InputStream is = res.openRawResource(R.raw.simpsons);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            json = new String(buffer,"UTF-8");
//            JSONArray jsonArray = new JSONArray(json);
//
//            for(int i=0; i<jsonArray.length();i++){
//                JSONObject obj = jsonArray.getJSONObject(i);
//                if(obj.getString("student_id").equals(student_id)){
//                    courseList.add(obj.getString("course_name"));
//                    gradeList.add(obj.getString("grade"));
//                    nameList.add(obj.getString("name"));
//                }
//            }
//        }catch(IOException e){
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    public void clickQuery1(View view) {

        //take in student ID from editText
        studID = Integer.parseInt(IDText.getText().toString());

        Query query1 = dbrgrades.orderByChild("student_id").equalTo(studID);
        query1.addListenerForSingleValueEvent(getValueEventListener());

    }

    public void clickQuery2(View view) {

        //take in student ID from editText
        studID = Integer.parseInt(IDText.getText().toString());

        Query query1 = dbrgrades.orderByChild("student_id").startAt(studID);
        query1.addListenerForSingleValueEvent(getValueEventListener());

    }

    public ValueEventListener getValueEventListener()
    {
        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    // clear the arraylist to refill it
                     getGradeList = new ArrayList<>();
                    //Toast.makeText(getApplicationContext(),"listening",Toast.LENGTH_SHORT).show();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Grade grade = snapshot.getValue(Grade.class);
                        getGradeList.add(grade);
                    }
                    Toast.makeText(getApplicationContext(), "Query Finished", Toast.LENGTH_SHORT).show();

                    //put in info to temp holders for recyclerview
                    assignToAdapter();
                    updateRecyclerView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(getApplicationContext(), "ERROR: Query unsuccessful", Toast.LENGTH_SHORT).show();
            }
        };
        return valueEventListener;
    }

    public void assignToAdapter()
    {
        List<String> ass_class = new ArrayList<>();
        List<String> ass_grads = new ArrayList<>();
        List<String> ass_names = new ArrayList<>();

        for (Grade obj : this.getGradeList)
        {
            ass_class.add(obj.getcourse_name());
            ass_grads.add(obj.getgrade());
            ass_names.add(studentIDs.get(obj.getstudent_id()));
        }
        mAdapter = new MyAdapter(ass_class, ass_grads, ass_names);
    }

    public void updateRecyclerView()
    {
        this.recView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recView.setLayoutManager(layoutManager);
        this.recView.setAdapter(mAdapter);
    }


    //go to push
    public void pushClick(View view){
        startActivity(new Intent(PullActivity.this, PushActivity.class));
    }

    //exit and log out
    public void homeClick(View view){
        auth.signOut();
        user = null;
        finish();
    }


}

