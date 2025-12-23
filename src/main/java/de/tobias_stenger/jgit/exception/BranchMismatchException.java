package de.tobias_stenger.jgit.exception;

public class BranchMismatchException extends RuntimeException {
    public BranchMismatchException(String branchName) {
        super("The provided Branch '" + branchName + "' doesn't match the actual branch of the repository");
    }
}
