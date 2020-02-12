package Challenge;

import javafx.scene.image.Image;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * A Smart Enemy is able to calculate the best path to the player ane take
 * appropriate moves in order to chase the player around the map, they can
 * backtrack if no path to the player is not available.
 * @author Chuks Ajeh, Angelo Balistoy
 * @version 3.0
 */
class SmartEnemy extends Enemy {

    /**
     * The sprite used to represent the smart enemy.
     */
    private static final Image SPRITE;

    static {
        SPRITE = new Image("images/ENTITY_SMART_ENEMY.png");
    }

    /**
     * Constructs an smart enemy
     * @param position the position of the Enemy
     * @param direction The initial direction the enemy will take.
     */
    SmartEnemy(Position position, int direction) {
        super(SPRITE, position, direction);
    }

    /**
     * Gets the next direction of the Smart Enemy.
     * @param level The level being used.
     * @return An int from 0-3 representing NESW.
     */
    int nextDirection(Level level){
        // using wavefront:
        Player player = level.getPlayer();
        return this.nextDirection(level, player);
    }

    /**
     * Gets the next direction based on the player's location and impassable objects.
     * @param level The level object which holds the entity and cell grid
     * @param player The player in the level
     * @return An int from 0-3 representing NESW.
     */
    private int nextDirection(Level level, Player player) {

        Random random = new Random();

        // Grab cell and entity grid to flatten
        Cell[][] cellGrid = level.getCellGrid();
        Entity[][] entityGrid = level.getEntityGrid();
        // Get a flattened grid combining entity and levels, then converting impassable and passable to 0s and 1s
        // respectively
        int[][] flattenedGrid = SmartEnemy.flatten(entityGrid, cellGrid);


        // Get a grid of distances using waterfront planning (BFS)
        BFSVertex[][] waveFrontGrid = this.waveFrontPlanningSearch(flattenedGrid, player, entityGrid);

        int srcX= this.getPosition().x;
        int srcY = this.getPosition().y;
        // If we could not find the target with the waveFrontPlanningSearch(..).
        if (waveFrontGrid[srcX][srcY] == null) {
            return random.nextInt(4);
        } else {
            int enemyDist = waveFrontGrid[srcX][srcY].getDist();
            // Get the nextNode to travel.
            BFSVertex nextNode = this.getNextNode(waveFrontGrid,waveFrontGrid[srcX][srcY], enemyDist);
            // Get the relative distance.
            int newEnemyY =nextNode.getY() - this.getPosition().y;
            int newEnemyX = nextNode.getX() - this.getPosition().x;

            // Using the next location that we know, find the direction the enemy must take.
            final int[] row = {0,1,0,-1};
            final int[] col = {-1,0,1,0};
            // For directions UP, RIGHT, DOWN, LEFT:
            for(int i = 0; i < 4; i++) {
                if (newEnemyX == row[i] && newEnemyY == col[i]) {
                    this.setDirection(i);
                    return i;
                }
            }
        }


        return random.nextInt(4);
    }

    /**
     * Gets the next cell/node the SmartEnemy must take on the shortest path to the player.
     * Returns null if no path was found from the player to the enemy.
     * @param bfsGrid The filled BFSgrid.
     * @param enemyBFSNode The Enemy BFSNode.
     * @param enemyDist The enemy's distance from the player.
     * @return the next Node in the path
     */
    private BFSVertex getNextNode(BFSVertex[][] bfsGrid, BFSVertex enemyBFSNode, int enemyDist) {
        //make sure this is the starting node (Enemy Node)
        BFSVertex[] adjacentNodes = getSurroundingNodes(bfsGrid, enemyBFSNode);
        for (BFSVertex adjNode : adjacentNodes) {
            if (adjNode != null) {
                if (adjNode.getDist() == enemyDist-1) {
                    return adjNode;
                }

            }
        }

        return enemyBFSNode;
    }

    /**
     * Gets the surrounding nodes of a BFS Node.
     * @param BFSgrid The BFSgrid filled with BFS Vertices.
     * @param node The node to get adjacent nodes.
     * @return All the BFS vertices above, to the left, below and to the right of the node.
     */
    private BFSVertex[] getSurroundingNodes(BFSVertex[][] BFSgrid , BFSVertex node){
        // we assume the node passed is not null:
        BFSVertex[] adjNodes = new BFSVertex[4];
        BFSVertex up    = node.getY()-1 > 0 ? BFSgrid[node.getX()][node.getY()-1] : null;
        BFSVertex left  = node.getX()-1 > 0 ? BFSgrid[node.getX()-1][node.getY()] : null;
        BFSVertex down  = node.getY()+1 < BFSgrid[0].length ? BFSgrid[node.getX()][node.getY()+1] : null;
        BFSVertex right = node.getX()+1 < BFSgrid.length ? BFSgrid[node.getX() + 1][node.getY()] : null;

        // as long as the vertex isn't null then add it to the list of available adjacent nodes
        if (null != up) {
            adjNodes[0] = up;
        } else if (null != down) {
            adjNodes[1] = down;
        } else if (null != left) {
            adjNodes[2] = left;
        } else if (null != right) {
            adjNodes[3] = right;
        }

        return adjNodes;
    }

//    /**
//     *Gets the next location the enemy will take.
//     * @param path The path the enemy will take.
//     * @return The next location in the path.
//     */
//    private static int[] getNextNodeInPath(Stack<BFSVertex> path) {
//        //
//        BFSVertex vertex = path.pop();
//
//        while (vertex.getDist() != 1) {
//            vertex = path.pop();
//        }
//
//        return new int[]{vertex.getX(), vertex.getY()};
//    }

