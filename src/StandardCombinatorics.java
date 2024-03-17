import java.util.*;
import java.util.stream.Collectors;

public class StandardCombinatorics {
    /**
     * Returns a list of all binary strings of length n
     */
    public static List<String> getBinaryStrings(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // TODO: add your variables
        for (int i = 0; i < n; i++) {
            variables.add(new Solver.Variable(List.of(0, 1)));
        }

        // TODO: add your constraints

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        // TODO: use result to construct answer
        List<String> resultStrings = result.stream()
                                        .map(arr -> Arrays.stream(arr)
                                            .mapToObj(String::valueOf)
                                            .collect(Collectors.joining()))
                                        .collect(Collectors.toList());
        return resultStrings;
    }

    /**
     * Returns a list of all combinations of k elements from the set {1,...,n} without repetitions
     */
    public static List<int[]> getCombinationsWithoutRepetition(int n, int k) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        List<Integer> domain = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            domain.add(i+1);
        }

        for (int i = 0; i < k; i++){
            variables.add(new Solver.Variable(domain));
        }

        for (int i = 0; i < k-1; i++) {
            constraints.add(new Solver.GrConstraint(i+1, i));
        }

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        // System.out.println("Solutions:");
        // for (int[] solution : result) {
        //     System.out.println(Arrays.toString(solution));
        // }

        return result;
    }

    /**
     * Returns a list of all combinations of k elements from the set {1,...,n} with repetitions
     */
    public static List<int[]> getCombinationsWithRepetition(int n, int k) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        List<Integer> domain = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            domain.add(i+1);
        }

        for (int i = 0; i < k; i++){
            variables.add(new Solver.Variable(domain));
        }

        for (int i = 0; i < k-1; i++) {
            constraints.add(new Solver.GrEqConstraint(i+1, i));
        }

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions();

        return result;
    }

    /**
     * Returns a list of all subsets in the set {1,...,n}
     */
    public static List<int[]> getSubsets(int n) {

        List<int[]> result = new ArrayList<>();
        int[] emptySet = new int[0];
        result.add(emptySet);

        for (int k = 0; k < n; k++) {
            result.addAll(getCombinationsWithoutRepetition(n, k));
        }

        return result;
    }

    /**
     * Returns a list of all permutations in the set {1,...,n}
     */
    public static List<int[]> getSetPermutations(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        List<Integer> domain = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            domain.add(i+1);
        }

        for (int i = 0; i < n; i++){
            variables.add(new Solver.Variable(domain));
        }

        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                constraints.add(new Solver.NeqConstraint(i, j));
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

        // System.out.println("Solutions:");
        // for (int[] solution : result) {
        //     System.out.println(Arrays.toString(solution));
        // }

        return result;
    }
}
