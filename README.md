# java-multi-threaded-server
This project implements a Java-based application to simulate a
client/server architecture for aggregating sensor data. The aim is to build
a scalable system where multiple simulated sensing devices (clients)
send periodic environmental data to a central server, which processes
and visualizes the data in real time using a graphical interface.
The system leverages Java Swing for GUIs, Java sockets for network
communication, and object serialization for efficient data transfer. The
client allows users to manually adjust sensor values and dynamically
change the device name. The server aggregates the data, performs
basic analytics such as averages, and displays them in a structured,
real-time interface. The solution is designed to handle multiple client
connections concurrently using a multi-threaded server

2.1 Overall Architecture
The system follows a client/server model:
● Clients represent individual sensor devices, each running as an
independent instance. They collect data from three sensors (e.g.,
temperature, humidity, and pressure) and transmit the readings
periodically to the server. Clients can also modify sensor values or
change their device name during runtime.
● The Server acts as a central data aggregator. It listens for
incoming client connections, receives data, performs analytics
(such as averages and historical tracking), and displays the results
in a real-time GUI.
2.2 Communication Flow
The communication between the client and the server is implemented
over TCP using Java sockets. Data is encapsulated in serialized
SensorData objects containing the device name and sensor readings.
Serialization ensures efficient and structured transmission.
Flow Diagram
1. The client connects to the server through a socket.
2. The client sends SensorData objects at regular intervals or when
values change.
3. The server receives and deserializes the data.
4. The server updates its GUI with the received information and
performs any required analytics.
2.3 Class Diagram
The application is divided into the following core classes:
Client-side
1. Client: Handles GUI, data generation, and communication with the
server.
2. SensorData: Represents the message format for data
transmission.
Server-side
1. ThreadedServer: Manages incoming client connections and
delegates each to a separate thread.
2. ThreadedConnectionHandler: Processes communication for
individual clients.
3. ServerGUI: Displays live sensor data and performs analytics (e.g.,
averages, min/max values).
