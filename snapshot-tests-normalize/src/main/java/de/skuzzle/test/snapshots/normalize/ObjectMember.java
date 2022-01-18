package de.skuzzle.test.snapshots.normalize;

import java.util.Optional;

/**
 * A pointer to a class member belonging to a certain object instance. Allows to read and
 * modify the member's value in place.
 *
 * @author Simon Taddiken
 * @see ObjectTraversal
 */
public interface ObjectMember {

    /**
     * The object of which this is a direct child attribute.
     *
     * @return Our parent.
     */
    Object parent();

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
     * the implementation. For example, an implementation that is based on getter and
     * setter methods might throw an {@link UnsupportedOperationException} if there is not
     * getter method for the attribute.
     *
     * @return The value (may be null)
     * @throws UnsupportedOperationException If reading the value is not possible.
     */
    Object value();

    /**
     * Sets the member's value to the respective value. Whether setting the value is
     * supported highly depends on the implementation. For example, an implementation that
     * is based on fields will throw an {@link UnsupportedOperationException} if the field
     * is declared final.
     *
     * @param value The value to set (may be null)
     * @throws UnsupportedOperationException If setting the value is not possible.
     */
    void setValue(Object value);
}
