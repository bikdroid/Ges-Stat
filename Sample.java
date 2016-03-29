import com.leapmotion.leap.*;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.io.*;

class Sample {
	public static String msg;
	public static void main(String[] args) {

		Controller controller = new Controller();
		SampleListener samplistener = new SampleListener();

		// Sample Listener receives the commands from controller
		controller.addListener(samplistener);

		// Keep this process running until Enter is pressed
		System.out.println("Press Enter to quit...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		controller.removeListener(samplistener);
	}

	static class SampleListener extends Listener {
		
		 void writeToFile( String arg)
		    {	
		    	try{
		    		String data = arg;
		    		
		    		File file =new File("C:/Users/Bikramjit/myapi/output.txt");
		    		
		    		//if file doesnt exists, then create it
		    		if(!file.exists()){
		    			file.createNewFile();
		    		}
		    		
		    		//true = append file
		    		FileWriter fileWritter = new FileWriter(file.getName(),true);
		    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		    	        bufferWritter.write(data);
		    	        bufferWritter.close();
		    	        fileWritter.close();
		    	    
			        System.out.println("Done");
			        
		    	}catch(IOException e){
		    		e.printStackTrace();
		    	}
		    }
		 
		
		/* void sendToSocket(String str) throws IOException{
			
			 ServerSocket listener = new ServerSocket(3024);
			 Socket clientSocket = new Socket("10.21.50.35", 3024);
		        try {
		            while (true) {
		                Socket socket = listener.accept();
		                
		                System.out.println("Conected");
		                
		                try {
		                    PrintWriter out =
		                        new PrintWriter(socket.getOutputStream(), true);
		                   out.println(str);
		                    out.close();
		                } finally {
		                    socket.close();
		                    
		                }
		            }
		        }catch(Exception e){
		        	System.out.println("Exception1");
		        	e.printStackTrace();
		        }
		        finally {
		            listener.close();
		        }
		 }*/
		 
		 void sendToServerSocket(String arg) throws UnknownHostException, IOException{
			 try {

		            Socket sclient = new Socket("10.21.50.35",3099);
		// data streams
		            DataOutputStream doToServer = new DataOutputStream(sclient.getOutputStream());

		            doToServer.writeUTF(arg);

		            DataInputStream doFrmServer = new DataInputStream(sclient.getInputStream());
		           // currentTempValue = doFrmServer.readUTF();



		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		 
		 }

		public void onFrame(Controller controller) {
			// FileOutputStream fos=null;
			//BufferedWriter bw;
		
				//File f = new File("C:/Users/Bikramjit/myapi/output.txt");
				//if (!(f.exists())) {
				//	f.createNewFile();
				//}
				//FileWriter fw = new FileWriter(f,true);

				// fos = new FileOutputStream(f);

				Frame frame = controller.frame();
				// System.out.println("Frame id: " + frame.id() + ", timestamp:
				// " + frame.timestamp() + ", hands:"
				// + frame.hands().count() + ", gestures : " +
				// frame.gestures().get(0).type());
				// Hands
				
				
				for (Hand hand : frame.hands()) {
					String handType;
					// String handType = hand.isLeft() ? "Left Hand" : "Right
					// Hand";
					if (hand.isLeft()) {
						handType = "Left Hand";
					} else if (hand.isRight()) {
						handType = "Right Hand";
					} else {
						handType = "";
					}
					// System.out.println(" " + handType + " , id: " + hand.id()
					// + " , palm position: "
					// + hand.palmPosition() + " , hand direction: " +
					// hand.direction().toString());
					if (handType.equals("Right Hand") || handType.equals("Left Hand")) {
						Finger finger = hand.fingers().frontmost();
						System.out.println("Finger tip move to : " + finger.direction().getX());
						if (finger.direction().getX() < 0) {
							/*
							bw = new BufferedWriter(fw);
							bw.write("temp-down");
							bw.close();
							fw.close();
							*/
							//writeToFile("temp-down");
							try {
								sendToServerSocket("temp-down");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							/*
							bw = new BufferedWriter(fw);
							bw.write("temp-up");
							
							bw.close();
							fw.close();
							*/
							//writeToFile("temp-up");
							try {
								sendToServerSocket("temp-up");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						/*
						 * try { System.out.println(Unirest.get(
						 * "http://127.0.0.1:3000/db" ).asJson().toString()); }
						 * catch (UnirestException e) { // TODO Auto-generated
						 * catch block e.printStackTrace(); }
						 */

					} else {
						System.out.println("inside");
						//bw.close();
						break;
					}

				}
				// System.out.println("reached");
				 
				 //fw.close();

			
		}

		public void onConnect(Controller controller) {
			System.out.println("Connected");
			controller.enableGesture(Gesture.Type.TYPE_SWIPE);
			controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
			controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
			controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
		}

	}

}