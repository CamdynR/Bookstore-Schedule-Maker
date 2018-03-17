public class Cashier {
    public String name;
    public double startTime;
    public double endTime;
    public double breakTime;
    public int[] allocations;

    public Cashier(String name, double startTime, double endTime, double breakTime){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakTime = breakTime;
        this.allocations = new int[(int)((endTime - startTime)/0.25 + 1)];
    }

    public Cashier(String name, double startTime, double endTime){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakTime = 0;
        this.allocations = new int[(int)((endTime - startTime)/0.25 + 1)];
    }
}
