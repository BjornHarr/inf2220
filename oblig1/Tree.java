import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class Tree{
    private Node root;

    Tree(String element){
        this.root = new Node(element, null);
    }

    public void add(String newElement){
        root.add(newElement);
    }

    public String search(String searchElement){
        return (root != null ? root.search(searchElement) : null);
    }

    public void statistics(int numOfElements){
        System.out.println("\n***************** Statistics *****************\n");

        //Nodes in each depth
        ArrayList<Integer> nodesInDepth = new ArrayList<Integer>();
        nodesInDepth.add(1);
        nodesInDepth = root.nodesInEachDepth(new ArrayList<Integer>(), 1);

        for (int i = 0; i < nodesInDepth.size(); i++){
            System.out.println("Nodes in depth " + i + ": " + nodesInDepth.get(i));
        }
        System.out.println("----------------------------------------------");

        //Tree depth
        int depth = nodesInDepth.size() - 1;
        System.out.println("Tree depth: " + depth);
        System.out.println("----------------------------------------------");

        //Avrage depth
        double avrageDepth = 0;
        for (int i = 0; i < nodesInDepth.size(); i++){
            avrageDepth += nodesInDepth.get(i) * i;
        }

        avrageDepth = avrageDepth / numOfElements;

        System.out.printf("Avrage depth is: %2.2f\n", avrageDepth);
        System.out.println("----------------------------------------------");

        //First word
        String firstWord = root.firstWord();
        System.out.println("The alphabetically first word is: " + firstWord);
        System.out.println("----------------------------------------------");

        //Last word
        String lastWord = root.lastWord();
        System.out.println("The alphabetically last  word is: " + lastWord);

        System.out.println("\n*************** End Statistics ***************\n");
    }

//
//  Similar words
//
    //Returns an ArrayList of all simlar words, by swapping two letters
    public ArrayList<String> swapLetters(String word){
        ArrayList<String> possibilities = new ArrayList<String>();

        for(int i = 0; i < word.length()-1; i++){
            char[] swapWord = word.toCharArray();

            char tmpChar = swapWord[i];
            swapWord[i] = swapWord[i+1];
            swapWord[i+1] = tmpChar;

            String tmpWord = new String(swapWord);

            if (search(tmpWord) != null){
                if (!exists(possibilities, tmpWord)){
                    possibilities.add(tmpWord);
                }
            }
        }

        return possibilities;
    }

    //Returns an ArrayList of all simlar words, by replacing one and one letters
    public ArrayList<String> replaceLetters(String word){
        ArrayList<String> possibilities = new ArrayList<String>();

        for(int i = 0; i < word.length(); i++){
            for(char j = 'a'; j < 'z'; j++){
                char[] tmpCharWord = word.toCharArray();
                tmpCharWord[i] = j;

                String tmpWord = search(new String(tmpCharWord));

                if (tmpWord != null){
                    if (!exists(possibilities, tmpWord)){
                        possibilities.add(tmpWord);
                    }
                }
            }
        }

        return possibilities;
    }

    //Returns an ArrayList of all simlar words, by adding a letter in between
    public ArrayList<String> addLetters(String word){
        ArrayList<String> possibilities = new ArrayList<String>();

        for(int i = 0; i <= word.length(); i++){
            for(char j = 'a'; j <= 'z'; j++){
                ArrayList<Character> newWord = new ArrayList<Character>();
                for (int k = 0; k < word.length(); k++){
                    newWord.add(word.charAt(k));
                }

                newWord.add(i, j);

                StringBuilder sb = new StringBuilder(newWord.size());
                for (char c : newWord){
                        sb.append(c);
                }

                String result = search(sb.toString());

                if (result != null){
                    if (!exists(possibilities, result)){
                        possibilities.add(result);
                    }
                }
            }
        }
        return possibilities;
    }

    //Returns an ArrayList of all simlar words, by removing one and one letter
    public ArrayList<String> removeLetters(String word){
        ArrayList<String> possibilities = new ArrayList<String>();

        ArrayList<Character> listWord = new ArrayList<Character>();
        for(int i = 0; i < word.length(); i++){
            listWord.add(word.charAt(i));
        }

        for(int i = 0; i < word.length(); i++){
            ArrayList<Character> tmpWord = new ArrayList<Character>();
            for (Character c : listWord){
                tmpWord.add(c);
            }

            tmpWord.remove(i);

            StringBuilder sb = new StringBuilder(tmpWord.size());
            for (char c : tmpWord){
                    sb.append(c);
            }

            String result = search(sb.toString());
            if (result != null){
                if (!exists(possibilities, result)){
                    possibilities.add(result);
                }
            }
        }
        return possibilities;
    }

    //Checks if an element allready exists in the given ArrayList
    private boolean exists(ArrayList<String> list, String checkWord){
        for (String s : list){
            if (s.equals(checkWord)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args)throws Exception{
        Scanner sc = new Scanner(new File("dictionary.txt"));
        String firstElement = sc.nextLine();
        Tree tree = new Tree(firstElement);
        int numOfElements = 1; // Var used in statistics()

        while(sc.hasNextLine()){
            String next = sc.nextLine();
            tree.add(next);
            numOfElements++;
        }

        tree.statistics(numOfElements);

        boolean run = true;

        while(run){
            System.out.println("----------------------------------------------");
            System.out.println("\nInput search word ('q' to exit)\n");
            sc = new Scanner(System.in);
            String in = sc.nextLine();
            in = in.toLowerCase();

            if (in.equals("q")){
                run = false;
            }else{
                String hent = tree.search(in);

                if (hent != null){
                    System.out.println("Word found: " + hent + "\n");
                }else{
                    System.out.println("---------------- Similar Words ---------------");
                    ArrayList<String> swapLetters = tree.swapLetters(in);
                    if (swapLetters.size() > 0){
                        System.out.println("Swaped letters:");
                        for(int i = 0; i < swapLetters.size(); i++){
                            System.out.println(swapLetters.get(i));
                        }
                    }
                    ArrayList<String> replaceLetter = tree.replaceLetters(in);
                    if (replaceLetter.size() > 0){
                        System.out.println("\nReplaced letters:");
                        for(int i = 0; i < replaceLetter.size(); i++){
                            System.out.println(replaceLetter.get(i));
                        }
                    }
                    ArrayList<String> addLetter = tree.addLetters(in);
                    if (addLetter.size() > 0){
                        System.out.println("\nAdded letters:");
                        for(int i = 0; i < addLetter.size(); i++){
                            System.out.println(addLetter.get(i));

                        }
                    }
                    ArrayList<String> removeLetter = tree.removeLetters(in);
                    if (removeLetter.size() > 0){
                        System.out.println("\nRemoved letters:");
                        for(int i = 0; i < removeLetter.size(); i++){
                            System.out.println(removeLetter.get(i));
                        }
                    }
                    System.out.println(); //For a prettier UI
                }
            }
        }
        sc.close();
    }

    public class Node{
        private Node parent; //To be used for deleting nodes
        private Node left;
        private Node right;
        private String element;

        Node(String element, Node parent){
            this.left = null;
            this.right = null;
            this.element = element;
            this.parent = parent;
        }

        //Insert Node
        public void add(String newElement){
            if (element.compareTo(newElement) > 0){
                if (left != null){
                    left.add(newElement);
                }else{
                    left = new Node(newElement, this);
                }
            }else if (element.compareTo(newElement) < 0){
                if (right != null){
                    right.add(newElement);
                }else{
                    right = new Node(newElement, this);
                }
            }else{
                System.out.println("Adding same word");
            }
        }

        //Search Node
        public String search(String searchElement){
            if (left != null && element.compareTo(searchElement) > 0){
                return left.search(searchElement);

            }else if (right != null && element.compareTo(searchElement) < 0){
                return right.search(searchElement);

            }else if (element.compareTo(searchElement) == 0){
                return this.element;

            }else{
                return null;
            }
        }

        //Calculating number of nodes in each depth
        public ArrayList<Integer> nodesInEachDepth(ArrayList<Integer> list, int currentDepth){
            if (list.size() >= currentDepth){
                list.set(currentDepth-1, list.get(currentDepth-1) + 1);
            }else{
                list.add(1);
            }

            list = (left != null ? left.nodesInEachDepth(list, currentDepth + 1) : list);
            list = (right != null ? right.nodesInEachDepth(list, currentDepth + 1) : list);

            return list;
        }

        //Returns the alphabetically first word
        public String firstWord(){
            return (left == null ? element : left.firstWord());
        }

        //Returns the alphabetically last word
        public String lastWord(){
            return (right == null ? element : right.lastWord());
        }
    }
}
