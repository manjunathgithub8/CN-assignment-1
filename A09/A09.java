
import javax.net.ssl.*;
import java.io.*;


public class A09 {
    public static void main(String args[]) throws Exception {
        SSLSocket client = null;                              //secure socket to connect to server
        try {
            //input parameter from command line arguments
            String username=args[2];
            String pass=args[3];
            String host=args[0];
            int port=Integer.parseInt(args[1]);
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            client = (SSLSocket) sslsocketfactory.createSocket(host,port);                       //providing host and port to establish a secure connection



            InputStream is = client.getInputStream();                                 // inputstream for socket
            BufferedReader sockin = new BufferedReader(new InputStreamReader(is));   //creating bufferedreader object of inputstream reader
                                                                                     //for getting output given by server

            OutputStream os = client.getOutputStream ();                             //outputstream for socket

            PrintWriter sockout = new PrintWriter (os, true);               //to provide commands to the server



            System.out.println ("" + sockin.readLine ());                           //prints validation output of connection to server
            sockout.println("USER "+username);                                       // user pop3 command
            System.out.println ("" + sockin.readLine ());
            sockout.println("PASS "+pass);                                           //pop3 pass command
            String err=sockin.readLine();
                                                               //prints error if wrong user details
            System.out.println(""+err);

            if(err.startsWith("-ERR"))                          //exits if wrong user details
            {
                System.out.println("Wrong login details.");
                System.exit(0);
            }
            sockout.println("stat");                           //pop3 stat command to find total no.of mails
            String stat=sockin.readLine();
            System.out.println("stat : "+stat);

            int last_mail;
            String[] s2 = stat.split(" ");
            last_mail=Integer.parseInt(s2[1]);                            //splitting only number from output(total no.of mails)
                                                                          //this is also the last mail index
            System.out.println("total number of mails : "+last_mail);
            if(last_mail<1)                                                //checking if no mails are there and exits if true
            {
                System.out.println("No mails");
                System.exit(0);
            }

            int n=1,ind=1;
            String filename="firstemail.txt";
            while(n<=2) {
                sockout.println("RETR "+ind);                                //pop3 retr command to retrieve email of specified index(initially 1, later last index)
                String temp = sockin.readLine();
                System.out.println("\nretrieving email message "+ind+" : " + temp);
                FileWriter fw = new FileWriter(filename);                    //file writer to retrieve the email and print to a file
                PrintWriter fileout = new PrintWriter(fw);

                while (true) {
                    if (temp.startsWith(".")) {                             //the email message ends with dot and printing is stop

                        fileout.close();
                        fw.close();

                        break;
                    }


                    temp = sockin.readLine();
                    fileout.println(temp);                                   //printing each line to file in loop


                }
                filename="lastemail.txt";                                   //file name changed to last email to save the last email message
                ind=last_mail;                                              //index of last email
             n++;
            }                                                              //printing repeated again for last email

            System.out.println("\nEmails are saved to text files");

            //deleting
            sockout.println("dele 1");                                     //pop3 delete command- deleting 1st mail
            System.out.println("\nDeleted 1: "+sockin.readLine());
            if(last_mail>1)                                                //if no.of mails is more than 1 then delete last mail
            {
                sockout.println("dele "+last_mail);
                System.out.println("Deleted "+last_mail+" : "+sockin.readLine());
            }

            sockout.println("QUIT");                                                   //pop3 quit command
            System.out.println("\nSigning off using quit command\n"+sockin.readLine());

            os.close();
            is.close();
            sockin.close();
            sockout.close();
            client.close();

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        finally {

                    System.out.println("\npop3 server closed");
                    client.close();                                      //closing the socket

        }
    }
}