    /**
     * Finds a path from the smart enemy to the player.
     * @param flattenedLevel The level which combines all impassable entities and cells.
     * @param player The player.
     * @param entities The entities within the level.
     * @return The path to the player.
     */
    private BFSVertex[][] waveFrontPlanningSearch(int[][] flattenedLevel, Player player, Entity[][] entities) {

        Position playerPos = player.getPosition();
        Position enemyPos = this.getPosition();
        // Print out useful message 1;
        final int[] row = {-1,0,0,1};
        final int[] col = {0,-1,1,0};
        // Array to check if equivalent index has been visited for BFS.
        boolean[][] visited = new boolean[flattenedLevel.length][flattenedLevel[0].length];

        // set the source node as visited and enqueue
        visited[playerPos.x][playerPos.y] = true;

        Queue<BFSVertex> vertices = new LinkedList<>();
        Stack<BFSVertex> pathToReturn = new Stack<>();
        vertices.add(new BFSVertex(playerPos.x, playerPos.y, 0));

        int srcX;
        int srcY;
        int dist;

        boolean enemyNotFound;

        do {

            // pop front not from queue and process it
            BFSVertex bfsVertex = vertices.poll();
            pathToReturn.push(bfsVertex);

            // source node and distance
            assert bfsVertex != null;

            srcX = bfsVertex.getX();
            srcY = bfsVertex.getY();
            dist = bfsVertex.getDist();
            // This flips false if the vertex's x and y is equal to the enemy's position
            enemyNotFound = !(srcX == enemyPos.x && srcY == enemyPos.y);
            dist += 1;

            for (int i = 0 ; i < 4 ; i++ ) {
                // check for all 4 possible movements from current cell and enqueue
                if (isValid(flattenedLevel, visited, srcX + row[i],srcY + col[i])) {

                    // mark each cell as visited and enqueue it
                    visited[srcX + row[i]][srcY + col[i]] = true;
                    int nextVertexX = srcX + row[i];
                    int nextVertexY = srcY + col[i];

                    vertices.add(new BFSVertex(nextVertexX,nextVertexY, dist));
                }
            }

        } while (!vertices.isEmpty() && enemyNotFound);


        BFSVertex[][] bfsGrid = new BFSVertex[flattenedLevel.length][flattenedLevel[0].length];
        // Use the stack to fill the BFS array
        while(!pathToReturn.empty()){
            bfsGrid[pathToReturn.peek().getX()][pathToReturn.peek().getY()]=pathToReturn.pop();

        }

        return bfsGrid;
    }

    /**
     * Checks whether the cell is valid or not for BFS.
     * @param flattenedLevel The flattened level.
     * @param visited The array showing all visited nodes.
     * @param row The row (x) coordinate
     * @param col The column (y) coordinate
     * @return if the move is valid
     */
    private static boolean isValid (int[][] flattenedLevel, boolean[][] visited, int row, int col) {

        final int MAX_ROW_BOUNDARY = flattenedLevel.length - 1;
        final int MAX_COLUMN_BOUNDARY = flattenedLevel[0].length - 1;
        final boolean NOT_OUT_OF_BOUNDS = (row >= 0) && (row < MAX_ROW_BOUNDARY) &&
                (col >= 0) && (col < MAX_COLUMN_BOUNDARY);
        final boolean CAN_VISIT = !visited[row][col];
        final boolean IS_ENEMY_TRAVERSABLE = flattenedLevel[row][col] == 0;

        return (NOT_OUT_OF_BOUNDS && CAN_VISIT && IS_ENEMY_TRAVERSABLE);
    }

    /**
     * Gets a array showing passable and impassable objects in the level
     * @param entityGrid The entity grid for the level.
     * @param cellGrid The cell grid for the level.
     * @return  An array with 1 representing impassable objects and a 0 representing passable objects.
     */
    private static int[][] flatten(Entity[][]entityGrid, Cell[][] cellGrid) {
        // HOW? HOW IS THIS STILL WORKING HEIGHT AND WIDTH ARE FLIPPED? -Angelo 06/12/19
        // I be dumdum - Gnome
        int height = entityGrid.length;
        int width = entityGrid[0].length;

        int[][] level = new int[height][width];

        for (int i = 0 ; i < height ; i++ ) {
            for (int j = 0 ; j < width ; j++ ) {
                //Impassable objects (1): Entities and anything that is not ground.
                if (!(entityGrid[i][j] instanceof SmartEnemy) && !(cellGrid[i][j] instanceof Ground)) {
                    level[i][j] = 1;
                }

            }
        }

        return level;
    }
}

