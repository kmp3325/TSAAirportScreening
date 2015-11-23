public class PassengerReport {

    private final Passenger passenger;
    private final boolean passed;

    public PassengerReport(Passenger passenger, boolean passed) {
        this.passenger = passenger;
        this.passed = passed;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public boolean getPassed() {
        return passed;
    }
}
