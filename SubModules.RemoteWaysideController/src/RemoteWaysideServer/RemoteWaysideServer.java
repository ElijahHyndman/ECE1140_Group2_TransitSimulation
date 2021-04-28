package RemoteWaysideServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

/** handles all of the network setup to make a RemoteWaysideService available to the clients through stubs.
 *  Implements Runnable so RemoteWaysideServer can be launched on a new thread
 *
 *  Goal: Make the RMI stub available over the network so Client may use Remote Invocation to control the Wayside Controller on the Raspberry pi
 *
 *  Note About IP Addresses:
 *  Must be configured with the correct IP Address which other computers on your network may refer to your computer using. If
 *  you instantiate your RemoteWaysideServer on a machine with IP "192.169.0.143" but provide an IP in the constructor of "192.168.0.1" then
 *  your server will never be findable.
 *
 *  System Layout:
 *  RemoteWaysideController (on Laptop) object that can be treated as if it were a local WaysideController, but instead manages a remote connection to a remote WaysideControllerService
 (this)RemoteWaysideServer     (on Pi) hosts the RemoteWaysideService and makes it available for the Client across a network.
 *  RemoteConnection    (on Laptop) manages the Client->Server connection for the client. If successful, provides the stub for remote objects
 *  RemoteWaysideStub   (created on Pi, used by Laptop) defines the methods which a Client may invoke remotely
 *  RemoteWaysideService    (on Pi) implements the methods defined by the RemoteWaysideStub
 *  RemoteTrackStub     (created on Laptop, used by Pi) defines the methods which a remote WaysideController may invoke remotely
 *  RemoteTrack         (on Laptop) implements the methods defined by the RemoteTrackStub
 *
 *  Useful References:
 *  RMIRegistry not started reference: https://stackoverflow.com/questions/1823305/rmi-connection-refused-with-localhost
 *
 * @author Elijah
 */
public class RemoteWaysideServer implements Runnable {


    /***********************************************************************************************************************/
    /** Default Members
     *
     * DEFAULT_COMMUNICATION_PORT   int, the default port which a server will create its RMIRegistry onto for binding the RMI Stubs
     * DEFAULT_SERVER_IP    String, if an ip address is not provided, then the server will run with the loop-back 127.0.0.1 IP address for local use
     * SERVICE_STUB_NAME    String, the String Key which the RemoteWaysideService's stub shall be bound to in the RMIRegistry.
     *                          -This is the String Key which the client shall use to locate the service's stub for remote invocation
     */
    private final int DEFAULT_COMMUNICATION_PORT = 5099;
    private final String DEFAULT_SERVER_IP = "127.0.0.1";
    private final String SERVICE_STUB_NAME = "Service";

    /** Members
     *
     * service  RemoteWaysideService, the local service object which we are hosting and whose RMI stub we are providing to the client.
     */
    private RemoteWaysideService service;
    private String deliminator = "***************************************************";

    /** Network Members
     *
     * registry     Registry, RMIRegistry where we will bind the RMIStub of the RemoteWaysideService to
     * communicationPort int, Network Port on the machine where we will create the RMIRegistry for Client access
     * serverIP     String, user provided String of this host machine. Must be correct, shall not be checked.
     *                  -Difficult to find correct private IP address from within Java
     */
    private static Registry registry = null;
    private int communicationPort = DEFAULT_COMMUNICATION_PORT;
    private String serverIP = DEFAULT_SERVER_IP;
    /***********************************************************************************************************************/

    /** default constructor creates a new RemoteWaysideServer object on localhost which can be run on a new thread.
     * @before nothing
     * @after registry has been created on communication port and stored locally
     * @after system IP address for java has been set so machine is findable
     * @after RemoteWaysideService has been created and ready for hosting
     */
    public RemoteWaysideServer() {
        try {
            // Must configure system IP Address for java (so client may reach this machine using java)
            // Create the service which we are hosting
            // Get the RMIRegistry located on the communicationPort
            System.setProperty("java.rmi.server.useLocalHostname","true");
            service = new RemoteWaysideService();

            System.out.println(deliminator);
            // Retrieve registry
            registry = getRMIRegistry();

            // Export stub for client use
            makeStubAvailable(communicationPort);
            System.out.println(deliminator);
        } catch (Exception e) {
            System.err.println("Failed to construct new RemoteWaysideServer");
            e.printStackTrace();
        }
        System.out.println("Server Ready");
    }


