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
//import com.rti.dds.infrastructure.ConditionSeq;
//import com.rti.dds.infrastructure.Duration_t;
//import com.rti.dds.infrastructure.RETCODE_NO_DATA;
//import com.rti.dds.infrastructure.RETCODE_TIMEOUT;
//import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
//import com.rti.dds.infrastructure.StatusKind;
//import com.rti.dds.infrastructure.WaitSet;
//import com.rti.dds.subscription.InstanceStateKind;
//import com.rti.dds.subscription.ReadCondition;
//import com.rti.dds.subscription.SampleInfo;
//import com.rti.dds.subscription.SampleInfoSeq;
//import com.rti.dds.subscription.SampleStateKind;
//import com.rti.dds.subscription.Subscriber;
//import com.rti.dds.subscription.ViewStateKind;
//import com.rti.dds.topic.Topic;
//
///**
// * Simple example showing all Connext code in one place for readability.
// */
//public class BrakeSubscriber extends Application implements AutoCloseable {
//
//    private DomainParticipant participant = null; // Usually one per application
//    private BreakDataReader reader = null;
//    private final BreakSeq dataSeq = new BreakSeq();
//    private final SampleInfoSeq infoSeq = new SampleInfoSeq();
//
//    private int processData() {
//        int samplesRead = 0;
//
//        try {
//            // Take available data from DataReader's queue
//            reader.take(dataSeq, infoSeq,
//                    ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
//                    SampleStateKind.ANY_SAMPLE_STATE,
//                    ViewStateKind.ANY_VIEW_STATE,
//                    InstanceStateKind.ANY_INSTANCE_STATE);
//
//            for (int i = 0; i < dataSeq.size(); ++i) {
//                SampleInfo info = infoSeq.get(i);
//
//                if (info.valid_data) {
//                    System.out.println("Received" + dataSeq.get(i));
//                }
//                samplesRead++;
//            }
////            SampleInfo info = infoSeq.get(0);
////
////            if (info.valid_data) {
////                System.out.println("Received" + dataSeq.get(0));
////            }
//
//        } catch (RETCODE_NO_DATA noData) {
//            // No data to process, not a problem
//        } finally {
//            // Data loaned from Connext for performance. Return loan when done.
//            reader.return_loan(dataSeq, infoSeq);
//        }
//
//        return samplesRead;
//    }
//
//    private void runApplication() {
//        // Start communicating in a domain
//        participant = Objects.requireNonNull(
//                DomainParticipantFactory.get_instance().create_participant(
//                        getDomainId(),
//                        DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        // A Subscriber allows an application to create one or more DataReaders
//        Subscriber subscriber = Objects.requireNonNull(
//                participant.create_subscriber(
//                        DomainParticipant.SUBSCRIBER_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        // Register the datatype to use when creating the Topic
//        String typeName = BreakTypeSupport.get_type_name();
//        BreakTypeSupport.register_type(participant, typeName);
//
//        // Create a Topic with a name and a datatype
//        Topic topic = Objects.requireNonNull(
//                participant.create_topic(
//                        "BrakeTopic",
//                        typeName,
//                        DomainParticipant.TOPIC_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        // This DataReader reads data on "Example DistanceChangeIntervention" Topic
//        reader = (BreakDataReader) Objects.requireNonNull(
//                subscriber.create_datareader(
//                        topic,
//                        Subscriber.DATAREADER_QOS_DEFAULT,
//                        null, // listener
//                        StatusKind.STATUS_MASK_NONE));
//
//        // Create ReadCondition that triggers when data in reader's queue
//        ReadCondition condition = reader.create_readcondition(
//                SampleStateKind.ANY_SAMPLE_STATE,
//                ViewStateKind.ANY_VIEW_STATE,
//                InstanceStateKind.ANY_INSTANCE_STATE);
//
//        // WaitSet will be woken when the attached condition is triggered, or timeout
//        WaitSet waitset = new WaitSet();
//        waitset.attach_condition(condition);
//        final Duration_t waitTimeout = new Duration_t(1, 0);
//
//        int samplesRead = 0;
//        ConditionSeq activeConditions = new ConditionSeq();
//
//        // Main loop. Wait for data to arrive and process when it arrives
//        while (!isShutdownRequested() && samplesRead < getMaxSampleCount()) {
//            try {
//                // Wait fills in activeConditions or times out
//                waitset.wait(activeConditions, waitTimeout);
//
//                // Read condition triggered, process data
//                samplesRead += processData();
//
//            } catch (RETCODE_TIMEOUT timeout) {
//                // No data received, not a problem
//                System.out.printf("No data after %d seconds.%n", waitTimeout.sec);
//            }
//        }
//    }
//
//    @Override
//    public void close() {
//        // Delete all entities (DataReader, Topic, Subscriber, DomainParticipant)
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
//        // subscriberApplication.close() automatically called
//        try (BrakeSubscriber subscriberApplication = new BrakeSubscriber()) {
//            subscriberApplication.parseArguments(args);
//            subscriberApplication.addShutdownHook();
//            subscriberApplication.runApplication();
//        }
//
//        // Releases the memory used by the participant factory. Optional at application exit.
//        DomainParticipantFactory.finalize_instance();
//    }
//}
//
/*
 * (c) Copyright, Real-Time Innovations, 2020.  All rights reserved.
 * RTI grants Licensee a license to use, modify, compile, and create derivative
 * works of the software solely for use with RTI Connext DDS. Licensee may
 * redistribute copies of the software provided that all such copies are subject
 * to this license. The software is provided "as is", with no warranty of any
 * type, including any warranty for fitness for any purpose. RTI is under no
 * obligation to maintain or support the software. RTI shall not be liable for
 * any incidental or consequential damages arising out of the use or inability
 * to use the software.
 */

