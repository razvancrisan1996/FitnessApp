package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.Database.YogaDB;
import com.example.myapplication.Model.Exercise;
import com.example.myapplication.Utils.Common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class Daily_Training extends AppCompatActivity {

    Button btnStart;
    ImageView ex_image;
    TextView txtGetReady, txtCountdown, txtTimer, ex_name;
    ProgressBar progressBar;
    LinearLayout layoutGetReady;

    int ex_id=0, limit_time=0;

    List<Exercise> exerciseList = new ArrayList<>();
    YogaDB yogaDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily__training);

        initData();
        yogaDB = new YogaDB(this);

        btnStart = (Button) findViewById(R.id.btnStart);
        ex_image = (ImageView) findViewById(R.id.detail_image);
        ex_name = (TextView) findViewById(R.id.title);
        txtCountdown = (TextView) findViewById(R.id.txtCountdown);
        txtGetReady = (TextView) findViewById(R.id.txtGetReady);
        txtTimer = (TextView) findViewById(R.id.timer);
        layoutGetReady = (LinearLayout) findViewById(R.id.layout_get_ready);
        progressBar = (MaterialProgressBar) findViewById(R.id.progressBar);

        //set data
        progressBar.setMax(exerciseList.size());

        setExerciseInformation(ex_id);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnStart.getText().toString().toLowerCase().equals("start")){
                    showGetReady();
                    btnStart.setText("done");
                } else if (btnStart.getText().toString().toLowerCase().equals("done")){
                    if (yogaDB.getSettingMode() == 0)
                        exercisesEasyCountDown.cancel();
                    else if (yogaDB.getSettingMode() == 1)
                        exercisesMediumCountDown.cancel();
                    else if (yogaDB.getSettingMode() == 2)
                        exercisesHardCountDown.cancel();
                    restTimeCountDown.cancel();
                    if (ex_id < exerciseList.size()){
                        showRestTime();
                        ex_id++;
                        progressBar.setProgress(ex_id);
                        txtTimer.setText("");
                    }
                    else {
                        showFinished();
                    }
                } else{
                    if (yogaDB.getSettingMode() == 0)
                        exercisesEasyCountDown.cancel();
                    else if (yogaDB.getSettingMode() == 1)
                        exercisesMediumCountDown.cancel();
                    else if (yogaDB.getSettingMode() == 2)
                        exercisesHardCountDown.cancel();

                    restTimeCountDown.cancel();

                    if (ex_id < exerciseList.size())
                        setExerciseInformation(ex_id);
                    else
                        showFinished();


                }

            }
        });

    }

    private void showRestTime() {
        ex_image.setVisibility(View.INVISIBLE);
        txtTimer.setVisibility(View.INVISIBLE);
        btnStart.setText("Skip");
        btnStart.setVisibility(View.VISIBLE);
        layoutGetReady.setVisibility(View.VISIBLE);

        restTimeCountDown.start();

        txtGetReady.setText("Rest Time");

    }

    private void showGetReady() {
        ex_image.setVisibility(View.INVISIBLE);
        btnStart.setVisibility(View.INVISIBLE);
        txtTimer.setVisibility(View.VISIBLE);

        layoutGetReady.setVisibility(View.VISIBLE);

        txtGetReady.setText("GET READY");
        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long l) {
                txtCountdown.setText(""+(l-1000)/1000);
            }

            @Override
            public void onFinish() {
                showExercises();
            }
        }.start();
        {

        }
    }

    private void showExercises() {
        if (ex_id < exerciseList.size()){
            ex_image.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
            layoutGetReady.setVisibility(View.INVISIBLE);

            if (yogaDB.getSettingMode() == 0)
                exercisesEasyCountDown.start();
            else if (yogaDB.getSettingMode() == 1)
                exercisesMediumCountDown.start();
            else if (yogaDB.getSettingMode() == 2)
                exercisesHardCountDown.start();


            //SetData
            ex_image.setImageResource(exerciseList.get(ex_id).getImage_id());
            ex_name.setText(exerciseList.get(ex_id).getName());

        }else
            showFinished();
    }

    private void showFinished() {
        ex_image.setVisibility(View.INVISIBLE);
        btnStart.setVisibility(View.INVISIBLE);
        txtTimer.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        layoutGetReady.setVisibility(View.VISIBLE);
        txtGetReady.setText("FINISHED!");
        txtCountdown.setText("Congratulation!\n You're done with today exercises");
        txtCountdown.setTextSize(20);

        //Save the workout into DB
        yogaDB.saveDay(""+ Calendar.getInstance().getTimeInMillis());


    }

    CountDownTimer exercisesEasyCountDown = new CountDownTimer(Common.TIME_LIMIT_EASY, 1000) {
        @Override
        public void onTick(long l) {
            txtTimer.setText(""+(l/1000));
        }

        @Override
        public void onFinish() {
            if (ex_id < exerciseList.size()-1){
                ex_id++;
                progressBar.setProgress(ex_id);
                txtTimer.setText("");
                setExerciseInformation(ex_id);
                btnStart.setText("Start");

            } else{
                showFinished();
            }
        }
    };
    CountDownTimer exercisesMediumCountDown = new CountDownTimer(Common.TIME_LIMIT_MEDIUM, 1000) {
        @Override
        public void onTick(long l) {
            txtTimer.setText(""+(l/1000));
        }

        @Override
        public void onFinish() {
            if (ex_id < exerciseList.size()-1){
                ex_id++;
                progressBar.setProgress(ex_id);
                txtTimer.setText("");
                setExerciseInformation(ex_id);
                btnStart.setText("Start");

            } else{
                showFinished();
            }
        }
    };
    CountDownTimer exercisesHardCountDown = new CountDownTimer(Common.TIME_LIMIT_HARD, 1000) {
        @Override
        public void onTick(long l) {
            txtTimer.setText(""+(l/1000));
        }

        @Override
        public void onFinish() {
            if (ex_id < exerciseList.size()-1){
                ex_id++;
                progressBar.setProgress(ex_id);
                txtTimer.setText("");
                setExerciseInformation(ex_id);
                btnStart.setText("Start");

            } else{
                showFinished();
            }
        }
    };

    CountDownTimer restTimeCountDown = new CountDownTimer(10000, 1000) {
        @Override
        public void onTick(long l) {
            txtCountdown.setText(""+(l/1000));
        }

        @Override
        public void onFinish() {
            setExerciseInformation(ex_id);
            showExercises();
        }
    };

    private void setExerciseInformation(int id) {
        ex_image.setImageResource(exerciseList.get(id).getImage_id());
        ex_name.setText(exerciseList.get(id).getName());
        btnStart.setText("Start");

        ex_image.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.VISIBLE);
        txtTimer.setVisibility(View.VISIBLE);

        layoutGetReady.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        exerciseList.add(new Exercise(R.drawable.easy_pose,"Easy Pose"));
        exerciseList.add(new Exercise(R.drawable.cobra_pose,"Cobra Pose"));
//        exerciseList.add(new Exercise(R.drawable.downward_facing_dog,"Downward Facing Dog"));
//        exerciseList.add(new Exercise(R.drawable.boat_pose,"Boat Pose"));
//        exerciseList.add(new Exercise(R.drawable.half_pigeon,"Half Pigeon"));
//        exerciseList.add(new Exercise(R.drawable.low_lunge,"Low Lunge"));
//        exerciseList.add(new Exercise(R.drawable.upward_bow,"Upward Bow"));
//        exerciseList.add(new Exercise(R.drawable.crescent_lunge,"Crescent Lunge"));
//        exerciseList.add(new Exercise(R.drawable.warrior_pose,"Warrior Pose"));
//        exerciseList.add(new Exercise(R.drawable.bow_pose,"Bow Pose"));
//        exerciseList.add(new Exercise(R.drawable.warrior_pose_2,"Warrior Pose 2"));
    }

}
