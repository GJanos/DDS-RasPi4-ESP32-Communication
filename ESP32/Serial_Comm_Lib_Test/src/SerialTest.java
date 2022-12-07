//
//
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import gnu.io.CommPortIdentifier;
//import gnu.io.SerialPort;
//import gnu.io.SerialPortEvent;
//import gnu.io.SerialPortEventListener;
//import java.util.Enumeration;
//
//
//public class SerialTest implements SerialPortEventListener {
//    SerialPort serialPort;
//    /** The port we're normally going to use. */
//    private static final String[] PORT_NAMES = {
//            "/dev/ttyUSB0", // Linux
//    };
//    /**
//     * A BufferedReader which will be fed by a InputStreamReader
//     * converting the bytes into characters
//     * making the displayed results codepage independent
//     */
//    private BufferedReader input;
//    /** The output stream to the port */
//    private OutputStream output;
//    /** Milliseconds to block while waiting for port open */
//    private static final int TIME_OUT = 2000;
//    /** Default bits per second for COM port. */
//    private static final int DATA_RATE = 9600;
//
//    public void initialize() {
//
//        CommPortIdentifier portId = null;
//        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
//
//        //First, Find an instance of serial port as set in PORT_NAMES.
//        while (portEnum.hasMoreElements()) {
//            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
//            for (String portName : PORT_NAMES) {
//                if (currPortId.getName().equals(portName)) {
//                    portId = currPortId;
//                    break;
//                }
//            }
//        }
//        if (portId == null) {
//            System.out.println("Could not find COM port.");
//            return;
//        }
//
//        try {
//            // open serial port, and use class name for the appName.
//            serialPort = (SerialPort) portId.open(this.getClass().getName(),
//                    TIME_OUT);
//
//            // set port parameters
//            serialPort.setSerialPortParams(DATA_RATE,
//                    SerialPort.DATABITS_8,
//                    SerialPort.STOPBITS_1,
//                    SerialPort.PARITY_NONE);
//
//            // open the streams
//            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
//            output = serialPort.getOutputStream();
//
//            // add event listeners
//            serialPort.addEventListener(this);
//            serialPort.notifyOnDataAvailable(true);
//        } catch (Exception e) {
//            System.err.println(e.toString());
//        }
//    }
//
//    /**
//     * This should be called when you stop using the port.
//     * This will prevent port locking on platforms like Linux.
//     */
//    public synchronized void close() {
//        if (serialPort != null) {
//            serialPort.removeEventListener();
//            serialPort.close();
//        }
//    }
//
//    /**
//     * Handle an event on the serial port. Read the data and print it.
//     */
//    public synchronized void serialEvent(SerialPortEvent oEvent) {
//        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
//            try {
//                String inputLine=input.readLine();
//                System.out.println(inputLine);
//            } catch (Exception e) {
//                System.err.println(e.toString());
//            }
//        }
//        // Ignore all the other eventTypes, but you should consider the other ones.
//    }
//
//    public static void main(String[] args) throws Exception {
//        SerialTest main = new SerialTest();
//        main.initialize();
//        Thread t= new Thread(() -> {
//            //the following line will keep this app alive for 1000 seconds,
//            //waiting for events to occur and responding to them (printing incoming messages to console).
//            try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
//        });
//        t.start();
//        System.out.println("Started");
//    }
//}

// Program to Open a connection to Serial Port and close it
// Check the size of the Read and Write Buffers
// Compiled using jSerialComm-2.9.0.jar
//

//javac -cp .;jSerialComm-2.9.0.jar SerialPortsAvail.java
//java  -cp .;jSerialComm-2.9.0.jar SerialPortsAvail

//https://www.xanthium.in/cross-platform-serial-port-programming-tutorial-java-jdk-arduino-embedded-system-tutorial
// (c) 2022 www.xanthium.in
// Program to Receive tha data send from a  Arduino UNO over Serial Port
//
// Compiled using jSerialComm-2.9.0.jar
//
// Tutorial -> https://www.xanthium.in/cross-platform-serial-port-programming-tutorial-java-jdk-arduino-embedded-system-tutorial



//// (c) 2022 www.xanthium.in
////
////
import com.fazecast.jSerialComm.*; // Serial Port classes

