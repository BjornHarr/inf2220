/*
    NOTE:

        - Sykler:
            - Jeg lagrer alle sykler, og har ikke sett etter en god maate for aa hindre at jeg
              lagrer samme flere ganger
        - Slack og latest start:
            - Dette er ikke en vakker maate aa gjore det... Jeg har hatt det travelt med 2260, og har ikke
              hatt tid til aa forbedre.

*/
import java.util.List;
import java.util.ArrayList;

public class Planner{
    private Task[] allTasks;
    private ArrayList<String> cycles;
    private ArrayList<Task> startTasks;

    public Planner(int numOfTasks){
        allTasks = new Task[numOfTasks];
        cycles = new ArrayList<String>();
        startTasks = new ArrayList<Task>();
    }

    //Lagrer en ny task til Planner (jeg setter ikke avhengigheter)
    public void newTask(int id, String name, int time, int staff, ArrayList<Integer> dependencies){
        Task newTask = new Task(this, id, name, time, staff, dependencies);
        allTasks[id - 1] = newTask;
    }

    //Setter avhengigheter
    public void setDependencies(){
        for (Task t : allTasks){
            for (int j : t.getDependencies()){
                t.setDependency(allTasks[j-1]);
            }

            //Dersom ingen forfedre, maa det vaere en startTask
            if (t.getCntPred() == 0){
                startTasks.add(t);
            }
        }
    }

    /*
    Finner ut om Planner er gjennomforbar.
    Rekursivt kall paa alle Tasks uten "forfedre"
    */
    public boolean realizable(){
        int j = 0; // integer used for counting number of tasks without a dependency
        for (Task t : startTasks){
            t.realizable();
            j++;
        }

        if (j == 0){
            System.out.println("\nERROR: No possible startingpoint found\n");
            return false;
        }

        if (cycles.size() == 0){
            return true;
        }

        return false;
    }

    //Returnerer den korteste mulige tiden for gjennomforing
    public int optimalTime(){
        int optimalTime = 0;
        for (Task t : startTasks){
            int itTime = t.findOptimalTime();
            if (itTime > optimalTime){
                optimalTime = itTime;
            }
        }
        return optimalTime;
    }

    //Setter tidligste start for alle Tasks
    public void setEarliestStart(){
        for (Task t : startTasks){
            t.setEarliestStart(0);
        }
    }

    //Setter slack, og som en konsekvens latest start i alle Tasks
    public void setSlack(){
        //Setter at startnodene skal brukes
        for (Task t : startTasks){
            t.use();
        }

        for (Task task : allTasks){ // NOTE: For aa vaere sikker paa aa iterere gjennom alt
            ArrayList<Task> slackSet = new ArrayList<Task>();
            //setter slack for utkanter
            int slack = -1;
            for (Task t : allTasks){
                if (t.getUse()){
                    slackSet.add(t);
                    int time = t.getTime();
                    if (slack < time){
                        slack = time;
                    }
                }
            }

            for (Task t : slackSet){
                t.setSlack(slack);
                t.used();
            }
        }
    }

    // returnerer informasjon om alle Tasks
    public String getInfo(){
        String info = "\n-------------- INFO --------------";

        for (int i = 0; i < allTasks.length; i++){
            info += allTasks[i].getInfo();
        }

        return info;
    }

    //Kjorer gjennom for alle tidsenheter, og printer info om hvile tasks som starter/holder paa/avsluttes
    public String getRunInfo(){
        String retString = "";

        int completedTasks = 0;
        int time = 0;
        while (completedTasks < allTasks.length){
            String infoOnTime = "  Time " + time + ":";
            boolean timeSet = false;
            int currentStaff = 0;

            for (Task t : allTasks){
                if (t.getEarliestStart() == time){
                    infoOnTime += "\n       Starting: " + t.getId();
                    currentStaff += t.getStaff();
                }else if ((time > t.getEarliestStart()) && (time < t.getEarliestStart() + t.getTime())){
                    currentStaff += t.getStaff();
                }else if (time == t.getEarliestStart() + t.getTime()){
                    infoOnTime += "\n       Finished: " + t.getId();
                    completedTasks++;
                }
            }

            if (infoOnTime.length() > 10){
                infoOnTime += "\n  Current staff: " + currentStaff + "\n\n";
                retString += infoOnTime;
            }
            time++;
        }

        retString += "---------------------------------\n";

        return retString;
    }

