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
        System.out.println("***************** Statistics *****************");

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

        System.out.println("*************** End Statistics ***************");
    }

//
//  Spellcheck
//
    //Returns an array of all possible ways a word can have, with two letters swapped
    public ArrayList<String> swapLetters(String word){
        ArrayList<String> swaps = new ArrayList<String>();

        char[] charWord = word.toCharArray();

        for(int i = 0; i < word.length()-1; i++){
            char[] swapWord = charWord;
            char tmpChar = swapWord[i+1];
            swapWord[i+1] = swapWord[i];
            swapWord[i] = tmpChar;

            String tmpWord = new String(swapWord);

            if (search(tmpWord) != null){
                swaps.add(tmpWord);
            }
        }

        return swaps;
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
            System.out.println("Input search word? (q to exit)");
            sc = new Scanner(System.in);
            String in = sc.nextLine();
            in = in.toLowerCase();

            if (in.equals("q")){
                run = false;
            }else{
                String hent = tree.search(in);

                if (hent.equals(in)){
                    System.out.println("Word found: " + hent + "\n");
                }else{
                    ArrayList<String> swaps = tree.swapLetters(in);
                    for(int i = 0; i < swaps.size(); i++){
                        System.out.println(swaps.get(i));
                    }
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

//
//  Add and search methods
//

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
            if (element.compareTo(searchElement) < 0){
                if (left == null){
                    return null;
                }
                return left.search(searchElement);
            }else if (element.compareTo(searchElement) > 0){
                if (right == null){
                    return null;
                }
                return right.search(searchElement);
            }else if (element.compareTo(searchElement) == 0){
                return this.element;
            }else{
                return null;
            }
        }

        //Delete Node : TODO


//
//  Statistics
//
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

//
//  Getters
//

        //Fetching the element outside of current Node
        public String getElement(){
            return this.element;
        }
    }
}
