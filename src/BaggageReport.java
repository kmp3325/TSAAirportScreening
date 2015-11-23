public class BaggageReport {

    private final Passenger passenger;
    private final boolean passed;

    public BaggageReport(Passenger passenger, boolean passed) {
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