    //Kalles fra Tasks for aa lettere lagre flere sykler
    public void startCycle(int id){
        cycles.add(0, String.format("Cycle(%d): %s", cycles.size(), id));
    }

    //Kalles fra Tasks for aa lettere lagre flere sykler
    public void addToCycle(int id){
        String add = cycles.get(0) + " " + id;
        cycles.set(0, add);
    }

    public void printCycles(){
        for (int i = cycles.size()-1; i >= 0; i--){
            System.out.println(cycles.get(i));
        }
    }

    public class Task{
        private final Planner plan;
        private final int id, time, staff;
        private final String name;
        private ArrayList<Integer> dependencies;
        private ArrayList<Task> outEdges;
        private int earliestStart, latestStart, cntPredecessors, slack;
        private boolean checked, use;


        Task(Planner plan, int id, String name, int time, int staff, ArrayList<Integer> dependencies){
            this.plan = plan;
            this.id = id;
            this.name = name;
            this.time = time;
            this.staff = staff;
            this.dependencies = dependencies;
            this.outEdges = new ArrayList<Task>();
            this.cntPredecessors = 0;
            this.checked = false;
            this.use = false;
            earliestStart = -1;
            latestStart = -1;
            slack = -1;
        }

        public ArrayList<Integer> getDependencies(){
            return dependencies;
        }

        public void setDependency(Task dependency){
            dependency.setOutEdge(this);
            cntPredecessors++;
        }

        public void setOutEdge(Task successor){
            outEdges.add(successor);
        }

        //Ser etter sykler i Tasks (Tasks som er avhengige av hverandre)
        public Task realizable(){
            checked = true;

            for (Task t : outEdges){
                if (t.getChecked()){
                    plan.startCycle(id);
                    checked = false;
                    return t;
                }

                Task tmp = t.realizable(); // recursive call
                if (tmp != null){
                    plan.addToCycle(id);
                    if (tmp != this){
                        checked = false;
                        return tmp;
                    }
                }
            }

            checked = false;
            return null;
        }

        //setter tidligste start
        public void setEarliestStart(int in){
            if (in > earliestStart || earliestStart == -1){
                earliestStart = in;
            }

            for (Task t : outEdges){
                t.setEarliestStart(in + time);
            }
        }

        public int findOptimalTime(){
            if (outEdges.size() == 0){
                return earliestStart + time;
            }

            int retTime = -1;
            for (Task t : outEdges){
                int tmpTime = t.findOptimalTime();

                if (retTime < tmpTime){
                    retTime = tmpTime;
                }
            }
            return retTime;
        }

        //Setter slack, og som konsekvens ogsaa latestStart
        public void setSlack(int in){
            this.slack = in - time;
            this.latestStart =  earliestStart + slack;
        }

        /*
        Definerer om en Task er blitt brukt
        Klargjor alle direkte avhengige Tasks til aa bli brukt
        */
        public void used(){
            this.use = false;
            for (Task t : outEdges){
                t.use();
            }
        }

        //Klargjor Task til bruk ()
        public void use(){
            this.use = true;
        }

        //Printer informasjon om en Task
        public String getInfo(){
            String info = String.format("\nID: %d\nName: %s\nTime: %d\nStaff: %d", id, name, time, staff);
            info = String.format("%s\nEarliest Start: %d\nLatest Start: %d", info, earliestStart, latestStart);
            info = String.format("%s\nSlack: %d\n", info, slack);

            if (outEdges.size() > 0){
                info += String.format("Successor: %d", outEdges.get(0).getId());
                for (int i = 1; i < outEdges.size(); i++){
                    info = String.format("%s, %d", info, outEdges.get(i).getId());
                }
            }

            info += "\n";

            return info;
        }

    /*
      GET:
        Alt under er kun metoder for aa hente verdier fra en Task
    */
        public int getId(){
            return id;
        }

        public int getCntPred(){
            return cntPredecessors;
        }

        public int getTime(){
            return time;
        }

        public int getEarliestStart(){
            return earliestStart;
        }

        public int getStaff(){
            return staff;
        }

        public boolean getChecked(){
            return checked;
        }

        public boolean getUse(){
            return use;
        }

        public ArrayList<Task> getOutEdges(){
            return outEdges;
        }
    }
}
