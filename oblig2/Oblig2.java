import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Oblig2{
    public static void main(String[] args)throws IOException {

        //Reading from file
        Scanner sc = new Scanner(new File(args[0]));
        int length = Integer.parseInt(sc.nextLine());

        Planner p = new Planner(length);

        while (sc.hasNext()){
            int id = Integer.parseInt(sc.next());
            String name = sc.next();
            int time = Integer.parseInt(sc.next());
            int staff = Integer.parseInt(sc.next());
            ArrayList<Integer> dependencies = new ArrayList<Integer>();

            int dependency = Integer.parseInt(sc.next());
            while (dependency != 0){
                dependencies.add(dependency);
                dependency = Integer.parseInt(sc.next());
            }

            p.newTask(id, name, time, staff, dependencies);
        }

        p.setDependencies();

        if (p.realizable()){

            p.setEarliestStart();
            p.setSlack();
            int optimalTime = p.optimalTime();

            //Printing helpfull info about tasks
            System.out.println(p.getInfo());

            System.out.println("----- Optimal run time: " + optimalTime + " ------\n");

            System.out.println(p.getRunInfo());



        }else{
            System.out.println("The plan is NOT realizable");
            p.printCycles();
        }


    }
}
