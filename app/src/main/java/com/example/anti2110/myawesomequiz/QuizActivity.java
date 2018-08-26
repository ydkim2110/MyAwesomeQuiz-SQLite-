package com.example.anti2110.myawesomequiz;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tv_question;
    private TextView tv_score;
    private TextView tv_question_count;
    private TextView tv_count_down;
    private RadioGroup radioGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button btn_confirm_next;

    private ColorStateList textColorDefaultRb;

    private List<Question> questionList;
    private int quetionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tv_question = findViewById(R.id.tv_question);
        tv_score = findViewById(R.id.tv_score);
        tv_question_count = findViewById(R.id.tv_question_count);
        tv_count_down = findViewById(R.id.tv_countdown);
        radioGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_btn1);
        rb2 = findViewById(R.id.radio_btn2);
        rb3 = findViewById(R.id.radio_btn3);
        btn_confirm_next = findViewById(R.id.btn_confirm_next);

        textColorDefaultRb = rb1.getTextColors();

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();

        btn_confirm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered) {
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select and answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void checkAnswer() {
        answered = true;

        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNr = radioGroup.indexOfChild(rbSelected) + 1;

        if(answerNr == currentQuestion.getAnswerNr()) {
            score++;
            tv_score.setText("Score: "+score);
        }

        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                tv_question.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                tv_question.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                tv_question.setText("Answer 3 is correct");
                break;
        }

        if(quetionCounter < questionCountTotal) {
            btn_confirm_next.setText("Next");
        } else {
            btn_confirm_next.setText("Finish");
        }
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        radioGroup.clearCheck();

        if(quetionCounter < questionCountTotal) {
            currentQuestion = questionList.get(quetionCounter);

            tv_question.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            quetionCounter++;
            tv_question_count.setText("Question: "+quetionCounter+"/"+questionCountTotal);
            answered = false;
            btn_confirm_next.setText("Confirm");

        } else {
            finishQuiz();
        }

    }

    private void finishQuiz() {
        finish();
    }

}
