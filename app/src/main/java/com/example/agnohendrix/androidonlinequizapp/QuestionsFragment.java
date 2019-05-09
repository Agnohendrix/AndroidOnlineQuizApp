package com.example.agnohendrix.androidonlinequizapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Model.Question;
import com.example.agnohendrix.androidonlinequizapp.ViewHolder.QuestionsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class QuestionsFragment extends Fragment {


    View myFragment;

    RecyclerView listQuestions;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Question, QuestionsViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questions;

    ImageView addQ;

    public QuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_questions, container, false);

        listQuestions = myFragment.findViewById(R.id.listQuestions);
        layoutManager = new LinearLayoutManager(container.getContext());
        listQuestions.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Question> options =
                new FirebaseRecyclerOptions.Builder<Question>()
                        .setQuery(questions, Question.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Question, QuestionsViewHolder>(options) {
            @Override
            //Populates inflated items
            protected void onBindViewHolder(@NonNull final QuestionsViewHolder holder, final int position, @NonNull final Question model) {
                holder.question_category.setText(this.getSnapshots().getSnapshot(position).getKey());
                holder.question.setText(model.getQuestion());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.question.setTextColor(Color.YELLOW);
                        holder.question_category.setTextColor(Color.YELLOW);
                        Toast.makeText(getContext(), model.getCorrectAnswer(), Toast.LENGTH_LONG).show();

                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                holder.question.setTextColor(Color.BLACK);
                                holder.question_category.setTextColor(Color.BLACK);
                            }
                        });
                        alertDialog.setTitle("Question " + getSnapshots().getSnapshot(position).getKey());
                        alertDialog.setMessage("Modify question");
                        View modifyQuestion = inflater.inflate(R.layout.modify_question, null);

                        TextView imagelbl = modifyQuestion.findViewById(R.id.label_image);
                        TextView imageLinkLbl = modifyQuestion.findViewById(R.id.label_image_link);


                        Button cancel = modifyQuestion.findViewById(R.id.modify_cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.question.setTextColor(Color.BLACK);
                                holder.question_category.setTextColor(Color.BLACK);
                                alertDialog.dismiss();
                            }
                        });


                        Button confirm = modifyQuestion.findViewById(R.id.modify_confirm);

                        final EditText question = modifyQuestion.findViewById(R.id.m_question);
                        final EditText qCat = modifyQuestion.findViewById(R.id.m_question_category);
                        final EditText qAnswerA = modifyQuestion.findViewById(R.id.m_answerA);
                        final EditText qAnswerB = modifyQuestion.findViewById(R.id.m_answerB);
                        final EditText qAnswerC = modifyQuestion.findViewById(R.id.m_answerC);
                        final EditText qAnswerD = modifyQuestion.findViewById(R.id.m_answerD);
                        final EditText qCorrectAnswer = modifyQuestion.findViewById(R.id.m_correct_answer);
                        final ImageView qImage = modifyQuestion.findViewById(R.id.m_question_image);
                        final EditText qImageLnk = modifyQuestion.findViewById(R.id.m_question_image_link);

                        question.setText(model.getQuestion());
                        qCat.setText(model.getCategoryId());
                        qAnswerA.setText(model.getAnswerA());
                        qAnswerB.setText(model.getAnswerB());
                        qAnswerC.setText(model.getAnswerC());
                        qAnswerD.setText(model.getAnswerD());
                        qCorrectAnswer.setText(model.getCorrectAnswer());
                        if (model.getIsImageQuestion().equals("true")) {
                            Picasso.get().load(model.getImage()).into(qImage);
                            qImage.setVisibility(View.VISIBLE);
                            qImageLnk.setText(model.getImage());
                        } else {
                            imagelbl.setVisibility(View.GONE);
                            qImage.setVisibility(View.GONE);
                            imageLinkLbl.setVisibility(View.GONE);
                            qImageLnk.setVisibility(View.GONE);


                            //No image, layout is modified
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.BELOW, R.id.m_correct_answer);
                            cancel.setLayoutParams(params);

                            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params2.addRule(RelativeLayout.BELOW, R.id.m_correct_answer);
                            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            confirm.setLayoutParams(params2);
                        }

                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Modify DB
                                String isImage;
                                if (qImageLnk.getText().toString().equals(""))
                                    isImage = "false";
                                else
                                    isImage = "true";

                                //Creates instance to modify DB
                                Question mod = new Question(question.getText().toString(),
                                        qAnswerA.getText().toString(),
                                        qAnswerB.getText().toString(),
                                        qAnswerC.getText().toString(),
                                        qAnswerD.getText().toString(),
                                        qCorrectAnswer.getText().toString(),
                                        qImageLnk.getText().toString(),
                                        isImage,
                                        qCat.getText().toString());

                                //TO-DO Add Firebase modify.

                                //End
                                holder.question.setTextColor(Color.BLACK);
                                holder.question_category.setTextColor(Color.BLACK);
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.setView(modifyQuestion);
                        alertDialog.show();
                    }
                });
            }

            @NonNull
            @Override
            public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_layout, viewGroup, false);
                QuestionsViewHolder viewHolder = new QuestionsViewHolder(view);
                return viewHolder;
            }
        };

        addQ = myFragment.findViewById(R.id.add_questions);
        addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog add = new AlertDialog.Builder(getContext()).create();
                add.setTitle("Add Question");
                final View addQuestion = inflater.inflate(R.layout.question_add, null);

                final Spinner cat = addQuestion.findViewById(R.id.sp_question_category);
                final List<String> items = new ArrayList<String>();

                DatabaseReference categories = database.getReference("Category");
                categories.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            items.add(ds.getKey() + " - " + ds.child("name").getValue());
                            //Toast.makeText(getContext(), items.get(items.size()-1), Toast.LENGTH_LONG).show();
                        }
                        final ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
                        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cat.setAdapter(spAdapter);
                        spAdapter.notifyDataSetChanged();

                        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                parent.setSelection(position);
                                parent.getItemAtPosition(position);
                                //parent.setBackgroundColor(Color.BLACK);
                                String valueCat = cat.getSelectedItem().toString().substring(0, Math.min(cat.getSelectedItem().toString().length(), 2));
                                //String value2 = valueCat.substring(0, 2);
                                //Toast.makeText(getContext(), valueCat + " ciccia", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(getContext(), "Nothing", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //Get EditView data

                final EditText question = addQuestion.findViewById(R.id.a_question);
                final EditText qAnswerA = addQuestion.findViewById(R.id.a_answerA);
                final EditText qAnswerB = addQuestion.findViewById(R.id.a_answerB);
                final EditText qAnswerC = addQuestion.findViewById(R.id.a_answerC);
                final EditText qAnswerD = addQuestion.findViewById(R.id.a_answerD);
                final EditText qCorrectAnswer = addQuestion.findViewById(R.id.a_correct_answer);
                final ImageView qImage = addQuestion.findViewById(R.id.a_question_image);
                final EditText qImageLnk = addQuestion.findViewById(R.id.a_question_image_link);


                qImageLnk.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            Toast.makeText(getContext(), qImageLnk.getText().toString(), Toast.LENGTH_LONG).show();
                            if (!qImageLnk.getText().toString().isEmpty()) {
                                Picasso.get()
                                        .load(qImageLnk.getText().toString())
                                        .placeholder(R.drawable.ic_image_black_24dp)
                                        .error(R.drawable.ic_image_black_24dp)
                                        .into(qImage, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(getContext(), "Loaded!", Toast.LENGTH_LONG).show();

                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Toast.makeText(getContext(), "Error!", Toast.LENGTH_LONG).show();
                                                qImageLnk.setText("");
                                            }
                                        });
                            }

                        }
                    }
                });

                Button cancel = addQuestion.findViewById(R.id.add_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.dismiss();
                        //Toast.makeText(getContext(),cat.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                Button confirm = addQuestion.findViewById(R.id.add_confirm);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean ok = true;
                        ShapeDrawable sd = new ShapeDrawable();
                        sd.setShape(new RectShape());
                        sd.getPaint().setColor(Color.RED);
                        sd.getPaint().setStrokeWidth(10f);
                        sd.getPaint().setStyle(Paint.Style.STROKE);

                        ShapeDrawable good = new ShapeDrawable();
                        good.setShape(new RectShape());
                        good.getPaint().setColor(Color.TRANSPARENT);
                        good.getPaint().setStrokeWidth(0f);

                        if (qCorrectAnswer.getText().toString().isEmpty()) {
                            qCorrectAnswer.setBackground(sd);
                            qCorrectAnswer.requestFocus();
                            ok = false;
                        } else if (qCorrectAnswer.getText().toString().equals(qAnswerA.getText().toString()) ||
                                qCorrectAnswer.getText().toString().equals(qAnswerB.getText().toString()) ||
                                qCorrectAnswer.getText().toString().equals(qAnswerC.getText().toString()) ||
                                qCorrectAnswer.getText().toString().equals(qAnswerD.getText().toString())) {
                            qCorrectAnswer.setBackground(good);
                        } else {
                            qCorrectAnswer.setBackground(sd);
                            qCorrectAnswer.requestFocus();
                            ok = false;
                            Toast.makeText(getContext(), "CorrectAnswer must match A, B, C or D!", Toast.LENGTH_LONG).show();
                        }

                        if (qAnswerD.getText().toString().isEmpty()) {
                            qAnswerD.setBackground(sd);
                            qAnswerD.requestFocus();
                            ok = false;
                        } else {
                            qAnswerD.setBackground(good);
                        }

                        if (qAnswerC.getText().toString().isEmpty()) {
                            qAnswerC.setBackground(sd);
                            qAnswerC.requestFocus();
                            ok = false;
                        } else {
                            qAnswerC.setBackground(good);
                        }

                        if (qAnswerB.getText().toString().isEmpty()) {
                            qAnswerB.setBackground(sd);
                            qAnswerB.requestFocus();
                            ok = false;
                        } else {
                            qAnswerB.setBackground(good);
                        }

                        if (qAnswerA.getText().toString().isEmpty()) {
                            qAnswerA.setBackground(sd);
                            qAnswerA.requestFocus();
                            ok = false;
                        } else {
                            qAnswerA.setBackground(good);
                        }
                        /*
                        if (qCat.getText().toString().isEmpty()) {
                            qCat.setBackground(sd);
                            qCat.requestFocus();
                            ok = false;
                        } else {
                            qCat.setBackground(good);
                        }*/

                        if (question.getText().toString().isEmpty()) {
                            question.setBackground(sd);
                            question.requestFocus();
                            ok = false;
                        } else {
                            question.setBackground(good);
                        }

                        if (ok) {
                            //Add Firebase behavior
                            String isImage = "false";
                            if (qImageLnk.getText().toString().equals(""))
                                isImage = "false";
                            else
                                isImage = "true";

                            final Question newQ = new Question(question.getText().toString(),
                                    qAnswerA.getText().toString(),
                                    qAnswerB.getText().toString(),
                                    qAnswerC.getText().toString(),
                                    qAnswerD.getText().toString(),
                                    qCorrectAnswer.getText().toString(),
                                    qImageLnk.getText().toString(),
                                    isImage,
                                    cat.getSelectedItem().toString().substring(0, Math.min(cat.getSelectedItem().toString().length(), 2)));
                            Toast.makeText(getContext(), newQ.getImage() + " " + newQ.getIsImageQuestion(), Toast.LENGTH_LONG).show();
                            ValueEventListener listener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //Toast.makeText(getContext(), String.valueOf(dataSnapshot.getChildrenCount()), Toast.LENGTH_LONG).show();
                                    int num = (int) dataSnapshot.getChildrenCount() + 1;
                                    DecimalFormat decimalFormat = new DecimalFormat("00");
                                    String number = decimalFormat.format(num);
                                    questions.child(number).setValue(newQ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Question successfully registered!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    Toast.makeText(getContext(), number, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                                }
                            };
                            questions.addListenerForSingleValueEvent(listener);
                            add.dismiss();
                        }
                    }
                });
                add.setView(addQuestion);
                add.show();
            }
        });

        adapter.notifyDataSetChanged();
        listQuestions.setAdapter(adapter);
        adapter.startListening();
        return myFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}


