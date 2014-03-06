package org.gterral.infinispan.test.util;

public class InfinispanTestException extends RuntimeException {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Static fields/initializers 
    //~ ----------------------------------------------------------------------------------------------------------------

    private static final long serialVersionUID = 8721408109488358435L;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors 
    //~ ----------------------------------------------------------------------------------------------------------------

    public InfinispanTestException(Throwable cause) {
        super(cause);
    }

    public InfinispanTestException(String message) {
        super(message);
    }

    public InfinispanTestException(String message, Throwable cause) {
        super(message, cause);
    }

}
