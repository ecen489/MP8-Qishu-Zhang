package com.example.mp8_qishu_zhang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

enum Student
{
    BART, LISA, RALPH, MILHOUSE;
}

public class PushActivity extends AppCompatActivity
{
    private Button push_button;
    private EditText class_id, class_name, grade;
    private RadioButton bartRad, ralphRad, lisaRad, milhouseRAD;


    private FirebaseDatabase fDB;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference refDB;
    private DatabaseReference tableDB;


    private Student student = Student.BART;
    private final Map<Student, Integer> studentIDs = new HashMap<Student, Integer>()
    {
        {
            put(Student.BART, 123);
            put(Student.RALPH, 404);
            put(Student.MILHOUSE, 456);
            put(Student.LISA, 888);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        fDB = FirebaseDatabase.getInstance();
        refDB = fDB.getReference();
        tableDB = refDB.child("simpsons/grades/");

        this.push_button = findViewById(R.id.push_button);

        this.class_id = findViewById(R.id.edtxt_courseID);
        this.class_name = findViewById(R.id.edtxt_courseName);
        this.grade = findViewById(R.id.edtxt_grade);

        this.bartRad = findViewById(R.id.rad_bart);
        View.OnClickListener bartClk = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                student = Student.BART;
            }
        };
        this.bartRad.setOnClickListener(bartClk);

        this.ralphRad = findViewById(R.id.rad_ralph);
        View.OnClickListener ralphClk = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                student = Student.RALPH;
            }
        };
        this.ralphRad.setOnClickListener(ralphClk);

        this.lisaRad = findViewById(R.id.rad_lisa);
        View.OnClickListener lisaClk = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                student = Student.LISA;
            }
        };
        this.lisaRad.setOnClickListener(lisaClk);

        this.milhouseRAD = findViewById(R.id.rad_milhouse);
        View.OnClickListener milhouseClk = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                student = Student.MILHOUSE;
            }
        };
        this.milhouseRAD.setOnClickListener(milhouseClk);

    }
}
