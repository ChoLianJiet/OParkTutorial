package com.opark.opark.feedback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opark.opark.KenaMap;
import com.opark.opark.R;
import com.opark.opark.model.Car;
import com.opark.opark.share_parking.MapsMainActivity;

public class FeedbackDialog extends DialogFragment implements FeedbackModel.FeedbackModelCallback{

    private static final String TAG = "FeedbackDialog";

    View view;
    int selectedEmojiIndex;
    TextView promptText;
    ImageView img;
    TextView emojiText;
    EditText userComment;
    LinearLayout upperContainer;
    LinearLayout lowerContainer;

    int[] emojiList = {R.id.vomitEmoji, R.id.sadEmoji, R.id.neutralEmoji, R.id.happyEmoji, R.id.loveEmoji};
    int[] emojiDrawable = {R.drawable.speechless_squareboi, R.drawable.not_satisfied_squareboi, R.drawable.mehhh_squareboi,
            R.drawable.satisfied_squareboi, R.drawable.very_satisfied_squareboi};
    String[] emojiTextList = {"Horrible!", "Bad", "Meh", "Good!", "Great!"};

    FirebaseAuth mAuth;
    String firebaseUserUID;
    StorageReference ref;

    FeedbackModel feedbackModel;

    @Override
    public void onLastFeedbackCompleted(boolean isCompleted, Car car) {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_feedback, null);

        mAuth = FirebaseAuth.getInstance();
        firebaseUserUID = mAuth.getCurrentUser().getUid();
        ref = FirebaseStorage.getInstance().getReference();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        promptText = view.findViewById(R.id.feedback_prompt_text);
        upperContainer = view.findViewById(R.id.selectedEmoji);
        lowerContainer = view.findViewById(R.id.feedback_description_container);

        img = view.findViewById(R.id.emoji);
        emojiText = view.findViewById(R.id.emojiText);

        feedbackModel = new FeedbackModel(getContext(), this);

        userComment = view.findViewById(R.id.userCommentText);
        userComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (userComment.getRight() - userComment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (selectedEmojiIndex < 2 && userComment.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "We are concerned, please describe more about your experience :)", Toast.LENGTH_LONG).show();
                        } else {

                            feedbackModel.getAverageRating(selectedEmojiIndex+1);

                            Toast.makeText(getContext(), "User comment: " + userComment.getText().toString() +
                                    " Rating: " + selectedEmojiIndex+1, Toast.LENGTH_LONG).show();
                            getDialog().dismiss();
                            getActivity().finish();
                            MapsMainActivity.foundUser = null;
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        for (int i = 0; i < 5; i++) {
            ImageView emojiImg = view.findViewById(emojiList[i]);
            emojiImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promptText.setVisibility(View.GONE);
                    upperContainer.setVisibility(View.VISIBLE);
                    lowerContainer.setVisibility(View.VISIBLE);
                    getSelectedEmojiFromId(v.getId());
                    img.setImageDrawable(getResources().getDrawable(emojiDrawable[selectedEmojiIndex]));
                    emojiText.setText(emojiTextList[selectedEmojiIndex]);
//                    updateEmojiState();
                }
            });
        }

        Car car = getCarDetails(getArguments());

        promptText.setText("How was your experience with " + car.carBrand + " " + car.carModel + ", " + car.carPlate +
        "? Your feedback helps us to make your experience better next time.");

        return view;
    }

    private void getSelectedEmojiFromId(int emojiId) {
        switch (emojiId) {
            case R.id.vomitEmoji:
                selectedEmojiIndex = 0;
                break;
            case R.id.sadEmoji:
                selectedEmojiIndex = 1;
                break;
            case R.id.neutralEmoji:
                selectedEmojiIndex = 2;
                break;
            case R.id.happyEmoji:
                selectedEmojiIndex = 3;
                break;
            case R.id.loveEmoji:
                selectedEmojiIndex = 4;
                break;
        }
    }

    public Bundle setCarDetails(Car car) {

        Bundle bundle = new Bundle();
        bundle.putString("brand", car.carBrand);
        bundle.putString("color", car.carColour);
        bundle.putString("model", car.carModel);
        bundle.putString("plateNumber", car.carModel);
        return bundle;
    }

    private Car getCarDetails(Bundle bundle) {
        Car car = new Car(bundle.getString("color"),bundle.getString("brand"),
                bundle.getString("model"),bundle.getString("plateNumber"));
        return car;
    }
}
