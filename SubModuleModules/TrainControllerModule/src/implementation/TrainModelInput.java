package implementation;

public class TrainModelInput {

    private final double actualVelocity;
    private final double commandedVelocity;
    private final double authority;
    private final String beacon;
    private final double speedLimit;

    public TrainModelInput(double actualSpeed, double commandedSpeed, double currentSpeedLimit, double currentAuthority, String currentBeacon){

        actualVelocity = actualSpeed;
        commandedVelocity = commandedSpeed;
        authority = currentAuthority;
        beacon = currentBeacon;
        speedLimit = currentSpeedLimit;
    }

    public double getActualSpeed(){
        return actualVelocity;
    }

    public double getCommandedVelocity(){
        return commandedVelocity;
    }

    public double getAuthority(){ return authority; }

    public String getBeacon(){ return beacon; }

    public double getSpeedLimit(){ return speedLimit; }
}
