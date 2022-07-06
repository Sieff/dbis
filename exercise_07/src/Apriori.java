import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Apriori {

    List<Set<Integer>> transactions = new ArrayList<>();
    private final float minSup = 0.01f;

    public Apriori() {
        readFile();

        Set<Set<Integer>> current_candidates = new HashSet<>();
        int candidate_size = 0;
        do {
            candidate_size++;
            current_candidates = buildCandidates(candidate_size, current_candidates);
            current_candidates = filterCandidatesByMinSup(current_candidates, minSup, candidate_size);
            System.out.println("itemsets with " + candidate_size + " items: " + current_candidates.size());
            System.out.println();
        } while (!current_candidates.isEmpty());
    }

    private Set<Set<Integer>> buildCandidates(int candidateSize, Set<Set<Integer>> candidates) {
        Set<Set<Integer>> new_candidates = new HashSet<>();
        if (candidateSize == 1) {
            for (Set<Integer> transaction : transactions) {
                for (Integer item : transaction) {
                    Set<Integer> set = new HashSet<>();
                    set.add(item);
                    new_candidates.add(set);
                }
            }
        } else {
            for (Set<Integer> candidate1 : candidates) {
                for (Set<Integer> candidate2 : candidates) {
                    Set<Integer> intersection = new HashSet<>(candidate1); // use the copy constructor
                    intersection.retainAll(candidate2);
                    if (intersection.size() == candidateSize - 2) {
                        Set<Integer> newCandidate = new HashSet<>(candidate1);
                        newCandidate.addAll(candidate2);
                        new_candidates.add(newCandidate);
                    }
                }
            }
        }
        return new_candidates;
    }

    private Set<Set<Integer>> filterCandidatesByMinSup(Set<Set<Integer>> candidates, Float minSup, int candidateSize) {
        Set<Set<Integer>> newCandidates = new HashSet<>();
        if (candidateSize >= 2) {
            System.out.println("Itemsets of size " + candidateSize + ":");
        }
        for (Set<Integer> candidate : candidates) {
            int absoluteSupport = 0;
            for (Set<Integer> transaction : transactions) {
                if (transaction.containsAll(candidate)) {
                    absoluteSupport++;
                }
            }
            float support = absoluteSupport / (float) transactions.size();
            if (support >= minSup) {
                newCandidates.add(candidate);
                if (candidateSize >= 2) {
                    ArrayList<Integer> sort = new ArrayList<>(candidate);
                    Collections.sort(sort);
                    System.out.println("(" + sort + ", " + support * 100 + "%)" );
                }
            }
        }
        return newCandidates;
    }


    private void readFile() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("transactions.txt"), StandardCharsets.ISO_8859_1))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                processTransaction(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processTransaction(String transactionString) {
        String[] items = transactionString.split(" ");
        Set<Integer> transaction = Arrays.stream(items).mapToInt(Integer::parseInt).boxed().collect(Collectors.toSet());
        transactions.add(transaction);
    }

}
