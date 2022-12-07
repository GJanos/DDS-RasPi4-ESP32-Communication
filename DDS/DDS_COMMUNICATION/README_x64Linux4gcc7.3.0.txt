Welcome to your first Connext DDS example! 

This example was generated for architecture x64Linux4gcc7.3.0, using the
data type SpeedChangeIntervention from interfaces_2.idl.
This example builds two applications, named SpeedChangeInterventionPublisher
and SpeedChangeInterventionSubscriber.

Simple Example
============
If you have generated this example using:
> rtiddsgen -example <arch> <idl>.idl
This is the simple example. This shows how to create a single DataWriter
and DataReader to send and receive data over Connext DDS.

Advanced Example
===============
If you have generated the advanced example using:
> rtiddsgen -example <arch> -exampleTemplate advanced <idl>.idl
The code is similar to the simple example, with a few key differences:
    - Both examples use WaitSets to block a thread until data is available.
      This is the safest way to get data, because it does not affect any
      middleware threads. In addition, the advanced example installs listeners
      on both the DataReader and DataWriter with callbacks that you can
      implement to accomplish a desired behavior. These listener callbacks
      are triggered for various events, such as discovering a matched
      DataWriter or DataReader. Listener callbacks are called back from
      a middleware thread, which you should not block.
    - The simple example sets is_default_qos=true in the XML file, and creates
      the DDS entities without specifying a profile. However, the advanced
      example sets is_default_qos=false, and specifies the QoS profile to use
      from the XML file when creating DDS entities. is_default_qos=false
      is recommended in a production application.

To Build this example:
======================
 
Build with Ant:
---------------
If you plan to use ant to build, make sure that ant is in your path.
 
From your command shell, in the directory where you generated this example, type:
> ant
 
Build with Eclipse:
-------------------
Import the generated code:
1. From the File menu, choose "Open Projects from File System..."
2. In "Import Projects from File System or Archive", use the "Directory..." button
   to browse to the directory where you generated this example. Select the
   directory and click Finish.
 
After you import, the project builds automatically.
 
Build with makefile:
--------------------
 
Make sure that java and javac are in your path before you start. From your
command shell, type:
> make -f makefile_interfaces_2_x64Linux4gcc7.3.0

To Modify the Data:
===================
To modify the data being sent, in the default package (or module you specified),
open/double-click and edit the SpeedChangeInterventionPublisher.java file
where it says:
/* Modify the instance to be written here */

To Run This Example:
====================
 
Run with Ant:
-------------
Type 
> ant SpeedChangeInterventionPublisher
or
> ant SpeedChangeInterventionSubscriber
 
Run with Eclipse:
-----------------
Select Run > Run and choose either the SpeedChangeInterventionPublisher or SpeedChangeInterventionSubscriber
application.
To run the other application, select Run > Run Configurations and select the
one you want to run. 
Click Run.
The output appears in Eclipse's console window.

Run with makefile:
------------------
 
Make sure that java and javac are in your path before you start. From your
command shell, type:
> make -f makefile_interfaces_2_x64Linux4gcc7.3.0 SpeedChangeInterventionPublisher
> make -f makefile_interfaces_2_x64Linux4gcc7.3.0 SpeedChangeInterventionSubscriber
