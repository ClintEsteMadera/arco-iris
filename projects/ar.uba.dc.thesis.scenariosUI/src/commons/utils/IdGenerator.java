package commons.utils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import ar.uba.dc.thesis.common.Identifiable;

public class IdGenerator {

	private ConcurrentMap<Class<? extends Identifiable>, AtomicLong> ids;

	/**
	 * This class is not meant to be instantiated externally.
	 */
	public IdGenerator() {
		super();
		this.ids = new ConcurrentHashMap<Class<? extends Identifiable>, AtomicLong>();
	}

	/**
	 * Obtains the next clean id to be used as unique identifier for instances of {@link Identifiable}
	 * 
	 * @param clazz
	 *            the class to provide the unique id for. This is useful in order to contemplate different sequences for
	 *            different classes.
	 * @param identifiables
	 * @return an id to be used as unique identifier for instances of {@link Identifiable}
	 */
	public synchronized Long getNextId(Class<? extends Identifiable> clazz, List<? extends Identifiable> identifiables) {
		this.ids.putIfAbsent(clazz, this.getInitialValueForId(clazz, identifiables));
		AtomicLong id = this.ids.get(clazz);
		return id.getAndIncrement();
	}

	/**
	 * Return the recently requested id to the pool since it was not used.
	 * 
	 * @param identifiables
	 */
	public synchronized void returnRecentlyRequestedId(Class<? extends Identifiable> clazz,
			List<? extends Identifiable> identifiables) {
		AtomicLong id = this.ids.get(clazz);
		id.decrementAndGet();
	}

	private AtomicLong getInitialValueForId(Class<? extends Identifiable> clazz,
			List<? extends Identifiable> identifiables) {
		AtomicLong initialValueCandidate = new AtomicLong(0);

		if (identifiables != null) {
			for (Identifiable identifiable : identifiables) {
				initialValueCandidate.set(Math.max(initialValueCandidate.get(), identifiable.getId()));
			}
		}
		if (initialValueCandidate.get() != 0) {
			initialValueCandidate.incrementAndGet();
		}

		return initialValueCandidate;
	}

}
