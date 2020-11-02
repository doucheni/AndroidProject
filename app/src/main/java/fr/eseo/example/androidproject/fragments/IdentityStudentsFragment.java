package fr.eseo.example.androidproject.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.UserModel;

/**
 * Class of IdentityStudentsFragment
 * Display information about students who worked on the project
 */
public class IdentityStudentsFragment extends Fragment {

    // Argument's name
    private static final String ARG_STUDENT = "student";

    // Argument
    private UserModel student;

    public IdentityStudentsFragment() {
        // Required empty public constructor
    }

    /**
     * Create an instance of IdentityStudentsFragment
     *
     * @param student, a UserModel of student
     * @return A new instance of fragment IdentityStudentsFragment.
     */
    public static IdentityStudentsFragment newInstance(UserModel student) {
        IdentityStudentsFragment fragment = new IdentityStudentsFragment();
        Bundle args = new Bundle();
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
        View v = inflater.inflate(R.layout.fragment_identity_students, container, false);

        // Initialization TextViews
        // Views from XML layout
        TextView surename = v.findViewById(R.id.studentSurName);
        TextView forename = v.findViewById(R.id.studentForeName);

        // Update TextView's text
        surename.setText(student.getUserSurname());
        forename.setText(student.getUserForename());

        return v;
    }
}