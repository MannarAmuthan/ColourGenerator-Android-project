package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client
{
	 // initialize socket and input output streams
	 private Socket socket            = null;
	 private DataInputStream  input   = null;
	 private DataOutputStream out     = null;
	 private boolean connected=false;
	 private Context context;

	 public Client(String address, final int port, final Context context)
	 {
		  // establish a connection
		  this.context=context;
		  try
		  {
			   socket = new Socket(address, port);
			   Toast.makeText(context,"connected",Toast.LENGTH_SHORT).show();
			   connected=true;

			   // takes input from terminal
			   input  = new DataInputStream(System.in);

			   // sends output to the socket
			   out    = new DataOutputStream(socket.getOutputStream());

			   new Thread(new Runnable(){
					@Override
					public void run(){
						 Receiver receiver=new Receiver(socket.getPort(),context);
					}
			   }).start();

		  }
		  catch(UnknownHostException u)
		  {
			   Toast.makeText(context,u.toString(),Toast.LENGTH_SHORT).show();
		  }
		  catch(IOException i)
		  {
			   Toast.makeText(context,i.toString(),Toast.LENGTH_SHORT).show();
		  }

	 }

	 public boolean send(String line){
		  try
		  {
			   out.writeUTF(line);
		  }
		  catch(Exception i)
		  {
			   Toast.makeText(context,i.toString(),Toast.LENGTH_SHORT).show();
			   return false;
		  }
		  return true;
	 }

	 public void close(){

			   try{
					input.close();
					out.close();
					socket.close();
			   } catch (IOException i){
			   }
		  }

	 }






