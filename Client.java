/*Name: Ankith Tunuguntla
  Date: 11/14/2023
  Course: CSE 123
  TA Name: Jay Dharmadhikari
  Program Name: Disaster Relief*/

import java.util.*;

public class Client {
    private static Random rand = new Random();

    public static void main(String[] args) throws Exception {
        // List<Location> scenario = createRandomScenario(10, 10, 100, 1000, 100000);
        List<Location> scenario = createSimpleScenario();
        System.out.println(scenario);
        
        double budget = 2000;
        Allocation allocation = allocateRelief(budget, scenario);
        printResult(allocation, budget);
    }

    /*Method name: allocateRelief
    Method parameters: 
        -> double budget: stores the budget to be used for allocationof resources
        -> List<Location> sites: stores a list of location objects that contain
               the location name, location cost and its population
    Method function: to return the best allocation considering the list of locations 
               and budget provided. The best allocation of resources is decided 
               based on which allocation has helped the most number of people with
               the least cost.

               If two allocations help the same maximum number of people, the best 
               allocation is decided on which allocation entails a lesser cost.

               If two allocations help the same maximum number of people with the same 
               least cost, either of the allocations would be returned.
    Method return type: Allocation
    Method exceptions: none*/

    public static Allocation allocateRelief(double budget, List<Location> sites) {
        Set<Allocation> allocations = new HashSet<Allocation>();
        Allocation option = new Allocation();
        optionsGenerate(sites, allocations, option, budget, 0);
        Allocation bestAllocation = new Allocation();
        int mostPeopleHelped = 0;
        double leastCost = 0;
        for(Allocation itr : allocations){
            if(itr.totalPeople() > mostPeopleHelped){
                mostPeopleHelped = itr.totalPeople();
                leastCost = itr.totalCost();
                bestAllocation = itr;
            }
            else if(itr.totalPeople() == mostPeopleHelped){
                double budgetUsed = itr.totalCost();
                if(budgetUsed <= leastCost){
                    bestAllocation = itr;
                }
            }
        }
        return bestAllocation;
    }

    /*Method name: optionsGenerate
    Method parameters: 
        -> List<Location> sites: stores a list of location objects that contain
               the location name, location cost and its population
        -> Set<Allocation> allocations: stores a set of all possible allocations of 
               resources within the given budget
        -> Allocation option: an Allocation object storing any particular allocation
        -> double budget: stores the budget to be used for allocationof resources
        -> int index: stores the index that is used to go through the list of locations
    Method function: to find and add all possible allocations (within the budget) 
        to the set of allocations. Considering a positive budget, the function 
        goes through the list of locations and adds it to a particular allocation.
        In addition, if all locations have been reviewed, then the allocation is added to 
        the set of allocations. 
    Method return type: void
    Method exceptions: none*/

    private static void optionsGenerate(List<Location> sites, Set<Allocation> allocations,
                                        Allocation option, double budget, int index){
        if(index == sites.size() || budget <= 0){
            allocations.add(option);
        }
        else{
            optionsGenerate(sites, allocations, option, budget, index + 1);
            Location loc = sites.get(index);
            if(budget - loc.getCost() >= 0){
                Allocation newAlloc = option.withLoc(loc);
                optionsGenerate(sites, allocations, option.withLoc(loc), budget - loc.getCost(), 
                index + 1);
            }
        }
    }

    // PROVIDED HELPER METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!**

    public static void printResult(Allocation alloc, double budget) {
        System.out.println("Result: ");
        System.out.println("  " + alloc);
        System.out.println("  People helped: " + alloc.totalPeople());
        System.out.printf("  Cost: $%.2f\n", alloc.totalCost());
        System.out.printf("  Unused budget: $%.2f\n", (budget - alloc.totalCost()));
    }

    public static List<Location> createRandomScenario(int numLocs, int minPop, int maxPop, double minCostPer, double maxCostPer) {
        List<Location> result = new ArrayList<>();

        for (int i = 0; i < numLocs; i++) {
            int pop = rand.nextInt(minPop, maxPop + 1);
            double cost = rand.nextDouble(minCostPer, maxCostPer) * pop;
            result.add(new Location("Location #" + i, pop, round2(cost)));
        }

        return result;
    }

    public static List<Location> createSimpleScenario() {
        List<Location> result = new ArrayList<>();

        result.add(new Location("Location #1", 50, 500));
        result.add(new Location("Location #2", 100, 700));
        result.add(new Location("Location #3", 60, 1000));
        result.add(new Location("Location #4", 20, 1000));
        result.add(new Location("Location #5", 200, 900));

        return result;
    }    

    private static double round2(double num) {
        return Math.round(num * 100) / 100.0;
    }
}
