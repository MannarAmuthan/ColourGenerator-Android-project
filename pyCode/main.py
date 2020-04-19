import socket
import select
import time


class SocketServer:

    def listToString(self,s):
       str1 = ""
       i=0
       for ele in s:
          if i>1:
            str1 += ele
          i=i+1
       return str1


    def __init__(self, host = '0.0.0.0', port = 2010):
        """ Initialize the server with a host and port to listen to. """
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.host = host
        self.port = port
        self.sock.bind((host, port))
        self.sock.listen(1)

    def close(self):
        """ Close the server socket. """
        print('Closing server socket (host {}, port {})'.format(self.host, self.port))
        if self.sock:
            self.sock.close()
            self.sock = None

    def run_server(self):
        """ Accept and handle an incoming connection. """
        print('Starting socket server (host {}, port {})'.format(self.host, self.port))

        client_sock, client_addr = self.sock.accept()

        print('Client {} connected'.format(client_addr))

        stop = False
        while not stop:
            if client_sock:
                # Check if the client is still connected and if data is available:
                try:
                    rdy_read, rdy_write, sock_err = select.select([client_sock,], [], [])
                except select.error:
                    print('Select() failed on socket with {}'.format(client_addr))
                    return 1

                try:

                   if len(rdy_read) > 0:
                       read_data = client_sock.recv(255)
                       serial=read_data.split()
                       raw= ''+serial[0]
                       array=list(raw)
                       print(array)
                       self.data=self.listToString(array)
                       # Check if socket has been closed
                       if len(read_data) == 0:
                          print('{} closed the socket.'.format(client_addr))
                          stop = True
                       else:
                          print('>>> Received: {}'.format(self.data))
                          if self.data == 'quit':
                            stop = True
                          if self.data == 'Are you connected?':
                            self.data="yes"

                          client_sock.send(self.data)
                          #else:
                          #    self.data=read_data.rstrip()
                          #    if self.data == 'Are you connected?':
                          #       client_sock.send('yes')
                          #    client_sock.send(read_data)

                except:
                    print('Something went wrong')
                    stop=True


            else:
                print("No client is connected, SocketServer can't receive data")
                stop = True

        # Close socket
        print('Closing connection with {}'.format(client_addr))
        client_sock.close()
        return 0




def main():
    server = SocketServer()
    while True:
     server.run_server()
     print ('Exiting')

if __name__ == "__main__":
    main()


