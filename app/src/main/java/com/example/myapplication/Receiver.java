package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

import java.net.*;
import java.io.*;

public class Receiver
{
	 //initialize socket and input stream
	 private Socket          socket   = null;
	 private ServerSocket    server   = null;
	 private DataInputStream in       =  null;
	 private Context context;

	 // constructor with port
	 public Receiver(int port, Context context)
	 {
		  // starts server and waits for a connection
		  this.context=context;
		  try
		  {
			   server = new ServerSocket(port);
			   System.out.println("Server started");

			   System.out.println("Waiting for a client ...");

			   socket = server.accept();
			   Toast.makeText(context,"Client Accepted",Toast.LENGTH_SHORT).show();

			   // takes input from the client socket
			   in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			   String line = "";

			   // reads message from client until "Over" is sent
			   while (!line.equals("Over"))
			   {
					try
					{
						 line = in.readUTF();
						 Toast.makeText(context,line,Toast.LENGTH_SHORT).show();

					}
					catch(IOException i)
					{
						 System.out.println(i);
					}
			   }
			   System.out.println("Closing connection");

			   // close connection
			   socket.close();
			   in.close();
		  }
		  catch(IOException i)
		  {
			   System.out.println(i);
		  }
	 }

}
