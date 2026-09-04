package sunrisedentalsystem.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    private static final int WORK_FACTOR = 12;

    private PasswordUtil() {
    }

    public static String hashPassword(
            String password) {

        if (password == null
                || password.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Password cannot be empty."
            );
        }

        return BCrypt.hashpw(
                password,
                BCrypt.gensalt(
                        WORK_FACTOR
                )
        );
    }

    public static boolean matchesPassword(
            String password,
            String passwordHash) {

        if (password == null
                || passwordHash == null
                || passwordHash.trim().isEmpty()) {

            return false;
        }

        try {

            return BCrypt.checkpw(
                    password,
                    passwordHash
            );

        } catch (IllegalArgumentException e) {

            return false;
        }
    }
}