package de.skuzzle.test.snapshots.normalize;

import java.util.Collection;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * A pointer to a class member belonging to a certain object instance. Allows to read and
 * modify the member's value in place.
 *
 * @author Simon Taddiken
 * @see ObjectTraversal
 */
@API(status = Status.EXPERIMENTAL)
public interface ObjectMember {

    /**
     * The object of which this is a direct child attribute.
     *
     * @return Our parent.
     */
    Object parent();

    /**
     * If {@link #parent()} has been discovered in a supported collection type (array,
     * {@link Iterable}, {@link Collection}) this method returns the respective
     * collection.
     * <p>
     * Note that, during traversal, only the first occurrence of an object instance is
     * taken into account. If the same object is once referenced in a collection
     * <em>and</em> once directly, then depending on attribute discovery order, you might
     * or might not encounter the one with available collection parent.
     *
     * @return Pointer to the collection in which {@link #parent()} has been discovered
     *         while traversing an object graph.
     */
    Optional<Object> collectionParent();

    /**
     * The canonical name of the member.
     *
     * @return The name.
     */
    String name();

    default boolean hasTypeCompatibleTo(Class<?> type) {
        return type.isAssignableFrom(valueType());
    }

    /**
     * The type of this member.
     *
     * @return The type.
     */
    Class<?> valueType();

    /**
     * Reads the member's value. Whether reading the value is supported highly depends on
     * the implementation. Implementations are advised to provide a best effort approach
     * for determining the value without failure.
     * <p>
     * If this member is {@link #isWriteOnly()} null is returned.
     *
     * @return The value (may be null)
     * @throws UnsupportedOperationException If reading the value fails.
     */
    Object value();

    /**
     * Sets the member's value to the respective value. Whether setting the value is
     * supported highly depends on the implementation. If the member is
     * {@link #isReadonly()}, nothing happens.
     *
     * @param value The value to set (may be null)
     * @throws UnsupportedOperationException If setting the value is not possible.
     */
    void setValue(Object value);

    boolean isReadonly();

    boolean isWriteOnly();
}
