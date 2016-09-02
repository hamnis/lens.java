package net.hamnaberg.lens;

import org.junit.Test;

import static org.junit.Assert.*;

public class LensTests {

    @Test
    public void testModifyPerson() {
        Lens<Person, String> nameLens = new Lens<>(p -> p.name, (n, p) -> new Person(n, p.age));
        Lens<Person, Integer> ageLens = new Lens<>(p -> p.age, (a, p) -> new Person(p.name, a));

        Person person = new Person("Erlend Hamnaberg", 32);

        Person updated = nameLens.set("Ola Nordmann", person);

        assertEquals("Name lens did not work", person.name, nameLens.get(person));
        assertNotEquals("Name lens did not work", person, updated);
        assertEquals("Age lens did not work", Integer.valueOf(person.age), ageLens.get(person));
        assertNotEquals("Age lens did not work", person.age, ageLens.set(35, person));
    }

}
