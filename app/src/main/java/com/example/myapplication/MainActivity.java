package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import yuku.ambilwarna.AmbilWarnaDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {
	 ColorPicker accurateColorPicker = null;
	 int red, green, blue;
	 TextView textView;
	 Client client;
	 Double cyan,magenta,yellow,key;

	 @Override
	 protected void onCreate (Bundle savedInstanceState) {
		  super.onCreate (savedInstanceState);
		  setContentView (R.layout.activity_main);

		  if (android.os.Build.VERSION.SDK_INT > 9) {
			   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			   StrictMode.setThreadPolicy(policy);
		  }

		  Button button = findViewById (R.id.button);

		  textView = findViewById (R.id.result);

		  final AmbilWarnaDialog colorpicker = new AmbilWarnaDialog (this, Color.RED, new AmbilWarnaDialog.OnAmbilWarnaListener () {
			   @Override
			   public void onCancel (AmbilWarnaDialog dialog) {

			   }

			   @RequiresApi (api = Build.VERSION_CODES.O)
			   @Override
			   public void onOk (AmbilWarnaDialog dialog, int color) {
					int r = (color >> 16) & 0xFF;
					int g = (color >> 8) & 0xFF;
					int b = (color >> 0) & 0xFF;
					red = r;
					green = g;
					blue = b;
					accurateColorPicker = new ColorPicker (MainActivity.this, red, green, blue);
					accurateColorPicker.show ();
					Button okColor = (Button) accurateColorPicker.findViewById (R.id.okColorButton);
					okColor.setOnClickListener (new View.OnClickListener () {
						 @Override
						 public void onClick (View v) {
							  int rgb = accurateColorPicker.getColor ();
							  String hexColor = String.format ("#%06X", (0xFFFFFF & rgb));
							  float[] cmyk = cmykFromRgb (rgb);
							  textView.setText ("CYAN " + cmyk[0] * 100.0 + "% \n MAGENTA " + cmyk[1] * 100.0 + "% \n YELLOW " + cmyk[2] * 100.0 + "% \n BLACK " + cmyk[3] * 100.0 + " %");
							  cyan=cmyk[0] * 100.0;
							  magenta=cmyk[1] * 100.0;
							  yellow=cmyk[2] * 100.0;
							  key=cmyk[3] * 100.0;
							  sendDataToController();
							  //Toast.makeText (getApplicationContext (), hexColor, Toast.LENGTH_LONG).show ();
							  accurateColorPicker.dismiss ();
						 }
					});
			   }

		  });

		  button.setOnClickListener (new View.OnClickListener () {
			   @Override
			   public void onClick (View v) {
					colorpicker.show ();
			   }
		  });
	 }


	 public static float[] cmykFromRgb (int rgbColor) {
		  int red = (0xff0000 & rgbColor) >> 16;
		  int green = (0xff00 & rgbColor) >> 8;
		  int blue = (0xff & rgbColor);
		  float black = Math.min (1.0f - red / 255.0f,
				  Math.min (1.0f - green / 255.0f, 1.0f - blue / 255.0f));
		  float cyan = 1.0f;
		  float magenta = 1.0f;
		  float yellow = 1.0f;
		  if (black != 1.0f) {
			   // black 1.0 causes zero divide
			   cyan = (1.0f - (red / 255.0f) - black) / (1.0f - black);
			   magenta = (1.0f - (green / 255.0f) - black) / (1.0f - black);
			   yellow = (1.0f - (blue / 255.0f) - black) / (1.0f - black);
		  }
		  return new float[]{cyan, magenta, yellow, black};
	 }

	 @Override
	 public boolean onCreateOptionsMenu (Menu menu) {
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.about_menu, menu);
		  return true;
	 }

	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		  // Handle item selection
		  switch (item.getItemId()) {
			   case R.id.about:
					Intent intent=new Intent (MainActivity.this,AboutActivity.class);
					startActivity (intent);
					return true;

			   default:
			   	 return true;
		  }
	 }

	 private void sendDataToController(){
		  connectIfNotConnected();
		  AlertDialog.Builder builder = new AlertDialog.Builder(this);
		  builder.setCancelable(true);
		  builder.setTitle("Confirmation");
		  builder.setMessage("Do you want to generate colour?");
		  builder.setPositiveButton("Confirm",
				  new DialogInterface.OnClickListener() {
					   @Override
					   public void onClick(DialogInterface dialog, int which) {
					   	 new Thread(new Runnable(){
							  @Override
							  public void run(){

								   String c=String.valueOf(getFormattedDouble(cyan));
								   String m=String.valueOf(getFormattedDouble(magenta));
								   String y=String.valueOf(getFormattedDouble(yellow));
								   String k=String.valueOf(getFormattedDouble(key));

								   try{
										client.send("$$");
										Thread.sleep(100);
										client.send(c);
										Thread.sleep(100);
										client.send(m);
										Thread.sleep(100);
										client.send(y);
										Thread.sleep(100);
										client.send(k);
										Thread.sleep(100);
										client.send("$$");
								   } catch (InterruptedException e){
										e.printStackTrace();
								   }

							  }
						 }).start();
					   }
				  });
		  builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			   	 dialog.dismiss();
			   }
		  });

		  AlertDialog dialog = builder.create();
		  dialog.show();


	 }


	 private void connectIfNotConnected(){

	 	  if(client==null){
	 	  	  Toast.makeText(getApplicationContext(),"going to create",Toast.LENGTH_SHORT).show();
			  client = new Client("192.168.43.30", 2010, getApplicationContext());
		  }

	 	  else if(!client.send("##")){
			   client = new Client("192.168.43.30", 2010,getApplicationContext());
		  }
	 }

	 private Double getFormattedDouble(Double toBeTruncated){
		  Double truncatedDouble = BigDecimal.valueOf(toBeTruncated)
				  .setScale(2, RoundingMode.HALF_UP)
				  .doubleValue();
		  return truncatedDouble;
	 }

}
