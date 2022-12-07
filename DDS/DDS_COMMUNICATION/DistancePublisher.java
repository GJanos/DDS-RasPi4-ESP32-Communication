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


import java.util.Objects;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;

/**
 * Simple example showing all Connext code in one place for readability.
 */
public class DistancePublisher extends Application implements AutoCloseable {

    // Usually one per application
    private DomainParticipant participant = null;
    private Publisher publisher = null;
    private Topic topic = null;
    private DistanceDataWriter writer = null;
    private long startTime = 0;
    private int samplesWritten = 0;

    public void initializeDDS() {
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
        String typeName = DistanceTypeSupport.get_type_name();
        DistanceTypeSupport.register_type(participant, typeName);

        // Create a Topic with a name and a datatype
        topic = Objects.requireNonNull(
                participant.create_topic(
                        "DistanceTopic",
                        typeName,
                        DomainParticipant.TOPIC_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE));

        // This DataWriter writes data on "Example DistanceChangeIntervention" Topic
        writer = (DistanceDataWriter) Objects.requireNonNull(
                publisher.create_datawriter(
                        topic,
                        Publisher.DATAWRITER_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE));

        startTime = System.currentTimeMillis();
    }
    public void publishDistance(double serialReadDistance) {
        Distance data = new Distance();
        // Create data sample for writing
        data.message = serialReadDistance == -1 ? "Invalid distance" : "Success";
        data.id = Integer.toString(samplesWritten++);
        data.distance = serialReadDistance;
        data.timestamp = System.currentTimeMillis() - startTime;
        System.out.println("Writing Distance, count " + samplesWritten);
        writer.write(data, InstanceHandle_t.HANDLE_NIL);
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
}
