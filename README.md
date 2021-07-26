# ColourGenerator-Android-project [Semester Project]

Members

Amuthan*
Deva*
Jeyaganesh*
Ajith

Raspbbery pi, Android App and Working computer(optional for control) all are connected to the same network point.


--Android Application Code part--:

1.Connection made between Android app to rasperry Pi:
       for this functionality code added in Android application part:


      
       -----------------------------------------------------------

       client = new Client(RASPBERRYPI_IP_ADDRESS, PORT, getApplicationContext());

       -----------------------------------------------------------

       	 public Client(String address, final int port, final Context context){

		  this.context=context;
		  try
		  {
			   socket = new Socket(address, port);
			   Toast.makeText(context,"connected",Toast.LENGTH_SHORT).show();
			   connected=true;
			   input  = new DataInputStream(System.in);
			   out    = new DataOutputStream(socket.getOutputStream());
			   

		  }
		  catch(UnknownHostException u)
		  {
			   Toast.makeText(context,NO_NETWORK_FOUND,Toast.LENGTH_SHORT).show();
		  }
		  catch(IOException i)
		  {
			   Toast.makeText(context,NETWORK_FOUND_BUT_CANNOT_CONNECT,Toast.LENGTH_SHORT).show();
		  }

	  } 

	  --------------------------------------------------------------

     Here "address" is raspberry pi's current network ip address. and "port" represents specific application gateway ,it need to connect. Socket is an endpoint of computer network where it makes connection to the other socket. So socket needs address and port. The message "connected" made to alert the user that it is connected. And then input and output pipes made between raspberry pi and android application. In above all process if any mistakes occured specific error(excepetion) will be alerted to user via toast message.


2.Sending data to Raspberry pi     

----------------------------------------------------
	String c,m,y,k;

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

--------------------------------------------------

	 public boolean send(String line){
		  try
		  {
			   out.writeUTF(line);
		  }
		  catch(Exception i)
		  {
			   Toast.makeText(context,ERROR_OCCURED,Toast.LENGTH_SHORT).show();
			   return false;
		  }
		  return true;
	 }

---------------------------------------------------

     Here before sending our colour data, "$$" were send for acknowledging that "colour data is ready, and receive it..". And to avoid any other data from unauthorized connection. And data of cyan,magenta,yellow and key will send. Between every data 100 millisecond time gap is made to avoid any confusion in connection pipeline.
     Using output pipe that was previously configured, data will be send.



--Raspberry Pi Code part--:

1.Accepting connection:


-------------------------------------------------------

    def __init__(self, host = ADDRESS, port = PORT):
        """ Initialize the server with a host and port to listen to. """
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.host = host
        self.port = port
        self.sock.bind((host, port))
        self.sock.listen(1)

-------------------------------------------------------

server = SocketServer()

-------------------------------------------------------

client_sock, client_addr = self.sock.accept()

print('Client {} connected'.format(client_addr))

-------------------------------------------------------



     So Socket with address and port that configured in the android app was created in the raspberry pi also. And accept any connection that makes with that port.


2.Receiving data:

------------------------------------------------------

                       read_data = client_sock.recv(255)
                       if len(read_data) == 0:
                          print('{} closed the socket.'.format(client_addr))
                          stop = True
                       else:
                          print('>>> Received: {}'.format(self.data))
                          colourList=getFourColorsFromThisData(read_data);

------------------------------------------------------

Firstly data was read from the socket. "255" means the length of data we are about to receive.We receive less than 255 only. And if the data length was 0, then connection means closed and we enable the "stop" action.If not then we print and store tha colour data for processing and usage. 
