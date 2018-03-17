/* This is the Cashier class, it's rather simple. Keeps track of the cashier's name, time they
 * will arrive an leave work, the time of their break (which will be assigned at a later date),
 * and an array of the register numbers they will be at.
 *
 * <p>Bugs: None (that I know of)
 *
 * @author Camdyn Rasque, Published on March 16th, 2018
 */

public class Cashier {
    public String name;
    public double startTime;
    public double endTime;
    public double breakTime;
    public int[] allocations;

    /* Initializes the Cashier object, takes in a break time. Might get rid of this
    *  as I don't think I want to assign the break time at initialization*/
    public Cashier(String name, double startTime, double endTime, double breakTime){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakTime = breakTime;
        this.allocations = new int[(int)((endTime - startTime)/0.25 + 1)];
    }

    /* Initializes the Cashier object just like above, but without the break time.
    *  Break will be assigned later*/
    public Cashier(String name, double startTime, double endTime){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakTime = 0;
        this.allocations = new int[(int)((endTime - startTime)/0.25 + 1)];
    }
}
