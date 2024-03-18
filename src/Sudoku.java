import java.util.*;

public class Sudoku {
    /**
     * Returns the filled in sudoku grid.
     *
     * @param grid the partially filled in grid. unfilled positions are -1.
     * @return the fully filled sudoku grid.
     */
//    public static int[][] solve(int[][] grid) {
//        // Initialize lists for variables and constraints
//        List<Solver.Variable> variables = new ArrayList<>();
//        List<Solver.Constraint> constraints = new ArrayList<>();
//
//        int squareSide = (int)Math.sqrt(grid.length);
//
//        // Available values for each row, column, and square
//        List<HashSet<Integer>> columnSets = new ArrayList<>(grid.length);
//        List<HashSet<Integer>> rowSets = new ArrayList<>(grid.length);
//        List<HashSet<Integer>> squareSets = new ArrayList<>(squareSide * squareSide);
//
//        // Initialize
//        List<Integer> domain = new ArrayList<>();
//
//        // Id groups of numbers in same cols, rows, squares. Used to create constraints later.
//        List<List<Integer>> rowVarIdGroups = new ArrayList<>();
//        List<List<Integer>> columnVarIdGroups = new ArrayList<>();
//        List<List<Integer>> squareVarIdGroups = new ArrayList<>();
//        for (int i = 1; i <= squareSide * squareSide; i++) {
//            domain.add(i);
//        }
//
//        for (int i = 0; i < squareSide * squareSide; i++) {
//            squareSets.add(new HashSet<>(domain));
//            squareVarIdGroups.add(new ArrayList<>());
//        }
//        for (int i = 0; i < grid.length; i++) {
//            columnSets.add(new HashSet<>(domain));
//            rowSets.add(new HashSet<>(domain));
//            rowVarIdGroups.add(new ArrayList<>());
//            columnVarIdGroups.add(new ArrayList<>());
//        }
//
//        // Remove existing numbers and initialize relation col, row, square lists
//        for (int i = 0; i < grid.length; i++) {
//            for (int j = 0; j < grid.length; j++) {
//                if (grid[i][j] == -1) continue;
//                int prefilledNumber = grid[i][j];
//                int squareIndex = (i / squareSide) * squareSide + (j / squareSide);
//
//                squareSets.get(squareIndex).remove(prefilledNumber);
//                columnSets.get(j).remove(prefilledNumber);
//                rowSets.get(i).remove(prefilledNumber);
//            }
//        }
//
//        // Initialize variables and relate them in cols, rows, squares
//        List<List<Integer>> variableDomains = new ArrayList<>();
//        List<Integer> xPositions = new ArrayList<>();
//        List<Integer> yPositions = new ArrayList<>();
//        for (int i = 0; i < grid.length; i++) {
//            for (int j = 0; j < grid.length; j++) {
//                if (grid[i][j] != -1) continue;
//                int varId = variableDomains.size();
//                xPositions.add(i);
//                yPositions.add(j);
//                int squareIndex = (i / squareSide) * squareSide + (j / squareSide);
//
//                squareVarIdGroups.get(squareIndex).add(varId);
//                columnVarIdGroups.get(i).add(varId);
//                rowVarIdGroups.get(j).add(varId);
//
//                HashSet<Integer> currDomain = new HashSet<>(domain);
//                currDomain.removeAll(squareSets.get(squareIndex));
//                currDomain.removeAll(rowSets.get(i));
//                currDomain.removeAll(columnSets.get(j));
//                variableDomains.add(new ArrayList<>(currDomain));
//            }
//        }
//
//        // Add variables
//        for (List<Integer> varDomain : variableDomains){
//            variables.add(new Solver.Variable(varDomain));
//        }
//
//        // Add constraints
//        for (int i = 0; i < variableDomains.size(); i++) {
//            HashSet<Integer> relatedVariables = new HashSet<>();
//
//            int x = xPositions.get(i);
//            int y = yPositions.get(i);
//            int squareIndex = (x / squareSide) * squareSide + (y / squareSide);
//
//            relatedVariables.addAll(squareVarIdGroups.get(squareIndex));
//            relatedVariables.addAll(columnVarIdGroups.get(x));
//            relatedVariables.addAll(rowVarIdGroups.get(y));
//            // Remove self
//            relatedVariables.remove(i);
//
//            // Add not equal constraint for this variable and all others
//            for (Integer otherVarId : relatedVariables) {
//                constraints.add(new Solver.NeqConstraint(i, otherVarId));
//            }
//        }
//
//        // Convert to arrays
//        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
//        variablesArray = variables.toArray(variablesArray);
//        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
//        constraintsArray = constraints.toArray(constraintsArray);
//
//        if(variablesArray.length==0 || variablesArray==null) return grid;
//
//        // Use solver
//        Solver solver = new Solver(variablesArray, constraintsArray);
//        int[] result = solver.findOneSolution();
//
//        for (int i = 0; i < result.length; i++) {
//            grid[xPositions.get(i)][yPositions.get(i)] = result[i];
//        }
//
//        return grid;
//    }

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

                    Solver.Variable variable = new Solver.Variable(domain);
                    variables.add(variable);
                    List<Integer> constraintIndice = new ArrayList<>();

                    for (int n = 0; n < grid.length; n++) {
                        if (i != n) {
                            constraints.add(new Solver.NeqConstraint(i * grid.length + j, n * grid.length + j));
                            constraintIndice.add(constraintCnt++);
                        }
                        if (j != n) {
                            constraints.add(new Solver.NeqConstraint(i * grid.length + j, i * grid.length + n));
                            constraintIndice.add(constraintCnt++);
                        }

                        int squareXLeft = i - i % squareSide;
                        int squareXRight = squareXLeft + squareSide - 1;
                        int squareYLeft = j - j % squareSide;
                        int squareYRight = squareYLeft + squareSide - 1;

                        for (int x = squareXLeft; x <= squareXRight; x ++) {
                            for (int y = squareYLeft; y <= squareYRight; y ++) {
                                if (x == i && y == j) continue;
                                constraints.add(new Solver.NeqConstraint(i * grid.length + j, x * grid.length + y));
                                constraintIndice.add(constraintCnt++);
                            }
                        }
                    }
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
