/* This class is the man class in the schedule making program. It essentially takes in
 * what times the store will open and close, which registers will be opened, and which
 * employees will be working and from what time to what time. With this information, it
 * will output the allocations of who will be assigned to which register, and from what
 * time to what time.
 *
 * <p>Bugs: Currently does not do any of what it's supposed to do. It successfully takes
 * in all of the input through. Still working on how to do the actual allocation bit
 * itself.
 *
 * @author Camdyn Rasque, Published on March 16th, 2018
 */

import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class Schedule_Maker {

    private static int[] regCS = {106, 107};
    private static int[] regBooks = {114};
    private static int[] regPerks = {115};
    private static int[] regLibraryWalk = {116, 117, 118, 119, 120, 121};
    private static int[] regSupplies = {122, 123};
    private static int[] regWomens = {124, 125};

    /* This method will convert a time (e.g. 7:45am) into military time, as a double
    *  (7:45am would become 7.75; Assumes time is in increments of 15 minutes)
    *
    *  @Param time is the input time in string form
    *  @Return the time as a double
    * */
    private static double stringToTime(String time){
        int hours, minutes;
        int ampmIndex = 0;
        double finalTime = 0;
        if(time.charAt(1) != ':'){
            hours = 10*(time.charAt(0) - '0') + (time.charAt(1) - '0');
            minutes = 10*(time.charAt(3) - '0') + (time.charAt(4) - '0');
        } else {
            hours = time.charAt(0) - '0';
            minutes = 10*(time.charAt(2) - '0') + (time.charAt(3) - '0');
        }
        if(time.length() == 7){
            ampmIndex = 5;
        } else if(time.length() == 6){
            ampmIndex = 4;
        }
        if(time.charAt(ampmIndex) == 'p'){
            if(hours < 12){
                hours += 12;
            }
        }
        finalTime += hours;
        if(minutes == 15){
            finalTime += 0.25;
        } else if(minutes == 30){
            finalTime += 0.50;
        } else if(minutes == 45){
            finalTime += 0.75;
        }
        return finalTime;
    }


    /* This method will convert a time that's in military time as a double, into a string
     *  (14.25 would become 2:15pm; Assumes time is in increments of 0.25 (15 minutes))
     *
     *  @Param time is the input time in string form
     *  @Return the time as a double
     * */
    public static String timeToString(double time){
        String finalTime = null;
        if(time >= 24.00){
            System.err.println("Time too large");
            return null;
        } else if(time < 0) {
            System.err.println("Time must be positive");
            return null;
        }
        int hours = (int)time;
        double decMinutes = time - hours;
        if(decMinutes != 0 && decMinutes != 0.25 && decMinutes != 0.50 && decMinutes != 0.75){
            System.err.println("Please only enter either 0.25, 0.50, or 0.75 for minutes");
            return null;
        }
        int minutes = 0;
        if(decMinutes == 0.25){
            minutes = 15;
        } else if(decMinutes == 0.50){
            minutes = 30;
        } else if(decMinutes == 0.75){
            minutes = 45;
        }
        if(hours > 12){
            hours = hours % 12;
            if(hours == 0){
                hours += 12;
            }
            finalTime = hours + ":" + minutes;
            if(minutes == 0){
                finalTime += minutes;
            }
            finalTime += "pm";
        } else {
            finalTime = hours + ":" + minutes;
            if(minutes == 0){
                finalTime += minutes;
            }
            finalTime += "am";
        }
        return finalTime;
    }

    /* This method is just to save space, skips numLines number of lines
    *
    * @Param scan is the input scanner, will be used to call nextLine()
    * @Param numLines is the number of lines to be skipped
    */
    private static void skipLines(Scanner scan, int numLines){
        for(int i = 0; i < numLines; i++){
            scan.nextLine();
        }
    }

    /* This method returns an array that is the intersection of two arrays
    *
    * @Param array1 is the first array to be input
    * @Param array2 is the second array to be input
    * @Return an array that is all of the elements the two arrays have in common
    * */
    private static int[] intersection(int[] array1, int[] array2){
        int i, j, count = 0;
        int[] newArr = new int[Math.max(array1.length, array2.length)];
        for(i = 0; i < array1.length; i++){
            for(j = 0; j < array2.length; j++){
                if(array1[i] == array2[j]){
                    newArr[count] = array1[i];
                }
            }
        }
        int[] returnArr = new int[newArr.length];
        for(i = 0; i < newArr.length; i++){
            returnArr[i] = newArr[i];
        }
        return returnArr;
    }

    /* This method will give a List<Cashier> of all of the people who are currently at
    * work at the given time.
    *
    * @Param time is the input time which will be checked
    * @Param inputList is the list of every cashier who is working that day and the
    *        times that they are working
    * @Return List<Cashier> will be the list of all of the cashiers working at the given
    *         time
    * */
    public static List<Cashier> atWorkNow(double time, List<Cashier> inputList){
        List<Cashier> atWork = new ArrayList<>();
        for(int i = 0; i < inputList.size(); i++){
            if(inputList.get(i).startTime < time && inputList.get(i).endTime > time){
                atWork.add(inputList.get(i));
            }
        }
        return atWork;
    }

    /* This method will take a register number, and find it's index in the given
    *  List of registers
    *
    *  @Param regNum is the register number to be found
    *  @Param input is the list of registers that will be searched
    *  @Return int is the index of that register in the list
    * */
    public static int indexOfRegister(List<Register> input, int regNum){
        int indexOfReg = -1;
        for(int i = 0; i < input.size(); i++){
            if(input.get(i).regNumber == regNum){
                indexOfReg = i;
            }
        }
        return indexOfReg;
    }

    // The main method of the whole program, this is where all of the allocations happen
    // Throws a FileNotFoundException since it will be taking in input from a template file
    // I created, called Schedule_Maker_Template.txt
    public static void main(String[] args) throws FileNotFoundException {
        String FILE_PATH = "./src/Schedule_Maker_Template.txt";
        Scanner scan = new Scanner(new FileInputStream(FILE_PATH));
        List<Register> registerList = new ArrayList<>();
        List<Cashier> cashierList = new ArrayList<>();
        int[] regNums;
        List<int[]> regZones = new ArrayList<>();
        List<Cashier> tempList = new ArrayList<>();
        int numReg, numCashiers, randCashier, randReg, i, j;
        String openStr, closeStr, tempName;
        double openTime, closeTime;
        Random rand = new Random();

        // Grabs the Register Opening Times
        skipLines(scan, 2);
        openStr = scan.nextLine();
        openTime = stringToTime(openStr);

        // Grabs the Register Closing Times
        skipLines(scan, 2);
        closeStr = scan.nextLine();
        closeTime = stringToTime(closeStr);

        // Grabs the number of Registers to open, initializes Reg array
        skipLines(scan, 3);
        numReg = scan.nextLine().charAt(0) - '0';
        regNums = new int[numReg];

        // Grabs the Register numbers, makes Register objects and assigns to array
        skipLines(scan, 3);
        for(i = 0; i < numReg; i++){
            Register newReg = new Register(openTime, closeTime, scan.nextInt());
            if(newReg.regNumber == 114){
                newReg.closeTime = 16.00;
            }
            registerList.add(newReg);
            regNums[i] = registerList.get(i).regNumber;
        }

        // Grabs the number of Registers to open, initializes Reg array
        skipLines(scan, 3);
        numCashiers = scan.nextInt();

        // Grabs the Cashiers, makes Cashier objects and assigns to array
        skipLines(scan, 4);
        for(i = 0; i < numCashiers; i++){
            scan.nextLine();
            tempName = scan.next() + " " + scan.next();
            Cashier newCashier = new Cashier(tempName, stringToTime(scan.next()),
                                            stringToTime(scan.next()));
            cashierList.add(newCashier);
        }

        // Checks the zones with multiple registers to see which are open
        regLibraryWalk = intersection(regLibraryWalk, regNums);
        regSupplies = intersection(regSupplies, regNums);
        regWomens = intersection(regWomens, regNums);

        regZones.add(regBooks);
        regZones.add(regPerks);
        regZones.add(regLibraryWalk);
        regZones.add(regSupplies);
        regZones.add(regWomens);

        // regCS, regBooks, regPerks, regLibraryWalk, regSupplies, regWomens
        for(i = 0; i < regZones.size(); i++){
            tempList = atWorkNow(openTime, cashierList);
            //randCashier = rand.nextInt(tempList.length);
            for(j = 0; j < tempList.size(); j++){
                randReg = rand.nextInt(regZones.get(i).length);

            }
        }
    }
}