public class SerialTest
{
    public static void main (String[] Args) {
        int BaudRate = 9600;
        int DataBits = 64;
        int StopBits = SerialPort.ONE_STOP_BIT;
        int Parity   = SerialPort.NO_PARITY;
        SerialPort [] AvailablePorts = SerialPort.getCommPorts();
        //Open the second Available port
        SerialPort MySerialPort = AvailablePorts[1];
        // Set Serial port Parameters
        MySerialPort.setComPortParameters(BaudRate,DataBits,StopBits,Parity);//Sets all serial port parameters at one time
        MySerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        MySerialPort.openPort(); //open the port
        try {
            Thread.sleep(2000);//Delay added to so Arduino-//-can recover after RESET
            }catch (Exception e)
        {e.printStackTrace();}
        if (MySerialPort.isOpen())
            System.out.println("\n" + MySerialPort.getSystemPortName() + " is Open ");
        MySerialPort.flushIOBuffers();

//            while (true)
//            {
//                byte[] readBuffer = new byte[100];
//                int numRead = MySerialPort.readBytes(readBuffer, readBuffer.length);
//                System.out.print("Read " + numRead + " bytes -");
//                //System.out.println(readBuffer);
//                double distance = new Double(readBuffer); //convert bytes to String
//
//                MySerialPort.rea
//                System.out.println("Received -> "+ S);
//            }

        try {
            while (true) {
                while (MySerialPort.bytesAvailable() <= 0)
                    Thread.sleep(20);

                int availableBytes = MySerialPort.bytesAvailable();
                byte[] readBuffer = new byte[availableBytes];
                int numRead = MySerialPort.readBytes(readBuffer, readBuffer.length);
                String message =  new String(readBuffer, 0, numRead); //new String(readBuffer, StandardCharsets.UTF_8);
                if(availableBytes > 5){
                    try {
                        double distance = Double.parseDouble(message);
                        System.out.println(distance);
                    }catch (NumberFormatException numberFormatException){
                        numberFormatException.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        MySerialPort.closePort(); //Close the port


    }//end of main()

}

/*
//GDCuyasen
//https://gmac.2600tech.com/
//*/
//import java.nio.charset.StandardCharsets;
//import java.util.Scanner;
//
//import com.fazecast.jSerialComm.SerialPort;
//import com.fazecast.jSerialComm.SerialPortDataListener;
//import com.fazecast.jSerialComm.SerialPortEvent;
//
//public class SerialTest {
//
//    SerialPort activePort;
//    SerialPort[] ports = SerialPort.getCommPorts();
//
//    public void showAllPort() {
//        int i = 0;
//        for(SerialPort port : ports) {
//            System.out.print(i + ". " + port.getDescriptivePortName() + " ");
//            System.out.println(port.getPortDescription());
//            i++;
//        }
//    }
//
//    public void setPort(int portIndex) {
//        activePort = ports[portIndex];
//
//        if (activePort.openPort())
//            System.out.println(activePort.getPortDescription() + " port opened.");
//
//        activePort.addDataListener(new SerialPortDataListener() {
//
////            @Override
////            public void serialEvent(SerialPortEvent event) {
////                int size = event.getSerialPort().bytesAvailable();
////                byte[] buffer = new byte[size];
////                //System.out.println("Available bites: "+ size);
////                event.getSerialPort().readBytes(buffer, size);
////               // String S = new String(buffer, StandardCharsets.UTF_8);
////                //System.out.println(S);
////                String message = "";
////                for(byte b:buffer)
////                    message += (char)b;
////                System.out.println(message.length() + "  " +size);
//////                message = message.replaceAll("\\n", "");
//////                System.out.println(message);
////            }
//            @Override
//            public void serialEvent(com.fazecast.jSerialComm.SerialPortEvent event) {
//                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
//                    return;
//                }
//
//                int bytesAvailable = activePort.bytesAvailable();
//                if (bytesAvailable <= 0) {
//                    return;
//                }
//                byte[] buffer = new byte[bytesAvailable];
//
//                int bytesRead = activePort.readBytes(buffer, Math.min(buffer.length, bytesAvailable));
//                String response = new String(buffer, 0, bytesRead);
//                System.out.println(response);
//
//            }
//
//
//            @Override
//            public int getListeningEvents() {
//                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
//            }
//        });
//    }
//
//    public void start() {
//        showAllPort();
//        Scanner reader = new Scanner(System.in);
//        System.out.print("Port? ");
//        int p = reader.nextInt();
//        setPort(p);
//        reader.close();
//    }
//
//    public static void main(String[] args) {
//        SerialTest mainClass = new SerialTest();
//        mainClass.start();
//    }
//
//}

