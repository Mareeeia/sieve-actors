package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import java.util.ArrayList;
import java.util.List;

import static edu.rice.pcdp.PCDP.async;
import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 *
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     *
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        final SieveActorActor firstActor = new SieveActorActor(2);
        for (int i = 3; i <= limit; i += 2) {
            Integer num = i;
            finish(() -> firstActor.process(num));
        }
        int count = 0;

        SieveActorActor iter = firstActor;

        while (iter != null) {
            iter = iter.nextActor;
            count++;
        }

        return count;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {
        private SieveActorActor nextActor;
        private final Integer prime;

        public SieveActorActor(Integer prime) {
            this.prime = prime;
        }
        /**
         * Process a single message sent to this actor.
         *
         * TODO complete this method.
         *
         * @param msg Received message
         */
        @Override
        public void process(Object msg) {
            final int candidate = (Integer) msg;
            if (candidate <= 0) {
                if (nextActor != null) {
                    nextActor.send(msg);
                }
            }
            else {
                if (candidate % prime != 0) {
                    if (nextActor == null) {
                        nextActor = new SieveActorActor(candidate);
                    }
                    nextActor.send(msg);
                }
            }
        }
    }
}
