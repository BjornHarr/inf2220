public class Ex4 {
    // Place inside the Node class
    // elemenet is an int stored in the Node.
    public int sum(){
        int sum = element;

        sum += (left != null ? left.sum() : 0);
        sum += (right != null ? right.sum() : 0);

        return sum;
    }


    //depth is nums [0], height is nums[1]
    public int calc(int depth){
        this.depth = depth++;

        this.height = (left != null ? left.calc(depth) : 0);
        int tempHeight = (right != null ? right.calc(depth) : 0);

        if (this.height < tempHeight){
            this.height = tempHeight;
        }

        return height+1;
    }
}
