package electronicspos.util;

import java.util.UUID;

public class IDGenerator {

    /**
     * Generates a positive random integer ID
     * Note: collisions are still possible for very large datasets
     */
    public static int generateInt() {
        return Math.abs(UUID.randomUUID().hashCode());
    }

    /**
     * Generates a random unique long ID (safer)
     */
    public static long generateLong() {
        return Math.abs(UUID.randomUUID().getMostSignificantBits());
    }

    /**
     * Generates a standard UUID string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // Example usage
    public static void main(String[] args) {
        System.out.println("Random int ID: " + generateInt());
        System.out.println("Random long ID: " + generateLong());
        System.out.println("UUID string: " + generateUUID());
    }
}