package net.hamnaberg.lens;

import org.junit.Test;

import static org.junit.Assert.*;

public class LensTests {

    @Test
    public void testModifyPerson() {
        Lens<Person, String> nameLens = Lens.of(p -> p.name, (p, n) -> new Person(n, p.age));
        Lens<Person, Integer> ageLens = Lens.of(p -> p.age, (p, a) -> new Person(p.name, a));

        Person person = new Person("Erlend Hamnaberg", 32);

        Person updated = nameLens.set(person, "Ola Nordmann");

        assertEquals("Name lens did not work", person.name, nameLens.get(person));
        assertNotEquals("Name lens did not work", person, updated);
        assertEquals("Age lens did not work", Integer.valueOf(person.age), ageLens.get(person));
        assertNotEquals("Age lens did not work", person.age, ageLens.set(person, 35));
    }

}