    /** creates a new RemoteWaysideServer object which can be run on a new thread.
     */
    public RemoteWaysideServer(String IPString, int communicationPort) {
        try {
            // Configure system IP Address for java (so client may reach this machine using java)
            // Create the service which we are hosting
            // Get the RMIRegistry located on the communicationPort
            this.serverIP = IPString;
            this.communicationPort = communicationPort;
            System.setProperty("java.rmi.server.hostname",serverIP);
            service = new RemoteWaysideService();

            System.out.println(deliminator);

            // Retrieve registry
            registry = getRMIRegistry();

            // Export stub for client use
            makeStubAvailable(communicationPort);
            System.out.println(deliminator);
        } catch (Exception e) {
            System.err.println("Failed to create new RemoteWaysideService during RemoteWaysideServer construction");
            e.printStackTrace();
        }
        System.out.println("Server Ready");
    }


    /** the RMI registry is where stubs are stored by the server and retrieved from by the client.
     * RMI Registries exist on a port number, and an RMI registry may or may not already exist on that port
     *
     * @before RMIRegistry may already exist on the communication port
     * @after RMIRegistry exists on the communication port and a reference to it has been stored locally, or an exception has been thrown
     */
    public Registry getRMIRegistry() throws Exception {
        // Retrieve RMI Registry object from the port
        System.out.printf("Registry operation (port %d): ",communicationPort);
        try {                                                               // Try
            // If we can create registry...
            registry = LocateRegistry.createRegistry(communicationPort);
            System.out.println("(creation: success)");

        } catch (ExportException exp) {                                     // Catch 1: Registry already exists
            // If we cannot create registry, but an RMI registry exists there...
            System.out.printf("(creation: failure) ");
            try {
                registry = LocateRegistry.getRegistry(communicationPort);
                System.out.println("(retrieval: success)");
            } catch (Exception e) {
                System.out.println("(retrieval: failure)");
                e.printStackTrace();
                return null;
            }
            System.out.println("RMI Registry successfully retrieved");

        } catch (Exception e) {                                         // Catch 2: general exceptions
            // Cannot create one and cann retrieve
            System.out.println("(total failure)");
            throw new Exception("RMI Registry cannot be created in communication port");
        }
        System.out.printf("===%s\n",registry.toString());
        return registry;
    }


    /** creates stub from "service" member, loads stub onto specified TCP socketPort number (default = 1099);
     *
     * @before RMIRegistry exits and has been stored in class member.
     * @after RemoteWaysideStub for the hosted RemoteWaysideService has been bound to the RMI Registry
     */
    public void makeStubAvailable(int socketPort) throws Exception {
        // Quit if RMI Registry is invalid
        if(registry == null) {
            System.err.println("Server attempted to export stub but the RMI Registry fetched is null.");
            throw new Exception("Attempted to export RMI Stub before RMI Registry has been fetched");
        }

        // Create RMI Stub
        RemoteWaysideStub stub = null;
        int useAnyPortAvailable = 0;
        try {
            // Export RMI Stub to specified TCP port
            stub = (RemoteWaysideStub) UnicastRemoteObject.exportObject(service,socketPort);
            System.out.printf("Stub operation (port %d): Success\n", socketPort);
            System.out.printf("===%s\n", stub.toString());
        } catch (Exception e) {
            System.out.printf("Stub operation (port %d): failure\n", socketPort);
            e.printStackTrace();
            throw new Exception("Failure to create RMI Stub");
        }
        // Bind RMI stub to RMI Registry
        try {
            registry.rebind(SERVICE_STUB_NAME, stub);
            System.out.println("Stub Binding Operation: Success");
        } catch (Exception e){
            System.out.printf("Stub Binding operation (port %d): failure\n",socketPort);
            throw new Exception("Failure to bind RMI stub to RMI Registry");
        }
    }

    public WaysideController.WaysideController getController() throws RemoteException {
        return service.getController();
    }


    @Override
    public void run() {

    }

    public static void main(String[] args) {
        new RemoteWaysideServer("192.168.0.111",5099);
    }
}