import java.util.*;

class Solver {
    static class Variable {

        List<Integer> domain;
        // you can add more attributes

        /**
         * Constructs a new variable.
         * @param domain A list of values that the variable can take
         */
        public Variable(List<Integer> domain) {
            this.domain = domain;
        }

        // deep copy with new memory allocation
        public Variable copy() {
            List<Integer> newDomain = new ArrayList<>();
            for (int i = 0; i < domain.size(); i++) {
                newDomain.add(domain.get(i));
            }

            return new Variable(newDomain);
        }
    }

    static abstract class Constraint {
        /**
         * Tries to reduce the domain of the variables associated to this constraint, using inference
         */
        abstract boolean infer(/* you can add params */);
    }

    static class NoRepetitionConstraint extends Constraint {

        // values of the assigned variables
        int[] solution;
        // unassigned variables
        List<Variable> otherVariables;

        public NoRepetitionConstraint(int[] solution, List<Variable> otherVariables) {
            this.solution = solution;
            this.otherVariables = otherVariables;
        }

        // false if at least one of the variable has empty domain with current solution.
        @Override
        boolean infer() {
            // renew domain of each variable with current solution
            for (Variable variable : otherVariables) {

                List<Integer> newDomain = new ArrayList<>();

                for (int value : variable.domain) {

                    boolean valid = true;
                    for (int existedValue : solution) {
                        if (value == existedValue) {
                            valid = false;
                            break;
                        }
                    }

                    if(valid) newDomain.add(value);
                }

                if(newDomain.size() == 0) return false;
                variable.domain = newDomain;
            }

            return true;
        }
    }

    // Example implementation of the Constraint interface.
    // It enforces that for given variable X, it holds that 5 < X < 10.
    //
    // This particular constraint will most likely not be very useful to you...
    // Remove it and design a few constraints that *can* help you!
    static abstract class BetweenFiveAndTenConstraint {
        Variable var;

        public BetweenFiveAndTenConstraint(Variable var) {
            this.var = var;
        }

        void infer() {
            List<Integer> newDomain = new LinkedList<>();

            for (Integer x : this.var.domain) {
                if (5 < x && x < 10)
                    newDomain.add(x);
            }

            this.var.domain = newDomain;
        }
    }

    static class State {

    }

    Variable[] variables;
    Constraint[] constraints;
    List<int[]> solutions;
    // you can add more attributes

    /**
     * Constructs a solver.
     * @param variables The variables in the problem
     * @param constraints The constraints applied to the variables
     */
    public Solver(Variable[] variables, Constraint[] constraints) {
        this.variables = variables;
        this.constraints = constraints;
        solutions = new LinkedList<>();
    }

    /**
     * Searches for one solution that satisfies the constraints.
     * @return The solution if it exists, else null
     */
    int[] findOneSolution() {
        solve(false);

        return !solutions.isEmpty() ? solutions.get(0) : null;
    }

    /**
     * Searches for all solutions that satisfy the constraints.
     * @return The solution if it exists, else null
     */
    List<int[]> findAllSolutions() {
        solve(true);
//        printSolution();
        return solutions;
    }

    /**
     * Main method for solving the problem.
     * @param findAllSolutions Whether the solver should return just one solution, or all solutions
     */
    void solve(boolean findAllSolutions) {
        // here you can do any preprocessing you might want to do before diving into the search

        search(findAllSolutions /* you can add more params */);
    }

    /**
     * Solves the problem using search and inference.
     */
    void search(boolean findAllSolutions /* you can add more params */) {
        // TODO: implement search using search and inference

        backtrack(variables, new int[variables.length], 0);

    }

    private void backtrack(Variable[] variables1, int[] solution1, int decideLevel) {
        // all variables are assigned
        if (decideLevel == variables1.length) {
            solutions.add(Arrays.copyOf(solution1, solution1.length));
            return;
        }

        // make decision when there are multiple possible values for the next variable
        // search & inference: assign possible values to a variable
        // , and use constraints to validate it while minimizing the domain for rest variables
        for(int i = 0; i < variables1[decideLevel].domain.size(); i++) {
            // invalid if one of the variable has empty domain
            boolean valid = true;
            solution1[decideLevel] = variables1[decideLevel].domain.get(i);
            for(Constraint constraint : constraints) {
                if(!constraint.infer()) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                backtrack(variables1, solution1, decideLevel+1);
            }
        }

    }

    void printSolution() {
        for (int[] sol : solutions) {
            for (int i : sol) {
                System.out.print(i);
            }
            System.out.println("");
        }
    }
}