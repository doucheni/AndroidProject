package fr.eseo.example.androidproject.fragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.util.List;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.UserModel;
import fr.eseo.example.androidproject.room.EseoDatabase;
import fr.eseo.example.androidproject.room.entities.MarksStudents;


public class IdentityStudentsFragmentVote extends Fragment {

    // Argument's name
    private static final String ARG_STUDENT = "student";
    private static final String ARG_PROJECT = "project";

    // Argument
    private UserModel student;
    private Button validateBTN;
    private EditText studentMarkET;
    private View view;
    private List<MarksStudents> studentNote;

    private LinearLayout marksResults;

    public IdentityStudentsFragmentVote() {
        // Required empty public constructor
    }


    /**
     * Create an instance of IdentityStudentsFragmentVote
     *
     * @param student, a UserModel of student
     * @param project, the projectModel
     * @return A new instance of fragment IdentityStudentsFragmentVote.
     */
    public static IdentityStudentsFragmentVote newInstance(ProjectModel project, UserModel student) {
        IdentityStudentsFragmentVote fragment = new IdentityStudentsFragmentVote();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT, project);
        args.putSerializable(ARG_STUDENT, student);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            student = (UserModel)getArguments().getSerializable(ARG_STUDENT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vote_student, container, false);

        // Initialization TextViews
        // Views from XML layout
        TextView surename = view.findViewById(R.id.studentSurName);
        TextView forename = view.findViewById(R.id.studentForeName);

        // Update TextView's text
        surename.setText(student.getUserSurname());
        forename.setText(student.getUserForename());
        this.validateBTN = view.findViewById(R.id.btn_send_data);
        this.studentMarkET = view.findViewById(R.id.student_mark);
        this.marksResults = view.findViewById(R.id.student_note);
        //Button to send the grade in the database
        this.initButtonValidateAction();

        // Get and present marks from database
        this.updateMarks();
        return view;
    }


    /**
     * Add a OnClickListener on validateBTN
     * Insert mark in the database
     * If there is no data, display a toast
     */
    private void initButtonValidateAction(){
        validateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Get values from EditText
                final String markString = studentMarkET.getText().toString();

                // If there is a mark
                if(!markString.equals("")){
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            int mark = Integer.parseInt(markString);
                            MarksStudents marksStudents = new MarksStudents(student.getUserId(),mark);
                            EseoDatabase.getDatabase(view.getContext()).marksStudentDAO().insertAll(marksStudents);
                        }
                    });
                }
                Toast.makeText(v.getContext(), "Votre note a bien été enregistrée", Toast.LENGTH_LONG).show();
                studentMarkET.setText("");

            }
        });
    }


    /**
     * Create a TextView for each Student Marks find in the database
     * If there is no marks, display nothing
     */
    private void presentMarksFromDB(){
        // For each marks, adding a textView
        if(studentNote.size() > 0){
            for(MarksStudents mark : studentNote){
                final TextView markView = new TextView(view.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 20, 0, 0);
                markView.setLayoutParams(params);
                markView.setText(mark.getMark() + " / 20");
                markView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorWhiteText));
                Typeface face = ResourcesCompat.getFont(view.getContext(), R.font.roboto);
                markView.setTypeface(face);


                        try{
                            marksResults.addView(markView);
                        }catch (Exception e){
                            e.printStackTrace();
                        }



            }
        }else{
            // If there is no marks, adding a default message
            final TextView markView = new TextView(view.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 20, 0, 0);
            markView.setLayoutParams(params);
            markView.setText(" ");
            markView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorWhiteText));
            Typeface face = ResourcesCompat.getFont(view.getContext(), R.font.roboto);
            markView.setTypeface(face);

            // Adding the textview in the main thread

                    try{
                        marksResults.addView(markView);
                    }catch(Exception e){
                        e.printStackTrace();
                    }


        }
    }

    /**
     * Get Students Marks from database
     * Stock values in list of MarksStudents
     * Call presentMarksFromDB()
     */
    private void updateMarks(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                // Get comments and marks from database
                studentNote = EseoDatabase.getDatabase(view.getContext()).marksStudentDAO().getMarks(student.getUserId());

                // Create TextView for each marks
                presentMarksFromDB();


            }
        });
    }


}
