///*
// * (c) Copyright, Real-Time Innovations, 2020.  All rights reserved.
// * RTI grants Licensee a license to use, modify, compile, and create derivative
// * works of the software solely for use with RTI Connext DDS. Licensee may
// * redistribute copies of the software provided that all such copies are subject
// * to this license. The software is provided "as is", with no warranty of any
// * type, including any warranty for fitness for any purpose. RTI is under no
// * obligation to maintain or support the software. RTI shall not be liable for
// * any incidental or consequential damages arising out of the use or inability
// * to use the software.
// */
//
//import java.util.Objects;
//
//import com.rti.dds.domain.DomainParticipant;
//import com.rti.dds.domain.DomainParticipantFactory;
//import com.rti.dds.infrastructure.InstanceHandle_t;
//import com.rti.dds.infrastructure.StatusKind;
//import com.rti.dds.publication.Publisher;
//import com.rti.dds.topic.Topic;
//
///**
// * Simple example showing all Connext code in one place for readability.
// */
//public class SpeedPublisher extends Application implements AutoCloseable {
//
//    // Usually one per application
//    private DomainParticipant participant = null;
////    private SerialSpeedReader serialSpeedReader = null;
//    private Publisher publisher = null;
//    private Topic topic = null;
//    private SpeedDataWriter writer = null;
//
//    private void initializeDDS(){
//        // Start communicating in a domain
//        participant = Objects.requireNonNull(
//                DomainParticipantFactory.get_instance().create_participant(
//                        getDomainId(),
//                        DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        // A Publisher allows an application to create one or more DataWriters
//        publisher = Objects.requireNonNull(
//                participant.create_publisher(
//                        DomainParticipant.PUBLISHER_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        // Register the datatype to use when creating the Topic
//        String typeName = SpeedTypeSupport.get_type_name();
//        SpeedTypeSupport.register_type(participant, typeName);
//
//        // Create a Topic with a name and a datatype
//        topic = Objects.requireNonNull(
//                participant.create_topic(
//                        "SpeedTopic",
//                        typeName,
//                        DomainParticipant.TOPIC_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        // This DataWriter writes data on "Example SpeedChangeIntervention" Topic
//        writer = (SpeedDataWriter) Objects.requireNonNull(
//                publisher.create_datawriter(
//                        topic,
//                        Publisher.DATAWRITER_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//    }
//
////    private void initializeSpeedReader(){
////        serialSpeedReader = new SerialSpeedReader();
////        serialSpeedReader.initialize();
////        Thread t= new Thread(() -> {
////            //the following line will keep this app alive for 1000 seconds,
////            //waiting for events to occur and responding to them (printing incoming messages to console).
////            try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
////        });
////        t.start();
////        System.out.println("Started");
////    }
//    private void runApplication() {
//        initializeDDS();
//
////        initializeSpeedReader();
//
//        // Create data sample for writing
//        Speed data = new Speed();
//
//        for (int samplesWritten = 0; !isShutdownRequested()
//                && samplesWritten < getMaxSampleCount(); samplesWritten++) {
//
//            // Modify the data to be written here
//            data.timestamp = samplesWritten;
//
//            System.out.println("Writing Speed, count " + samplesWritten);
//
//            writer.write(data, InstanceHandle_t.HANDLE_NIL);
//            try {
//                final long sendPeriodMillis = 995; // 1 second
//                Thread.sleep(sendPeriodMillis);
//            } catch (InterruptedException ix) {
//                System.err.println("INTERRUPTED");
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void close() {
//        // Delete all entities (DataWriter, Topic, Publisher, DomainParticipant)
//        if (participant != null) {
//            participant.delete_contained_entities();
//
//            DomainParticipantFactory.get_instance()
//                    .delete_participant(participant);
//        }
//    }
//
//    public static void main(String[] args) {
//        // Create example and run: Uses try-with-resources,
//        // publisherApplication.close() automatically called
//        try (SpeedPublisher publisherApplication = new SpeedPublisher()) {
//            publisherApplication.parseArguments(args);
//            publisherApplication.addShutdownHook();
//            publisherApplication.runApplication();
//        }
//
//        // Releases the memory used by the participant factory. Optional at application exit.
//        DomainParticipantFactory.finalize_instance();
//    }
//}


//import java.util.Objects;
//
//import com.rti.dds.domain.DomainParticipant;
//import com.rti.dds.domain.DomainParticipantFactory;
//import com.rti.dds.infrastructure.InstanceHandle_t;
//import com.rti.dds.infrastructure.StatusKind;
//import com.rti.dds.publication.Publisher;
//import com.rti.dds.topic.Topic;
//
///**
// * Simple example showing all Connext code in one place for readability.
// */
//public class SpeedPublisher extends Application implements AutoCloseable {
//
//    // Usually one per application
//    private DomainParticipant participant = null;
//    private Publisher publisher = null;
//    private Topic topic = null;
//    private SpeedDataWriter writer = null;
//    private long startTime = 0;
//    private int speedChangeNum = 5;
//    private int speedID = 0;
//
//    private double speed = 50; //km/h
//
//    public void initializeDDS() {
//        // Start communicating in a domain
//        participant = Objects.requireNonNull(
//                DomainParticipantFactory.get_instance().create_participant(
//                        getDomainId(),
//                        DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        // A Publisher allows an application to create one or more DataWriters
//        publisher = Objects.requireNonNull(
//                participant.create_publisher(
//                        DomainParticipant.PUBLISHER_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        // Register the datatype to use when creating the Topic
//        String typeName = SpeedTypeSupport.get_type_name();
//        SpeedTypeSupport.register_type(participant, typeName);
//
//        // Create a Topic with a name and a datatype
//        topic = Objects.requireNonNull(
//                participant.create_topic(
//                        "SpeedTopic",
//                        typeName,
//                        DomainParticipant.TOPIC_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        // This DataWriter writes data on "Example BreakChangeIntervention" Topic
//        writer = (SpeedDataWriter) Objects.requireNonNull(
//                publisher.create_datawriter(
//                        topic,
//                        Publisher.DATAWRITER_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        startTime = System.currentTimeMillis();
//    }
//
//    private double getRandomNumber(double min, double max) {
//        return (Math.random() * (max - min)) + min;
//    }
//
//    private double changeSpeed(boolean accelarate){
//        speed = accelarate ? speed + 2 : speed - 2;
//        speed = speed > 200 ? 200 : speed; // limiting max speed to 200 km/h
//        speed = speed < 0 ? 0 : speed; // limiting min speed to 0 km/h
//        return speed;
//    }
//    private void runApplication() {
//        initializeDDS();
//        // Create data sample for writing
//        Speed data = new Speed();
//
//        while(!isShutdownRequested()){
//            boolean accelarate = getRandomNumber(0.0, 1.0) >= 0.5;
//
//            for (int samplesWritten = 0; !isShutdownRequested()
//                    && samplesWritten < speedChangeNum; samplesWritten++) {
//
//                // Modify the data to be written here
//                data.id = Integer.toString(speedID++);
//                data.message = "Success";
//                data.speed = changeSpeed(accelarate);
//                data.timestamp = System.currentTimeMillis() - startTime;
//
//                System.out.println("Writing Brake, count " + speedID);
//
//                writer.write(data, InstanceHandle_t.HANDLE_NIL);
//                try {
//                    final long sendPeriodMillis = 199; // ~0.2 second
//                    Thread.sleep(sendPeriodMillis);
//                } catch (InterruptedException ix) {
//                    System.err.println("INTERRUPTED");
//                    break;
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public void close() {
//        // Delete all entities (DataWriter, Topic, Publisher, DomainParticipant)
//        if (participant != null) {
//            participant.delete_contained_entities();
//
//            DomainParticipantFactory.get_instance()
//                    .delete_participant(participant);
//        }
//    }
//
//    public static void main(String[] args) {
//        // Create example and run: Uses try-with-resources,
//        // publisherApplication.close() automatically called
//        try (SpeedPublisher publisherApplication = new SpeedPublisher()) {
//            publisherApplication.parseArguments(args);
//            publisherApplication.addShutdownHook();
//            publisherApplication.runApplication();
//        }
//
//        // Releases the memory used by the participant factory. Optional at application exit.
//        DomainParticipantFactory.finalize_instance();
//    }
//}

import java.util.Objects;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.*;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.ReadCondition;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

/**
 * Simple example showing all Connext code in one place for readability.
 */
public class SpeedPublisher extends Application implements AutoCloseable {

    // Usually one per application
    private DomainParticipant participant = null;
    private Publisher publisher = null;
    private Topic topic = null;
    private SpeedDataWriter writer = null;
    private BrakeSubscriber brakeSubscriber = null;
    private long startTime = 0;
    private int speedChangeNum = 5;
    private int speedID = 0;

    private double speed = 50; //km/h
    private boolean emergencyShutdown = false;

    private void initializeDDS() {
        // Start communicating in a domain
        participant = Objects.requireNonNull(
                DomainParticipantFactory.get_instance().create_participant(
                        getDomainId(),
                        DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE));

        // A Publisher allows an application to create one or more DataWriters
        publisher = Objects.requireNonNull(
                participant.create_publisher(
                        DomainParticipant.PUBLISHER_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE));

        // Register the datatype to use when creating the Topic
        String typeName = SpeedTypeSupport.get_type_name();
        SpeedTypeSupport.register_type(participant, typeName);

        // Create a Topic with a name and a datatype
        topic = Objects.requireNonNull(
                participant.create_topic(
                        "SpeedTopic",
                        typeName,
                        DomainParticipant.TOPIC_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE));

        // This DataWriter writes data on "Example BreakChangeIntervention" Topic
        writer = (SpeedDataWriter) Objects.requireNonNull(
                publisher.create_datawriter(
                        topic,
                        Publisher.DATAWRITER_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE));

        startTime = System.currentTimeMillis();
    }

    private void initBrakeSubscriber(String[] args){
        brakeSubscriber = new BrakeSubscriber();
        brakeSubscriber.parseArguments(args);
        brakeSubscriber.addShutdownHook();
        brakeSubscriber.initialize();
    }

    private double getRandomNumber(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    private double changeSpeed(boolean accelarate){
        speed = accelarate ? speed + 2 : speed - 2;
        speed = speed > 200 ? 200 : speed; // limiting max speed to 200 km/h
        speed = speed < 0 ? 0 : speed; // limiting min speed to 0 km/h
        return speed;
    }
    private void runApplication(String[] args) {
        initializeDDS();
        initBrakeSubscriber(args);

        Speed data = new Speed();

        while(!isShutdownRequested()){

            boolean accelarate = getRandomNumber(0.0, 1.0) >= 0.5;

            for (int samplesWritten = 0; !isShutdownRequested()
                    && samplesWritten < speedChangeNum; samplesWritten++) {

                double speedAfterBrake = brakeSubscriber.readBrake();
                //-1es nullazas bekavarhat!!!

                if(speedAfterBrake == -1){
                    if(!emergencyShutdown){
                        data.speed = changeSpeed(accelarate);
                    }
                }else if(speedAfterBrake == -2){
                    // here might tweek this to be more realistic
                    //shutdown után csak lassítani fogunk szóval valahogy ez kellene
                    //
                    emergencyShutdown = true;
                    //speed = 0;
                    data.speed = 0;
                    data.id = Integer.toString(speedID++);
                    data.message = "SHUTDOWN";
                    data.timestamp = System.currentTimeMillis() - startTime;
                    System.out.println("SHUTDOWN!!");
                    writer.write(data, InstanceHandle_t.HANDLE_NIL);
                } else if (speedAfterBrake == -3) {
                    emergencyShutdown = false;
                    data.speed = speed;

                }else{
                    speed = speedAfterBrake;
                    data.speed = speed;
                }
                if(!emergencyShutdown){
                    // Modify the data to be written here
                    data.id = Integer.toString(speedID++);
                    data.message =  speedAfterBrake == -3 ? "RESET" : "SUCCESS";
                    data.timestamp = System.currentTimeMillis() - startTime;

                    System.out.println("Writing Brake, count " + speedID);

                    writer.write(data, InstanceHandle_t.HANDLE_NIL);
                }

                try {
                    final long sendPeriodMillis = 100; // ~0.2 second
                    Thread.sleep(sendPeriodMillis);
                } catch (InterruptedException ix) {
                    System.err.println("INTERRUPTED");
                    break;
                }
            }
        }
    }

    @Override
    public void close() {
        // Delete all entities (DataWriter, Topic, Publisher, DomainParticipant)
        if (participant != null) {
            participant.delete_contained_entities();

            DomainParticipantFactory.get_instance()
                    .delete_participant(participant);
        }
    }

    public static void main(String[] args) {
        // Create example and run: Uses try-with-resources,
        // publisherApplication.close() automatically called
        try (SpeedPublisher publisherApplication = new SpeedPublisher()) {
            publisherApplication.parseArguments(args);
            publisherApplication.addShutdownHook();
            publisherApplication.runApplication(args);
        }

        // Releases the memory used by the participant factory. Optional at application exit.
        DomainParticipantFactory.finalize_instance();
    }
}