import java.io.File;
import java.util.Scanner;

public class Tree{
    private Node root;

    Tree(String element){
        this.root = new Node(element, null);
    }

    public void add(String newElement){
        root.add(newElement);
    }

    public Node search(String searchElement){
        return root.search(searchElement);
    }

    public static void main(String[] args)throws Exception{
        Scanner sc = new Scanner(new File("dictionary.txt"));

        String firstElement = sc.nextLine();

        Tree test = new Tree(firstElement);

        while(sc.hasNext()){
            String next = sc.nextLine();
            test.add(next);
        }

        boolean run = true;

        while(run){
            System.out.println("Input search word? (q to exit)");
            sc = new Scanner(System.in);
            String in = sc.nextLine();

            if (in.equals("q")){
                run = false;
            }else{
                Node hent = test.search(in);
                System.out.println(hent.getElement() + "\n");
            }
        }

        sc.close();
    }

    public class Node{
        private Node parent;
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
            if (element.compareTo(newElement) < 0){
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
        public Node search(String searchElement){
            if (element.compareTo(searchElement) < 0){
                if (left.getElement().equals(searchElement)){
                    return left.search(searchElement);
                }else{
                    return this;
                }
            }else if (element.compareTo(searchElement) < 0){
                if (right.getElement().equals(searchElement)){
                    return right.search(searchElement);
                }else{
                    return this;
                }
            }else{
                return this;
            }
        }

        //Delete Node : TODO

        //Fetching the element outside of current Node
        public String getElement(){
            return this.element;
        }
    }
}
