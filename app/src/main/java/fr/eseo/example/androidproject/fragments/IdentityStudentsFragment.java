package fr.eseo.example.androidproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.UserModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IdentityStudentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IdentityStudentsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_STUDENT = "student";

    // TODO: Rename and change types of parameters
    private UserModel student;

    private TextView surename;
    private TextView forename;

    public IdentityStudentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param student Parameter 1.
     * @return A new instance of fragment IdentityStudentsFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        surename = v.findViewById(R.id.studentSurName);
        forename = v.findViewById(R.id.studentForeName);

        surename.setText(student.getUserSurname());
        forename.setText(student.getUserForename());

        return v;
    }
}