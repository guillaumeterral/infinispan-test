package org.gterral.infinispan.test.util;

public final class InfinispanTestHelper {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors 
    //~ ----------------------------------------------------------------------------------------------------------------

    private InfinispanTestHelper() {
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Wraps the caused exception in a {@link ConnectivityException} if its not already such an exception.
     *
     * @param  exception the caused exception
     *
     * @return the wrapper exception
     */
    public static InfinispanTestException wrapInConnectivityException(Throwable exception) {
        if (exception instanceof InfinispanTestException) {
            return (InfinispanTestException) exception;
        } else {
            return new InfinispanTestException(exception);
        }
    }
}
