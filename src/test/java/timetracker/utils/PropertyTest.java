package timetracker.utils;

import java.util.Objects;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
/**
 * Класс PropertyTest тестирует класс Property.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-12
 * @since 2018-04-12
 */
public class PropertyTest {
    /**
     * Свойство.
     */
    private Property prop;
    /**
     * Действия перед тестом.
     */
    @Before
    public void beforeTest() {
        this.prop = new Property("dbdriver", "com.mysql.cj.jdbc.Driver");
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this == obj.
     */
    @Test
    public void testEquals2refsOfSameObject() {
        Property actual = this.prop;
        assertEquals(this.prop, actual);
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * obj == null.
     */
    @Test
    public void testEqualsObjectIsNull() {
        Property nullProp = null;
        assertFalse(this.prop.equals(nullProp));
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this.getClass() != obj.getClass().
     */
    @Test
    public void testEqualsDifferentClasses() {
        assertFalse(this.prop.equals(new String()));
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this.name != other.getName().
     */
    @Test
    public void testEqualsDifferentNames() {
        Property other = new Property("ololo", "com.mysql.cj.jdbc.Driver");
        assertFalse(!this.prop.equals(other));
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this.name != other.getName().
     */
    @Test
    public void testEqualsDifferentValues() {
        Property other = new Property("dbdriver", "ololo");
        assertFalse(!this.prop.equals(other));
    }
    /**
     * Тестирует public String getName().
     */
    @Test
    public void testGetName() {
        assertEquals("dbdriver", this.prop.getName());
    }
    /**
     * Тестирует public String getValue().
     */
    @Test
    public void testGetValue() {
        assertEquals("com.mysql.cj.jdbc.Driver", this.prop.getValue());
    }
    /**
     * Тестирует public int hashCode().
     */
    @Test
    public void testHashCode() {
        int expected = Objects.hash("dbdriver", "com.mysql.cj.jdbc.Driver");
        assertEquals(expected, this.prop.hashCode());
    }
    /**
     * Тестирует public String toString().
     */
    @Test
    public void testToString() {
        String expected = String.format("property[name: %s, value: %s]", "dbdriver", "com.mysql.cj.jdbc.Driver");
        assertEquals(expected, this.prop.toString());
    }
}