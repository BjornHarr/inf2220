public class PrefiksDybdeBinTre{
    private Node root;

    public static void main(String[] args){

    }

    class Node{
        protected Node left;
        protected Node right;
        private Node parent;
        private int element;

        Node(int element, Node parent){
            this.left = null;
            this.right = null;
            this.parent = parent;
            this.element = element;
        }

        public void insert(int newElement){
            if (newElement < this.element){
                if (left != null){
                    left.add(newElement);
                }else{
                    left = new Node(newElement, this);
                }
            }else{
                if (right != null){
                    right.add(newElement);
                }else{
                    right = new Node(newElement, this);
                }
            }
        }

        public int search(int searchElement, boolean remove){
            if (this.element == searchElement){
                if (remove){
                    return this.remove();
                }else{
                    return element;
                }
            }else{
                if (searchElement < this.element){
                    return left.search(searchElement, remove);
                }else{
                    return right.search(searchElement, remove);
                }
            }
        }

        public int remove(){
            if (left != null && right != null){
                if (parent.getElement() < this.element){

                }

            }else if (left != null && right == null){
                if (parent.getElement() < this.element){
                    parent.left = this.left;
                }else{
                    parent.right = this.left;
                }
            }else if (left == null && right != null){
                if (parent.getElement() < this.element){
                    parent.left = this.right;
                }else{
                    parent.right = this.right;
                }
            }else{
                //Both left and right is null
            }

            return this.element;
        }

        public int getElement(){
            return this.element;
        }

        //Method for organizing / stabilizing the tree?
    }
}
