import java.util.*;

public class Sudoku {
    /**
     * Returns the filled in sudoku grid.
     *
     * @param grid the partially filled in grid. unfilled positions are -1.
     * @return the fully filled sudoku grid.
     */

    public static int[][] solve(int[][] grid) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();
        int squareSide = (int)Math.sqrt(grid.length);
        int constraintCnt = 0;

        // add your variables and constraints
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if(grid[i][j]!=-1) variables.add(new Solver.Variable(grid[i][j]));
                else {
                    List<Integer> domain = new ArrayList<>();
                    for (int n = 1; n <= grid.length; n++) {
                        domain.add(n);
                    }
                    List<Integer> constraintIndice = new ArrayList<>();

                    for (int n = 0; n < grid.length; n++) {
                        if (i != n) {
                            domain.remove((Object)grid[n][j]);
                            constraintIndice.add(constraintCnt++);
                            constraints.add(new Solver.NeqConstraint(i * grid.length + j, n * grid.length + j));
                        }
                        if (j != n) {
                            domain.remove((Object)grid[i][n]);
                            constraintIndice.add(constraintCnt++);
                            constraints.add(new Solver.NeqConstraint(i * grid.length + j, i * grid.length + n));
                        }

                        int squareXLeft = i - i % squareSide;
                        int squareXRight = squareXLeft + squareSide - 1;
                        int squareYLeft = j - j % squareSide;
                        int squareYRight = squareYLeft + squareSide - 1;

                        for (int x = squareXLeft; x <= squareXRight; x ++) {
                            for (int y = squareYLeft; y <= squareYRight; y ++) {
                                if (x == i && y == j) continue;
                                domain.remove((Object)grid[x][y]);
                                constraintIndice.add(constraintCnt++);
                                constraints.add(new Solver.NeqConstraint(i * grid.length + j, x * grid.length + y));
                            }
                        }
                    }
                    Solver.Variable variable = new Solver.Variable(domain);
                    variables.add(variable);
                    variable.constraintIds = constraintIndice.stream().mapToInt(Integer::intValue).toArray();
                }
            }
        }


        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        int[] result = solver.findOneSolution();
        int index = 0;

        // use result to construct answer
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = result[index++];
            }
        }
        return grid;
    }
}
