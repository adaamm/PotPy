package com.example.a390application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MoreDetailsDialogFragment extends DialogFragment {


    protected Button doneButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_more_details, container, false);


        //saveAssignmentButton = view.findViewById(R.id.saveAssignmentButton);
        doneButton = view.findViewById(R.id.backButton);

        /*saveAssignmentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String title = assignmentTitleEditText.getText().toString();
                String grade = assignmentGradeEditText.getText().toString();

                double convert = Double.parseDouble(grade);

                long id = getArguments().getLong("data");

                InsertPlant dbAssignments = new InsertAssignment(getActivity());
                if(!(title.equals(("")) || grade.equals(""))){
                    dbAssignments.insertAssignment(new Assignment(title, convert, id));
                    ((inspectPlantActivity)getActivity()).loadCourseListView(getArguments().getLong("data"));
                    getDialog().dismiss();
                }
            }
        });*/

        doneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }
}
