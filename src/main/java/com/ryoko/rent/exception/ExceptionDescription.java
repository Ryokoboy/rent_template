package com.ryoko.rent.exception;

public class ExceptionDescription {
    public final static String UsernameNotFoundException = "User not found by email: %1$s";
    public static final String RepositoryException = "Something went wrong during the %1$s %2$s, please, try again!";
    public static final String CustomNotFoundException = "%1$s not found by %2$s: %3$s";
    public static final String AccessException = "You are trying to %1$s someone else's data!";
}
