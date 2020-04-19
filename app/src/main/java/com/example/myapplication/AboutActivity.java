package com.example.myapplication;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {
	 TextView contentText;

	 @Override
	 protected void onCreate (Bundle savedInstanceState) {
		  super.onCreate (savedInstanceState);
		  setContentView (R.layout.activity_about);

		  contentText=findViewById (R.id.content);
		  String s="Team Members \n \n"+"Amuthan (1616106) \n"+"Arjith (1616109) \n"+"Devanathan (1616120) \n"+"Jayaganesh (1616129) ";
		  contentText.setText(s);

	 }
}
