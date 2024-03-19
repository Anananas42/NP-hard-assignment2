import java.util.*;

public class NQueens {
    /**
     * Returns the number of N-Queen solutions
     */
    public static int getNQueenSolutions(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // Restrict the first variable to range [1, ceil(n / 2)] to eliminate some horizontal symmetries
        List<Integer> domainFirst = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            domainFirst.add(i+1);
        }
        variables.add(new Solver.Variable(domainFirst));

        // Add rest of variables with full range
        List<Integer> domain = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            domain.add(i+1);
        }        
        for (int i = 1; i < n; i++){
            variables.add(new Solver.Variable(domain));
        }

        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                // No column collisions
                constraints.add(new Solver.NeqConstraint(i, j));

                // No diagonal collisions
                constraints.add(new Solver.NeqOffsetConstraint(i, j, j-i));
                constraints.add(new Solver.NeqOffsetConstraint(i, j, i-j));
            }
        }

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        return result.size();
    }
}
