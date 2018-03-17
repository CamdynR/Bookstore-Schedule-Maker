/* This is the Register class, used to make Register objects to keep track of who's assigned to it (As an
 * array of Cashiers), when they are assigned to it, and what time the register opens and closes.
 *
 * <p>Bugs: None (that I know of)
 *
 * @author Camdyn Rasque, Published on March 16th, 2018
 */

public class Register {
    public int regNumber;
    public Cashier[] shifts;
    public double openTime;
    public double closeTime;

    /** This initializes the Register. Takes in the time it opens and closes, creates and array of that
     *  size, and initializes the array with "empty" cashiers.
     *
     *  @Param openTime, the time the register will be opened, as a double
     *  @Param closeTime, the time the register will be closed, as a double
     *  @Parma regNumber, the number of the register that will be assigned to the regNumber instance var
     */
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

    /** This method takes in a cashier, and the times that they are assigned to this register object,
     *  and adds them to the Cashier array.
     *
     *  @Param cashier is the cashier object that will be assigned to the array
     *  @Param startTime is the initial start time, as a double
     *  @Param endTime is the time that they will leave this register
     *  @Return boolean, this method returns true if it was successful and false if the times
     *          are out of bounds or have been assigned already
     */
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
                return false;
            } else {
                System.err.println("Input end time too late, cashier leaves work at "
                        + Schedule_Maker.timeToString(cashier.endTime) + ", input time was "
                        + Schedule_Maker.timeToString(endTime) + ".");
                return false;
            }
        }
        return true;
    }

    /** This method takes in a time in military form as a double, and outputs the corresponding index
     *  of that time in the Cashier array the object has. Assumes time is given in increments of 0.25
     *  (Essentially increments of 15 minutes - It's easier to deal with doubles)
     *
     * @Param time is the input time as a double (e.g. 2:15pm -> 14.25)
     * @Return int, the index of the given time on the Cashier array for this object
     */
    public int timeToIndex(double time){
        if(time > this.closeTime){
            System.err.println("Invalid input, register will be closed before that time.");
            return -1;
        }
        time -= this.openTime;
        int index = (int)(time / 0.25);
        return index;
    }

    /** This method simply prints the register number and all of the allocations to itself
     */
    public void print(){
        System.out.println("Register #" + this.regNumber);
        for(int i = 0; i < this.shifts.length; i++){
            String time = Schedule_Maker.timeToString(openTime + 0.25*i);
            System.out.println(time + ": " + this.shifts[i].name);
        }
    }
}