import java.util.Objects;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.ConditionSeq;
import com.rti.dds.infrastructure.Duration_t;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.RETCODE_TIMEOUT;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.infrastructure.WaitSet;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.ReadCondition;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

/**
 * Simple example showing all Connext code in one place for readability.
 */
public class BrakeSubscriber extends Application implements AutoCloseable {

    private DomainParticipant participant = null; // Usually one per application
    private BreakDataReader reader = null;
    private final BreakSeq dataSeq = new BreakSeq();
    private final SampleInfoSeq infoSeq = new SampleInfoSeq();
    private ReadCondition condition = null;
    private WaitSet waitset = null;
    private Duration_t waitTimeout = null;
    private ConditionSeq activeConditions = null;
    private Subscriber subscriber = null;
    private Topic topic = null;

    public void initialize(){
        // Start communicating in a domain
        participant = Objects.requireNonNull(
                DomainParticipantFactory.get_instance().create_participant(
                        getDomainId(),
                        DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE));

        // A Subscriber allows an application to create one or more DataReaders
        subscriber = Objects.requireNonNull(
                participant.create_subscriber(
                        DomainParticipant.SUBSCRIBER_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE));

        // Register the datatype to use when creating the Topic
        String typeName = BreakTypeSupport.get_type_name();
        BreakTypeSupport.register_type(participant, typeName);

        // Create a Topic with a name and a datatype
        topic = Objects.requireNonNull(
                participant.create_topic(
                        "BrakeTopic",
                        typeName,
                        DomainParticipant.TOPIC_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE));

        // This DataReader reads data on "Example DistanceChangeIntervention" Topic
        reader = (BreakDataReader) Objects.requireNonNull(
                subscriber.create_datareader(
                        topic,
                        Subscriber.DATAREADER_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE));
        // Create ReadCondition that triggers when data in reader's queue
        condition = reader.create_readcondition(
                SampleStateKind.ANY_SAMPLE_STATE,
                ViewStateKind.ANY_VIEW_STATE,
                InstanceStateKind.ANY_INSTANCE_STATE);

        // WaitSet will be woken when the attached condition is triggered, or timeout
        waitset = new WaitSet();
        waitset.attach_condition(condition);
        waitTimeout = new Duration_t(0, 100000000); //~0.2 sec
        activeConditions = new ConditionSeq();
    }

    //-1 invalid data
    //-2 shutdown
    //-3 reset
    public double readBrake() {
        //might make enums for -1,-2,-3...
        double speedAfterBrake = -1;

        try {
            // Wait fills in activeConditions or times out
            waitset.wait(activeConditions, waitTimeout);

            try {
                // Take available data from DataReader's queue
                reader.take(dataSeq, infoSeq,
                        ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
                        SampleStateKind.ANY_SAMPLE_STATE,
                        ViewStateKind.ANY_VIEW_STATE,
                        InstanceStateKind.ANY_INSTANCE_STATE);

                SampleInfo info = infoSeq.get(0);

                if (info.valid_data) {
                    if(dataSeq.get(0).message.equals("SHUTDOWN")){
                        speedAfterBrake = -2;
                    } else if (dataSeq.get(0).message.equals("RESET")) {
                        speedAfterBrake = -3;
                    }
                    else{
                        speedAfterBrake = dataSeq.get(0).amount;
                    }
                }
            } catch (RETCODE_NO_DATA noData) {
                // No data to process, not a problem
            } finally {
                // Data loaned from Connext for performance. Return loan when done.
                reader.return_loan(dataSeq, infoSeq);
            }

        } catch (RETCODE_TIMEOUT timeout) {
            // No data received, not a problem
            System.out.printf("No data after %d seconds.%n", waitTimeout.sec);
        }
        return speedAfterBrake;
    }

    @Override
    public void close() {
        // Delete all entities (DataReader, Topic, Subscriber, DomainParticipant)
        if (participant != null) {
            participant.delete_contained_entities();

            DomainParticipantFactory.get_instance()
                    .delete_participant(participant);
        }
    }
}

