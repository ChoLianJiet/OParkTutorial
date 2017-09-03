package com.opark.opark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.stepstone.apprating.AppRatingDialog;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFeedbackDialog();
        showPointsAcquiredDialog();
    }

    private void showFeedbackDialog() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(2)
                .setTitle("Rate this application")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.titleTextColor)
                .setDescriptionTextColor(R.color.descriptionTextColor)
                .setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .create(MainActivity.this)
                .show();
    }

    private void showPointsAcquiredDialog() {

        new AwesomeSuccessDialog(this)
                .setTitle("100 Points Acquired!")
                .setMessage("Thank you for using OPark :)")
                .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                .setCancelable(true)
                .setDoneButtonText(getString(R.string.dialog_ok_button))
                .setDoneButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                .setDoneButtonTextColor(R.color.white)
                .setDoneButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        //click
                    }
                })
                .show();
    }


}