public class Register {
    public int regNumber;
    public Cashier[] shifts;
    public double openTime;
    public double closeTime;

    public Register(double openTime, double closeTime, int regNumber){
        this.regNumber = regNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        double arraySize = ((closeTime - openTime)/0.25) + 1;
        this.shifts = new Cashier[(int)arraySize];
        Cashier empty = new Cashier("empty", openTime, closeTime, 0);
        for(int i = 0; i < this.shifts.length; i++){
            this.shifts[i] = empty;
        }
    }

    public boolean assignCashier(Cashier cashier, double startTime, double endTime){
        for(int i = timeToIndex(startTime); i < timeToIndex(endTime); i++){
            if(!this.shifts[i].name.equals("empty")){
                return false;
            }
        }
        if(startTime >= cashier.startTime && endTime <= cashier.endTime){
            int indexStart = (int)((startTime-this.openTime)/0.25);
            int indexEnd = (int)(indexStart + ((endTime - startTime)/0.25));
            for(int i = indexStart; i <= indexEnd; i++){
                this.shifts[i] = cashier;
            }
        } else {
            if(startTime >= cashier.startTime){
                System.err.println("Input start time too early, cashier starts at "
                                    + Schedule_Maker.timeToString(cashier.startTime) + ", input time was "
                                    + Schedule_Maker.timeToString(startTime) + ".");
            } else {
                System.err.println("Input end time too late, cashier leaves work at "
                        + Schedule_Maker.timeToString(cashier.endTime) + ", input time was "
                        + Schedule_Maker.timeToString(endTime) + ".");
            }
        }
        return true;
    }

    public int timeToIndex(double time){
        if(time > this.closeTime){
            System.err.println("Invalid input, register will be closed before that time.");
            return -1;
        }
        time -= this.openTime;
        int index = (int)(time / 0.25);
        return index;
    }
}
