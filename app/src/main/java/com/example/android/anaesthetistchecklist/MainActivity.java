package com.example.android.anaesthetistchecklist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // variables for calculating BMI
    Float weightOk;
    Float heightOk;
    Float BMI;

    // variables for questions which can disqualify patient from operation
    boolean medicines;
    boolean meal;
    boolean fluids;
    boolean denture;
    boolean earrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // calculate BMI method
    public void calculateBMIButtonClicked(View view) {

        // get weight from weight field
        EditText weight = (EditText) findViewById(R.id.weight_of_patient);
        weightOk = Float.parseFloat(weight.getText().toString());

        // get height form height field
        EditText height = (EditText) findViewById(R.id.height_of_patient);
        heightOk = Float.parseFloat(height.getText().toString());

        // do math on height and weight -> calculate BMI
        BMI = (weightOk) / ((heightOk / 100) * (heightOk / 100));

        // showBMI in a Toast
        Toast.makeText(this, String.format("%.2f", BMI), Toast.LENGTH_SHORT).show();
    }

    // did the patient took his medicines
    public void onMedicinesButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.medicines_check_yes:
                if (checked)
                    medicines = true;
                break;
            case R.id.medicines_check_no:
                if (checked)
                    medicines = false;
                break;
        }
    }

    // does the patient fast
    public void onMealButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.meal_check_yes:
                if (checked)
                    meal = true;
                break;
            case R.id.meal_check_no:
                if (checked)
                    meal = false;
                break;
        }
    }

    // did the patient removed his denture
    public void onDentureButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.denture_check_yes:
                if (checked)
                    denture = true;
                break;
            case R.id.denture_check_no:
                if (checked)
                    denture = false;
                break;
        }
    }

    // did the patient removed his earrings

    public void onEarringsButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.earrings_check_yes:
                if (checked)
                    earrings = true;
                break;
            case R.id.earrings_check_no:
                if (checked)
                    earrings = false;
                break;
        }
    }

    // two things:
    // 1. checks what kind of fluids did patient drink
    // 2. checks all the questions and gives GO/NOT GO recommendation in a toast
    public void onCheckButtonClicked(View view) {

        // what and when patient drunk?
        CheckBox fluidsNothing = (CheckBox) findViewById(R.id.fluids_check_box1);
        boolean fluids1 = fluidsNothing.isChecked();

        CheckBox fluidsWaterOk = (CheckBox) findViewById(R.id.fluids_check_box2);
        boolean fluids2 = fluidsWaterOk.isChecked();

        CheckBox fluidsWaterWrong = (CheckBox) findViewById(R.id.fluids_check_box3);
        boolean fluids3 = fluidsWaterWrong.isChecked();

        CheckBox otherFluids = (CheckBox) findViewById(R.id.fluids_check_box4);
        boolean fluids4 = otherFluids.isChecked();

        // converts multiple answers question answer to right/wrong answer
        if (fluids1)
            fluids = true;
        else if (fluids4)
            fluids = false;
        else if (fluids2 && fluids3)
            fluids = false;
        else if (fluids2)
            fluids = true;
        else if (fluids3)
            fluids = false;
        else
            fluids = false;

        //variable for storing info on which question was answered wrong
        String whatIsWrong = "";

        // checks which answers disqualified patient from operation
        if (!medicines)
            whatIsWrong = "\n" + getString(R.string.medicines2);
        if (!meal)
            whatIsWrong = whatIsWrong + "\n" + getString(R.string.meal2);
        if (!fluids)
            whatIsWrong = whatIsWrong + "\n" + getString(R.string.fluids2);
        if (!denture)
            whatIsWrong = whatIsWrong + "\n" + getString(R.string.denture2);
        if (!earrings)
            whatIsWrong = whatIsWrong + "\n" + getString(R.string.earrings2);

        // decides if patient can be operated on, and if not shows which
        // answers disqualified him/her
        if (medicines && meal && fluids && denture && earrings)
            Toast.makeText(this, getString(R.string.good_to_go), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getString(R.string.go_back) + "\n"
                    + getString(R.string.check) + ":" + whatIsWrong, Toast.LENGTH_LONG).show();
    }

    // sends summary of the interview with the patient
    // (in the form of an email) to the supposed hospital system
    public void sendViaEmail(View view) {

        String medicinesText, mealText, fluidsText, dentureText, earringsText;

        // settles if question was answered right or wrong (for the purpose of summary)
        if (medicines)
            medicinesText = getString(R.string.ok);
        else
            medicinesText = getString(R.string.wrong);

        if (meal)
            mealText = getString(R.string.ok);
        else
            mealText = getString(R.string.wrong);

        if (fluids)
            fluidsText = getString(R.string.ok);
        else
            fluidsText = getString(R.string.wrong);

        if (denture)
            dentureText = getString(R.string.ok);
        else
            dentureText = getString(R.string.wrong);

        if (earrings)
            earringsText = getString(R.string.ok);
        else
            earringsText = getString(R.string.wrong);

        // gets patient name
        EditText nameOfPatient = (EditText) findViewById(R.id.name_of_patient);
        String nameOfPatientGotten = nameOfPatient.getText().toString();

        // creates summary used as the text of the email
        String checkMessage = getString(R.string.patient_name2) + nameOfPatientGotten
                + "\n\nBMI: " + String.format("%.2f", BMI)
                + "\n\n" + getString(R.string.check_result)
                + "\n" + getString(R.string.medicines) + medicinesText
                + "\n" + getString(R.string.meal) + mealText
                + "\n" + getString(R.string.fluids) + fluidsText
                + "\n" + getString(R.string.denture) + dentureText
                + "\n" + getString(R.string.earrings) + earringsText;

        // creates subject of the email
        String subject = getString(R.string.checklist_for) + nameOfPatientGotten;

        //sends intent to the email app
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, checkMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
}
